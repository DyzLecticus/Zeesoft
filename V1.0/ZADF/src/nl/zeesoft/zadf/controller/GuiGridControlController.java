package nl.zeesoft.zadf.controller;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import nl.zeesoft.zadf.gui.GuiButton;
import nl.zeesoft.zadf.gui.GuiLabel;
import nl.zeesoft.zadf.gui.GuiPanel;
import nl.zeesoft.zadf.gui.GuiPanelObjectList;
import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zadf.gui.GuiPropertySelectValue;
import nl.zeesoft.zadf.gui.panel.PnlGridControl;
import nl.zeesoft.zadf.gui.property.PrpCheckBox;
import nl.zeesoft.zadf.gui.property.PrpComboBox;
import nl.zeesoft.zadf.gui.property.PrpDateTime;
import nl.zeesoft.zadf.gui.property.PrpDecimal;
import nl.zeesoft.zadf.gui.property.PrpFloat;
import nl.zeesoft.zadf.gui.property.PrpIdRef;
import nl.zeesoft.zadf.gui.property.PrpIdRefList;
import nl.zeesoft.zadf.gui.property.PrpInteger;
import nl.zeesoft.zadf.gui.property.PrpLong;
import nl.zeesoft.zadf.gui.property.PrpString;
import nl.zeesoft.zadf.gui.property.PrpStringBuffer;
import nl.zeesoft.zadf.model.GuiModelContextFilter;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;

public abstract class GuiGridControlController extends GuiPropertyPanelController {
	private	DbCollection					collection				= null;

	private boolean							showAddUpdateRemove 	= true;
	private GuiButton 						buttonAdd 				= null;
	private GuiButton 						buttonUpdate			= null;
	private GuiButton 						buttonRemove			= null;
	
	private GuiButton 						buttonPrev 				= null;
	private GuiButton 						buttonFetch				= null;
	private GuiButton 						buttonNext 				= null;
	private PrpInteger 						start 					= null;
	private PrpInteger 						limit 					= null;

	private boolean							showResults 			= true;
	private GuiLabel 						results					= null;
	
	private List<String>					filterProperties		= new ArrayList<String>();

	private List<PrpCheckBox>				filterActives			= new ArrayList<PrpCheckBox>();
	private List<GuiLabel>					filterPropertyLabels	= new ArrayList<GuiLabel>();
	private List<PrpCheckBox>				filterInverts			= new ArrayList<PrpCheckBox>();
	private List<PrpComboBox>				filterOperators			= new ArrayList<PrpComboBox>();
	private List<GuiProperty>				filterInputs			= new ArrayList<GuiProperty>();
	
	private List<GuiModelContextFilter> 	filters					= new ArrayList<GuiModelContextFilter>();

	public GuiGridControlController(PnlGridControl controlPanel,boolean showResults,boolean showAddUpdateRemove) {
		super(controlPanel);
		this.showResults = showResults;
		this.showAddUpdateRemove = showAddUpdateRemove;
	}
		
	protected void refresh() {
		PnlGridControl panel = (PnlGridControl) getGuiObject();
		GuiPanelObjectList panelObjects = panel.getPanelObjects();

		clearFilterProperties();
		panelObjects.clear();

		JPanel propertyPanel = null;
		if (collection!=null) {
			GuiPanel fetchPanel = new GuiPanel("fetchPanel",0,0) {
				@Override
				public void renderComponent() {
					getPanelObjects().calculateWeights();
					getPanelObjects().getColumnWeights().clear();
					getPanelObjects().getColumnWeights().add(0.001D);
					getPanelObjects().getColumnWeights().add(0.001D);
					getPanelObjects().getColumnWeights().add(0.001D);
					getPanelObjects().getColumnWeights().add(0.3D);
					getPanelObjects().getColumnWeights().add(0.3D);
					if (showAddUpdateRemove) {
						getPanelObjects().getColumnWeights().add(0.394D);
						getPanelObjects().getColumnWeights().add(0.001D);
						getPanelObjects().getColumnWeights().add(0.001D);
						getPanelObjects().getColumnWeights().add(0.001D);
					} else {
						getPanelObjects().getColumnWeights().add(0.397D);
					}
					super.renderComponent();
				}
				
			};
			fetchPanel.setGridWidth(5);
			fetchPanel.setAnchor(GridBagConstraints.FIRST_LINE_START);
			fetchPanel.setFill(GridBagConstraints.HORIZONTAL);

			fetchPanel.getPanel().setLayout(new GridBagLayout());
			fetchPanel.getPanel().setMinimumSize(new Dimension(300,40));
			fetchPanel.getPanel().setPreferredSize(new Dimension(300,40));
			fetchPanel.getGridBagConstraints().fill = GridBagConstraints.HORIZONTAL;
			fetchPanel.getGridBagConstraints().anchor = GridBagConstraints.FIRST_LINE_START;
			fetchPanel.getGridBagConstraints().gridheight = 1;
			fetchPanel.getGridBagConstraints().gridwidth = 1;
			fetchPanel.getGridBagConstraints().gridx = 0;
			fetchPanel.getGridBagConstraints().gridy = 0;
			fetchPanel.getGridBagConstraints().weightx = 1.0;
			fetchPanel.getGridBagConstraints().weighty = 1.0;

			int column = 0;
			buttonPrev = new GuiButton("prev",0,column,"<");
			column++;
			buttonFetch = new GuiButton("fetch",0,column,"Fetch");
			column++;
			buttonNext = new GuiButton("next",0,column,">");
			column++;
			
			start = new PrpInteger("start",0,column,new DtInteger(0));
			start.setMaxDigits(8);
			column++;
			start.setFill(GridBagConstraints.HORIZONTAL);
			limit = new PrpInteger("limit",0,column,new DtInteger(100));
			limit.setMaxDigits(4);
			column++;
			limit.setFill(GridBagConstraints.HORIZONTAL);
			
			results = new GuiLabel("results",0,column,"");
			column++;
			results.setFill(GridBagConstraints.NONE);
			results.setAnchor(GridBagConstraints.CENTER);
			if (!showResults) {
				results.getLabel().setVisible(false);
			}

			if (showAddUpdateRemove) {
				buttonAdd = new GuiButton("add",0,column,"Add");
				column++;
				buttonUpdate = new GuiButton("update",0,column,"Update");
				column++;
				buttonRemove = new GuiButton("remove",0,column,"Remove");
				column++;
			}
			
			fetchPanel.getPanelObjects().add(buttonPrev);
			fetchPanel.getPanelObjects().add(buttonFetch);
			fetchPanel.getPanelObjects().add(buttonNext);
			fetchPanel.getPanelObjects().add(start);
			fetchPanel.getPanelObjects().add(limit);
			fetchPanel.getPanelObjects().add(results);
			if (showAddUpdateRemove) {
				fetchPanel.getPanelObjects().add(buttonAdd);
				fetchPanel.getPanelObjects().add(buttonUpdate);
				fetchPanel.getPanelObjects().add(buttonRemove);
			}
			
			panelObjects.add(fetchPanel);
			
			for (DbCollectionProperty dbProp: collection.getFilterProperties()) {
				if (!dbProp.getPropertyClassName().getValue().equals("")) {
					String propertyClassName = dbProp.getPropertyClassName().getValue();
					if (propertyClassName.equals(PrpIdRefList.class.getName())) {
						propertyClassName = PrpIdRef.class.getName();
					}
					String propertyName = collection.getName().getValue() + Generic.SEP_STR + dbProp.getName().getValue();
					GuiProperty guiProp = (GuiProperty) Generic.instanceForName(propertyClassName); 
					guiProp.setName(propertyName);
					addFilterProperty(false,dbProp.getName().getValue(),dbProp.getLabel().getValue(),false,guiProp,dbProp.getToolTipText(guiProp.getValueObject()));
				}
			}
			
			GuiLabel lbl = null;

			lbl = new GuiLabel("filter",1,0,"<html><b>Filters</b></html>");
			lbl.setAnchor(GridBagConstraints.FIRST_LINE_START);
			lbl.setGridWidth(5);
			panelObjects.add(lbl);

			lbl = new GuiLabel("active",2,0,"Active");
			lbl.setAnchor(GridBagConstraints.FIRST_LINE_START);
			panelObjects.add(lbl);

			lbl = new GuiLabel("property",2,1,"Property");
			lbl.setAnchor(GridBagConstraints.FIRST_LINE_START);
			panelObjects.add(lbl);

			lbl = new GuiLabel("invert",2,2,"Invert");
			lbl.setAnchor(GridBagConstraints.FIRST_LINE_START);
			panelObjects.add(lbl);
			
			lbl = new GuiLabel("operator",2,3,"Operator");
			lbl.setAnchor(GridBagConstraints.FIRST_LINE_START);
			panelObjects.add(lbl);

			lbl = new GuiLabel("value",2,4,"Value");
			lbl.setAnchor(GridBagConstraints.FIRST_LINE_START);
			panelObjects.add(lbl);
			
			for (int p = 0; p<filterActives.size(); p++) {
				panelObjects.add(filterActives.get(p));
				panelObjects.add(filterPropertyLabels.get(p));
				panelObjects.add(filterInverts.get(p));
				panelObjects.add(filterOperators.get(p));
				panelObjects.add(filterInputs.get(p));
			}
			
			panelObjects.calculateWeights();
			panelObjects.getColumnWeights().clear();
			panelObjects.getColumnWeights().add(0.01D);
			panelObjects.getColumnWeights().add(0.01D);
			panelObjects.getColumnWeights().add(0.01D);
			panelObjects.getColumnWeights().add(0.01D);
			panelObjects.getColumnWeights().add(0.96D);
			propertyPanel = panelObjects.renderJPanel();

			if (showAddUpdateRemove) {
				int userLevel = GuiController.getInstance().getSession().getUserLevel();
				if (userLevel>collection.getUserLevelAdd().getValue()) {
					buttonAdd.setEnabled(false);
				}
				if (userLevel>collection.getUserLevelUpdate().getValue()) {
					buttonUpdate.setEnabled(false);
				}
				if (userLevel>collection.getUserLevelRemove().getValue()) {
					buttonRemove.setEnabled(false);
				}
			}

			if (!isEnabled()) {
				setEnabled();
			}
		}
		panel.setJPanel(propertyPanel);
	}

	private void clearFilterProperties() {
		filterProperties.clear();
		filterActives.clear();
		filterPropertyLabels.clear();
		filterInverts.clear();
		filterOperators.clear();
		filterInputs.clear();
	}
	
	private void addFilterProperty(boolean active, String name, String label, boolean invert, GuiProperty input, String toolTip) {
		int index = (filterActives.size() + 3);

		String[] operators = null;
		String operator = QryFetchCondition.OPERATOR_EQUALS;
		if (
			(input instanceof PrpIdRef) ||
			(input instanceof PrpIdRefList)
			) {
			operator = QryFetchCondition.OPERATOR_CONTAINS;
			operators = new String[]{
				QryFetchCondition.OPERATOR_EQUALS,
				QryFetchCondition.OPERATOR_CONTAINS
				};
		} else if (
			(input instanceof PrpLong) ||
			(input instanceof PrpDecimal) ||
			(input instanceof PrpFloat) ||
			(input instanceof PrpInteger) ||
			(input instanceof PrpDateTime)
			) {
			operators = new String[]{
				QryFetchCondition.OPERATOR_EQUALS,
				QryFetchCondition.OPERATOR_GREATER,
				QryFetchCondition.OPERATOR_GREATER_OR_EQUALS,
				QryFetchCondition.OPERATOR_LESS,
				QryFetchCondition.OPERATOR_LESS_OR_EQUALS
				};
		} else if (
			(input instanceof PrpString)
			) {
			operators = new String[]{
				QryFetchCondition.OPERATOR_EQUALS,
				QryFetchCondition.OPERATOR_CONTAINS,
				QryFetchCondition.OPERATOR_MATCHES,
				QryFetchCondition.OPERATOR_STARTSWITH,
				QryFetchCondition.OPERATOR_ENDSWITH
				};
		} else if (
			(input instanceof PrpStringBuffer)
			) {
			operators = new String[]{
				QryFetchCondition.OPERATOR_EQUALS,
				QryFetchCondition.OPERATOR_CONTAINS,
				QryFetchCondition.OPERATOR_MATCHES,
				QryFetchCondition.OPERATOR_STARTSWITH,
				QryFetchCondition.OPERATOR_ENDSWITH
				};
		} else if (
			(input instanceof PrpCheckBox)
			) {
			operators = new String[]{QryFetchCondition.OPERATOR_EQUALS};
		} else {
			operators = Generic.getValuesFromString(QryFetchCondition.OPERATORS);
		}
		
		PrpCheckBox a = new PrpCheckBox("active" + index,index,0,new DtBoolean(active));
		a.setAnchor(GridBagConstraints.FIRST_LINE_START);
		a.setFill(GridBagConstraints.NONE);
		
		GuiLabel p = new GuiLabel("property" + index,index,1,label);
		p.setAnchor(GridBagConstraints.FIRST_LINE_START);
		p.setFill(GridBagConstraints.NONE);
		p.getLabel().setToolTipText(toolTip);

		PrpCheckBox i = new PrpCheckBox("invert" + index,index,2,new DtBoolean(invert));
		i.setAnchor(GridBagConstraints.FIRST_LINE_START);
		i.setFill(GridBagConstraints.NONE);
		
		PrpComboBox o = new PrpComboBox("operator" + index,index,3,new DtString(operator),operators);
		o.setAnchor(GridBagConstraints.FIRST_LINE_START);
		o.setFill(GridBagConstraints.HORIZONTAL);
		
		input.setRow(index);
		input.setColumn(4);
		input.setAnchor(GridBagConstraints.FIRST_LINE_START);
		input.setFill(GridBagConstraints.HORIZONTAL);
		input.setToolTipText(toolTip);
				
		filterProperties.add(name);
		filterActives.add(a);
		filterPropertyLabels.add(p);
		filterInverts.add(i);
		filterOperators.add(o);
		filterInputs.add(input);

		for (GuiModelContextFilter f: filters) {
			if (f.getProperty().equals(name)) {
				a.getValueObject().setValue(f.isActive());
				i.getValueObject().setValue(f.isInvert());
				o.getValueObject().setValue(f.getOperator());
				input.getValueObject().setValue(f.getValue().getValue());
				if (input instanceof GuiPropertySelectValue) {
					((GuiPropertySelectValue) input).setStringValue(f.getStringValue());
				}
			}
		}
	}

	public List<GuiModelContextFilter> getFilters() {
		filters = new ArrayList<GuiModelContextFilter>();
		for (int p = 0; p<filterActives.size(); p++) {
			boolean active = ((DtBoolean) filterActives.get(p).getNewValueObjectFromComponentValue()).getValue();
			String property = filterProperties.get(p);
			boolean invert = ((DtBoolean) filterInverts.get(p).getNewValueObjectFromComponentValue()).getValue();
			String operator = ((DtString) filterOperators.get(p).getNewValueObjectFromComponentValue()).getValue();
			DtObject value = (DtObject) filterInputs.get(p).getNewValueObjectFromComponentValue();
			String stringValue = "";
			if (filterInputs.get(p) instanceof GuiPropertySelectValue) {
				stringValue = ((GuiPropertySelectValue) filterInputs.get(p)).getStringValue();
			}
			filters.add(new GuiModelContextFilter(active, property, invert, operator, value, stringValue));
		}
		return filters;
	}
	
	public void setFilters(List<GuiModelContextFilter> filters) {
		if (filters!=null) {
			this.filters = filters;
		}
	}

	public int getStart() {
		int r = 0;
		if (start!=null) {
			r = ((DtInteger) start.getNewValueObjectFromComponentValue()).getValue();
		}
		return r;
	}

	public int getLimit() {
		int r = 0;
		if (limit!=null) {
			r = ((DtInteger) limit.getNewValueObjectFromComponentValue()).getValue();
		}
		return r;
	}

	public void setStart(int s) {
		if (start!=null) {
			((DtInteger) start.getValueObject()).setValue(checkStart(s));
			start.updateComponentValue();
		}
	}

	public void setLimit(int l) {
		if (limit!=null) {
			((DtInteger) limit.getValueObject()).setValue(checkLimit(l));
			limit.updateComponentValue();
		}
	}

	public void setResults(String text, String toolTip) {
		if (results!=null) {
			if ((toolTip!=null) && (toolTip.equals(""))) {
				toolTip = null;
			}
			results.getLabel().setText(text);
			results.getLabel().setToolTipText(toolTip);
		}
		if (start!=null) {
			start.setToolTipText(toolTip);
		}
		if (limit!=null) {
			limit.setToolTipText(toolTip);
		}
	}
	
	private int checkStart(int start) {
		if (start<0) {
			start = 0;
		}
		return start;
	}
	
	private int checkLimit(int limit) {
		if (limit<0) {
			limit = 1;
		}
		return limit;
	}
	
	/**
	 * @param collection the collection to set
	 */
	public synchronized void setCollection(DbCollection collection) {
		this.collection = collection;
		refresh();
	}
}
