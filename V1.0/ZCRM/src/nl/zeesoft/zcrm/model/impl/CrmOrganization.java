package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;

@PersistCollection(module=ZCRMModel.MODULE_ZCRM,nameSingle="Organization",nameMulti="Organizations")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Organizations define legal entities.\n" +
	"")
public class CrmOrganization extends CrmOrganizationObject {
	private DtIdRef						invoiceAddress				= new DtIdRef();
	
	@PersistReference(property="invoiceAddress",label="Invoice address",className="nl.zeesoft.zcrm.model.impl.CrmOrganizationAddress",entityLabel="Organizations",entity=false,removeMe=false)
	public DtIdRef getInvoiceAddress() {
		return invoiceAddress;
	}

	@Override
	@PersistReference(property="employees",label="Employees",className="nl.zeesoft.zcrm.model.impl.CrmPerson",entityLabel="Employers",removeMe=false)
	public DtIdRefList getEmployees() {
		return super.getEmployees();
	}
}
