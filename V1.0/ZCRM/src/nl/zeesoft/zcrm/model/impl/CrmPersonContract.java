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

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Person contract",nameMulti="Person contracts")
@CacheableCollection
@ConstrainObjectUnique(properties={"organization;name"})
@ConstrainObjectAccess(userLevelFetch=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER,userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER)
@DescribeCollection(
description=
	"Person contracts define customer specific prices for services and/or products.\n" +
	"")
public class CrmPersonContract extends CrmContractObject {
	private DtIdRef 					customer							= new DtIdRef();

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="organization",label="Organization",className="nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization",entityLabel="Person contracts")
	public DtIdRef getOrganization() {
		return super.getOrganization();
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="customer",label="Customer",className="nl.zeesoft.zcrm.model.impl.CrmPerson",entityLabel="Contracts")
	public DtIdRef getCustomer() {
		return customer;
	}
}

