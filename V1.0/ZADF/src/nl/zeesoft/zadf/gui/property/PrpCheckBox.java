package nl.zeesoft.zadf.gui.property;

import javax.swing.JCheckBox;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;

public class PrpCheckBox extends GuiProperty {

	public PrpCheckBox(String name,int row,int column,DtBoolean valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpCheckBox() {
		super("",0,0,new DtBoolean());
	}
	
	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			JCheckBox comp = new JCheckBox();
			comp.setToolTipText(getToolTipText());
			setComponent(comp);
			updateComponentValue();
		}
	}

	@Override
	public void updateComponentValue() {
		JCheckBox comp = (JCheckBox) getComponent();
		comp.setSelected((Boolean) getValueObject().getValue());
	}

	@Override
	public DtBoolean getNewValueObjectFromComponentValue() {
		DtBoolean valueObj = new DtBoolean();
		JCheckBox comp = (JCheckBox) getComponent();
		if (comp!=null) {
			valueObj.setValue(comp.isSelected());
		}
		return valueObj;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new Boolean(false);
	}
	
	@Override 
	public void setToolTipText(String toolTipText) {
		super.setToolTipText(toolTipText);
		if (getComponent()!=null) {
			((JCheckBox) getComponent()).setToolTipText(toolTipText);
		}
	}	
}
