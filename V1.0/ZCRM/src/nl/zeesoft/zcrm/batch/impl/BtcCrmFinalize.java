package nl.zeesoft.zcrm.batch.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization;
import nl.zeesoft.zcrm.model.impl.CrmInvoiceBatch;
import nl.zeesoft.zcrm.model.impl.CrmInvoiceDetailObject;
import nl.zeesoft.zcrm.model.impl.CrmInvoiceObject;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationInvoice;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationInvoiceDetail;
import nl.zeesoft.zcrm.model.impl.CrmPersonInvoice;
import nl.zeesoft.zcrm.model.impl.CrmPersonInvoiceDetail;
import nl.zeesoft.zcrm.model.impl.CrmServiceProduct;
import nl.zeesoft.zcrm.model.impl.CrmServiceProductValueAddedTax;
import nl.zeesoft.zcrm.model.impl.CrmValueAddedTax;
import nl.zeesoft.zcrm.model.impl.CrmValueAddedTaxPercentage;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zodb.model.impl.DbUser;

public class BtcCrmFinalize extends BtcCrmObject {
	private List<CrmInvoiceBatch>				invoiceBatches 		= new ArrayList<CrmInvoiceBatch>(); 
	private SortedMap<Long,CrmValueAddedTax> 	valueAddedTaxes 	= new TreeMap<Long,CrmValueAddedTax>(); 
	
	private Date								finalizeDate		= null;
	
	public BtcCrmFinalize() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND,0);
		finalizeDate = cal.getTime();
	}

	@Override
	public String execute(BtcLog log) {
		String err = super.execute(log);
		if (organizations.size()==0) {
			return err;
		}
		getInvoiceBatches(log.getExecutingAsUser());
		if (invoiceBatches.size()==0) {
			if (!finalizeAll()) {
				log.addLogLine("No unfinalized invoice batches have been checked for finalization");
			} else {
				log.addLogLine("No unfinalized invoice batches have been found");
			}
			return err;
		}
		QryTransaction t = null;
		List<CrmDeliveryOrganization> updateOrganizations = new ArrayList<CrmDeliveryOrganization>();
		CrmDeliveryOrganization org = null;
		List<CrmInvoiceObject> invoices = null;
		for (CrmInvoiceBatch btc: invoiceBatches) {
			org = getDeliveryOrganizationForInvoiceBatch(btc,organizations,log.getExecutingAsUser());
			invoices = getInvoices(btc,log.getExecutingAsUser());
			String first = "";
			String last = "";

			BigDecimal batchTurnOverExclVAT = new BigDecimal("0.00");
			BigDecimal batchProfit = new BigDecimal("0.00");
			
			t = new QryTransaction(log.getExecutingAsUser()); 
			for (CrmInvoiceObject inv: invoices) {
				String invoiceNumber = Generic.minStrInt(org.getNextInvoiceNumber().getValue(),8);
				org.getNextInvoiceNumber().setValue(org.getNextInvoiceNumber().getValue() + 1);
				if (!updateOrganizations.contains(org)) {
					updateOrganizations.add(org);
				}
				inv.getName().setValue(invoiceNumber);
				inv.getFinalized().setValue(finalizeDate);

				BigDecimal invoiceTurnOverExclVAT = new BigDecimal("0.00");
				BigDecimal invoiceProfit = new BigDecimal("0.00");
				
				for (MdlDataObject invDetObj: inv.getChildObjects("details")) {
					CrmInvoiceDetailObject invDet = (CrmInvoiceDetailObject) invDetObj;
					CrmServiceProduct sp = getServiceProductById(invDet.getServiceProduct().getValue(),log.getExecutingAsUser());
					CrmServiceProductValueAddedTax prodVat = getServiceProductValueAddedTaxForInvoice(inv,sp,log.getExecutingAsUser());
					CrmValueAddedTax vat = this.getValueAddedTax(prodVat.getValueAddedTax().getValue(),log.getExecutingAsUser());
					CrmValueAddedTaxPercentage perc = getValueAddedTaxPercentageForInvoice(inv,vat,log.getExecutingAsUser());

					invDet.getServiceProductVATPercentage().setValue(perc.getPercentage().getValue());
					
					BigDecimal turnOverExclVAT = new BigDecimal("0.00");
					BigDecimal profit = new BigDecimal("0.00");
					
					if (invDet.getTurnOver().getValue().compareTo(Generic.ZERO_VALUE_BIG_DECIMAL)>0) {
						BigDecimal totalVAT = (invDet.getTurnOver().getValue().divide(new BigDecimal("100"))).multiply(invDet.getServiceProductVATPercentage().getValue());
						totalVAT = totalVAT.setScale(2, BigDecimal.ROUND_HALF_UP);
						turnOverExclVAT = invDet.getTurnOver().getValue().subtract(totalVAT);
					}

					profit = turnOverExclVAT.subtract(invDet.getCost().getValue());
					invDet.getTurnOverExclVAT().setValue(turnOverExclVAT);
					invDet.getProfit().setValue(profit);
					
					invoiceTurnOverExclVAT = invoiceTurnOverExclVAT.add(turnOverExclVAT);
					invoiceProfit = invoiceProfit.add(profit);
					
					t.addQuery(new QryUpdate(invDet));
				}

				inv.getTurnOverExclVAT().setValue(invoiceTurnOverExclVAT);
				inv.getProfit().setValue(invoiceProfit);
				
				batchTurnOverExclVAT = batchTurnOverExclVAT.add(invoiceTurnOverExclVAT);
				batchProfit = batchProfit.add(invoiceProfit);
				
				if (first.length()==0) {
					first = invoiceNumber;
				}
				last = invoiceNumber;

				t.addQuery(new QryUpdate(inv));
			}
			
			btc.getName().setValue(org.getName().getValue() + " " + first + " - " + last);
			btc.getFinalize().setValue(true);
			btc.getFinalized().setValue(finalizeDate);
			btc.getTurnOverExclVAT().setValue(batchTurnOverExclVAT);
			btc.getProfit().setValue(batchProfit);
			
			String msg = "Finalized invoice batch: " + btc.getName().getValue() + ", profit: " + btc.getProfit().getValue();
			btc.getLog().getValue().append(msg);
			btc.getLog().getValue().append("\n");

			t.addQuery(new QryUpdate(btc));
			
			DbIndex.getInstance().executeTransaction(t, this);
			if (getTransactionErrors(t, log)) {
				return err;
			} else {
				log.addLogLine(msg);
			}
		}
		
		t = new QryTransaction(log.getExecutingAsUser()); 
		for (CrmDeliveryOrganization updOrg: updateOrganizations) {
			t.addQuery(new QryUpdate(updOrg));
		}
		DbIndex.getInstance().executeTransaction(t, this);
		
		return err;
	}

	protected boolean finalizeAll() {
		return false;
	}

	private void getInvoiceBatches(DbUser executeAsUser) {
		QryFetch f = new QryFetch(CrmInvoiceBatch.class.getName());
		if (!finalizeAll()) {
			f.getConditions().add(new QryFetchCondition("finalize",QryFetchCondition.OPERATOR_EQUALS,new DtBoolean(true)));
		}
		f.getConditions().add(new QryFetchCondition("finalized",QryFetchCondition.OPERATOR_EQUALS,new DtDateTime()));
		f.setOrderBy("start",true);
		DbIndex.getInstance().executeFetch(f, executeAsUser, this);
		for (MdlObjectRef ref: f.getMainResults().getReferences()) {
			CrmInvoiceBatch btc = (CrmInvoiceBatch) ref.getDataObject();
			invoiceBatches.add(btc);
		}
	}
	
	private CrmDeliveryOrganization getDeliveryOrganizationForInvoiceBatch(CrmInvoiceBatch btc,List<CrmDeliveryOrganization> organizations,DbUser executeAsUser) {
		CrmDeliveryOrganization org = null;
		for (CrmDeliveryOrganization organization: organizations) {
			if (btc.getOrganization().getValue().equals(organization.getId().getValue())) {
				org = organization;
				break;
			}
		}
		return org;
	}
	
	private List<CrmInvoiceObject> getInvoices(CrmInvoiceBatch btc,DbUser executeAsUser) {
		List<CrmInvoiceObject> invoices = new ArrayList<CrmInvoiceObject>();
		QryFetch f = null;

		int start = 0;			
		f = new QryFetch(CrmOrganizationInvoice.class.getName());
		f.getConditions().add(new QryFetchCondition("invoiceBatch",QryFetchCondition.OPERATOR_CONTAINS,btc.getId()));
		f.getConditions().add(new QryFetchCondition("finalized",QryFetchCondition.OPERATOR_EQUALS,new DtDateTime()));
		f.setStartLimit(start, MAX_RESULT_SIZE);
		DbIndex.getInstance().executeFetch(f, executeAsUser, this);
		while(f.getMainResults().getReferences().size()>0) {
			for (MdlObjectRef ref: f.getMainResults().getReferences()) {
				CrmInvoiceObject inv = (CrmInvoiceObject) ref.getDataObject();
				btc.addChildObject("invoices", inv);				
				invoices.add(inv);
				getInvoiceDetails(inv,executeAsUser);
			}
			start = start + MAX_RESULT_SIZE;
			f.setStartLimit(start, MAX_RESULT_SIZE);
			DbIndex.getInstance().executeFetch(f, executeAsUser, this);
		}

		start = 0;
		f = new QryFetch(CrmPersonInvoice.class.getName());
		f.getConditions().add(new QryFetchCondition("invoiceBatch",QryFetchCondition.OPERATOR_CONTAINS,btc.getId()));
		f.getConditions().add(new QryFetchCondition("finalized",QryFetchCondition.OPERATOR_EQUALS,new DtDateTime()));
		f.setStartLimit(start, MAX_RESULT_SIZE);
		DbIndex.getInstance().executeFetch(f, executeAsUser, this);
		while(f.getMainResults().getReferences().size()>0) {
			for (MdlObjectRef ref: f.getMainResults().getReferences()) {
				CrmInvoiceObject inv = (CrmInvoiceObject) ref.getDataObject();
				btc.addChildObject("invoices", inv);				
				invoices.add(inv);
				getInvoiceDetails(inv,executeAsUser);
			}
			start = start + MAX_RESULT_SIZE;
			f.setStartLimit(start, MAX_RESULT_SIZE);
			DbIndex.getInstance().executeFetch(f, executeAsUser, this);
		}

		return invoices;
	}
	
	private void getInvoiceDetails(CrmInvoiceObject inv,DbUser executeAsUser) {
		QryFetch f = null;
		int start = 0;			
		if (inv instanceof CrmOrganizationInvoice) {
			f = new QryFetch(CrmOrganizationInvoiceDetail.class.getName());
		} else if (inv instanceof CrmPersonInvoice) {
			f = new QryFetch(CrmPersonInvoiceDetail.class.getName());
		}
		f.getConditions().add(new QryFetchCondition("invoice",QryFetchCondition.OPERATOR_CONTAINS,inv.getId()));
		f.setStartLimit(start, MAX_RESULT_SIZE);
		DbIndex.getInstance().executeFetch(f, executeAsUser, this);
		while(f.getMainResults().getReferences().size()>0) {
			for (MdlObjectRef ref: f.getMainResults().getReferences()) {
				CrmInvoiceDetailObject invDet = (CrmInvoiceDetailObject) ref.getDataObject();
				inv.addChildObject("details", invDet);				
			}
			start = start + MAX_RESULT_SIZE;
			f.setStartLimit(start, MAX_RESULT_SIZE);
			DbIndex.getInstance().executeFetch(f, executeAsUser, this);
		}
	}

	private CrmServiceProductValueAddedTax getServiceProductValueAddedTaxForInvoice(CrmInvoiceObject invoice,CrmServiceProduct serviceProduct,DbUser executeAsUser) {
		CrmServiceProductValueAddedTax vat = null;
		for (MdlDataObject obj: serviceProduct.getChildObjects("valueAddedTaxes")) {
			CrmServiceProductValueAddedTax pObj = (CrmServiceProductValueAddedTax) obj;
			if (pObj.getStart().getValue().getTime()<=invoice.getFinalized().getValue().getTime()) {
				vat = pObj;
				break;
			}
		}
		return vat;
	}

	private CrmValueAddedTax getValueAddedTax(long id,DbUser executeAsUser) {
		CrmValueAddedTax vat = valueAddedTaxes.get(id);
		if (vat==null) {
			QryFetch f = new QryFetch(CrmValueAddedTax.class.getName(),id);
			DbIndex.getInstance().executeFetch(f,executeAsUser,this);
			if (f.getMainResults().getReferences().size()>0) {
				vat = (CrmValueAddedTax) f.getMainResults().getReferences().get(0).getDataObject();
				valueAddedTaxes.put(id,vat);
			}
			if (vat!=null) {
				f = new QryFetch(CrmValueAddedTaxPercentage.class.getName());
				f.getConditions().add(new QryFetchCondition("valueAddedTax",QryFetchCondition.OPERATOR_CONTAINS,vat.getId()));
				f.setOrderBy("start",false);
				DbIndex.getInstance().executeFetch(f,executeAsUser,this);
				for (MdlObjectRef ref: f.getMainResults().getReferences()) {
					vat.addChildObject("percentages", ref.getDataObject());
				}
			}
		}
		return vat;
	}

	private CrmValueAddedTaxPercentage getValueAddedTaxPercentageForInvoice(CrmInvoiceObject invoice,CrmValueAddedTax vat,DbUser executeAsUser) {
		CrmValueAddedTaxPercentage percentage = null;
		for (MdlDataObject obj: vat.getChildObjects("percentages")) {
			CrmValueAddedTaxPercentage pObj = (CrmValueAddedTaxPercentage) obj;
			if (pObj.getStart().getValue().getTime()<=invoice.getFinalized().getValue().getTime()) {
				percentage = pObj;
				break;
			}
		}
		return percentage;
	}
}
