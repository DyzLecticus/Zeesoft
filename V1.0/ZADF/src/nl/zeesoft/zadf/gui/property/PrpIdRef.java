package nl.zeesoft.zadf.gui.property;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.gui.GuiPropertySelectObject;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;

public class PrpIdRef extends GuiPropertySelectObject {
	public PrpIdRef(String name,int row,int column,DtIdRef valueObject) {
		super(name,row,column,valueObject);
	}

	public PrpIdRef() {
		super("",0,0,new DtIdRef());
	}

	@Override
	public DtIdRef getNewValueObjectFromComponentValue() {
		return (DtIdRef) getValueObject();
	}

	@Override
	public void updateComponentValue() {
		if (getValueObject()!=null) {
			if (((DtIdRef) getValueObject()).getValue()==0) {
				setStringValue("");
			} else if (getStringValue().equals("")) {
				List<Long> idList = new ArrayList<Long>();
				idList.add(((DtIdRef) getValueObject()).getValue());
				fetchReferences(idList);
			}
		}
		super.updateComponentValue();
	}
}
