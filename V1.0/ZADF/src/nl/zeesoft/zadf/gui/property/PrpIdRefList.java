package nl.zeesoft.zadf.gui.property;

import nl.zeesoft.zadf.gui.GuiPropertySelectObject;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;

public class PrpIdRefList extends GuiPropertySelectObject {
	public PrpIdRefList(String name,int row,int column,DtIdRefList valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpIdRefList() {
		super("",0,0,new DtIdRefList());
	}

	@Override
	public DtIdRefList getNewValueObjectFromComponentValue() {
		return (DtIdRefList) getValueObject();
	}

	@Override
	public void updateComponentValue() {
		if (getValueObject()!=null) {
			if (((DtIdRefList) getValueObject()).getValue().size()==0) {
				setStringValue("");
			} else if (getStringValue().equals("")) {
				fetchReferences(((DtIdRefList) getValueObject()).getValue());
			}
		}
		super.updateComponentValue();
	}
}
