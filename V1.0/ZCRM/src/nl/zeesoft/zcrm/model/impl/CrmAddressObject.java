package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zodb.model.annotations.ConstrainPropertyAlphabetic;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyAlphanumeric;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtString;

public abstract class CrmAddressObject extends CrmDescriptionObject {
	private DtString 					line1						= new DtString();
	private DtString 					line2						= new DtString();
	private DtString 					line3						= new DtString();
	private DtString 					postalCode					= new DtString();
	private DtString 					city						= new DtString();
	private DtIdRef 					country						= new DtIdRef();

	@ConstrainPropertyLength(minValue=0,maxValue=128)
	@ConstrainPropertyMandatory
	@PersistProperty(property="line1",label="Line 1")
	public DtString getLine1() {
		return line1;
	}
	
	@ConstrainPropertyLength(minValue=0,maxValue=128)
	@PersistProperty(property="line2",label="Line 2")
	public DtString getLine2() {
		return line2;
	}
	
	@ConstrainPropertyLength(minValue=0,maxValue=128)
	@PersistProperty(property="line3",label="Line 3")
	public DtString getLine3() {
		return line3;
	}
	
	@ConstrainPropertyLength(minValue=4,maxValue=6)
	@ConstrainPropertyMandatory
	@ConstrainPropertyAlphanumeric
	@PersistProperty(property="postalCode",label="Postal code")
	public DtString getPostalCode() {
		return postalCode;
	}
	
	@ConstrainPropertyLength(minValue=0,maxValue=128)
	@ConstrainPropertyMandatory
	@ConstrainPropertyAlphabetic
	@PersistProperty(property="city",label="City")
	public DtString getCity() {
		return city;
	}
	
	@ConstrainPropertyMandatory
	@PersistReference(property="country",label="Country",className="nl.zeesoft.zcrm.model.impl.CrmCountry",entityLabel="Addresses")
	public DtIdRef getCountry() {
		return country;
	}
}
