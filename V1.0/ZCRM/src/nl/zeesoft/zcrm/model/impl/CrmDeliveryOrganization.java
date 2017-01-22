package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.batch.impl.BtcCrmFinalize;
import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.annotations.CacheableCollection;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectAccess;
import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.DescribeCollection;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.impl.DbUser;

@PersistCollection(module=ZCRMModel.MODULE_ZCRM,nameSingle="Delivery organization",nameMulti="Delivery organizations")
@CacheableCollection
@ConstrainObjectUnique
@ConstrainObjectAccess(userLevelUpdate=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelRemove=ZCRMModel.USER_LEVEL_CRM_ADMIN,userLevelAdd=ZCRMModel.USER_LEVEL_CRM_ADMIN)
@DescribeCollection(
description=
	"Delivery organizations define legal entities that can deliver services and/or products to customers.\n" +
	"")
public class CrmDeliveryOrganization extends CrmOrganizationObject {
	private DtInteger nextInvoiceNumber	= new DtInteger(1);

	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			CrmDeliveryOrganization changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (CrmDeliveryOrganization) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (CrmDeliveryOrganization) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				if (!changedObject.getNextInvoiceNumber().getValue().equals(getNextInvoiceNumber().getValue())) {
					if (!(q.getSource() instanceof BtcCrmFinalize)) {
						q.addError("2008",getModelCollectionProperty("nextInvoiceNumber").getName(),"Only an instance of @1 may change the next invoice number",BtcCrmFinalize.class.getName());
						ok = false;
					}
				}
			}
		}
		return ok;
	}

	@PersistProperty(property="nextInvoiceNumber",label="Next invoice number")
	public DtInteger getNextInvoiceNumber() {
		return nextInvoiceNumber;
	}
}
