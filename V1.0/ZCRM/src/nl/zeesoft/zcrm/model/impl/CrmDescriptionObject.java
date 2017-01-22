package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public abstract class CrmDescriptionObject extends MdlDataObject {
	private DtStringBuffer				description					= new DtStringBuffer();

	@ConstrainPropertyLength(minValue=0,maxValue=1024)
	@PersistProperty(property="description",label="Description")
	public DtStringBuffer getDescription() {
		return description;
	}
}
