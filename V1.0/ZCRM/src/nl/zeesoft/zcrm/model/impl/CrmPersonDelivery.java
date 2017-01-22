package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Person delivery",nameMulti="Person deliveries")
@ConstrainObjectUnique(properties={"customer;name"})
@ConstrainObjectAccess(userLevelFetch=ZCRMModel.USER_LEVEL_FETCH_PEOPLE,userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_DELIVERY_MANAGER,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_DELIVERY_MANAGER,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_DELIVERY_MANAGER)
@DescribeCollection(
description=
	"Person deliveries register customer product deliveries.\n" +
	"")
public class CrmPersonDelivery extends CrmDeliveryObject {
	private DtIdRef 					customer								= new DtIdRef();
	private DtIdRef 					invoiceDetail							= new DtIdRef();

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="serviceProduct",label="Service or product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Person deliveries",removeMe=false)
	public DtIdRef getServiceProduct() {
		return super.getServiceProduct();
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="customer",label="Customer",className="nl.zeesoft.zcrm.model.impl.CrmPerson",entityLabel="Deliveries")
	public DtIdRef getCustomer() {
		return customer;
	}

	@PersistReference(property="invoiceDetail",label="Invoice detail",className="nl.zeesoft.zcrm.model.impl.CrmPersonInvoiceDetail",entityLabel="Deliveries",entity=false,removeMe=false)
	public DtIdRef getInvoiceDetail() {
		return invoiceDetail;
	}
}

