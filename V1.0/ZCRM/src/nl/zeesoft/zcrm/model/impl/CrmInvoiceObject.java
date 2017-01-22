package nl.zeesoft.zcrm.model.impl;

import nl.zeesoft.zcrm.batch.impl.BtcCrmObject;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryRemove;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.annotations.ConstrainPropertyMandatory;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.DbUser;

public abstract class CrmInvoiceObject extends CrmInvoiceSummaryObject {
	private DtIdRef 					invoiceBatch						= new DtIdRef();
	private DtStringBuffer				addressLine1						= new DtStringBuffer();
	private DtStringBuffer 				addressLine2						= new DtStringBuffer();
	private DtStringBuffer 				addressLine3						= new DtStringBuffer();
	private DtStringBuffer 				addressPostalCode					= new DtStringBuffer();
	private DtStringBuffer 				addressCity							= new DtStringBuffer();
	private DtIdRef 					addressCountry						= new DtIdRef();
	private DtStringBuffer 				customerName						= new DtStringBuffer();
	private DtStringBuffer 				customerTelephone					= new DtStringBuffer();
	private DtStringBuffer 				customerEmail						= new DtStringBuffer();
	private DtIdRef 					organization						= new DtIdRef();
	private DtStringBuffer 				organizationName					= new DtStringBuffer();
	private DtStringBuffer 				organizationAccountNumber			= new DtStringBuffer();
	private DtStringBuffer 				organizationChamberOfCommerceCode	= new DtStringBuffer();
	private DtDateTime 					finalized							= new DtDateTime();

	@Override
	public boolean checkObjectConstraintsForUserQuery(DbUser user, QryObject q) {
		boolean ok = super.checkObjectConstraintsForUserQuery(user, q);
		if (ok) {
			ok = checkFinalized(q);
		}
		if (ok) {
			CrmInvoiceObject changedObject = null;
			if (q instanceof QryAdd) {
				changedObject = (CrmInvoiceObject) ((QryAdd) q).getDataObject();
			} else if (q instanceof QryUpdate) {
				changedObject = (CrmInvoiceObject) ((QryUpdate) q).getDataObject();
			}
			if (changedObject!=null) {
				if (!changedObject.getName().getValue().equals(getName().getValue())) {
					if (
						(!(q.getSource() instanceof BtcCrmObject)) &&
						(!(q.getSource() instanceof DbFactory))
						) {
						q.addError("2012",MdlObject.PROPERTY_NAME,"Only an instance of @1 may change these properties",BtcCrmObject.class.getName());
						ok = false;
					}
				}
			}
		}
		return ok;
	}
	
	@Override
	public boolean checkObjectConstraintsForUserRemoveQuery(DbUser user,QryRemove q) {
		boolean ok = super.checkObjectConstraintsForUserRemoveQuery(user, q);
		if (ok) {
			ok = checkFinalized(q);
		}
		return ok;
	}
	
	private boolean checkFinalized(QryObject q) {
		boolean ok = true;
		if (getFinalized().getValue()!=null){
			q.addError("2013","finalized","Invoice @1 has been finalized",getName().getValue());
			ok = false;
		}
		return ok;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="invoiceBatch",label="Invoice batch",className="nl.zeesoft.zcrm.model.impl.CrmInvoiceBatch",entityLabel="Invoices")
	public DtIdRef getInvoiceBatch() {
		return invoiceBatch;
	}

	@PersistProperty(property="addressLine1",label="Address line 1")
	public DtStringBuffer getAddressLine1() {
		return addressLine1;
	}

	@PersistProperty(property="addressLine2",label="Address line 2")
	public DtStringBuffer getAddressLine2() {
		return addressLine2;
	}

	@PersistProperty(property="addressLine3",label="Address line 3")
	public DtStringBuffer getAddressLine3() {
		return addressLine3;
	}

	@PersistProperty(property="addressPostalCode",label="Address postal code")
	public DtStringBuffer getAddressPostalCode() {
		return addressPostalCode;
	}

	@PersistProperty(property="addressCity",label="Address city")
	public DtStringBuffer getAddressCity() {
		return addressCity;
	}

	@PersistReference(property="addressCountry",label="Address country",className="nl.zeesoft.zcrm.model.impl.CrmCountry",entityLabel="Invoices",entity=false,removeMe=false)
	public DtIdRef getAddressCountry() {
		return addressCountry;
	}

	@PersistProperty(property="customerName",label="Customer name")
	public DtStringBuffer getCustomerName() {
		return customerName;
	}

	@PersistProperty(property="customerTelephone",label="Customer telephone")
	public DtStringBuffer getCustomerTelephone() {
		return customerTelephone;
	}

	@PersistProperty(property="customerEmail",label="Customer email")
	public DtStringBuffer getCustomerEmail() {
		return customerEmail;
	}

	@ConstrainPropertyMandatory
	@PersistReference(property="organization",label="Organization",className="nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization",entityLabel="Invoices")
	public DtIdRef getOrganization() {
		return organization;
	}

	@PersistProperty(property="organizationName",label="Organization name")
	public DtStringBuffer getOrganizationName() {
		return organizationName;
	}
	
	@PersistProperty(property="organizationAccountNumber",label="Organization account number")
	public DtStringBuffer getOrganizationAccountNumber() {
		return organizationAccountNumber;
	}

	@PersistProperty(property="organizationChamberOfCommerceCode",label="Organization CoC code")
	public DtStringBuffer getOrganizationChamberOfCommerceCode() {
		return organizationChamberOfCommerceCode;
	}

	@PersistProperty(property="finalized",label="Finalized")
	public DtDateTime getFinalized() {
		return finalized;
	}
}
