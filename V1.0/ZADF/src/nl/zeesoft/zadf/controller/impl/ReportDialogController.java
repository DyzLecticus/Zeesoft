package nl.zeesoft.zadf.controller.impl;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.format.FmtObject;
import nl.zeesoft.zadf.gui.GuiButton;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.gui.GuiLabel;
import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zadf.gui.GuiPropertySelectValue;
import nl.zeesoft.zadf.gui.panel.PnlReportFilters;
import nl.zeesoft.zadf.gui.property.PrpCheckBox;
import nl.zeesoft.zadf.gui.property.PrpIdRef;
import nl.zeesoft.zadf.gui.property.PrpIdRefList;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.GuiModel;
import nl.zeesoft.zadf.model.RepModel;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbFetch;
import nl.zeesoft.zadf.model.impl.DbFetchFilter;
import nl.zeesoft.zadf.model.impl.DbReport;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.database.query.QryError;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.model.MdlObjectRefListMap;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public class ReportDialogController extends GuiWindowController implements EvtEventSubscriber {
	private DbReport 										selectedReport			= null;
	private List<DbFetchFilter>  							selectedReportFilters	= new ArrayList<DbFetchFilter>();
	private List<DbCollection>  							collections				= new ArrayList<DbCollection>();
	private SortedMap<String,List<DbCollectionProperty>> 	collectionProperties	= new TreeMap<String,List<DbCollectionProperty>>();

	private PrpIdRef 										reportProperty			= null;
	private PnlReportFilters 								filterPanel				= null;
	private List<PrpCheckBox>								filterActives			= new ArrayList<PrpCheckBox>();
	private List<GuiProperty>								filterValues			= new ArrayList<GuiProperty>();
	private List<PrpCheckBox>								filterActivesEnabled	= new ArrayList<PrpCheckBox>();
	private List<GuiProperty>								filterValuesEnabled		= new ArrayList<GuiProperty>();
	private GuiButton 										fetchButton				= null;
	private GuiLabel 										labelFetchStatus		= null;
	
	private ClRequest										waitForRequest			= null;
	private boolean											fetching				= false;
	private boolean											cancelled				= false;
	private int												currentFetchIndex		= 0;
	private int												start 					= 0;
	private int												count					= 0;
	private MdlObjectRefListMap 							results					= new MdlObjectRefListMap();
	private String											resultsString			= "";
	
	private JFileChooser									fileChooser				= null;
	
	public ReportDialogController(GuiDialog reportDialog) {
		super(reportDialog);
		reportProperty = (PrpIdRef) reportDialog.getGuiObjectByName(DbReport.class.getName());
		reportProperty.addSubscriber(this);
		reportProperty.attachListeners();
		filterPanel = (PnlReportFilters) reportDialog.getGuiObjectByName(ZADFFactory.PANEL_REPORT_FILTERS);
		fetchButton = (GuiButton) reportDialog.getGuiObjectByName(ZADFFactory.BUTTON_REPORT_FETCH);
		labelFetchStatus = (GuiLabel) reportDialog.getGuiObjectByName(ZADFFactory.LABEL_FETCH_STATUS);
		RepModel.getInstance().addSubscriber(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";
		GuiObject sourceObject = null;
		
		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}

		if ((e.getSource()!=null) && (e.getSource() instanceof Component)) {
			sourceObject = getGuiObject().getGuiObjectForSourceComponent((Component) e.getSource());
		}
		
		//Messenger.getInstance().debug(this, "Handle action: " + action + ", source: " + sourceObject);

		if (action.equals(GuiWindowController.ACTION_CLOSE_FRAME)) {
			if (fetching) {
				cancelled = true;
			}
			super.actionPerformed(e);
		} else if (action.equals(GuiPropertySelectValue.SELECT_VALUE_CLICKED)) {
			// Pass select action on to listeners
			publishEvent(new EvtEvent(action,getGuiObject(),sourceObject));
		} else if (action.equals(ZADFFactory.BUTTON_REPORT_FETCH)) {
			fetch();
		}
	}

	private void selectedReport() {
		if (selectedReport==null) {
			refreshReportModel();
			return;
		}
		if (fileChooser==null) {
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		String fileName = 
			GuiConfig.getInstance().getReportDir() + 
			Generic.removeNonAllowedCharactersFromString(selectedReport.getName().getValue().replaceAll(" ","_"),Generic.ALPHANUMERIC + "_") + "." + 
			selectedReport.getFmt().getExtension().getValue();
		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				boolean returnVal = false;
				if (file.getName().endsWith("." + selectedReport.getFmt().getExtension().getValue())) {
					returnVal = true;
				}
				return returnVal;
			}
			@Override
			public String getDescription() {
				return "*." + selectedReport.getFmt().getExtension().getValue();
			}
		}; 
		fileChooser.setFileFilter(fileFilter);
		fileChooser.setSelectedFile(new File(fileName));
		
		selectedReportFilters.clear();
		collections.clear();
		collectionProperties.clear();
		for (DbFetch ftc: selectedReport.getFtcs()) {
			DbCollection dbCol = DbModel.getInstance().getCollectionByName(ftc.getColl().getName().getValue());
			if (!collections.contains(dbCol)) {
				collections.add(dbCol);
				if (GuiController.getInstance().getSession().isUserAdmin()) {
					collectionProperties.put(ftc.getColl().getName().getValue(),dbCol.getProperties());
				} else {
					collectionProperties.put(ftc.getColl().getName().getValue(),dbCol.getDetailProperties());
				}
			}
			for (DbCollection eDbCol: ftc.getEntiyColls()) {
				dbCol = DbModel.getInstance().getCollectionByName(eDbCol.getName().getValue());
				if (!collections.contains(dbCol)) {
					collections.add(dbCol);
					if (GuiController.getInstance().getSession().isUserAdmin()) {
						collectionProperties.put(eDbCol.getName().getValue(),dbCol.getProperties());
					} else {
						collectionProperties.put(eDbCol.getName().getValue(),dbCol.getDetailProperties());
					}
				}
			}
			// Merge filters by name
			for (DbFetchFilter filter: ftc.getFilters()) {
				boolean add = true;
				for (DbFetchFilter tst: selectedReportFilters) {
					if (tst.getName().getValue().equals(filter.getName().getValue())) {
						add = false;
						break;
					}
				}
				if (add) {
					selectedReportFilters.add(filter);
				}
			}
		}
		reportProperty.setToolTipText(getReportDescriptionToolTip());
	}
	
	public void refreshReportModel() {
		getGuiObject().setEnabled(false);
		RepModel.getInstance().loadEntities();
	}

	private void refreshReportFilters() {
		if (selectedReport!=null) {
			//Messenger.getInstance().debug(this, "Selected report: " + selectedReport.getName().getValue());
			filterPanel.getPanelObjects().clear();
			filterActives.clear();
			filterValues.clear();
			filterActivesEnabled.clear();
			filterValuesEnabled.clear();
			String tooltip = null;
			
			int row = 0;
			if (selectedReportFilters.size()>0) {
				GuiLabel lblFilters = new GuiLabel("reportFilters",row,0,"Filters");
				lblFilters.setAnchor(GridBagConstraints.FIRST_LINE_START);
				filterPanel.getPanelObjects().add(lblFilters);
				row++;
			}

			for (DbFetchFilter filter: selectedReportFilters) {
				boolean active = GuiModel.getInstance().getActiveForContext(filter.getId().toString());
				boolean enabled = false;
				if (
					(filter.getProp().getUserLevelVisible().getValue()>=GuiController.getInstance().getSession().getUserLevel()) &&
					((filter.getProp().getRefColl()==null) || (filter.getProp().getRefColl().getUserLevelFetch().getValue()>=GuiController.getInstance().getSession().getUserLevel()))
					) {
					enabled = true;
				}
				if (!enabled) {
					active = true;
				}
				tooltip = 
					filter.getProp().getColl().getNameSingle().getValue() + " " + 
					filter.getProp().getLabel().getValue().toLowerCase() + " ";
				if (filter.getInvert().getValue()) {
					tooltip = tooltip + "not ";
				}
				tooltip = tooltip + filter.getOper().getName().getValue() + " [value]";
				
				PrpCheckBox prpActive = new PrpCheckBox(filter.getName().getValue() + "_ACTIVE",row,0,new DtBoolean(active));
				prpActive.setAnchor(GridBagConstraints.FIRST_LINE_START);
				prpActive.renderComponent();
				prpActive.setEnabled(enabled);
				filterActives.add(prpActive);
				if (enabled) {
					filterActivesEnabled.add(prpActive);
				}
				filterPanel.getPanelObjects().add(prpActive);
				
				GuiLabel lblFilt = new GuiLabel(filter.getName().getValue() + "_LABEL",row,1,filter.getName().getValue());
				lblFilt.setAnchor(GridBagConstraints.FIRST_LINE_START);
				lblFilt.getLabel().setToolTipText(tooltip);
				filterPanel.getPanelObjects().add(lblFilt);
				
				String propertyClassName = filter.getProp().getPropertyClassName().getValue();
				if (propertyClassName.equals(PrpIdRefList.class.getName())) {
					propertyClassName = PrpIdRef.class.getName();
				}
				String propertyName = filter.getProp().getColl().getName().getValue() + Generic.SEP_STR + filter.getProp().getName().getValue();
				GuiProperty guiProp = (GuiProperty) Generic.instanceForName(propertyClassName);
				if (
					(filter.getValue().getValue().length()>0) ||
					(guiProp.getValueObject() instanceof DtString) ||
					(guiProp.getValueObject() instanceof DtStringBuffer)
					){
					guiProp.getValueObject().fromString(new StringBuffer(filter.getValue().getValue()));
				}
				if (guiProp instanceof GuiPropertySelectValue) {
					((GuiPropertySelectValue) guiProp).setStringValue(filter.getStringValue());
				}
				guiProp.setName(propertyName);
				guiProp.setRow(row);
				guiProp.setColumn(2);
				guiProp.setAnchor(GridBagConstraints.PAGE_START);
				guiProp.setFill(GridBagConstraints.HORIZONTAL);
				guiProp.addSubscriber(this);
				guiProp.renderComponent();
				guiProp.setToolTipText(tooltip);
				guiProp.setEnabled(enabled);
				filterValues.add(guiProp);
				if (enabled) {
					filterValuesEnabled.add(guiProp);
				}
				filterPanel.getPanelObjects().add(guiProp);

				row++;
			}

			if (selectedReportFilters.size()>0) {
				filterPanel.getPanelObjects().calculateWeights();
				filterPanel.getPanelObjects().getColumnWeights().clear();
				filterPanel.getPanelObjects().getColumnWeights().add(0.01D);
				filterPanel.getPanelObjects().getColumnWeights().add(0.10D);
				filterPanel.getPanelObjects().getColumnWeights().add(0.89D);
			} else {
				GuiLabel lblDesc = new GuiLabel("reportDescription",row,0,getReportDescriptionToolTip());
				lblDesc.setAnchor(GridBagConstraints.FIRST_LINE_START);
				filterPanel.getPanelObjects().add(lblDesc);
			}
		} else {
			//Messenger.getInstance().debug(this, "No report selected.");
			filterPanel.getPanelObjects().clear();
		}
		
		filterPanel.setJPanel(filterPanel.getPanelObjects().renderJPanel());
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if ((e.getType().equals(GuiProperty.CHANGE_EVENT)) && (e.getSource().equals(reportProperty))) {
			//Messenger.getInstance().debug(this, "Handle event: " + e.getType() + ", source: " + e.getSource());
			long id = reportProperty.getNewValueObjectFromComponentValue().getValue();
			if (id>0) {
				selectedReport = (DbReport) RepModel.getInstance().getCollectionObjectById(DbReport.class.getName(), id);
				selectedReport();
			} else {
				selectedReport = null;
			}
			labelFetchStatus.getLabel().setText(" ");
			refreshReportFilters();
		} else if (e.getType().equals(RepModel.LOADED_REPORT_MODEL)) {
			//Messenger.getInstance().debug(this, "Handle event: " + e.getType() + ", source: " + e.getSource());
			getGuiObject().setEnabled(true);
			long id = reportProperty.getNewValueObjectFromComponentValue().getValue();
			if (id>0) {
				if (selectedReport==null) {
					selectedReport = (DbReport) RepModel.getInstance().getCollectionObjectById(DbReport.class.getName(), id);
					if (selectedReport!=null) {
						selectedReport();
					} else {
						Messenger.getInstance().error(this,"Unable to select report with ID: " + id);
					}
				}
			}
		} else if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			//Messenger.getInstance().debug(this, "Handle event: " + e.getType() + ", source: " + e.getSource());
			ClRequest tmpRequest = (ClRequest) e.getValue();
			if ((waitForRequest!=null) && (waitForRequest.getId()==tmpRequest.getId())) {
				if (cancelled) {
					labelFetchStatus.getLabel().setText(" ");
					fetching = false;
					cancelled = false;
					setEnabled(true);
				} else {
					for (QryObject qry: tmpRequest.getQueryRequest().getQueries()) {
						QryFetch f = (QryFetch) qry;
						if (f.isResultsIncomplete()) {
							((GuiDialog) getGuiObject()).msgError("The database is unable to perform this request right now.\nPlease try again later.", "Busy");
							labelFetchStatus.getLabel().setText(" ");
							fetching = false;
							setEnabled(true);
							break;
						} else if (qry.getErrors().size()>0) {
							String errors = "";
							String show = "";
							int added = 0;
							for (QryError error: qry.getErrors()) {
								String err = DbModel.getQueryErrorMessageForCollection(error, f.getClassName());
								if (!errors.equals("")) {
									errors = errors + "\n";
									if (added<4) {
										show = show + "\n";
									}
								}
								errors = errors + err;
								if (added<3) {
									show = show + err;
								} else if (added<4) {
									show = show + "...";
								}
								added++;
							}
							((GuiDialog) getGuiObject()).msgError(show);
							Messenger.getInstance().debug(this, "Fetch response errors:\n" + errors);
							labelFetchStatus.getLabel().setText(" ");
							fetching = false;
							setEnabled(true);
							break;
						}
					}
					if (fetching) {
						QryFetch fetch = (QryFetch) ((QryFetchList) tmpRequest.getQueryRequest()).getQueries().get(0);
						if (fetch.getType().equals(QryFetch.TYPE_COUNT_REFERENCES)) {
							count = 0;
							for (QryObject qry: tmpRequest.getQueryRequest().getQueries()) {
								QryFetch f = (QryFetch) qry;
								count = count + f.getCount();
							}
							labelFetchStatus.getLabel().setText("Fetched " + results.getSize() + " / " + count);
							if (count > 0) {
								sendFetchRequest(false);
							} else {
								fetched();
								waitForRequest = null;
							}
						} else {
							DbFetch ftc = selectedReport.getFtcs().get(currentFetchIndex);
							int size = fetch.getMainResults().getReferences().size();
							if (size>0) {
								results.addReferenceList(QryFetch.MAIN_RESULTS,fetch.getMainResults());
								for (String entity: fetch.getEntities()) {
									results.addReferenceList(entity,fetch.getResults().getReferenceListForCollection(entity));
								}
							}
							int total = results.getReferenceListForCollection(QryFetch.MAIN_RESULTS).getReferences().size();
							if (size>=ftc.getLimit().getValue()) {
								start = start + ftc.getLimit().getValue();
								sendFetchRequest(false);
								labelFetchStatus.getLabel().setText("Fetched " + total + " / " + count);
							} else if (currentFetchIndex < (selectedReport.getFtcs().size() - 1)) {
								start = 0;
								currentFetchIndex++;
								sendFetchRequest(false);
							} else {
								count = results.getReferenceListForCollection(QryFetch.MAIN_RESULTS).getReferences().size();
								labelFetchStatus.getLabel().setText("Fetched " + total + " / " + count);
								fetched();
								waitForRequest = null;
							}
						}
					}
				}
			}
		} else if (e.getType().equals(FmtObject.FORMATTED)) {
			labelFetchStatus.getLabel().setText(resultsString + " " + e.getValue());
		}
	}
	
	private void fetch() {
		if (selectedReport!=null) {
			currentFetchIndex = 0;
			setEnabled(false);
			fetching = true;
			cancelled = false;
			labelFetchStatus.getLabel().setText(" ");
			start = 0;
			count = 0;
			results	= new MdlObjectRefListMap();
			sendFetchRequest(true);
		}
	}

	private void fetched() {
		setEnabled(true);
		fetching = false;
		cancelled = false;
		if (count>0) {

			if (selectedReport.getFtcs().size()>1) {
				// Order over multiple fetches
				String firstOBy = "";
				boolean firstAsc = true;
				String firstPC = "";
				
				String orderBy = null;
				boolean orderAsc = true;

				for (DbFetch f: selectedReport.getFtcs()) {
					if (firstOBy.length()==0) {
						firstOBy = f.getOBy().getName().getValue();
						firstAsc = f.getOrderAscending().getValue();
						firstPC = f.getOBy().getPropertyClassName().getValue();
						
						orderBy = firstOBy;
						orderAsc = firstAsc;
					} else if (
						(!firstOBy.equals(f.getOBy().getName().getValue())) ||
						(firstAsc!=f.getOrderAscending().getValue()) ||
						(!firstPC.equals(f.getOBy().getPropertyClassName().getValue())) 
						) {
						orderBy = null;
						orderAsc = true;
						break;
					}
				}
				
				if ((orderBy!=null) && (orderBy.length()>0)) {
					results.getReferenceListForCollection(QryFetch.MAIN_RESULTS).sortObjects(orderBy, orderAsc);
				}
			}
			
			String txt = labelFetchStatus.getLabel().getText();
			String format = selectedReport.getFmt().getName().getValue();
			String[] fm = format.split("\\."); 
			String fmt = fm[fm.length - 1];
			resultsString = txt + " > " + fmt;
			labelFetchStatus.getLabel().setText(resultsString);
			FmtObject formatter = (FmtObject) Generic.instanceForName(format);
			formatter.addSubscriber(this);
			formatter.setData(selectedReport.getFtcs(),collections,collectionProperties,results);
			StringBuffer data = formatter.format();
			int returnVal = fileChooser.showSaveDialog(getGuiObject().getComponent());
			if (returnVal==JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				labelFetchStatus.getLabel().setText(txt + " > " + format + " > " + selectedFile.getName());
				FileObject writer = new FileObject();
				writer.writeFile(selectedFile.getAbsolutePath(),data);
				if (selectedFile.getParent()!=null) {
					GuiConfig.getInstance().setReportDir(selectedFile.getParent());
					GuiConfig.getInstance().serialize();
				}
				publishEvent(new EvtEvent(ACTION_CLOSE_FRAME, this, ACTION_CLOSE_FRAME));
			}
			labelFetchStatus.getLabel().setText(" ");
		} else {
			((GuiDialog) getGuiObject()).msgInfo("The fetch results are empty","Empty results");
		}
	}
	
	private void sendFetchRequest(boolean count) {
		QryFetchList fetchList = new QryFetchList(null);
		if (count) {
			for (int i = 0; i < selectedReport.getFtcs().size(); i++) {
				fetchList.addQuery(getCountQuery(i));
			}
			labelFetchStatus.getLabel().setText("Counting ...");
		} else {
			fetchList.addQuery(getFetchQuery(currentFetchIndex,start));
		}
		ClSession session = GuiController.getInstance().getSession();
		ClRequest request = session.getRequestQueue().getNewRequest(this);
		request.setQueryRequest(fetchList);
		request.addSubscriber(this);
		session.getRequestQueue().addRequest(request, this);
		waitForRequest = request;
	}
	
	private QryFetch getCountQuery(int fetchIndex) {
		QryFetch fetch = null;
		if (selectedReport!=null) {
			DbFetch ftc = selectedReport.getFtcs().get(fetchIndex);
			fetch = new QryFetch(ftc.getColl().getName().getValue());
			fetch.setType(QryFetch.TYPE_COUNT_REFERENCES);
			boolean updateContext = false;
			if (results.getSize()==0) {
				updateContext = true;
			}
			for (DbFetchFilter filter: ftc.getFilters()) {
				boolean active = false;
				PrpCheckBox prpActive = getActiveForFilterName(filter.getName().getValue());
				if (prpActive.getNewValueObjectFromComponentValue().getValue()) {
					active = true;
					GuiProperty guiProp = getValueForFilterName(filter.getName().getValue());
					DtObject filterValue = guiProp.getNewValueObjectFromComponentValue();
					fetch.addCondition(new QryFetchCondition(filter.getProp().getName().getValue(),filter.getInvert().getValue(),filter.getOper().getName().getValue(),filterValue));
				}
				if (updateContext) {
					GuiModel.getInstance().setContextActive(filter.getId().toString(), active);
				}
			}
		}
		return fetch;
	}

	private QryFetch getFetchQuery(int fetchIndex,int start) {
		QryFetch fetch = null;
		if (selectedReport!=null) {
			fetch = getCountQuery(fetchIndex);
			DbFetch ftc = selectedReport.getFtcs().get(fetchIndex);
			if (ftc.getEntiyColls().size()>0) {
				fetch.setType(QryFetch.TYPE_FETCH_OBJECTS_ENTITY);
				for (DbCollection entityCol: ftc.getEntiyColls()) {
					fetch.getEntities().add(entityCol.getName().getValue());
				}
			} else {
				fetch.setType(QryFetch.TYPE_FETCH_OBJECTS);
			}
			fetch.setOrderBy(ftc.getOBy().getName().getValue(),ftc.getOrderAscending().getValue());
			fetch.setStartLimit(start,ftc.getLimit().getValue());
		}
		return fetch;
	}
	
	private String getReportDescriptionToolTip() {
		String r = null;
		if (selectedReport.getDescription().getValue().length()>0) {
			r = "<html>" + Generic.stringBufferReplace(selectedReport.getDescription().getValue(),"\n","<br/>") + "<html>";
		}
		return r;
	}
	
	private void setEnabled(boolean enabled) {
		fetchButton.setEnabled(enabled);
		reportProperty.setEnabled(enabled);
		for (GuiProperty prpActive: filterActivesEnabled) {
			prpActive.setEnabled(enabled);
		}
		for (GuiProperty guiProp: filterValuesEnabled) {
			guiProp.setEnabled(enabled);
		}
	}
	
	private PrpCheckBox getActiveForFilterName(String name) {
		PrpCheckBox prpActive = null;
		int i = getIndexForFilterName(name);
		if (i>=0) {
			prpActive = filterActives.get(i);
		}
		return prpActive;
	}

	private GuiProperty getValueForFilterName(String name) {
		GuiProperty prpValue = null;
		int i = getIndexForFilterName(name);
		if (i>=0) {
			prpValue = filterValues.get(i);
		}
		return prpValue;
	}

	private int getIndexForFilterName(String name) {
		int r = -1;
		int i = 0;
		for (DbFetchFilter filter: selectedReportFilters) {
			if (filter.getName().getValue().equals(name)) {
				r = i;
				break;
			}
			i++;
		}
		return r;
	}
}
