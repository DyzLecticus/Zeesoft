package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Value added tax",nameMulti="Value added taxes")
@CacheableCollection
@ConstrainObjectUnique(properties={"country;name"})
@ConstrainObjectAccess(userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Value added taxes define country specific value added taxes.\n" +
	"")
public class CrmValueAddedTax extends MdlDataObject {
	private DtIdRef 					country						= new DtIdRef();

	@ConstrainPropertyMandatory
	@PersistReference(property="country",label="Country",className="nl.zeesoft.zcrm.model.impl.CrmCountry",entityLabel="Value added taxes")
	public DtIdRef getCountry() {
		return country;
	}
}
