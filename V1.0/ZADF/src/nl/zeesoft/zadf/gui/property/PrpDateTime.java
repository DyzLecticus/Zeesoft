package nl.zeesoft.zadf.gui.property;

import nl.zeesoft.zadf.gui.GuiPropertySelectValue;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;

public class PrpDateTime extends GuiPropertySelectValue {
	public PrpDateTime(String name,int row,int column,DtDateTime valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpDateTime() {
		super("",0,0,new DtDateTime());
	}


	@Override
	public void updateComponentValue() {
		DtDateTime val = (DtDateTime) getValueObject();
		if (val.getValue()!=null) {
			setStringValue(val.getValue().toString());
		} else {
			setStringValue("");
		}
		super.updateComponentValue();
	}
	
	@Override
	public DtDateTime getNewValueObjectFromComponentValue() {
		return (DtDateTime) getValueObject();
	}
}
