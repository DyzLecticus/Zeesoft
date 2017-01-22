package nl.zeesoft.zadf.controller.impl;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.SwingUtilities;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zadf.gui.GuiPropertySelectObject;
import nl.zeesoft.zadf.gui.GuiPropertySelectValue;
import nl.zeesoft.zadf.gui.panel.PnlDetail;
import nl.zeesoft.zadf.gui.property.PrpIdRefList;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.GuiModelContextFilter;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbPropertyConstraint;
import nl.zeesoft.zadf.model.impl.DbReferenceFilter;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryError;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.MdlObjectRefListMap;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtFloat;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.DbUser;

public class DetailDialogController extends GuiWindowController implements EvtEventSubscriber {
	public static final String 					FETCHING_REFERENCES		= "FETCHING_REFERENCES";
	public static final String 					FETCHED_REFERENCES 		= "FETCHED_REFERENCES";

	public static final String 					SAVE_BUTTON_CLICKED 	= "SAVE_BUTTON_CLICKED";
	
	private GenericDetailController 			detailController		= null;

	private DbCollection						collection				= null;
	
	private MdlDataObject						originalObject			= null;

	private List<GuiModelContextFilter> 		filters					= new ArrayList<GuiModelContextFilter>();

	// Ensures only caller grid is refreshed
	private MainFrameGridController				callerGrid				= null;

	private boolean								initializing			= false;
	private boolean								handlingEvent			= false;
	private boolean								saving					= false;
	private boolean								fetching				= false;
	private String								fetchPropertyValuesText	= "";
	private boolean								formatting				= false;

	private MdlObjectRefListMap					referenceLists			= new MdlObjectRefListMap();
	
	private boolean								attachedChangeListeners = false;
	
	public DetailDialogController(GuiDialog detailDialog) {
		super(detailDialog);
		PnlDetail detailPanel = (PnlDetail) detailDialog.getGuiObjectByName(ZADFFactory.PANEL_DETAIL_PROPERTIES); 
		detailController = new GenericDetailController(detailPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ((!handlingEvent) && (!saving)) {
			handlingEvent = true;
			String action = "";
			GuiObject sourceObject = null;
			boolean handled = false;
	
			if (e.getActionCommand()!=null) {
				action = e.getActionCommand();
			}
	
			if ((e.getSource()!=null) && (e.getSource() instanceof Component)) {
				sourceObject = getGuiObject().getGuiObjectForSourceComponent((Component) e.getSource());
			}
			
			if (action.equals(ZADFFactory.BUTTON_DETAIL_SAVE)) {
				List<DbCollectionProperty> changed = getChangedProperties();
				if (originalObject.getId().getValue()==0) {
					save(getSaveObject(changed));
				} else {
					if (changed.size()>0) {
						save(getSaveObject(changed));
					} else {
						((GuiDialog) getGuiObject()).msgError("No changes to save");
					}
				}
				handled = true;
			} else if (action.equals(GuiPropertySelectValue.SELECT_VALUE_CLICKED)) {
				// Pass select action on to listeners
				publishEvent(new EvtEvent(e.getActionCommand(),getGuiObject(),sourceObject));
				handled = true;
			} else if (action.equals(GuiWindowController.ACTION_CLOSE_FRAME)) {
				boolean forward = false;
				if (originalObject.getId().getValue()==0) {
					forward = true;
				} else {
					List<DbCollectionProperty> changed = getChangedProperties();
					if (changed.size()>0) {
						forward = ((GuiDialog) getGuiObject()).msgConfirmYesNo("Changes have not been saved. Are you sure you want to close this dialog?","Are you sure?");
					} else {
						forward = true;
					}
				}
				if (forward) {
					super.actionPerformed(e);
				}
				handled = true;
			}
			if (!handled) {
				detailController.actionPerformed(e);
			}
			handlingEvent = false;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		propertyChanged(null);
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(DbCollection collection) {
		if (this.collection!=collection) {
			attachedChangeListeners = false;
			this.collection = collection;
	    	GuiDialog dialog = (GuiDialog) getGuiObject();
	    	dialog.getDialog().setIconImage(ZADFFactory.getIconImage(collection.getName().getValue()));
			detailController.setText("");
	    	detailController.setCollection(collection);
		}
		for (DbCollectionProperty dbProp:collection.getDetailProperties()) {
			String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
			GuiProperty prop = (GuiProperty) getGuiObjectByName(propertyName);
			if (prop!=null) {
				prop.setEnabled(false);
			}
		}
	}

	/**
	 * @param object the object to set
	 * @param extendedReferences the extendedReferences to set
	 */
	public void setObject(MdlDataObject object, SortedMap<Long, String> extendedReferences) {
		initializing = true;
		GuiDialog dialog = (GuiDialog) getGuiObject();
    	
		long originalId = -1;
		if (originalObject!=null) {
			originalId = originalObject.getId().getValue();
		}
		
		boolean add = false;
    	if (object!=null) {
			originalObject = object;
	    	if (originalObject.getId().getValue()==0) {
	    		add = true;
	    	}
		} else {
			originalObject = (MdlDataObject) Generic.instanceForName(collection.getName().getValue());
			Date now = new Date();
			originalObject.getCreatedOn().setValue(now);
			originalObject.getChangedOn().setValue(now);
    		add = true;
		}
    	object = MdlDataObject.copy(originalObject);

    	SortedMap<String,DtObject> newValueMap = new TreeMap<String,DtObject>();
    	SortedMap<String,String> newStringValueMap = new TreeMap<String,String>();
		for (DbCollectionProperty dbProp:collection.getProperties()) {
			DtObject newValue = null;
			for (GuiModelContextFilter filter: filters) {
				if (
					(
						(filter.getOperator().equals(QryFetchCondition.OPERATOR_EQUALS)) ||
						(filter.getOperator().equals(QryFetchCondition.OPERATOR_CONTAINS)) 
					) &&
					(filter.getProperty().equals(dbProp.getName().getValue()))
					) {
					if (dbProp.getPropertyClassName().getValue().equals(PrpIdRefList.class.getName())) {
						DtIdRefList idRefList = new DtIdRefList();
						DtIdRef idRef = (DtIdRef) filter.getValue();
						idRefList.getValue().add(idRef.getValue());
						newValue = idRefList;
						newValueMap.put(dbProp.getName().getValue(), idRefList);
						newStringValueMap.put(dbProp.getName().getValue(),filter.getStringValue());
						break;
					} else {
						newValue = filter.getValue();
						newValueMap.put(dbProp.getName().getValue(), filter.getValue());
						newStringValueMap.put(dbProp.getName().getValue(),filter.getStringValue());
						break;
					}
				}
			}
			if (newValue!=null) {
				if (add) {
					originalObject.setPropertyValue(dbProp.getName().getValue(), newValue);
				}
				object.setPropertyValue(dbProp.getName().getValue(), newValue);
			}
		}
    	
		String addOrUpdate = "Update";
		if (add) {
			addOrUpdate = "Add";
		}
    	dialog.getDialog().setTitle(addOrUpdate + " " + collection.getNameSingle().getValue().toLowerCase());
		
		if (object.getId().getValue()!=originalId) {
	    	referenceLists.clear();
			referenceLists.addReference(DbModel.CONTEXT_OBJECT_PREFIX,new MdlObjectRef(object));
		}

		detailController.setObject(object, extendedReferences);

		for (DbCollectionProperty dbProp:collection.getDetailProperties()) {
			DtObject newValue = newValueMap.get(dbProp.getName().getValue());
			String newStringValue = "";
			if (newValue!=null) {
				String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
				GuiProperty prop = (GuiProperty) getGuiObjectByName(propertyName);
				if (prop!=null) {
					prop.getValueObject().setValue(newValue.getValue());
					if (prop instanceof GuiPropertySelectValue) {
						GuiPropertySelectValue select = (GuiPropertySelectValue) prop;
						newStringValue = newStringValueMap.get(dbProp.getName().getValue());
						if (newStringValue==null) {
							newStringValue = "";
						}
						select.setStringValue(newStringValue);
					}
					prop.updateComponentValue();
				}
			}
		}

		List<DbCollectionProperty> enabledProps = collection.getEnabledProperties();
		for (DbCollectionProperty dbProp:collection.getDetailProperties()) {
			boolean enabled = getPropertyEnabledForObject(enabledProps,dbProp);
			String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
			GuiProperty prop = (GuiProperty) getGuiObjectByName(propertyName);
			if (prop!=null) {
				prop.setEnabled(enabled);
			}
		}

		//fetchCrc = 0;
		if (!fetchReferences(object)) {
			resetReferenceFilters();
		}
		initializing = false;
	}

	public void save(MdlDataObject object) {
		saving = true;
		QryTransaction transaction = new QryTransaction(null);
		if (originalObject.getId().getValue()==0) {
			transaction.addQuery(new QryAdd(object));
    	} else {
			transaction.addQuery(new QryUpdate(object));
    	}
		ClSession session = GuiController.getInstance().getSession();
		ClRequest request = session.getRequestQueue().getNewRequest(this);
		request.setQueryRequest(transaction);
		request.addSubscriber(this);
		session.getRequestQueue().addRequest(request, this);
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			//Messenger.getInstance().debug(this, "Handling event: " + e.getType());
			ClRequest request = (ClRequest) e.getValue();
			if (request.getQueryRequest() instanceof QryTransaction) {
				QryObject qry = (QryObject) ((QryTransaction) request.getQueryRequest()).getQueries().get(0);
				if (qry.getErrors().size()>0) {
					String errors = "";
					String show = "";
					int added = 0;
					for (QryError error: qry.getErrors()) {
						String err = DbModel.getQueryErrorMessageForCollection(error, collection.getName().getValue());
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
					Messenger.getInstance().debug(this, "Save response errors:\n" + errors);
				} else {
					callerGrid.delayedRefreshData();
					publishEvent(new EvtEvent(GuiWindowController.ACTION_CLOSE_FRAME,this,GuiWindowController.ACTION_CLOSE_FRAME));
				}
				saving = false;
			} else {
				referenceLists.clear();
				referenceLists.addReference(DbModel.CONTEXT_OBJECT_PREFIX,new MdlObjectRef(getSaveObject(getChangedProperties())));
				QryFetchList fList = (QryFetchList) request.getQueryRequest();
				for (QryObject qry: fList.getQueries()) {
					if (qry instanceof QryFetch) {
						QryFetch fetch = (QryFetch) qry;
						referenceLists.addReferenceList(fetch.getClassName(), fetch.getMainResults());
					}
				}
				resetReferenceFilters();
				fetching = false;
				publishEvent(new EvtEvent(FETCHED_REFERENCES,this,FETCHED_REFERENCES));
			}
		} else if ((e.getType().equals(GuiProperty.ACTION_EVENT)) || (e.getType().equals(GuiProperty.CHANGE_EVENT))) {
			propertyChanged(e);
		}
	}
	
	/**
	 * @param callerGrid the callerGrid to set
	 */
	public void setCallerGrid(MainFrameGridController callerGrid) {
		this.callerGrid = callerGrid;
	}

	/**
	 * @return the filters
	 */
	public List<GuiModelContextFilter> getFilters() {
		return filters;
	}

	private void propertyChanged(EvtEvent e) {
		if (!initializing) {
			if (!formatting) {
				formatting = true;
				for (DbCollectionProperty dbProp: getChangedProperties()) {
					String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
					GuiProperty guiProp = (GuiProperty) getGuiObjectByName(propertyName);
					DtObject valObj = guiProp.getNewValueObjectFromComponentValue();
					if (valObj instanceof DtString) {
						DtString dtString = (DtString) valObj;
						String original = dtString.getValue();
						if ((dbProp.getMinValue().getValue()>=0) && (dbProp.getMaxValue().getValue()<Integer.MAX_VALUE) && (dbProp.getMaxValue().getValue()>=dbProp.getMinValue().getValue())) { 
							if ((valObj.getValue()!=null) && (dtString.getValue().length()>dbProp.getMaxValue().getValue())) {
								dtString.setValue(dtString.getValue().substring(0,Integer.parseInt("" + dbProp.getMaxValue().getValue())));
							}
						}
						List<DbPropertyConstraint> constraints = dbProp.getConstrs();
						if (constraints.size()>0) {
							String constrain = "";
							for (DbPropertyConstraint dbPropConst: constraints) {
								constrain = dbPropConst.getName().getValue();
								constrain = dbPropConst.getName().getValue();
								if (constrain.equals(DtObject.CONSTRAIN_STRING_ALPHABETIC)) { 
									if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
										dtString.setValue(Generic.removeNonAllowedCharactersFromString(dtString.getValue(),Generic.ALPHABETIC));
									}
								} else if (constrain.equals(DtObject.CONSTRAIN_STRING_ALPHABETIC_UNDERSCORE)) { 
									if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
										dtString.setValue(Generic.removeNonAllowedCharactersFromString(dtString.getValue(),"_" + Generic.ALPHABETIC));
									}
								} else if (constrain.equals(DtObject.CONSTRAIN_STRING_ALPHANUMERIC)) { 
									if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
										dtString.setValue(Generic.removeNonAllowedCharactersFromString(dtString.getValue(),Generic.ALPHANUMERIC));
									}
								} else if (constrain.equals(DtObject.CONSTRAIN_STRING_ALPHANUMERIC_UNDERSCORE)) { 
									if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
										dtString.setValue(Generic.removeNonAllowedCharactersFromString(dtString.getValue(),"_" + Generic.ALPHANUMERIC));
									}
								} else if (constrain.equals(DtObject.CONSTRAIN_STRING_NO_SPACE)) { 
									if ((dtString.getValue()!=null) && (dtString.getValue().contains(" "))) {
										dtString.setValue(dtString.getValue().replace(" ", ""));
									}
								} else if (constrain.equals(DtObject.CONSTRAIN_STRING_UPPER_CASE)) { 
									if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
										dtString.setValue(dtString.getValue().toUpperCase());
									}
								} else if (constrain.equals(DtObject.CONSTRAIN_STRING_LOWER_CASE)) { 
									if ((dtString.getValue()!=null) && (dtString.getValue().length()>0)) {
										dtString.setValue(dtString.getValue().toLowerCase());
									}
								}
							}
						}
						if (!dtString.getValue().equals(original)) {
							final String value = dtString.getValue();
							final GuiProperty prop = guiProp;
						    SwingUtilities.invokeLater(new Runnable() {
						    	public void run() {
									prop.getValueObject().setValue(value);
									prop.updateComponentValue();
						    	}
						    });
						}
					} else if (valObj instanceof DtStringBuffer) {
						boolean changed = false;
						DtStringBuffer dtString = (DtStringBuffer) valObj;
						if ((dbProp.getMinValue().getValue()>=0) && (dbProp.getMaxValue().getValue()<Integer.MAX_VALUE) && (dbProp.getMaxValue().getValue()>=dbProp.getMinValue().getValue())) { 
							if ((valObj.getValue()!=null) && (dtString.getValue().length()>dbProp.getMaxValue().getValue())) {
								dtString.setValue(dtString.getValue().substring(0,Integer.parseInt("" + dbProp.getMaxValue().getValue())));
								changed = true;
							}
						}
						if (changed) {
							final StringBuffer value = dtString.getValue();
							final GuiProperty prop = guiProp;
						    SwingUtilities.invokeLater(new Runnable() {
						    	public void run() {
									prop.getValueObject().setValue(value);
									prop.updateComponentValue();
						    	}
						    });
						}
					} else if (valObj instanceof DtDecimal) {
						if ((dbProp.getMinValue().getValue()>=Long.MIN_VALUE) && (dbProp.getMaxValue().getValue()<=Long.MAX_VALUE)) {
							boolean changed = false;
							BigDecimal minValue = new BigDecimal(dbProp.getMinValue().getValue());
							BigDecimal maxValue = new BigDecimal(dbProp.getMaxValue().getValue());
							if (((BigDecimal)valObj.getValue()).compareTo(minValue) == -1) {
								valObj.setValue(new BigDecimal(dbProp.getMinValue().getValue()));
								changed = true;
							} else if (((BigDecimal)valObj.getValue()).compareTo(maxValue) == 1) {
								valObj.setValue(new BigDecimal(dbProp.getMaxValue().getValue()));
								changed = true;
							}
							if (changed) {
								final BigDecimal value = (BigDecimal) valObj.getValue();
								final GuiProperty prop = guiProp;
							    SwingUtilities.invokeLater(new Runnable() {
							    	public void run() {
										prop.getValueObject().setValue(value);
										prop.updateComponentValue();
							    	}
							    });
							}
						}
					} else if (valObj instanceof DtFloat) {
						if ((dbProp.getMinValue().getValue()>=Float.MIN_VALUE) && (dbProp.getMaxValue().getValue()<=Float.MAX_VALUE)) {
							boolean changed = false;
							float minValue = new Float(dbProp.getMinValue().getValue());
							float maxValue = new Float(dbProp.getMaxValue().getValue());
							if ((Float)valObj.getValue()<minValue) {
								valObj.setValue(new Float(minValue));
								changed = true;
							} else if ((Float)valObj.getValue()>maxValue) {
								changed = true;
								valObj.setValue(new Float(maxValue));
							}
							if (changed) {
								final float value = (Float) valObj.getValue();
								final GuiProperty prop = guiProp;
							    SwingUtilities.invokeLater(new Runnable() {
							    	public void run() {
										prop.getValueObject().setValue(value);
										prop.updateComponentValue();
							    	}
							    });
							}
						}
					} else if (valObj instanceof DtInteger) {
						if ((dbProp.getMinValue().getValue()>=Integer.MIN_VALUE) && (dbProp.getMaxValue().getValue()<=Integer.MAX_VALUE)) {
							boolean changed = false;
							int minValue = Integer.parseInt("" + dbProp.getMinValue());
							int maxValue = Integer.parseInt("" + dbProp.getMaxValue());
							if ((Integer)valObj.getValue()<minValue) {
								valObj.setValue(new Integer(minValue));
								changed = true;
							} else if ((Integer)valObj.getValue()>maxValue) {
								valObj.setValue(new Integer(maxValue));
								changed = true;
							}
							if (changed) {
								final int value = (Integer) valObj.getValue();
								final GuiProperty prop = guiProp;
							    SwingUtilities.invokeLater(new Runnable() {
							    	public void run() {
										prop.getValueObject().setValue(value);
										prop.updateComponentValue();
							    	}
							    });
							}
						}
					} else if (valObj instanceof DtLong) {
						boolean changed = false;
						if ((Long)valObj.getValue()<dbProp.getMinValue().getValue()) {
							valObj.setValue(new Long(dbProp.getMinValue().getValue()));
							changed = true;
						} else if ((Long)valObj.getValue()>dbProp.getMaxValue().getValue()) {
							valObj.setValue(new Long(dbProp.getMaxValue().getValue()));
							changed = true;
						}
						if (changed) {
							final long value = (Long) valObj.getValue();
							final GuiProperty prop = guiProp;
						    SwingUtilities.invokeLater(new Runnable() {
						    	public void run() {
									prop.getValueObject().setValue(value);
									prop.updateComponentValue();
						    	}
						    });
						}
					}
				}
				formatting = false;

				if (!fetchReferences(null)) {
					resetReferenceFilters();
				}
			}
		}
	}
	
	private void resetReferenceFilters() {
		//Messenger.getInstance().debug(this, "resetReferenceFilters");
		boolean attached = false;
		for (DbCollectionProperty dbProp: collection.getDetailProperties()) {
			String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
			GuiProperty prop = (GuiProperty) getGuiObjectByName(propertyName);
			if (prop!=null) {
				if (prop instanceof GuiPropertySelectObject) {
					GuiPropertySelectObject selProp = (GuiPropertySelectObject) prop;
					selProp.getFilters().clear();
					for (DbReferenceFilter dbRefFilt: dbProp.getFilters()) {
						String val = dbRefFilt.getValue().getValue();
						String strVal = "";
						if (val.startsWith(DbModel.CONTEXT_OBJECT_PREFIX)) {
							Object valObj = DbModel.getInstance().evaluateExpressionToValue(val,referenceLists);
							if (valObj!=null) {
								val = valObj.toString();
								if (val.startsWith(DbModel.CONTEXT_OBJECT_PREFIX)) {
									// Keep this debug
									Messenger.getInstance().debug(this, "Failed to evaluate: " + dbRefFilt.getValue().getValue() + " to filter value");
									val = prop.getNewValueObjectFromComponentValue().toString();
								} else {
									strVal = DbModel.getInstance().evaluateExpressionToStringValue(dbRefFilt.getValue().getValue(),referenceLists);
									if (strVal==null) {
										strVal = "";
									}
								}
							} else {
								val = prop.getNewValueObjectFromComponentValue().toString();
							}
						}
						DbReferenceFilter propFilt = (DbReferenceFilter) MdlDataObject.copy(dbRefFilt);
						propFilt.getValue().setValue(val);
						propFilt.setStringValue(strVal);
						selProp.getFilters().add(propFilt);
						//Messenger.getInstance().debug(this, "Property: " + selProp.getName() + ", filter: " + propFilt.getName().getValue() + ", value: " + val);
					}
				}
				if (!attachedChangeListeners) {
					prop.addSubscriber(this);
					prop.attachListeners();
					attached = true;
				}
			}
		}
		if (attached) {
			attachedChangeListeners = true;
		}
	}


	private List<DbCollectionProperty> getChangedProperties() {
		List<DbCollectionProperty> r = new ArrayList<DbCollectionProperty>();
		List<DbCollectionProperty> enabledProps = collection.getEnabledProperties();
		for (DbCollectionProperty dbProp:collection.getDetailProperties()) {
			if (enabledProps.contains(dbProp)) {
				if (!dbProp.getPropertyClassName().getValue().equals("")) {
					String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
					GuiProperty prop = (GuiProperty) getGuiObjectByName(propertyName);
					if (prop!=null) {
						DtObject newValue = prop.getNewValueObjectFromComponentValue();
						DtObject oriValue = originalObject.getPropertyValue(dbProp.getName().getValue());
						if (!newValue.equals(oriValue)) {
							r.add(dbProp);
						}
					}
				}
			}
		}
		return r;
	}

	private boolean fetchReferences(MdlDataObject obj) {
		if ((!fetching) && (collection!=null)) {
			//Messenger.getInstance().debug(this, "fetchReferences");
			fetching = true;
			QryFetchList fetchList = new QryFetchList(null);
			// For each reference property; add reference collection to entity list.
			QryFetch fetch = null;
			String txt = "";
			for (DbCollectionProperty dbProp: collection.getProperties()) {
				if (dbProp.getReferenceCollection().getValue()>0) {
					fetch = new QryFetch(dbProp.getRefColl().getName().getValue());
					String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
					GuiProperty prop = (GuiProperty) getGuiObjectByName(propertyName);
					if (prop!=null) {
						DtObject value = null;
						if (obj!=null) {
							value = obj.getPropertyValue(dbProp.getName().getValue());
						} else {
							value = prop.getNewValueObjectFromComponentValue();
						}
						//Messenger.getInstance().debug(this, "Property: " + dbProp.getName().getValue() + ", value: " + value);
						if (!txt.equals("")) {
							txt = txt + Generic.SEP_OBJ; 
						}
						txt = txt + prop.getName() + Generic.SEP_STR + value.toString(); 
						if (value instanceof DtIdRef) {
							fetch = new QryFetch(dbProp.getRefColl().getName().getValue());
							fetch.addCondition(new QryFetchCondition(MdlObject.PROPERTY_ID,QryFetchCondition.OPERATOR_EQUALS,value));
							fetchList.addQuery(fetch);
						} else if (value instanceof DtIdRefList) {
							DtIdRefList idRefList = (DtIdRefList) value;
							for (long id: idRefList.getValue()) {
								fetch = new QryFetch(dbProp.getRefColl().getName().getValue());
								fetch.addCondition(new QryFetchCondition(MdlObject.PROPERTY_ID,QryFetchCondition.OPERATOR_EQUALS,new DtLong(id)));
								fetchList.addQuery(fetch);
							}
						}
					}
				}
			}
			//Messenger.getInstance().debug(this, "Fetch list: " + fetchList.getQueries().size());
			if (fetchList.getQueries().size()>0) {
				if (!fetchPropertyValuesText.equals(txt)) {
					fetchPropertyValuesText = txt;
					publishEvent(new EvtEvent(FETCHING_REFERENCES,this,FETCHING_REFERENCES));
					ClSession session = GuiController.getInstance().getSession();
					ClRequest request = session.getRequestQueue().getNewRequest(this);
					request.setQueryRequest(fetchList);
					request.addSubscriber(this);
					session.getRequestQueue().addRequest(request, this);
				} else {
					fetching = false;
				}
			} else {
				fetching = false;
			}
		}
		return fetching;
	}
	
	private MdlDataObject getSaveObject(List<DbCollectionProperty> changed) {
		MdlDataObject object = MdlDataObject.copy(originalObject);
		for (DbCollectionProperty dbProp: changed) {
			String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
			GuiProperty prop = (GuiProperty) getGuiObjectByName(propertyName);
			if (prop!=null) {
				DtObject newValue = prop.getNewValueObjectFromComponentValue();
				object.setPropertyValue(dbProp.getName().getValue(),newValue);
			}
		}
		return object;
	}
	
	private boolean getPropertyEnabledForObject(List<DbCollectionProperty> enabledProps, DbCollectionProperty dbProp) {
		boolean enabled = true;
		if (!enabledProps.contains(dbProp)) {
			enabled = false;
		}
		if (enabled) {
			if ((collection!=null) && (collection.getName().getValue().equals(DbCollectionProperty.class.getName()))) {
				if (dbProp.getName().equals("userLevelEnabled")) {
					if (detailController.getObject()!=null) {
						if (
							(detailController.getObject().getName().getValue().equals(MdlObject.PROPERTY_ID)) ||
							(detailController.getObject().getName().getValue().equals(MdlObject.PROPERTY_CLASSNAME)) ||
							(detailController.getObject().getName().getValue().equals(MdlObject.PROPERTY_CREATEDBY)) ||
							(detailController.getObject().getName().getValue().equals(MdlObject.PROPERTY_CREATEDON)) ||
							(detailController.getObject().getName().getValue().equals(MdlObject.PROPERTY_CHANGEDBY)) ||
							(detailController.getObject().getName().getValue().equals(MdlObject.PROPERTY_CHANGEDON))
							) {
							enabled = false;
						}
					}
				}
			}
		}
		if (enabled) {
			if ((detailController.getObject()!=null) && (collection!=null)) {
				if (collection.getName().getValue().equals(DbUser.class.getName())) {
					DbUser obj = (DbUser) detailController.getObject();
					if (obj.getId().getValue()==1) {
						if (
							(dbProp.getName().equals("active")) ||
							(dbProp.getName().equals("level")) ||
							(dbProp.getName().equals("admin")) 
							) {
							enabled = false;
						}
					}
				} else if (collection.getName().getValue().equals(DbCollection.class.getName())) {
					if (!GuiController.getInstance().getSession().isUserAdmin()) {
						DbCollection obj = (DbCollection) detailController.getObject();
						if ((dbProp.getName().equals("userLevelFetch")) && (obj.getUserLevelFetch().getValue()<GuiController.getInstance().getSession().getUserLevel())) {
							enabled = false;
						} else if ((dbProp.getName().equals("userLevelUpdate")) && (obj.getUserLevelUpdate().getValue()<GuiController.getInstance().getSession().getUserLevel())) {
							enabled = false;
						} else if ((dbProp.getName().equals("userLevelRemove")) && (obj.getUserLevelRemove().getValue()<GuiController.getInstance().getSession().getUserLevel())) {
							enabled = false;
						} else if ((dbProp.getName().equals("userLevelAdd")) && (obj.getUserLevelAdd().getValue()<GuiController.getInstance().getSession().getUserLevel())) {
							enabled = false;
						}
					}
				} else if (collection.getName().getValue().equals(DbCollectionProperty.class.getName())) {
					if (!GuiController.getInstance().getSession().isUserAdmin()) {
						DbCollectionProperty obj = (DbCollectionProperty) detailController.getObject();
						if (obj.getUserLevelVisible().getValue()<GuiController.getInstance().getSession().getUserLevel()) {
							enabled = false;
						} else if (dbProp.getName().equals("userLevelEnabled")) {
							if (obj.getUserLevelEnabled().getValue()<GuiController.getInstance().getSession().getUserLevel()) {
								enabled = false;
							}
						}
					}
				}
			}
		}

		return enabled;
	}
}
