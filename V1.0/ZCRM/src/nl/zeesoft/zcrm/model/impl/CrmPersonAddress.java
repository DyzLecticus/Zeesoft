package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Person address",nameMulti="Person addresses")
@CacheableCollection
@ConstrainObjectUnique(properties={"person;name"})
@ConstrainObjectAccess(userLevelFetch=ZCRMModel.USER_LEVEL_FETCH_PEOPLE,userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Person addresses define person specific locations.\n" +
	"")
public class CrmPersonAddress extends CrmAddressObject {
	private DtIdRef 					person						= new DtIdRef();

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="country",label="Country",className="nl.zeesoft.zcrm.model.impl.CrmCountry",entityLabel="Person addresses")
	public DtIdRef getCountry() {
		return super.getCountry();
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="person",label="Person",className="nl.zeesoft.zcrm.model.impl.CrmPerson",entityLabel="Addresses")
	public DtIdRef getPerson() {
		return person;
	}
}
