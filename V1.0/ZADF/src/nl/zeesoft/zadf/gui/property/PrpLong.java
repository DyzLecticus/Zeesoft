package nl.zeesoft.zadf.gui.property;

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFormattedTextField;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtLong;

public class PrpLong extends GuiProperty {

	public PrpLong(String name,int row,int column,DtLong valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpLong() {
		super("",0,0,new DtLong());
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			NumberFormat fmt = NumberFormat.getIntegerInstance(Locale.US);
			fmt.setGroupingUsed(false);
			fmt.setMaximumIntegerDigits(10);
			JFormattedTextField comp = new JFormattedTextField(fmt);
			comp.setToolTipText(getToolTipText());
			setComponent(comp);
			updateComponentValue();
		}
	}

	@Override
	public void updateComponentValue() {
		JFormattedTextField comp = (JFormattedTextField) getComponent();
		comp.setText(getValueObject().toString());
	}

	@Override
	public DtLong getNewValueObjectFromComponentValue() {
		DtLong valueObj = new DtLong();
		JFormattedTextField comp = (JFormattedTextField) getComponent();
		if (comp!=null) {
			String txt = comp.getText();
			if (!txt.equals("")) {
				try {
					valueObj.setValue(Long.parseLong(txt));
				} catch (NumberFormatException e) {
					valueObj.setValue(0L);
				}
			} else {
				valueObj.setValue(0L);
			}
		}
		return valueObj;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new Long(0);
	}

	@Override 
	public void setToolTipText(String toolTipText) {
		super.setToolTipText(toolTipText);
		if (getComponent()!=null) {
			((JFormattedTextField) getComponent()).setToolTipText(toolTipText);
		}
	}	
}
