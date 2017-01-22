package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zodb.model.annotations.ConstrainPropertyAlphanumeric;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtString;

public abstract class CrmContactObject extends CrmDescriptionObject {
	private DtBoolean					active						= new DtBoolean(true);
	private DtString 					telephone					= new DtString();
	private DtString 					email						= new DtString();

	@PersistProperty(property="active",label="Active")
	public DtBoolean getActive() {
		return active;
	}

	@ConstrainPropertyLength(minValue=0,maxValue=13)
	@ConstrainPropertyAlphanumeric
	@PersistProperty(property="telephone",label="Telephone")
	public DtString getTelephone() {
		return telephone;
	}

	@ConstrainPropertyLength(minValue=0,maxValue=64)
	@PersistProperty(property="email",label="E-mail")
	public DtString getEmail() {
		return email;
	}
}
