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

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Person contract detail",nameMulti="Person contract details")
@CacheableCollection
@ConstrainObjectUnique(properties={"contract;serviceProduct"})
@ConstrainObjectAccess(userLevelFetch=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER,userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER)
@DescribeCollection(
description=
	"Person contract details define a customer specific price for a specific service or product.\n" +
	"")
public class CrmPersonContractDetail extends CrmContractDetailObject {
	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="contract",label="Contract",className="nl.zeesoft.zcrm.model.impl.CrmPersonContract",entityLabel="Contract details")
	public DtIdRef getContract() {
		return super.getContract();
	}
	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="serviceProduct",label="Service product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Person contract details",entity=false)
	public DtIdRef getServiceProduct() {
		return super.getServiceProduct();
	}
}

