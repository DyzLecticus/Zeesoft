package nl.zeesoft.zadf.gui.property;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtString;

public class PrpString extends GuiProperty {
	private int columns = 16;

	public PrpString(String name,int row,int column,DtString valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpString() {
		super("",0,0,new DtString());
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			DefaultFormatter fmt = new DefaultFormatter();
			JFormattedTextField comp = new JFormattedTextField(fmt);
			comp.setColumns(columns);
			comp.setToolTipText(getToolTipText());
			setComponent(comp);
			updateComponentValue();
		}
	}

	@Override
	public void updateComponentValue() {
		JFormattedTextField comp = (JFormattedTextField) getComponent();
		comp.setText((String) getValueObject().getValue());
	}

	@Override
	public DtString getNewValueObjectFromComponentValue() {
		DtString valueObj = new DtString();
		JFormattedTextField comp = (JFormattedTextField) getComponent();
		if (comp!=null) {
			valueObj.setValue(comp.getText());
		}
		return valueObj;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new String();
	}

	@Override 
	public void setToolTipText(String toolTipText) {
		super.setToolTipText(toolTipText);
		if (getComponent()!=null) {
			((JFormattedTextField) getComponent()).setToolTipText(toolTipText);
		}
	}	

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}
}
