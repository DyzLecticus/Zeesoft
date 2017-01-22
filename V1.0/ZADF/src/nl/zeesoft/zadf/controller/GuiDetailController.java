package nl.zeesoft.zadf.controller;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import javax.swing.JPanel;

import nl.zeesoft.zadf.gui.GuiLabel;
import nl.zeesoft.zadf.gui.GuiPanelObjectList;
import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zadf.gui.panel.PnlDetail;
import nl.zeesoft.zadf.gui.property.PrpIdRef;
import nl.zeesoft.zadf.gui.property.PrpIdRefList;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbPropertyConstraint;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtObject;

public abstract class GuiDetailController extends GuiPropertyPanelController {
	private	DbCollection				collection				= null;
	
	private MdlDataObject				object					= null;
	private SortedMap<Long, String> 	extendedReferences		= null;
	
	private List<String>				detailProperties		= new ArrayList<String>();

	private List<GuiLabel>				detailPropertyLabels	= new ArrayList<GuiLabel>();
	private List<GuiProperty>			detailInputs			= new ArrayList<GuiProperty>();
	
	private String						text					= "";
	
	public GuiDetailController(PnlDetail detailPanel) {
		super(detailPanel);
	}
		
	protected void refresh() {
		PnlDetail panel = (PnlDetail) getGuiObject();
		GuiPanelObjectList panelObjects = panel.getPanelObjects();

		clearDetailProperties();
		panelObjects.clear();

		JPanel propertyPanel = null;
		
		if (!text.equals("")) {
			GuiLabel txt = new GuiLabel("text",0,0,text);
			txt.setAnchor(GridBagConstraints.FIRST_LINE_START);
			txt.setFill(GridBagConstraints.BOTH);
			panelObjects.add(txt);
			propertyPanel = panelObjects.renderJPanel();
		} else if (collection!=null) {
			for (DbCollectionProperty dbProp: collection.getDetailProperties()) {
				if (!dbProp.getPropertyClassName().getValue().equals("")) {
					String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
					GuiProperty guiProp = (GuiProperty) Generic.instanceForName(dbProp.getPropertyClassName().getValue()); 
					guiProp.setName(propertyName);
					if (object!=null) {
						DtObject valObj = object.getPropertyValue(dbProp.getName().getValue());
						if ((valObj!=null) && (valObj.getValue()!=null)) {
							guiProp.setValueObject(valObj);
							if (extendedReferences!=null) { 
								if (guiProp instanceof PrpIdRef) {
									DtIdRef valRef = (DtIdRef) valObj;
									PrpIdRef prpRef = (PrpIdRef) guiProp;
									if (extendedReferences.containsKey(valRef.getValue())) {
										prpRef.setStringValue(extendedReferences.get(valRef.getValue()));
									}
								} else if (guiProp instanceof PrpIdRefList) {
									DtIdRefList valRefList = (DtIdRefList) valObj;
									PrpIdRefList prpRefList = (PrpIdRefList) guiProp;
									List<String> nameList = new ArrayList<String>();
									for (long id: valRefList.getValue()) {
										if (extendedReferences.containsKey(id)) {
											nameList.add(extendedReferences.get(id));
											if (nameList.size()>=3) {
												break;
											}
										}
									}
									prpRefList.setStringValue(DbModel.getStringValueForNameList(nameList));
								}
							}
						}
					}
					String label = dbProp.getLabel().getValue();
					List<DbPropertyConstraint> constraints = dbProp.getConstrs();
					if (constraints.size()>0) {
						for (DbPropertyConstraint constraint: constraints) {
							if (constraint.getName().getValue().equals(DtObject.CONSTRAIN_PROPERTY_MANDATORY)) {
								label = label + "*";
							}
						}
					}
					addDetailProperty(dbProp.getName().getValue(),label,guiProp,dbProp.getToolTipText(guiProp.getValueObject()));
				}
			}
			
			for (int p = 0; p<detailPropertyLabels.size(); p++) {
				panelObjects.add(detailPropertyLabels.get(p));
				panelObjects.add(detailInputs.get(p));
			}
			
			panelObjects.calculateWeights();
			panelObjects.getColumnWeights().clear();
			panelObjects.getColumnWeights().add(0.01D);
			panelObjects.getColumnWeights().add(0.99D);
			
			propertyPanel = panelObjects.renderJPanel();
			if (!isEnabled()) {
				setEnabled();
			}
		}
		panel.setJPanel(propertyPanel);
	}

	protected void refreshPropertyValues() {
		if ((text.equals("")) && (collection!=null)) {
			PnlDetail panel = (PnlDetail) getGuiObject();
			MdlDataObject displayObject = null;
			if (object!=null) {
				displayObject = object;
			} else {
				displayObject = (MdlDataObject) Generic.instanceForName(collection.getName().getValue());
			}
			for (DbCollectionProperty dbProp: collection.getDetailProperties()) {
				if (!dbProp.getPropertyClassName().getValue().equals("")) {
					String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
					GuiProperty guiProp = (GuiProperty) panel.getGuiObjectByName(propertyName); 
					if ((guiProp!=null) && (displayObject!=null)) {
						DtObject valObj = displayObject.getPropertyValue(dbProp.getName().getValue());
						if (valObj!=null) {
							guiProp.setValueObject(valObj);
							if (extendedReferences!=null) { 
								if (guiProp instanceof PrpIdRef) {
									DtIdRef valRef = (DtIdRef) valObj;
									PrpIdRef prpRef = (PrpIdRef) guiProp;
									if (extendedReferences.containsKey(valRef.getValue())) {
										prpRef.setStringValue(extendedReferences.get(valRef.getValue()));
									}
								} else if (guiProp instanceof PrpIdRefList) {
									DtIdRefList valRefList = (DtIdRefList) valObj;
									PrpIdRefList prpRefList = (PrpIdRefList) guiProp;
									List<String> nameList = new ArrayList<String>();
									for (long id: valRefList.getValue()) {
										if (extendedReferences.containsKey(id)) {
											nameList.add(extendedReferences.get(id));
											if (nameList.size()>=3) {
												break;
											}
										}
									}
									prpRefList.setStringValue(DbModel.getStringValueForNameList(nameList));
								}
							}
						}
						guiProp.updateComponentValue();
					}
				}
			}
		}
	}
	
	private void clearDetailProperties() {
		detailProperties.clear();
		detailPropertyLabels.clear();
		detailInputs.clear();
	}
	
	private void addDetailProperty(String name, String label, GuiProperty input, String toolTip) {
		int index = (detailPropertyLabels.size());

		GuiLabel p = new GuiLabel("property" + index,index,0,label);
		p.setAnchor(GridBagConstraints.FIRST_LINE_START);
		p.setFill(GridBagConstraints.NONE);
		p.getLabel().setToolTipText(toolTip);

		input.setRow(index);
		input.setColumn(1);
		input.setAnchor(GridBagConstraints.FIRST_LINE_START);
		input.setFill(GridBagConstraints.HORIZONTAL);
		input.setToolTipText(toolTip);
				
		detailProperties.add(name);
		detailPropertyLabels.add(p);
		detailInputs.add(input);
	}
	
	/**
	 * @param collection the collection to set
	 */
	public void setCollection(DbCollection collection) {
		this.collection = collection;
		if (text.equals("")) {
			refresh();
		}
	}

	/**
	 * @param object the object to set
	 * @param extendedReferences the extendedReferences to set
	 */
	public void setObject(MdlDataObject object, SortedMap<Long, String> extendedReferences) {
		this.object = object;
		this.extendedReferences = extendedReferences;
		if (text.equals("")) {
			refreshPropertyValues();
		}
	}

	/**
	 * @return the object
	 */
	public MdlDataObject getObject() {
		return object;
	}

	/**
	 * @return the extendedReferences
	 */
	public SortedMap<Long, String> getExtendedReferences() {
		return extendedReferences;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
		refresh();
	}

}
