package nl.zeesoft.zadf.gui.property;

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFormattedTextField;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtInteger;

public class PrpInteger extends GuiProperty {
	private int maxDigits = 10;

	public PrpInteger(String name,int row,int column,DtInteger valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpInteger() {
		super("",0,0,new DtInteger());
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			NumberFormat fmt = NumberFormat.getIntegerInstance(Locale.US);
			fmt.setGroupingUsed(false);
			fmt.setMaximumIntegerDigits(maxDigits);
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
	public DtInteger getNewValueObjectFromComponentValue() {
		DtInteger valueObj = new DtInteger();
		JFormattedTextField comp = (JFormattedTextField) getComponent();
		if (comp!=null) {
			String txt = comp.getText();
			if (!txt.equals("")) {
				try {
					valueObj.setValue(Integer.parseInt(txt));
				} catch (NumberFormatException e) {
					valueObj.setValue(0);
				}
			} else {
				valueObj.setValue(0);
			}
		}
		return valueObj;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new Integer(0);
	}

	@Override 
	public void setToolTipText(String toolTipText) {
		super.setToolTipText(toolTipText);
		if (getComponent()!=null) {
			((JFormattedTextField) getComponent()).setToolTipText(toolTipText);
		}
	}	

	/**
	 * @param maxDigits the maxDigits to set
	 */
	public void setMaxDigits(int maxDigits) {
		this.maxDigits = maxDigits;
	}

}
