package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;

public abstract class CrmContractObject extends CrmDescriptionObject {
	private DtBoolean					active								= new DtBoolean(true);
	private DtIdRef 					organization						= new DtIdRef();
	private DtDateTime					start								= new DtDateTime();
	private DtDateTime					end									= new DtDateTime();

	@PersistProperty(property="active",label="Active")
	public DtBoolean getActive() {
		return active;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="organization",label="Organization",className="nl.zeesoft.zcrm.model.impl.CrmOrganization",entityLabel="Contracts")
	public DtIdRef getOrganization() {
		return organization;
	}

	@ConstrainPropertyMandatory
	@PersistProperty(property="start",label="Start")
	public DtDateTime getStart() {
		return start;
	}

	@PersistProperty(property="end",label="End")
	public DtDateTime getEnd() {
		return end;
	}
}
