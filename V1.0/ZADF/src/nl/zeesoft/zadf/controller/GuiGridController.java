package nl.zeesoft.zadf.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import nl.zeesoft.zadf.gui.GuiButton;
import nl.zeesoft.zadf.gui.GuiGrid;
import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zadf.gui.GuiPropertySelectValue;
import nl.zeesoft.zadf.gui.panel.PnlGrid;
import nl.zeesoft.zadf.gui.panel.PnlGridControl;
import nl.zeesoft.zadf.gui.property.PrpPassword;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.GuiModel;
import nl.zeesoft.zadf.model.GuiModelContextFilter;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionFilter;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefList;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public abstract class GuiGridController extends GuiObjectController implements EvtEventSubscriber {
	public static final String					ADD_BUTTON_CLICKED		= "ADD_BUTTON_CLICKED";
	public static final String					UPDATE_BUTTON_CLICKED	= "UPDATE_BUTTON_CLICKED";
	public static final String					REMOVE_BUTTON_CLICKED	= "REMOVE_BUTTON_CLICKED";
	public static final String					GRID_SELECTION_CHANGED	= "GRID_SELECTION_CHANGED";

	private	DbCollection						collection				= null;
		
	private GuiGridControlController			controlController		= null;
	private GuiGrid								grid					= null;
		
	private List<GuiModelContextFilter> 		filters					= new ArrayList<GuiModelContextFilter>();

	private	DbCollectionProperty				orderBy					= null;
	private boolean								ascending				= true;
	
	private List<Long>							selectedIdList			= new ArrayList<Long>();

	private MdlObjectRefList					results					= null;
	private SortedMap<Long,String>				extendedReferences		= new TreeMap<Long,String>();
	
	private static final long					REFRESH_DELAY			= 200; // ms
	private long								nextRefreshDataTime		= 0;
	private	ClRequest							waitForRequest			= null;
	private GuiGridWorker						refreshDataWorker		= null;
	
	private String								context					= "";

	private Object								gridIsLockedBy			= null;
	
	public GuiGridController(PnlGrid gridPanel) {
		super(gridPanel);
		refreshDataWorker = new GuiGridWorker(this);
		refreshDataWorker.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GuiObject sourceObject = null;
		if ((e.getSource()!=null) && (e.getSource() instanceof Component)) {
			sourceObject = getGuiObject().getGuiObjectForSourceComponent((Component) e.getSource());
		}

		boolean handled = false;
		if (controlController!=null) {
			PnlGridControl panel = (PnlGridControl) controlController.getGuiObject();
			GuiButton buttonAdd = (GuiButton) panel.getPanelObjects().getGuiObjectByName("add");
			GuiButton buttonUpdate = (GuiButton) panel.getPanelObjects().getGuiObjectByName("update");
			GuiButton buttonRemove = (GuiButton) panel.getPanelObjects().getGuiObjectByName("remove");
			GuiButton buttonFetch = (GuiButton) panel.getPanelObjects().getGuiObjectByName("fetch");
			GuiButton buttonPrev = (GuiButton) panel.getPanelObjects().getGuiObjectByName("prev");
			GuiButton buttonNext = (GuiButton) panel.getPanelObjects().getGuiObjectByName("next");
			if ((buttonAdd!=null) && (e.getSource().equals(buttonAdd.getButton()))) {
				publishEvent(new EvtEvent(ADD_BUTTON_CLICKED,this,results));
				handled = true;
			} else if ((buttonUpdate!=null) && (e.getSource().equals(buttonUpdate.getButton()))) {
				publishEvent(new EvtEvent(UPDATE_BUTTON_CLICKED,this,results));
				handled = true;
			} else if ((buttonRemove!=null) && (e.getSource().equals(buttonRemove.getButton()))) {
				publishEvent(new EvtEvent(REMOVE_BUTTON_CLICKED,this,results));
				handled = true;
			} else if (e.getSource().equals(buttonFetch.getButton())) {
				controlController.setStart(controlController.getStart());
				controlController.setLimit(controlController.getLimit());
				delayedRefreshData();
				handled = true;
			} else if (e.getSource().equals(buttonPrev.getButton())) {
				controlController.setLimit(controlController.getLimit());
				int start = controlController.getStart() - controlController.getLimit();
				controlController.setStart(start);
				delayedRefreshData();
				handled = true;
			} else if (e.getSource().equals(buttonNext.getButton())) {
				controlController.setLimit(controlController.getLimit());
				int start = controlController.getStart() + controlController.getLimit();
				controlController.setStart(start);
				delayedRefreshData();
				handled = true;
			}
		}
		if ((!handled) && (controlController!=null)) {
			if (e.getActionCommand().equals(GuiPropertySelectValue.SELECT_VALUE_CLICKED)) {
				// Pass select action on to listeners
				publishEvent(new EvtEvent(e.getActionCommand(),getGuiObject(),sourceObject));
				handled = true;
			}
		}
		if ((!handled) && (grid!=null)) {
			if (e.getActionCommand().equals(GuiGrid.GRID_DOUBLE_CLICKED)) {
				selectedIdList = grid.getSelectedRowIds();
				if (selectedIdList.size()>0) {
					GuiModel.getInstance().setContextId(context, selectedIdList.get(0));
				}
				publishEvent(new EvtEvent(e.getActionCommand(),this,results));
				handled = true;
			} else if (e.getActionCommand().equals(GuiGrid.GRID_COLUMN_CLICKED)) {
				if ((collection!=null) && (!grid.getSelectedProperty().equals(""))) {
					for (DbCollectionProperty prop: getGridProperties(collection)) {
						if (prop.getName().equals(grid.getSelectedProperty())) {
							if (orderBy == prop) {
								ascending = (!ascending);
							} else {
								orderBy = prop;
							}
							GuiModel.getInstance().setContextOrderBy(context,orderBy);
							GuiModel.getInstance().setContextAscending(context,ascending);
							refreshProperties();
							break;
						}
					}
				} else {
					orderBy = null;
					ascending = true;
				}
				delayedRefreshData();
				handled = true;
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (grid!=null) {
			List<Long> newList = grid.getSelectedRowIds(); 
			lockGrid(this);
			selectedIdList = newList; 
			if (selectedIdList.size()>0) {
				GuiModel.getInstance().setContextId(context, selectedIdList.get(0));
			}
			MdlObjectRefList res = MdlObjectRefList.copy(results);
			unlockGrid(this);
			publishEvent(new EvtEvent(GRID_SELECTION_CHANGED,this,res));
		}
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			ClRequest tmpRequest = (ClRequest) e.getValue();
			if ((waitForRequest!=null) && (waitForRequest.getId()==tmpRequest.getId())) {
				QryFetch fetch = (QryFetch) ((QryFetchList) tmpRequest.getQueryRequest()).getQueries().get(0);
				DbCollection collection = this.collection;
				collection = DbModel.getInstance().getCollectionByName(fetch.getClassName());
	
				if (grid!=null) {
					if ((fetch.getResults()!=null) && (fetch.getMainResults().getReferences().size()>0)) {
						List<DbCollectionProperty> gridProperties = getGridProperties(collection);
						if (gridProperties.size()>=0) {
							boolean[] password = new boolean[gridProperties.size()];
							DtObject[] reference = new DtObject[gridProperties.size()];
							
							int num = 0;
							for (DbCollectionProperty dbProp: gridProperties) {
								GuiProperty guiProp = (GuiProperty) Generic.instanceForName(dbProp.getPropertyClassName().getValue());
								if (guiProp instanceof PrpPassword) {
									password[num] = true;
								} else {
									password[num] = false;
								}
								if (dbProp.getReferenceCollection().getValue()>0) {
									DtObject valObj = guiProp.getValueObject();
									reference[num] = valObj;
								} else {
									reference[num] = null;
								}
								num++;
							}
		
							final List<Long> idList = new ArrayList<Long>();
							final List<Object[]> data = new ArrayList<Object[]>();
							for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
								MdlDataObject obj = ref.getDataObject();
								long id = obj.getId().getValue();
								idList.add(id);
								Object[] object = new Object[gridProperties.size()];
								num = 0;
								for (DbCollectionProperty prop: gridProperties) {
									if (password[num]) {
										object[num] = "########";
									} else if (reference[num]!=null) {
										if (reference[num] instanceof DtIdRef) {
											DtIdRef idRef = (DtIdRef) obj.getPropertyValue(prop.getName().getValue());
											if (idRef.getValue()>0) {
												String name = fetch.getExtendedReferences().get(idRef.getValue());
												object[num] = name;
											} else {
												object[num] = "";
											}
										} else if (reference[num] instanceof DtIdRefList) {
											DtIdRefList idRefList = (DtIdRefList) obj.getPropertyValue(prop.getName().getValue());
											int added = 0;
											List<String> nameList = new ArrayList<String>();
											for (long rid: idRefList.getValue()) {
												if (added<3) {
													nameList.add(fetch.getExtendedReferences().get(rid));
												} else {
													break;
												}
												added++;
											}
											object[num] = DbModel.getStringValueForNameList(nameList);
										}
									}
									if (object[num]==null) {
										object[num] = obj.getPropertyValue(prop.getName().getValue()).getValue();
									}
									num++;
								}
								data.add(object);
							}
							if (nextRefreshDataTime==0) {
							    SwingUtilities.invokeLater(new Runnable() {
							    	public void run() {
							    		grid.setData(idList,data);
							      	}
							    });
								results = MdlObjectRefList.copy(fetch.getMainResults());
								extendedReferences = new TreeMap<Long,String>(fetch.getExtendedReferences());
								
								if (idList.size()>0) {
									long id = GuiModel.getInstance().getIdForContext(context);
									if ((id > 0) && (!idList.contains(id))) {
										id = 0;
									}
									if (id == 0) {
										id = idList.get(0);
									}
									if (id > 0) {
										final long selectedId = id;
									    SwingUtilities.invokeLater(new Runnable() {
									    	public void run() {
									    		grid.setSelectedRowId(selectedId);
									      	}
									    });	
									}
								}
							}
						}
					}
				}
	
				if ((collection!=null) && (controlController!=null)) {
					int count = 0;
					String objects = collection.getNameMulti().getValue().toLowerCase();
					if (fetch.getResults()!=null) { 
						count = fetch.getMainResults().getReferences().size();
					}
					if (fetch.getCount()==1) {
						objects = collection.getNameSingle().getValue().toLowerCase();
					}
					StringBuffer txt = new StringBuffer();
					txt.append("Showing ");
					txt.append(count);
					txt.append(" / ");
					txt.append(fetch.getCount());
					txt.append(" ");
					txt.append(objects);
					if (fetch.isResultsIncomplete()) {
						txt.insert(0,"<html><b>");
						txt.append("</b></html>");
					}
					final StringBuffer l = new StringBuffer();
					l.append("<html>");
					l.append(Generic.stringBufferReplace(new StringBuffer(fetch.getLog()),"\n","<br/>"));
					l.append("</html>");
					final String resultText = txt.toString();
					final String log = l.toString();
					SwingUtilities.invokeLater(new Runnable() {
				    	public void run() {
				    		controlController.setResults(resultText,log);
				      	}
				    });	
				}
			}
		}
	}
	
	protected List<DbCollectionProperty> getGridProperties(DbCollection collection) {
		return collection.getGridProperties();
	}

	protected void refreshProperties() {
		boolean publishSelectionChange = false;
		if (grid!=null) {
			grid.clearData();
			grid.clearProperties();
			lockGrid(this);
			if (selectedIdList.size()>0) {
				selectedIdList.clear();
				publishSelectionChange = true;
			}
			if (collection!=null) {
				List<DbCollectionProperty> gridProperties = getGridProperties(collection);
				if (gridProperties.size()>=0) {
					for (DbCollectionProperty prop: gridProperties) {
						String label = prop.getLabel().getValue();
						if (orderBy!=null) {
							if (prop==orderBy) {
								if (ascending) {
									label = label + " <";
								} else {
									label = label + " >";
								}
							}
						}
						Object value = null;
						if (!prop.getPropertyClassName().getValue().equals("")) {
							GuiProperty guiProp = (GuiProperty) Generic.instanceForName(prop.getPropertyClassName().getValue());
							value = guiProp.getGridSampleValueObject();
						}
						grid.addProperty(prop.getName().getValue(),label,value);
					}
				}
			}
			unlockGrid(this);
			grid.fireStructureChanged();
		}
		if (publishSelectionChange) {
			publishEvent(new EvtEvent(GRID_SELECTION_CHANGED,this,"Selected grid row changed"));
		}
	}
	
	private boolean refreshData() {
		boolean succes = false;
		if (grid!=null) {
			grid.clearData();
		}
		if (collection!=null) {
			lockGrid(this);
			QryFetchList fetchList = new QryFetchList(null);
			QryFetch fetch = new QryFetch(collection.getName().getValue());
			fetch.setType(QryFetch.TYPE_FETCH_OBJECTS_EXTEND);
			for (QryFetchCondition condition: filters) {
				fetch.addCondition(condition);
			}
			if (orderBy!=null) {
				fetch.setOrderBy(orderBy.getName().getValue(), ascending);
			}
			fetchList.addQuery(fetch);
			
			boolean sendRequest = false;
			try {
				if (controlController!=null) {
					int start = controlController.getStart();
					int limit = controlController.getLimit();
					if ((start>=0) && (controlController.getLimit()>0)) {
						GuiModel.getInstance().setContextStart(context,start);
						GuiModel.getInstance().setContextLimit(context,limit);
						fetch.setStartLimit(start,limit);
					}
					List<GuiModelContextFilter> filters = controlController.getFilters();
					GuiModel.getInstance().setContextFilters(context,filters);
					for (GuiModelContextFilter filter: filters) {
						if (filter.isActive()) { 
							fetch.addCondition(filter);
						}
					}
				}
				sendRequest = true;
			} catch(Exception e) {
				// The code above might fail because it depends on the frame state
			}

			if (sendRequest) {
				if (controlController!=null) {
					final String resultText = "Fetching ...";
					final String log = "Fetching ...";
					SwingUtilities.invokeLater(new Runnable() {
				    	public void run() {
				    		controlController.setResults(resultText,log);
				      	}
				    });
				}
				ClSession session = GuiController.getInstance().getSession();
				ClRequest request = session.getRequestQueue().getNewRequest(this);
				request.setQueryRequest(fetchList);
				request.addSubscriber(this);
				session.getRequestQueue().addRequest(request, this);
				waitForRequest = request;
				succes = true;
			}
			unlockGrid(this);
		} else {
			if (controlController!=null) {
				final String resultText = "";
				final String log = null;
				SwingUtilities.invokeLater(new Runnable() {
			    	public void run() {
			    		controlController.setResults(resultText,log);
			      	}
			    });
			}
			succes = true;
		}
		return succes;
	}
	
	protected void refresh() {
		refreshProperties();
		delayedRefreshData();
	}
	
	protected void delayedRefreshData() {
		lockGrid(this);
		nextRefreshDataTime = new Date().getTime() + REFRESH_DELAY;
		unlockGrid(this);
	}
	
	protected void checkRefreshData() {
		lockGrid(this);
		long now = new Date().getTime();
		long next = nextRefreshDataTime;
		unlockGrid(this);
		if ((next!=0) && (next<now)) {
			if (refreshData()) {
				lockGrid(this);
				nextRefreshDataTime = 0;
				unlockGrid(this);
			}
		}
	}
	
	protected void stopRefreshDataWorker() {
		lockGrid(this);
		if (refreshDataWorker.isWorking()) {
			refreshDataWorker.stop();
		}
		unlockGrid(this);
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(DbCollection collection) {
		lockGrid(this);
		this.collection = collection;
		
		List<GuiModelContextFilter> contextFilters = GuiModel.getInstance().getFiltersForContext(context);
		
		if (collection!=null) {
			filters.clear();
			unlockGrid(this);
			addDefaultFilters(collection);
			lockGrid(this);
			for (DbCollectionFilter filter: collection.getFilters()) {
				boolean add = true;
				if (!filter.getMandatory().getValue()) {
					for (GuiModelContextFilter filt: contextFilters) {
						if (filt.getProperty().equals(filter.getProp().getName().getValue())) {
							add = false;
							break;
						}
					}
				}
				if (add) {
					GuiProperty property = (GuiProperty) Generic.instanceForName(filter.getProp().getPropertyClassName().getValue());
					DtObject valueObj = property.getNewValueObjectFromComponentValue();
					valueObj = (DtObject) Generic.instanceForName(valueObj.getClass().getName());
					if (
						(filter.getValue().getValue().length()>0) ||
						(valueObj instanceof DtString) ||
						(valueObj instanceof DtStringBuffer)
						){
						valueObj.fromString(new StringBuffer(filter.getValue().getValue()));
					}
					if (filter.getMandatory().getValue()) {
						filters.add(new GuiModelContextFilter(
								true,
								filter.getProp().getName().getValue(),
								filter.getInvert().getValue(),
								filter.getOper().getName().getValue(),
								valueObj,
								filter.getStringValue()
								));
					} else {
						contextFilters.add(new GuiModelContextFilter(
								true,
								filter.getProp().getName().getValue(),
								filter.getInvert().getValue(),
								filter.getOper().getName().getValue(),
								valueObj,
								filter.getStringValue()
								));
					}
				}
			}
		}
		
		orderBy = GuiModel.getInstance().getOrderByForContext(context);
		ascending = GuiModel.getInstance().getAscendingForContext(context);
		if (orderBy!=null) {
			boolean found = false;
			if (collection!=null) {
				for (DbCollectionProperty prop: getGridProperties(collection)) {
					if (prop == orderBy) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				orderBy = null;
			}
		}
		if (orderBy==null) {
			if (collection!=null) {
				for (DbCollectionProperty prop: getGridProperties(collection)) {
					orderBy = prop;
					break;
				}
			}
		}
		if (controlController!=null) {
			controlController.setFilters(contextFilters);
			controlController.setCollection(collection);
			controlController.setStart(GuiModel.getInstance().getStartForContext(context));
			controlController.setLimit(GuiModel.getInstance().getLimitForContext(context));
		}
		unlockGrid(this);
	}
	
	/**
	 * Override to implement
	 * 
	 * @param collection The current collection
	 */
	protected void addDefaultFilters(DbCollection collection) {
		
	}

	/**
	 * @return the selectedIdList
	 */
	public List<Long> getSelectedIdList() {
		lockGrid(this);
		List<Long> r = new ArrayList<Long>(selectedIdList);
		unlockGrid(this);
		return r;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		lockGrid(this);
		this.context = context;
		unlockGrid(this);
	}

	/**
	 * @return the controlController
	 */
	public GuiGridControlController getControlController() {
		return controlController;
	}

	/**
	 * @param controlController the controlController to set
	 */
	public void setControlController(GuiGridControlController controlController) {
		this.controlController = controlController;
	}

	/**
	 * @return the grid
	 */
	public GuiGrid getGrid() {
		return grid;
	}

	/**
	 * @param grid the grid to set
	 */
	public void setGrid(GuiGrid grid) {
		this.grid = grid;
	}

	/**
	 * @return the collection
	 */
	public DbCollection getCollection() {
		lockGrid(this);
		DbCollection r = collection; 
		unlockGrid(this);
		return r;
	}

	/**
	 * @return the filters
	 */
	public List<GuiModelContextFilter> getFilters() {
		lockGrid(this);
		List<GuiModelContextFilter> r = new ArrayList<GuiModelContextFilter>(filters);
		unlockGrid(this);
		return r;
	}

	public void addFilter(GuiModelContextFilter filter) {
		lockGrid(this);
		filters.add(filter);
		unlockGrid(this);
	}

	public void removeFilter(GuiModelContextFilter filter) {
		lockGrid(this);
		filters.remove(filter);
		unlockGrid(this);
	}

	/**
	 * @return the results
	 */
	public MdlObjectRefList getResults() {
		lockGrid(this);
		MdlObjectRefList r = MdlObjectRefList.copy(results);
		unlockGrid(this);
		return r;
	}

	/**
	 * @return the extendedReferences
	 */
	public SortedMap<Long, String> getExtendedReferences() {
		lockGrid(this);
		SortedMap<Long, String> r = new TreeMap<Long, String>(extendedReferences);
		unlockGrid(this);
		return r;
	}
		
	private synchronized void lockGrid(Object source) {
		int attempt = 0;
		while (gridIsLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,"Lock failed after " + attempt + " attempts. Source:" + source);
				attempt = 0;
			}
		}
		gridIsLockedBy = source;
	}

	private synchronized void unlockGrid(Object source) {
		if (gridIsLockedBy==source) {
			gridIsLockedBy=null;
			notifyAll();
		}
	}
	
	private synchronized boolean gridIsLocked() {
		return (gridIsLockedBy!=null);
	}
	
}
