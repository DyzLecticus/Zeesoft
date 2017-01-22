package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Organization delivery",nameMulti="Organization deliveries")
@ConstrainObjectUnique(properties={"customer;name"})
@ConstrainObjectAccess(userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_DELIVERY_MANAGER,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_DELIVERY_MANAGER,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_DELIVERY_MANAGER)
@DescribeCollection(
description=
	"Organization deliveries register customer product deliveries.\n" +
	"")
public class CrmOrganizationDelivery extends CrmDeliveryObject {
	private DtIdRef 					customer							= new DtIdRef();
	private DtIdRef 					invoiceDetail						= new DtIdRef();

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="serviceProduct",label="Service or product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Organization deliveries",removeMe=false)
	public DtIdRef getServiceProduct() {
		return super.getServiceProduct();
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="customer",label="Customer",className="nl.zeesoft.zcrm.model.impl.CrmOrganization",entityLabel="Deliveries")
	public DtIdRef getCustomer() {
		return customer;
	}

	@PersistReference(property="invoiceDetail",label="Invoice detail",className="nl.zeesoft.zcrm.model.impl.CrmOrganizationInvoiceDetail",entityLabel="Deliveries",entity=false,removeMe=false)
	public DtIdRef getInvoiceDetail() {
		return invoiceDetail;
	}
}

