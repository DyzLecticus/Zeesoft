package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Service or product",nameMulti="Services and products")
@CacheableCollection
@ConstrainObjectUnique(properties={"organization;name"})
@ConstrainObjectAccess(userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Services and products define prices and value added taxes for services and/or products an organization delivers to its customers.\n" +
	"")
public class CrmServiceProduct extends CrmDescriptionObject {
	private DtBoolean					active								= new DtBoolean(true);
	private DtIdRef 					organization						= new DtIdRef();
	private DtBoolean					service								= new DtBoolean(true);

	@PersistProperty(property="active",label="Active")
	public DtBoolean getActive() {
		return active;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="organization",label="Organization",className="nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization",entityLabel="Services and products")
	public DtIdRef getOrganization() {
		return organization;
	}

	@PersistProperty(property="service",label="Service")
	public DtBoolean getService() {
		return service;
	}
}
