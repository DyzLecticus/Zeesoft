package nl.zeesoft.zadf.gui.property;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public class PrpStringBuffer extends GuiProperty {
	private int columns = 16;

	public PrpStringBuffer(String name,int row,int column,DtStringBuffer valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpStringBuffer() {
		super("",0,0,new DtStringBuffer());
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
		comp.setText(((StringBuffer) getValueObject().getValue()).toString());
	}

	@Override
	public DtStringBuffer getNewValueObjectFromComponentValue() {
		DtStringBuffer valueObj = new DtStringBuffer();
		JFormattedTextField comp = (JFormattedTextField) getComponent();
		if (comp!=null) {
			valueObj.setValue(new StringBuffer(comp.getText()));
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
