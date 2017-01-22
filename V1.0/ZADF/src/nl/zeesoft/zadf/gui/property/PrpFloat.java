package nl.zeesoft.zadf.gui.property;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFormattedTextField;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtFloat;

public class PrpFloat extends GuiProperty {

	public PrpFloat(String name,int row,int column,DtFloat valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpFloat() {
		super("",0,0,new DtFloat());
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			NumberFormat fmt = DecimalFormat.getInstance(Locale.US);
			fmt.setGroupingUsed(false);
			fmt.setMaximumIntegerDigits(10);
			fmt.setMinimumFractionDigits(1);
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
	public DtFloat getNewValueObjectFromComponentValue() {
		DtFloat valueObj = new DtFloat();
		JFormattedTextField comp = (JFormattedTextField) getComponent();
		if (comp!=null) {
			String txt = comp.getText();
			if (!txt.equals("")) {
				try {
					valueObj.setValue(Float.parseFloat(txt));
				} catch (NumberFormatException e) {
					valueObj.setValue(0F);
				}
			} else {
				valueObj.setValue(0F);
			}
		}
		return valueObj;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new Float(0);
	}

	@Override 
	public void setToolTipText(String toolTipText) {
		super.setToolTipText(toolTipText);
		if (getComponent()!=null) {
			((JFormattedTextField) getComponent()).setToolTipText(toolTipText);
		}
	}	
}
