package nl.zeesoft.zadf.controller.impl;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.gui.GuiGrid;
import nl.zeesoft.zadf.gui.GuiPropertySelectObject;
import nl.zeesoft.zadf.gui.GuiWindowObject;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.GuiModelContextFilter;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbReferenceFilter;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public class PrpIdRefDialogController extends GuiWindowController implements EvtEventSubscriber {
	private GuiPropertySelectObject property 			= null;
	private boolean					singleReturnValue	= true;
	private boolean					setAfterRefresh		= false;
	
	private List<Long> 				idList	 			= new ArrayList<Long>();
	private List<String> 			nameList 			= new ArrayList<String>();
	private DbCollection 			referenceCollection	= null;

	private String					unique				= "";
	
	private boolean					handlingEvent		= false;
	private boolean					fetching			= false;
	private int 					idListSize			= idList.size();
	
	private boolean					showId				= true;
	
	public PrpIdRefDialogController(GuiDialog idRefDialog,String unique) {
		super(idRefDialog);
		this.unique = unique;
		getGridCollectionController().addSubscriber(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((!handlingEvent) && (!fetching)) {
			handlingEvent = true;
			String action = "";
			if (e.getActionCommand()!=null) {
				action = e.getActionCommand();
			}
			
			if (action.equals(GuiWindowController.ACTION_CLOSE_FRAME)) {
				super.actionPerformed(e);
			}
	
			if (action.equals(GuiGrid.GRID_DOUBLE_CLICKED)) {
				action = ZADFFactory.BUTTON_ID_REF_REMOVE;
			}
				
			if (
				(action.startsWith(ZADFFactory.BUTTON_ID_REF_ADD)) ||
				(action.startsWith(ZADFFactory.BUTTON_ID_REF_REMOVE))
				) {
				setAfterRefresh = false; 
				boolean refresh = false;
				if (action.startsWith(ZADFFactory.BUTTON_ID_REF_ADD)) {
					List<Long> selectedIdList = getGridCollectionController().getSelectedIdList();
					if (selectedIdList.size()>0) {
						for (long id: selectedIdList) {
							if (!idList.contains(id)) {
								if (singleReturnValue) {
									idList.clear();
								}
								idList.add(id);
								refresh = true;
								if (singleReturnValue) {
									setAfterRefresh = true;
								}
								break;
							}
						}
					}
				} else if (action.startsWith(ZADFFactory.BUTTON_ID_REF_REMOVE)) {
					List<Long> selectedIdList = getGridValue().getSelectedRowIds();
					if (selectedIdList.size()>0) {
						for (long id: selectedIdList) {
							if (idList.contains(id)) {
								idList.remove(id);
								refresh = true;
							}
						}
					}
				}
				if (refresh) {
					refresh();
				}
			} else if (action.startsWith(ZADFFactory.BUTTON_ID_REF_SET)) {
				updateValueInCallingProperty();
				this.publishEvent(new EvtEvent(GuiWindowController.ACTION_CLOSE_FRAME, this, GuiWindowController.ACTION_CLOSE_FRAME));
				//((GuiDialog) getGuiObject()).getDialog().setVisible(false);
			}
			handlingEvent = false;
		}
	}
	
	private void updateValueInCallingProperty() {
		DtObject val = property.getValueObject();
		if (val instanceof DtIdRef) {
			DtIdRef idRef = (DtIdRef) val;
			if (idList.size()>0) {
				idRef.setValue(idList.get(0));
				property.setStringValue(nameList.get(0));
			} else {
				idRef.setValue(new Long(0));
				property.setStringValue("");
			}
		} else if (val instanceof DtIdRefList) {
			DtIdRefList idRefList = (DtIdRefList) val;
			if (idList.size()>0) {
				List<Long> newIdList = new ArrayList<Long>(idList);
				idRefList.setValue(newIdList);
				property.setStringValue(DbModel.getStringValueForNameList(nameList));
			} else {
				idRefList.setValue(new ArrayList<Long>());
				property.setStringValue("");
			}
		}
		property.updateComponentValue();
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			ClRequest request = (ClRequest) e.getValue();
			final List<Object[]> data = new ArrayList<Object[]>();
			
			idList.clear();
			nameList.clear();
			
			if (request.getQueryRequest()!=null) {
				for (QryObject fObj: request.getQueryRequest().getQueries()) {
					QryFetch fetch = (QryFetch) fObj; 
					if ((fetch.getResults()!=null) && (fetch.getMainResults().getReferences().size()>0)) {
						for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
							idList.add(ref.getId().getValue());
							nameList.add(ref.getName().getValue());
							Object[] object = null;
							if (showId) {
								object = new Object[2];
								object[0] = ref.getId().getValue();
								object[1] = ref.getName().getValue();
							} else {
								object = new Object[1];
								object[0] = ref.getName().getValue();
							}
							data.add(object);
						}
					}
				}
			}
		    SwingUtilities.invokeLater(new Runnable() {
		    	public void run() {
		    		getGridValue().setData(new ArrayList<Long>(idList),data);
		      	}
		    });	
		    fetching = false;
			
			if ((singleReturnValue) && (setAfterRefresh) && (nameList.size()==1)) {
			    SwingUtilities.invokeLater(new Runnable() {
			    	public void run() {
			    		actionPerformed(new ActionEvent(this, 0, ZADFFactory.BUTTON_ID_REF_SET));
			      	}
			    });	
			} else if (idListSize!=idList.size()) {
				updateValueInCallingProperty();
			}
			setAfterRefresh = false;
			idListSize=idList.size();
		} else if (e.getType().equals(GuiGrid.GRID_DOUBLE_CLICKED)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			if (e.getSource() instanceof PrpIdRefDialogGridController) {
				this.actionPerformed(new ActionEvent(this, 0, ZADFFactory.BUTTON_ID_REF_ADD));
			} else {
				this.actionPerformed(new ActionEvent(this, 0, ZADFFactory.BUTTON_ID_REF_REMOVE));
			}
		}
	}

	protected void refresh() {
		getGridValue().clearData();
		getGridValue().clearProperties();
		if (showId) {
			getGridValue().addProperty(MdlObject.PROPERTY_ID, "ID",0L);
		}
		getGridValue().addProperty(MdlObject.PROPERTY_NAME,referenceCollection.getPropertyByName(MdlObject.PROPERTY_NAME).getLabel().getValue(),"");
		getGridValue().fireStructureChanged();
		
		QryFetchList fetchList = new QryFetchList(null);
		for (long id: idList) {
			QryFetch fetch = new QryFetch(referenceCollection.getName().getValue(),id);
			fetch.setType(QryFetch.TYPE_FETCH_REFERENCES);
			fetchList.addQuery(fetch);
		}
		if (fetchList.getQueries().size()>0) {
			fetching = true;
			idListSize = idList.size();
			ClSession session = GuiController.getInstance().getSession();
			ClRequest request = session.getRequestQueue().getNewRequest(this);
			request.setQueryRequest(fetchList);
			request.addSubscriber(this);
			session.getRequestQueue().addRequest(request, this);
		}
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(GuiPropertySelectObject property) {
		this.property = property;
		singleReturnValue = true;
		
		idList.clear();
		getGridValue().clearData();

		String[] vals = Generic.getValuesFromString(property.getName());
		if (vals.length==1) {
			String colName = Generic.getValuesFromString(property.getName())[0];
			referenceCollection = DbModel.getInstance().getCollectionByName(colName);
		} else {
			String colName = Generic.getValuesFromString(property.getName())[0];
			String propName = Generic.getValuesFromString(property.getName())[1];
			DbCollection dbCol = DbModel.getInstance().getCollectionByName(colName);
			DbCollectionProperty dbProp = dbCol.getPropertyByName(propName);
			referenceCollection = dbProp.getRefColl();
		}
		
		DtObject val = property.getValueObject();
		String name = "";
		if (val instanceof DtIdRef) {
			DtIdRef idRef = (DtIdRef) val;
			if ((idRef.getValue()!=null) && (idRef.getValue()>0)) {
				idList.add(idRef.getValue());
			}
			name = referenceCollection.getNameSingle().getValue().toLowerCase(); 
		} else if (val instanceof DtIdRefList) {
			singleReturnValue = false;
			DtIdRefList idRefList = (DtIdRefList) val;
			if ((idRefList.getValue()!=null) && (idRefList.getValue().size()>0)) {
				for (long id: idRefList.getValue()) {
					idList.add(id);
				}
			}
			name = referenceCollection.getNameMulti().getValue().toLowerCase(); 
		}
		((GuiDialog) getGuiObject()).getDialog().setIconImage(ZADFFactory.getIconImage(referenceCollection.getName().getValue()));
		((GuiDialog) getGuiObject()).getDialog().setTitle("Select " + name);

		getGridCollectionController().setContext(property.getName());
		getGridCollectionController().setCollection(referenceCollection);
		
		if (referenceCollection.getPropertyByName(MdlObject.PROPERTY_ID).getOrderInSelect().getValue()<0) {
			showId = false;
		}
		
		// Add reference filters
		for (DbReferenceFilter filter: property.getFilters()) {
			DtObject valueObj = property.getNewValueObjectFromComponentValue();
			valueObj = (DtObject) Generic.instanceForName(valueObj.getClass().getName());
			if (
				(filter.getValue().getValue().length()>0) ||
				(valueObj instanceof DtString) ||
				(valueObj instanceof DtStringBuffer)
				){
				valueObj.fromString(new StringBuffer(filter.getValue().getValue()));
			}
			GuiModelContextFilter stateFilter = new GuiModelContextFilter(
				true,
				filter.getProp().getName().getValue(),
				filter.getInvert().getValue(),
				filter.getOper().getName().getValue(),
				valueObj,
				filter.getStringValue()
				);
			if (filter.getMandatory().getValue()) {
				getGridCollectionController().addFilter(stateFilter);
			} else {
				getGridCollectionController().getControlController().getFilters().add(stateFilter);
			}
		}

		getGridCollectionController().refresh();
		refresh();
	}
	
	private PrpIdRefDialogGridController getGridCollectionController() {
		return (PrpIdRefDialogGridController) GuiController.getInstance().getGuiObjectControllerByObjectName((ZADFFactory.GRID_ID_REF_COLLECTION + unique));
	}

	private GuiGrid getGridValue() {
		GuiWindowObject window = (GuiWindowObject) getGuiObject();
		return (GuiGrid) window.getGuiObjectByName(ZADFFactory.GRID_ID_REF_VALUE + unique);
	}

}
