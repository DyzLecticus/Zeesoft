package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(entity=false,module=ZCRMModel.MODULE_ZCRM,nameSingle="Person invoice detail",nameMulti="Person invoice details")
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelFetch=ZCRMModel.USER_LEVEL_CRM_CONTRACT_MANAGER,userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Person invoice details register customer product deliveries and prices.\n" +
	"")
public class CrmPersonInvoiceDetail extends CrmInvoiceDetailObject {
	private DtIdRef 					invoice							= new DtIdRef();
	private DtIdRef 					delivery						= new DtIdRef();
	private DtIdRef 					log								= new DtIdRef();
	private DtIdRef 					contractDetail					= new DtIdRef();

	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			CrmPersonInvoiceDetail changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (CrmPersonInvoiceDetail) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (CrmPersonInvoiceDetail) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				if (
					(changedObject.getDelivery().getValue().equals(0L)) &&
					(changedObject.getLog().getValue().equals(0L))
					) {
					q.addError("2016","Invoice detail must be linked to a delivery or a person log");
					ok = false;
				}
			}
		}
		return ok;
	}

	@Override
	@ConstrainPropertyMandatory
	@PersistReference(property="serviceProduct",label="Service or product",className="nl.zeesoft.zcrm.model.impl.CrmServiceProduct",entityLabel="Person invoice details",entity=false,removeMe=false)
	public DtIdRef getServiceProduct() {
		return super.getServiceProduct();
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="invoice",label="Invoice",className="nl.zeesoft.zcrm.model.impl.CrmPersonInvoice",entityLabel="Invoice detais")
	public DtIdRef getInvoice() {
		return invoice;
	}

	@PersistReference(property="delivery",label="Delivery",className="nl.zeesoft.zcrm.model.impl.CrmPersonDelivery",entityLabel="Invoice detais",entity=false,removeMe=false)
	public DtIdRef getDelivery() {
		return delivery;
	}

	@PersistReference(property="log",label="Log",className="nl.zeesoft.zcrm.model.impl.CrmPersonTaskLog",entityLabel="Invoice detais",entity=false,removeMe=false)
	public DtIdRef getLog() {
		return log;
	}

	@PersistReference(property="contractDetail",label="Contract detail",className="nl.zeesoft.zcrm.model.impl.CrmPersonContractDetail",entityLabel="Invoice detais",entity=false,removeMe=false)
	public DtIdRef getContractDetail() {
		return contractDetail;
	}
}

