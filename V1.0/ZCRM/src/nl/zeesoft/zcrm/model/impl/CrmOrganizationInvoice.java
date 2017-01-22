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

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Organization invoice",nameMulti="Organization invoices")
@CacheableCollection
@ConstrainObjectUnique(properties={"invoiceBatch;customer"})
@ConstrainObjectAccess(userLevelFetch=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER,userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Organization invoices summarize customer product deliveries and prices.\n" +
	"")
public class CrmOrganizationInvoice extends CrmInvoiceObject {
	private DtIdRef 					customer							= new DtIdRef();
	private DtIdRef 					address								= new DtIdRef();

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="invoiceBatch",label="Invoice batch",className="nl.zeesoft.zcrm.model.impl.CrmInvoiceBatch",entityLabel="Organization invoices")
	public DtIdRef getInvoiceBatch() {
		return super.getInvoiceBatch();
	}

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="organization",label="Organization",className="nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization",entityLabel="Organization invoices")
	public DtIdRef getOrganization() {
		return super.getOrganization();
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="customer",label="Customer",className="nl.zeesoft.zcrm.model.impl.CrmOrganization",entityLabel="Invoices",entity=false,removeMe=false)
	public DtIdRef getCustomer() {
		return customer;
	}

	@PersistReference(property="address",label="Address",className="nl.zeesoft.zcrm.model.impl.CrmOrganizationAddress",entityLabel="Invoices",entity=false,removeMe=false)
	public DtIdRef getAddress() {
		return address;
	}
}

