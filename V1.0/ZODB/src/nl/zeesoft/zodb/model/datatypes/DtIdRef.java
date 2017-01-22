package nl.zeesoft.zodb.model.datatypes;

import nl.zeesoft.zodb.model.MdlDataObject;

public class DtIdRef extends DtLong {
	public void setValue(MdlDataObject value) {
		super.setValue(value.getId().getValue());
	}

	public DtIdRef() {
		super(new Long(0));
	}

	public DtIdRef(long l) {
		super(l);
	}
}
