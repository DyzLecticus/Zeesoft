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

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Organization address",nameMulti="Organization addresses")
@CacheableCollection
@ConstrainObjectUnique(properties={"organization;name"})
@ConstrainObjectAccess(userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Organization addresses define organization specific locations.\n" +
	"")
public class CrmOrganizationAddress extends CrmAddressObject {
	private DtIdRef 					organization						= new DtIdRef();

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="country",label="Country",className="nl.zeesoft.zcrm.model.impl.CrmCountry",entityLabel="Organization addresses")
	public DtIdRef getCountry() {
		return super.getCountry();
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="organization",label="Organization",className="nl.zeesoft.zcrm.model.impl.CrmOrganization",entityLabel="Addresses")
	public DtIdRef getOrganization() {
		return organization;
	}
}
