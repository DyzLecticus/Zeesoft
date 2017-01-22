package nl.zeesoft.zadf.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;

import javax.swing.JPanel;

import nl.zeesoft.zadf.gui.property.PrpString;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;

public abstract class GuiPropertySelectValue extends GuiProperty {
	public static final String	SELECT_VALUE_CLICKED 	= "SELECT_VALUE_CLICKED";

	private GuiPanel			panel					= null;
	private PrpString			string					= null;
	private GuiButton			button					= null;
	
	private String				stringValue				= "";

	public GuiPropertySelectValue(String name,int row,int column,DtObject valueObject) {
		super(name,row,column,valueObject);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			panel = new GuiPanel("panel",0,0);

			string = new PrpString("string",0,0,new DtString(stringValue));
			string.setFill(GridBagConstraints.HORIZONTAL);
			string.setToolTipText(getToolTipText());
			
			button = new GuiButton(SELECT_VALUE_CLICKED,0,1,"...");
			button.setFill(GridBagConstraints.NONE);
			button.getButton().setMaximumSize(new Dimension(18,18));
			button.getButton().setPreferredSize(new Dimension(18,18));
			button.getButton().setToolTipText(getToolTipText());
			
			panel.getPanelObjects().add(string);
			panel.getPanelObjects().add(button);

			panel.getPanelObjects().setPadding(0);
			
			panel.getPanelObjects().calculateWeights();
			panel.getPanelObjects().getColumnWeights().clear();
			panel.getPanelObjects().getColumnWeights().add(0.99D);
			panel.getPanelObjects().getColumnWeights().add(0.01D);
			
			JPanel pnl = panel.getPanelObjects().renderJPanel();
			pnl.setToolTipText(getToolTipText());
			
			string.setEnabled(false);
			
			setComponent(pnl);
			updateComponentValue();
			
		}
	}

	@Override
	public void updateComponentValue() {
		if (getValueObject().getValue()==null) {
			setStringValue("");
		}
		((DtString) string.getValueObject()).setValue(stringValue);
		string.updateComponentValue();
		propertyChange(new PropertyChangeEvent(button.getComponent(),getName(),"",CHANGE_EVENT));
	}
	
	@Override
	public GuiObject getGuiObjectForSourceComponent(Component source) {
		GuiObject r = super.getGuiObjectForSourceComponent(source);
		if ((r==null) && (panel!=null)) {
			r = panel.getGuiObjectForSourceComponent(source);
			if (r!=null) {
				r = this;
			}
		}
		return r;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new String();
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (button!=null) {
			button.setEnabled(enabled);
		}
	}

	@Override
	public boolean isEnabled() {
		boolean e = false;
		if (button!=null) {
			e = button.isEnabled();
		}
		return e;
	}

	@Override 
	public void setToolTipText(String toolTipText) {
		if (string!=null) {
			string.setToolTipText(toolTipText);
		}
	}	

	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}
}
