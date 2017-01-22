package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zpo.model.impl.UsrTaskLog;

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Person log",nameMulti="Person logs")
@ConstrainObjectUnique(properties={"task;dateTime"})
@ConstrainObjectAccess(userLevelFetch=ZCRMModel.USER_LEVEL_FETCH_PEOPLE,userLevelUpdate=ZCRMModel.USER_LEVEL_FETCH_PEOPLE,userLevelRemove=ZCRMModel.USER_LEVEL_FETCH_PEOPLE,userLevelAdd=ZCRMModel.USER_LEVEL_FETCH_PEOPLE)
@DescribeCollection(
description=
	"Person logs record activities related to a certain person.\n" +
	"Logs linked to a service or product will be invoiced.\n" +
	"Invoicing will use the duration of the log as units.\n" +
	"")
public class CrmPersonTaskLog extends UsrTaskLog {
	private DtIdRef 					customer					= new DtIdRef();
	private DtIdRef 					serviceProduct				= new DtIdRef();
	private DtIdRef 					invoiceDetail				= new DtIdRef();

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="user",label="User",className="nl.zeesoft.zodb.model.impl.DbUser",entityLabel="Person logs")
	public DtIdRef getUser() {
		return super.getUser();
	}

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="task",label="Task",className="nl.zeesoft.zpo.model.impl.UsrTask",entityLabel="Person logs",removeMe=false)
	public DtIdRef getTask() {
		return super.getTask();
	}
	
	@ConstrainPropertyMandatory
	@PersistReference(property="customer",label="Customer",className="nl.zeesoft.zcrm.model.impl.CrmPerson",entityLabel="Logs")
	public DtIdRef getCustomer() {
		return customer;
	}

	@PersistReference(property="serviceProduct",label="Service or product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Person logs")
	public DtIdRef getServiceProduct() {
		return serviceProduct;
	}

	@PersistReference(property="invoiceDetail",label="Invoice detail",className="nl.zeesoft.zcrm.model.impl.CrmPersonInvoiceDetail",entityLabel="Logs",entity=false,removeMe=false)
	public DtIdRef getInvoiceDetail() {
		return invoiceDetail;
	}
}
