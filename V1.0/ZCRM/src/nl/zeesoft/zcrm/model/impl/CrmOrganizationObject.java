package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zodb.model.annotations.ConstrainPropertyLength;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyNoSpace;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtString;

public abstract class CrmOrganizationObject extends CrmContactObject {
	private DtString 					website						= new DtString();
	private DtString 					accountNumber				= new DtString();
	private DtString 					chamberOfCommerceCode		= new DtString();
	private DtIdRefList					employees					= new DtIdRefList();

	@ConstrainPropertyNoSpace
	@ConstrainPropertyLength(minValue=0,maxValue=128)
	@PersistProperty(property="website",label="Website")
	public DtString getWebsite() {
		return website;
	}
	
	@ConstrainPropertyNoSpace
	@ConstrainPropertyLength(minValue=0,maxValue=32)
	@PersistProperty(property="accountNumber",label="Account number")
	public DtString getAccountNumber() {
		return accountNumber;
	}
	
	@ConstrainPropertyNoSpace
	@ConstrainPropertyLength(minValue=0,maxValue=32)
	@PersistProperty(property="chamberOfCommerceCode",label="CoC code")
	public DtString getChamberOfCommerceCode() {
		return chamberOfCommerceCode;
	}

	@PersistReference(property="employees",label="Employees",className="nl.zeesoft.zcrm.model.impl.CrmPerson",entityLabel="Employers",entity=false,removeMe=false)
	public DtIdRefList getEmployees() {
		return employees;
	}
}
