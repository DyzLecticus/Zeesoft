package nl.zeesoft.zadf.gui.property;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFormattedTextField;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;

public class PrpDecimal extends GuiProperty {

	public PrpDecimal(String name,int row,int column,DtDecimal valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpDecimal() {
		super("",0,0,new DtDecimal());
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			NumberFormat fmt = DecimalFormat.getInstance(Locale.US);
			fmt.setGroupingUsed(false);
			fmt.setMaximumIntegerDigits(10);
			fmt.setMinimumFractionDigits(2);
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
	public DtDecimal getNewValueObjectFromComponentValue() {
		DtDecimal valueObj = new DtDecimal();
		JFormattedTextField comp = (JFormattedTextField) getComponent();
		if (comp!=null) {
			String txt = comp.getText();
			if (!txt.equals("")) {
				try {
					valueObj.setValue(new BigDecimal(txt));
				} catch (NumberFormatException e) {
					valueObj.setValue(new BigDecimal("0.00"));
				}
			} else {
				valueObj.setValue(new BigDecimal("0.00"));
			}
		}
		return valueObj;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new BigDecimal("0.00");
	}

	@Override 
	public void setToolTipText(String toolTipText) {
		super.setToolTipText(toolTipText);
		if (getComponent()!=null) {
			((JFormattedTextField) getComponent()).setToolTipText(toolTipText);
		}
	}	
}
