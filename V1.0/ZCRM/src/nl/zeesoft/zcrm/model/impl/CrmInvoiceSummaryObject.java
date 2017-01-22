package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.batch.impl.BtcCrmObject;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.impl.DbUser;

public abstract class CrmInvoiceSummaryObject extends MdlDataObject {
	private DtDecimal					turnOver							= new DtDecimal();
	private DtDecimal 					turnOverExclVAT						= new DtDecimal();
	private DtDecimal 					cost								= new DtDecimal();
	private DtDecimal 					profit								= new DtDecimal();

	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			CrmInvoiceSummaryObject changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (CrmInvoiceSummaryObject) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (CrmInvoiceSummaryObject) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				if (
					(!changedObject.getTurnOver().getValue().equals(getTurnOver().getValue())) ||
					(!changedObject.getTurnOverExclVAT().getValue().equals(getTurnOverExclVAT().getValue())) ||
					(!changedObject.getCost().getValue().equals(getCost().getValue())) ||
					(!changedObject.getProfit().getValue().equals(getProfit().getValue()))
					) {
					if (
						(!(q.getSource() instanceof BtcCrmObject)) &&
						(!(q.getSource() instanceof DbFactory))
						) {
						q.addError("2014",MdlObject.PROPERTY_NAME,"Only an instance of @1 may change these properties",BtcCrmObject.class.getName());
						ok = false;
					}
				}
			}
		}
		return ok;
	}
	
	@PersistProperty(property="turnOver",label="Turn over")
	public DtDecimal getTurnOver() {
		return turnOver;
	}

	@PersistProperty(property="turnOverExclVAT",label="Turn over excl. VAT")
	public DtDecimal getTurnOverExclVAT() {
		return turnOverExclVAT;
	}

	@PersistProperty(property="cost",label="Cost")
	public DtDecimal getCost() {
		return cost;
	}

	@PersistProperty(property="profit",label="Profit")
	public DtDecimal getProfit() {
		return profit;
	}
}
