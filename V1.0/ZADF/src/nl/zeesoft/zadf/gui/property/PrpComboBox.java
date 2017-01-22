package nl.zeesoft.zadf.gui.property;

import javax.swing.JComboBox;

import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zodb.model.datatypes.DtString;

public class PrpComboBox extends GuiProperty {
	private String[] options = null;

	public PrpComboBox(String name,int row,int column,DtString valueObject,String[] options) {
		super(name,row,column,valueObject);
		this.options = options;
	}
	
	public PrpComboBox() {
		super("",0,0,new DtString());
	}
	
	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			JComboBox<String> comp = new JComboBox<String>(options);
			comp.setToolTipText(getToolTipText());
			setComponent(comp);
			updateComponentValue();
		}
	}

	@Override
	public void updateComponentValue() {
		String value = (String) getValueObject().getValue();
		@SuppressWarnings("unchecked")
		JComboBox<String> comp = (JComboBox<String>) getComponent();
		for (int i = 0; i<options.length; i++) {
			if ((options[i]).toString().equals(value)) {
				comp.setSelectedIndex(i);
			}
		}
	}

	@Override
	public DtString getNewValueObjectFromComponentValue() {
		DtString valueObj = new DtString();
		String value = "";
		@SuppressWarnings("unchecked")
		JComboBox<String> comp = (JComboBox<String>) getComponent();
		if (comp!=null) {
			if (comp.getSelectedIndex()>=0) {
				value = options[comp.getSelectedIndex()];
			}
			valueObj.setValue(value);
		}
		return valueObj;
	}

	@Override
	public Object getGridSampleValueObject() {
		return new String();
	}

	@SuppressWarnings("unchecked")
	@Override 
	public void setToolTipText(String toolTipText) {
		super.setToolTipText(toolTipText);
		if (getComponent()!=null) {
			((JComboBox<String>) getComponent()).setToolTipText(toolTipText);
		}
	}	

	/**
	 * @return the options
	 */
	public String[] getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(String[] options) {
		this.options = options;
	}
}
