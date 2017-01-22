package nl.zeesoft.zcrm.batch.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zcrm.model.impl.CrmAddressObject;
import nl.zeesoft.zcrm.model.impl.CrmContactObject;
import nl.zeesoft.zcrm.model.impl.CrmContractDetailObject;
import nl.zeesoft.zcrm.model.impl.CrmContractObject;
import nl.zeesoft.zcrm.model.impl.CrmDeliveryObject;
import nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization;
import nl.zeesoft.zcrm.model.impl.CrmInvoiceBatch;
import nl.zeesoft.zcrm.model.impl.CrmInvoiceDetailObject;
import nl.zeesoft.zcrm.model.impl.CrmInvoiceObject;
import nl.zeesoft.zcrm.model.impl.CrmOrganization;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationAddress;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationContract;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationContractDetail;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationDelivery;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationInvoice;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationInvoiceDetail;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationTaskLog;
import nl.zeesoft.zcrm.model.impl.CrmPerson;
import nl.zeesoft.zcrm.model.impl.CrmPersonAddress;
import nl.zeesoft.zcrm.model.impl.CrmPersonContract;
import nl.zeesoft.zcrm.model.impl.CrmPersonContractDetail;
import nl.zeesoft.zcrm.model.impl.CrmPersonDelivery;
import nl.zeesoft.zcrm.model.impl.CrmPersonInvoice;
import nl.zeesoft.zcrm.model.impl.CrmPersonInvoiceDetail;
import nl.zeesoft.zcrm.model.impl.CrmPersonTaskLog;
import nl.zeesoft.zcrm.model.impl.CrmServiceProduct;
import nl.zeesoft.zcrm.model.impl.CrmServiceProductPrice;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zpo.model.impl.UsrTaskLog;

public class BtcCrmInvoice extends BtcCrmObject {
	private static final int						MAX_RESULT_SIZE		= 100;
	
	private SortedMap<Long,CrmInvoiceBatch> 		invoiceBatches 		= new TreeMap<Long,CrmInvoiceBatch>(); 
	private SortedMap<Long,CrmContactObject> 		customers 			= new TreeMap<Long,CrmContactObject>();
	private SortedMap<Long,List<CrmContractObject>>	customerContracts	= new TreeMap<Long,List<CrmContractObject>>();
	
	@Override
	public String execute(BtcLog log) {
		String err = super.execute(log);
		if (organizations.size()==0) {
			return err;
		}
		
		CrmInvoiceDetailObject 		det 		= null;
		
		List<CrmDeliveryObject> 	deliveries 	= new ArrayList<CrmDeliveryObject>(); 
		
		// Get first set of deliveries
		int start = 0;
		String className = CrmOrganizationDelivery.class.getName();
		deliveries = getInvoiceDeliveries(start,className,log.getExecutingAsUser());
		if (deliveries.size()==0) {
			className = CrmPersonDelivery.class.getName();
			deliveries = getInvoiceDeliveries(start,className,log.getExecutingAsUser());
		}
		while (deliveries.size()>0) {
			start = start + MAX_RESULT_SIZE;
			log.addLogLine("Invoice deliveries: " + deliveries.size());
			for (CrmDeliveryObject delivery: deliveries) {
				String customerCollection = "";
				long customerId = 0;
				if (delivery instanceof CrmOrganizationDelivery) {
					customerCollection = CrmOrganization.class.getName();
					customerId = ((CrmOrganizationDelivery) delivery).getCustomer().getValue();
				} else if (delivery instanceof CrmPersonDelivery) {
					customerCollection = CrmPerson.class.getName();
					customerId = ((CrmPersonDelivery) delivery).getCustomer().getValue();
				}
				BigDecimal units = new BigDecimal(delivery.getUnits() + ".00");
				String name = delivery.getId().toString();
				det = invoiceCustomerServiceProductDateTimeUnits(name,customerCollection,customerId,delivery.getServiceProduct().getValue(),units,delivery.getOrderDateTime().getValue(),delivery.getDeliveryDateTime().getValue(),delivery,log);
				if (det==null) {
					err = "Failed to invoice all service and/or product deliveries";
				} else {
					start--;
				}
			}
			if (err.length()>0) {
				break;
			}
			// Get next set of deliveries
			deliveries = getInvoiceDeliveries(start,className,log.getExecutingAsUser());
			if ((deliveries.size()==0) && (!className.equals(CrmPersonDelivery.class.getName()))) {
				start = 0;
				className = CrmPersonDelivery.class.getName();
				deliveries = getInvoiceDeliveries(start,className,log.getExecutingAsUser());
			}
		}
		
		if (err.length()==0) {
			List<UsrTaskLog> logs = new ArrayList<UsrTaskLog>(); 
			
			// Get first set of logs
			start = 0;
			className = CrmOrganizationTaskLog.class.getName();
			logs = getInvoiceLogs(start,className,log.getExecutingAsUser());
			if (logs.size()==0) {
				className = CrmPersonTaskLog.class.getName();
				logs = getInvoiceLogs(start,className,log.getExecutingAsUser());
			}
			while (logs.size()>0) {
				start = start + MAX_RESULT_SIZE;
				log.addLogLine("Invoice logs: " + logs.size());
				for (UsrTaskLog taskLog: logs) {
					String customerCollection = "";
					long customerId = 0;
					long serviceProductId = 0;
					if (taskLog instanceof CrmOrganizationTaskLog) {
						customerCollection = CrmOrganization.class.getName();
						customerId = ((CrmOrganizationTaskLog) taskLog).getCustomer().getValue();
						serviceProductId = ((CrmOrganizationTaskLog) taskLog).getServiceProduct().getValue();
					} else if (taskLog instanceof CrmPersonTaskLog) {
						customerCollection = CrmPerson.class.getName();
						customerId = ((CrmPersonTaskLog) taskLog).getCustomer().getValue();
						serviceProductId = ((CrmPersonTaskLog) taskLog).getServiceProduct().getValue();
					}
					String name = taskLog.getId().toString();
					det = invoiceCustomerServiceProductDateTimeUnits(name,customerCollection,customerId,serviceProductId,taskLog.getDuration().getValue(),taskLog.getDateTime().getValue(),taskLog.getDateTime().getValue(),taskLog,log);
					if (det==null) {
						err = "Failed to invoice all service and/or product logs";
					} else {
						start--;
					}
				}
				if (err.length()>0) {
					break;
				}
				// Get next set of logs
				logs = getInvoiceLogs(start,className,log.getExecutingAsUser());
				if ((logs.size()==0) && (!className.equals(CrmPersonTaskLog.class.getName()))) {
					start = 0;
					className = CrmPersonTaskLog.class.getName();
					logs = getInvoiceLogs(start,className,log.getExecutingAsUser());
				}
			}
		}
		
		totalizeBatches(log);
		
		return err;
	}

	private List<CrmDeliveryObject> getInvoiceDeliveries(int start, String deliveryCollection,DbUser executeAsUser) {
		List<CrmDeliveryObject> deliveries = new ArrayList<CrmDeliveryObject>();
		QryFetch f = null;
		f = new QryFetch(deliveryCollection);
		f.setStartLimit(start, MAX_RESULT_SIZE);
		f.getConditions().add(new QryFetchCondition("invoiceDetail",QryFetchCondition.OPERATOR_CONTAINS,new DtLong(0L)));
		DbIndex.getInstance().executeFetch(f, executeAsUser, this);
		for (MdlObjectRef ref: f.getMainResults().getReferences()) {
			deliveries.add((CrmDeliveryObject) ref.getDataObject());
		}
		return deliveries;
	}

	private List<UsrTaskLog> getInvoiceLogs(int start, String logCollection,DbUser executeAsUser) {
		List<UsrTaskLog> logs = new ArrayList<UsrTaskLog>();
		QryFetch f = null;
		f = new QryFetch(logCollection);
		f.setStartLimit(start, MAX_RESULT_SIZE);
		f.getConditions().add(new QryFetchCondition("invoiceDetail",QryFetchCondition.OPERATOR_CONTAINS,new DtLong(0L)));
		f.getConditions().add(new QryFetchCondition("customer",true,QryFetchCondition.OPERATOR_CONTAINS,new DtLong(0L)));
		f.getConditions().add(new QryFetchCondition("serviceProduct",true,QryFetchCondition.OPERATOR_CONTAINS,new DtLong(0L)));
		DbIndex.getInstance().executeFetch(f, executeAsUser, this);
		for (MdlObjectRef ref: f.getMainResults().getReferences()) {
			logs.add((UsrTaskLog) ref.getDataObject());
		}
		return logs;
	}
	
	private CrmDeliveryOrganization getDeliveryOrganizationForServiceProduct(CrmServiceProduct sp,List<CrmDeliveryOrganization> organizations,DbUser executeAsUser) {
		CrmDeliveryOrganization org = null;
		for (CrmDeliveryOrganization organization: organizations) {
			if (sp.getOrganization().getValue().equals(organization.getId().getValue())) {
				org = organization;
				break;
			}
		}
		return org;
	}

	private CrmInvoiceDetailObject invoiceCustomerServiceProductDateTimeUnits(String name,String customerCollection,long customerId,long serviceProductId,BigDecimal units,Date orderDateTime,Date deliveryDateTime,MdlDataObject linkDetailToObject,BtcLog log) {
		CrmInvoiceDetailObject det = null;
		CrmServiceProduct sp = getServiceProductById(serviceProductId,log.getExecutingAsUser());
		CrmDeliveryOrganization org = getDeliveryOrganizationForServiceProduct(sp,organizations,log.getExecutingAsUser());
		CrmContactObject cust = getCustomerById(customerCollection,customerId,log.getExecutingAsUser());
		CrmInvoiceObject inv = getOrCreateInvoiceForDateTimeCustomer(deliveryDateTime,cust,org,log);
		if (inv!=null) {
			//log.addLogLine("Creating invoice detail ...");
			det = createInvoiceDetail(name,inv,cust,orderDateTime,deliveryDateTime,units,sp,linkDetailToObject,log);
		}
		return det;
	}
	
	private CrmInvoiceObject getOrCreateInvoiceForDateTimeCustomer(Date dateTime,CrmContactObject cust,CrmDeliveryOrganization org,BtcLog log) {
		CrmInvoiceBatch		btc		= null;
		CrmInvoiceObject	inv 	= null;
		btc = getInvoiceBatchForDateTime(dateTime,org,log.getExecutingAsUser());
		if (btc==null) {
			//log.addLogLine("Creating invoice batch ...");
			Date startDate = getInvoiceBatchStart(dateTime); 
			Date endDate = getInvoiceBatchEnd(dateTime);
			btc = createInvoiceBatch(org,startDate,endDate,log);
		}
		if (btc!=null) {
			inv = getInvoiceForCustomer(cust,btc,log.getExecutingAsUser());
			if (inv==null) {
				//log.addLogLine("Creating invoice ...");
				inv = createInvoice(btc,cust,org,log);
			}
		}
		return inv;
	}
	
	private CrmInvoiceBatch getInvoiceBatchForDateTime(Date dateTime,CrmDeliveryOrganization organization,DbUser executeAsUser) {
		CrmInvoiceBatch btc = null;
		for (Entry<Long,CrmInvoiceBatch> entry: invoiceBatches.entrySet()) {
			if (
				(!entry.getValue().getFinalize().getValue()) &&
				(entry.getValue().getFinalized().getValue()==null) &&
				(entry.getValue().getOrganization().equals(organization.getId().getValue())) &&
				(entry.getValue().getStart().getValue().getTime()<=dateTime.getTime()) &&
				(entry.getValue().getEnd().getValue().getTime()>=dateTime.getTime())
				) {
				btc = entry.getValue();
				break;
			}
		}
		if (btc==null) {
 			QryFetch f = new QryFetch(CrmInvoiceBatch.class.getName());
			f.getConditions().add(new QryFetchCondition("organization",QryFetchCondition.OPERATOR_CONTAINS,organization.getId()));
			f.getConditions().add(new QryFetchCondition("start",QryFetchCondition.OPERATOR_LESS_OR_EQUALS,new DtDateTime(dateTime)));
			f.getConditions().add(new QryFetchCondition("end",QryFetchCondition.OPERATOR_GREATER_OR_EQUALS,new DtDateTime(dateTime)));
			f.getConditions().add(new QryFetchCondition("finalize",QryFetchCondition.OPERATOR_EQUALS,new DtBoolean(false)));
			f.getConditions().add(new QryFetchCondition("finalized",QryFetchCondition.OPERATOR_EQUALS,new DtDateTime()));
			DbIndex.getInstance().executeFetch(f, executeAsUser, this);
			for (MdlObjectRef ref: f.getMainResults().getReferences()) {
				btc = (CrmInvoiceBatch) ref.getDataObject();
				invoiceBatches.put(btc.getId().getValue(), btc);
			}

		}
		return btc;
	}

	private CrmContactObject getCustomerById(String customerCollection,long id,DbUser executeAsUser) {
		CrmContactObject customer = customers.get(id);
		if (customer==null) {
			QryFetch f = new QryFetch(customerCollection,id);
			DbIndex.getInstance().executeFetch(f,executeAsUser,this);
			if (f.getMainResults().getReferences().size()>0) {
				MdlObjectRef ref = f.getMainResults().getReferences().get(0);
				customer = (CrmContactObject) ref.getDataObject();
				customers.put(id,customer);
			}
		}
		return customer;
	}

	private CrmInvoiceObject getInvoiceForCustomer(CrmContactObject customer,CrmInvoiceBatch btc,DbUser executeAsUser) {
		CrmInvoiceObject inv = null;
		for (MdlDataObject obj: btc.getChildObjects("invoices")) {
			if ((obj instanceof CrmOrganizationInvoice) && (customer instanceof CrmOrganization)) {
				CrmOrganizationInvoice tst = (CrmOrganizationInvoice) obj;
				if (tst.getCustomer().getValue().equals(customer.getId().getValue())) {
					inv = tst;
				}
			} else if ((obj instanceof CrmPersonInvoice) && (customer instanceof CrmPerson)) {
				CrmPersonInvoice tst = (CrmPersonInvoice) obj;
				if (tst.getCustomer().getValue().equals(customer.getId().getValue())) {
					inv = tst;
				}
			}
		}
		if (inv==null) {
			QryFetch f = null;
			if (customer instanceof CrmOrganization) {
				f = new QryFetch(CrmOrganizationInvoice.class.getName());
			} else if (customer instanceof CrmPerson) {
				f = new QryFetch(CrmPersonInvoice.class.getName());
			}
			f.getConditions().add(new QryFetchCondition("invoiceBatch",QryFetchCondition.OPERATOR_CONTAINS,btc.getId()));
			f.getConditions().add(new QryFetchCondition("customer",QryFetchCondition.OPERATOR_CONTAINS,customer.getId()));
			DbIndex.getInstance().executeFetch(f, executeAsUser, this);
			for (MdlObjectRef ref: f.getMainResults().getReferences()) {
				inv = (CrmInvoiceObject) ref.getDataObject();
				btc.addChildObject("invoices",inv);
			}
		}
		return inv;
	}
	
	private CrmInvoiceBatch createInvoiceBatch(CrmDeliveryOrganization organization,Date start,Date end,BtcLog log) {
		CrmInvoiceBatch btc = new CrmInvoiceBatch();
		
		btc.getName().setValue(organization.getName().getValue() + " " + Generic.getDateTimeString(start,true,false));
		btc.getOrganization().setValue(organization);
		btc.getStart().setValue(start);
		btc.getEnd().setValue(end);

		QryTransaction t = new QryTransaction(log.getExecutingAsUser());
		t.addQuery(new QryAdd(btc));
		DbIndex.getInstance().executeTransaction(t,this);
		if (getTransactionErrors(t, log)) {
			btc = null;
		} else {
			log.addLogLine("Created invoice batch: " + btc.getName().getValue());
			invoiceBatches.put(btc.getId().getValue(), btc);
		}
		
		return btc;
	}

	private CrmInvoiceObject createInvoice(CrmInvoiceBatch btc,CrmContactObject customer,CrmDeliveryOrganization org,BtcLog log) {
		CrmInvoiceObject inv = null;

		CrmAddressObject address = getInvoiceAddressForCustomer(customer,log.getExecutingAsUser());
		
		if (customer instanceof CrmOrganization) {
			CrmOrganizationInvoice invoice = new CrmOrganizationInvoice();
			invoice.getCustomer().setValue(customer);
			if (address!=null) {
				invoice.getAddress().setValue(address);
			}
			inv = invoice;
		} else if (customer instanceof CrmPerson) {
			CrmPersonInvoice invoice = new CrmPersonInvoice();
			invoice.getCustomer().setValue(customer);
			if (address!=null) {
				invoice.getAddress().setValue(address);
			}
			inv = invoice;
		}
		
		QryTransaction t = new QryTransaction(log.getExecutingAsUser());
		inv.getName().setValue(customer.getName().getValue());
		inv.getInvoiceBatch().setValue(btc);
		inv.getCustomerName().setValue(customer.getName().getValue());
		inv.getCustomerTelephone().setValue(customer.getTelephone().getValue());
		inv.getCustomerEmail().setValue(customer.getEmail().getValue());
		inv.getOrganization().setValue(org);
		inv.getOrganizationName().setValue(org.getName().getValue());
		inv.getOrganizationAccountNumber().setValue(org.getAccountNumber().getValue());
		inv.getOrganizationChamberOfCommerceCode().setValue(org.getChamberOfCommerceCode().getValue());
		if (address!=null) {
			inv.getAddressCity().setValue(address.getCity().getValue());
			inv.getAddressCountry().setValue(address.getCountry().getValue());
			inv.getAddressLine1().setValue(address.getLine1().getValue());
			inv.getAddressLine2().setValue(address.getLine2().getValue());
			inv.getAddressLine3().setValue(address.getLine3().getValue());
			inv.getAddressPostalCode().setValue(address.getPostalCode().getValue());
		} else {
			StringBuffer invoiceBatchLog = btc.getLog().getValue();
			String warning = "WARNING: No invoice address found for customer: " + customer.getName().getValue();
			log.addLogLine(warning);
			invoiceBatchLog.append(warning);
			invoiceBatchLog.append("\n");
		}
		t.addQuery(new QryAdd(inv));
		DbIndex.getInstance().executeTransaction(t,this);
		if (getTransactionErrors(t, log)) {
			inv = null;
		} else {
			log.addLogLine("Created invoice: " + inv.getName().getValue());
			btc.addChildObject("invoices",inv);
		}
		return inv;
	}
	
	private CrmInvoiceDetailObject createInvoiceDetail(String name,CrmInvoiceObject inv,CrmContactObject customer,Date orderDateTime,Date deliveryDateTime,BigDecimal units,CrmServiceProduct sp,MdlDataObject linkDetailToObject,BtcLog log) {
		CrmInvoiceDetailObject det = null;

		CrmServiceProductPrice serviceProductPrice = getServiceProductPriceForDateTime(orderDateTime,sp,log.getExecutingAsUser());
		if (serviceProductPrice==null) {
			log.addError("Service or product price not found for: " + sp.getName().getValue() + " on " + Generic.getDateString(orderDateTime, true));
			return det;
		} 

		List<CrmContractObject> customerContracts = getContractsForCustomer(customer,sp.getOrganization().getValue(),log.getExecutingAsUser());
		CrmContractDetailObject contractDetail = getContractDetailForDateTimeServiceProduct(customer,orderDateTime,sp,customerContracts,log.getExecutingAsUser());
		
		QryTransaction t = new QryTransaction(log.getExecutingAsUser());
		if (inv instanceof CrmOrganizationInvoice) {
			CrmOrganizationInvoiceDetail invDetail = new CrmOrganizationInvoiceDetail();
			invDetail.getInvoice().setValue(inv);
			if (contractDetail!=null) {
				invDetail.getContractDetail().setValue(contractDetail);
			}
			if (linkDetailToObject instanceof CrmOrganizationDelivery) {
				invDetail.getDelivery().setValue(linkDetailToObject);
			} else if (linkDetailToObject instanceof CrmOrganizationTaskLog) {
				invDetail.getLog().setValue(linkDetailToObject);
			}
			det = invDetail;
		} else if (inv instanceof CrmPersonInvoice) {
			CrmPersonInvoiceDetail invDetail = new CrmPersonInvoiceDetail();
			invDetail.getInvoice().setValue(inv);
			if (contractDetail!=null) {
				invDetail.getContractDetail().setValue(contractDetail);
			}
			if (linkDetailToObject instanceof CrmPersonDelivery) {
				invDetail.getDelivery().setValue(linkDetailToObject);
			} else if (linkDetailToObject instanceof CrmPersonTaskLog) {
				invDetail.getLog().setValue(linkDetailToObject);
			}
			det = invDetail;
		}

		det.getName().setValue(name);
		det.getOrderDateTime().setValue(orderDateTime);
		det.getDeliveryDateTime().setValue(deliveryDateTime);
		det.getDeliveryUnits().setValue(units);
		det.getServiceProduct().setValue(sp);
		det.getServiceProductCostPerUnit().setValue(serviceProductPrice.getCostPerUnit().getValue());
		det.getServiceProductName().setValue(sp.getName().getValue());
		det.getServiceProductDescription().setValue(sp.getDescription().getValue());
		det.getServiceProductPricePerUnit().setValue(serviceProductPrice.getPricePerUnit().getValue());
		det.getServiceProductService().setValue(sp.getService().getValue());
		if (contractDetail!=null) {
			det.getContractDetailPricePerUnit().setValue(contractDetail.getPricePerUnit().getValue());
		}
		
		BigDecimal price = null;
		BigDecimal cost = new BigDecimal("0.00");
		BigDecimal turnOver = new BigDecimal("0.00");
		
		if (contractDetail!=null) {
			price = det.getContractDetailPricePerUnit().getValue();
		} else {
			price = det.getServiceProductPricePerUnit().getValue();
		}
		
		cost = det.getServiceProductCostPerUnit().getValue().multiply(units);
		turnOver = price.multiply(units);
		
		cost = cost.setScale(2,RoundingMode.HALF_UP);
		turnOver = turnOver.setScale(2,RoundingMode.HALF_UP);

		det.getCost().setValue(cost);
		det.getTurnOver().setValue(turnOver);
		
		t.addQuery(new QryAdd(det));

		DbIndex.getInstance().executeTransaction(t,this);
		if (getTransactionErrors(t, log)) {
			det = null;
		} else {
			//log.addLogLine("Created invoice detail: " + det.getName().getValue());
		}

		if (det!=null) {
			t = new QryTransaction(log.getExecutingAsUser());
			if (linkDetailToObject instanceof CrmOrganizationDelivery) {
				CrmOrganizationDelivery del = (CrmOrganizationDelivery) linkDetailToObject;
				del.getInvoiceDetail().setValue(det);
				t.addQuery(new QryUpdate(del));
			} else if (linkDetailToObject instanceof CrmPersonDelivery) {
				CrmPersonDelivery del = (CrmPersonDelivery) linkDetailToObject;
				del.getInvoiceDetail().setValue(det);
				t.addQuery(new QryUpdate(del));
			} else if (linkDetailToObject instanceof CrmOrganizationTaskLog) {
				CrmOrganizationTaskLog tl = (CrmOrganizationTaskLog) linkDetailToObject;
				tl.getInvoiceDetail().setValue(det);
				t.addQuery(new QryUpdate(tl));
			} else if (linkDetailToObject instanceof CrmPersonTaskLog) {
				CrmPersonTaskLog tl = (CrmPersonTaskLog) linkDetailToObject;
				tl.getInvoiceDetail().setValue(det);
				t.addQuery(new QryUpdate(tl));
			}
			DbIndex.getInstance().executeTransaction(t,this);
			if (getTransactionErrors(t, log)) {
				det = null;
			} else {
				//log.addLogLine("Linked invoice detail to: " + linkDetailToObject.getName().getValue());
			}
		}
		
		return det;
	}

	private void totalizeBatches(BtcLog log) {
		for (Entry<Long,CrmInvoiceBatch> entry: invoiceBatches.entrySet()) {
			CrmInvoiceBatch btc = entry.getValue();
			
			List<CrmInvoiceObject> invoices = new ArrayList<CrmInvoiceObject>();
			
			QryFetch f = new QryFetch(CrmOrganizationInvoice.class.getName());
			f.addCondition(new QryFetchCondition("invoiceBatch",QryFetchCondition.OPERATOR_CONTAINS,btc.getId()));
			DbIndex.getInstance().executeFetch(f, log.getExecutingAsUser(), this);
			for (MdlObjectRef ref: f.getMainResults().getReferences()) {
				invoices.add((CrmInvoiceObject) ref.getDataObject());
			}

			f = new QryFetch(CrmPersonInvoice.class.getName());
			f.addCondition(new QryFetchCondition("invoiceBatch",QryFetchCondition.OPERATOR_CONTAINS,btc.getId()));
			DbIndex.getInstance().executeFetch(f, log.getExecutingAsUser(), this);
			for (MdlObjectRef ref: f.getMainResults().getReferences()) {
				invoices.add((CrmInvoiceObject) ref.getDataObject());
			}
			
			BigDecimal cost = new BigDecimal("0.00");
			BigDecimal turnOver = new BigDecimal("0.00");
			
			if (invoices.size()>0) {
				for (CrmInvoiceObject inv: invoices) {
					totalizeInvoice(inv,log);
					cost = cost.add(inv.getCost().getValue());
					turnOver = turnOver.add(inv.getTurnOver().getValue());
				}
			}
			
			btc.getCost().setValue(cost);
			btc.getTurnOver().setValue(turnOver);

			String msg = "Totalized invoice batch: " + btc.getName().getValue() + ", turn over: " + btc.getTurnOver().getValue();
			btc.getLog().getValue().append(msg);
			btc.getLog().getValue().append("\n");

			QryTransaction t = new QryTransaction(log.getExecutingAsUser());
			t.addQuery(new QryUpdate(btc));
			DbIndex.getInstance().executeTransaction(t,this);
			if (!getTransactionErrors(t, log)) {
				log.addLogLine(msg);
			}
		}

	}

	private void totalizeInvoice(CrmInvoiceObject inv, BtcLog log) {
		List<CrmInvoiceDetailObject> invoiceDetails = new ArrayList<CrmInvoiceDetailObject>();
		
		QryFetch f = null;
		if (inv instanceof CrmOrganizationInvoice) {
			f = new QryFetch(CrmOrganizationInvoiceDetail.class.getName());
		} else if (inv instanceof CrmPersonInvoice) {
			f = new QryFetch(CrmOrganizationInvoiceDetail.class.getName());
		}
		f.addCondition(new QryFetchCondition("invoice",QryFetchCondition.OPERATOR_CONTAINS,inv.getId()));
		DbIndex.getInstance().executeFetch(f, log.getExecutingAsUser(), this);
		for (MdlObjectRef ref: f.getMainResults().getReferences()) {
			invoiceDetails.add((CrmInvoiceDetailObject) ref.getDataObject());
		}
		
		BigDecimal cost = new BigDecimal("0.00");
		BigDecimal turnOver = new BigDecimal("0.00");

		if (invoiceDetails.size()>0) {
			for (CrmInvoiceDetailObject det: invoiceDetails) {
				cost = cost.add(det.getCost().getValue());
				turnOver = turnOver.add(det.getTurnOver().getValue());
			}
		}

		inv.getCost().setValue(cost);
		inv.getTurnOver().setValue(turnOver);
		
		QryTransaction t = new QryTransaction(log.getExecutingAsUser());
		t.addQuery(new QryUpdate(inv));
		DbIndex.getInstance().executeTransaction(t,this);
		if (!getTransactionErrors(t, log)) {
			//log.addLogLine("Totalized invoice: " + inv.getName().getValue() + " -> " + inv.getTurnOver().getValue());
		}
	}

	private List<CrmContractObject> getContractsForCustomer(CrmContactObject customer,long organizationId, DbUser executeAsUser) {
		List<CrmContractObject> contracts = customerContracts.get(customer.getId().getValue());
		if (contracts==null) { 
			contracts = new ArrayList<CrmContractObject>();
			QryFetch f = null;
			
			if (customer instanceof CrmOrganization) {
				f = new QryFetch(CrmOrganizationContract.class.getName());
			} else if (customer instanceof CrmPerson) {
				f = new QryFetch(CrmPersonContract.class.getName());
			}
			
			f.getConditions().add(new QryFetchCondition("customer",QryFetchCondition.OPERATOR_CONTAINS,new DtLong(customer.getId().getValue())));
			f.getConditions().add(new QryFetchCondition("organization",QryFetchCondition.OPERATOR_CONTAINS,new DtLong(organizationId)));
			f.getConditions().add(new QryFetchCondition("active",QryFetchCondition.OPERATOR_EQUALS,new DtBoolean(true)));
			DbIndex.getInstance().executeFetch(f,executeAsUser,this);
	
			for (MdlObjectRef ref: f.getMainResults().getReferences()) {
				contracts.add((CrmContractObject) ref.getDataObject());
			}
	
			for (CrmContractObject contractObject: contracts) {
				if (contractObject instanceof CrmOrganizationContract) {
					f = new QryFetch(CrmOrganizationContractDetail.class.getName());
				} else if (contractObject instanceof CrmPersonContract) {
					f = new QryFetch(CrmPersonContractDetail.class.getName());
				}
				f.getConditions().add(new QryFetchCondition("contract",QryFetchCondition.OPERATOR_CONTAINS,new DtLong(contractObject.getId().getValue())));
				DbIndex.getInstance().executeFetch(f,executeAsUser,this);
				for (MdlObjectRef ref: f.getMainResults().getReferences()) {
					contractObject.addChildObject("details", ref.getDataObject());
				}
			}
			customerContracts.put(customer.getId().getValue(),contracts);
		}
		return contracts;
	}

	private CrmServiceProductPrice getServiceProductPriceForDateTime(Date dateTime,CrmServiceProduct serviceProduct,DbUser executeAsUser) {
		CrmServiceProductPrice price = null;
		for (MdlDataObject obj: serviceProduct.getChildObjects("prices")) {
			CrmServiceProductPrice pObj = (CrmServiceProductPrice) obj;
			if (pObj.getStart().getValue().getTime()<=dateTime.getTime()) {
				price = pObj;
				break;
			}
		}
		return price;
	}

	private CrmContractDetailObject getContractDetailForDateTimeServiceProduct(CrmContactObject customer,Date dateTime,CrmServiceProduct sp,List<CrmContractObject> customerContracts,DbUser executeAsUser) {
		CrmContractDetailObject detail = null;

		if (customerContracts.size()>0) {
			if (customer instanceof CrmOrganization) {
				for (CrmContractObject contractObj: customerContracts) {
					CrmOrganizationContract contract = (CrmOrganizationContract) contractObj;
					if (
						((contract.getStart().getValue()!=null) && (contract.getStart().getValue().getTime()<=dateTime.getTime())) &&
						((contract.getEnd().getValue()==null) || (contract.getEnd().getValue().getTime()>=dateTime.getTime()))
						) {
						for (MdlDataObject detailObject: contract.getChildObjects("details")) {
							CrmOrganizationContractDetail contractDetail = (CrmOrganizationContractDetail) detailObject;
							if (contractDetail.getServiceProduct().getValue().equals(sp.getId().getValue())) {
								detail = contractDetail;
							}
						}
					}
				}
			} else if (customer instanceof CrmPerson) {
				for (CrmContractObject contractObj: customerContracts) {
					CrmPersonContract contract = (CrmPersonContract) contractObj;
					if (
						((contract.getStart().getValue()!=null) && (contract.getStart().getValue().getTime()<=dateTime.getTime())) &&
						((contract.getEnd().getValue()==null) || (contract.getEnd().getValue().getTime()>=dateTime.getTime()))
						) {
						for (MdlDataObject detailObject: contract.getChildObjects("details")) {
							CrmPersonContractDetail contractDetail = (CrmPersonContractDetail) detailObject;
							if (contractDetail.getServiceProduct().getValue().equals(sp.getId().getValue())) {
								detail = contractDetail;
							}
						}
					}
				}
			}
		}
		
		return detail;
	}
	
	private CrmAddressObject getInvoiceAddressForCustomer(CrmContactObject customer, DbUser executeAsUser) {
		CrmAddressObject address = null;
		long addressId = 0;

		if (customer instanceof CrmOrganization) {
			CrmOrganization org = (CrmOrganization) customer;
			if (org.getInvoiceAddress().getValue()>0) {
				addressId = org.getInvoiceAddress().getValue();
			}
		} else if (customer instanceof CrmPerson) {
			CrmPerson pers = (CrmPerson) customer;
			if (pers.getInvoiceAddress().getValue()>0) {
				addressId = pers.getInvoiceAddress().getValue();
			}
		}
		
		if (addressId==0) {
			QryFetchList fl = new QryFetchList(executeAsUser);
			QryFetch f = null;
			if (customer instanceof CrmOrganization) {
				f = new QryFetch(CrmOrganizationAddress.class.getName());
				f.getConditions().add(new QryFetchCondition("organization",QryFetchCondition.OPERATOR_CONTAINS,new DtLong(customer.getId().getValue())));
			} else if (customer instanceof CrmPerson) {
				f = new QryFetch(CrmPersonAddress.class.getName());
				f.getConditions().add(new QryFetchCondition("person",QryFetchCondition.OPERATOR_CONTAINS,new DtLong(customer.getId().getValue())));
			}
	
			fl.addQuery(f);
			DbIndex.getInstance().executeFetchList(fl,this);
			
			if (f.getMainResults().getReferences().size()>0) {
				address = (CrmAddressObject) f.getMainResults().getReferences().get(0).getDataObject();
				QryTransaction t = new QryTransaction(executeAsUser);
				if (customer instanceof CrmOrganization) {
					CrmOrganization org = (CrmOrganization) customer;
					org.getInvoiceAddress().setValue(address);
					t.addQuery(new QryUpdate(org));
				} else if (customer instanceof CrmPerson) {
					CrmPerson pers = (CrmPerson) customer;
					pers.getInvoiceAddress().setValue(address);
					t.addQuery(new QryUpdate(pers));
				}
				DbIndex.getInstance().executeTransaction(t, this);
			}
		} else {
			QryFetchList fl = new QryFetchList(executeAsUser);
			QryFetch f = null;
			if (customer instanceof CrmOrganization) {
				f = new QryFetch(CrmOrganizationAddress.class.getName(),addressId);
			} else if (customer instanceof CrmPerson) {
				f = new QryFetch(CrmPersonAddress.class.getName(),addressId);
			}
			fl.addQuery(f);
			DbIndex.getInstance().executeFetchList(fl,this);
			if (f.getMainResults().getReferences().size()>0) {
				address = (CrmAddressObject) f.getMainResults().getReferences().get(0).getDataObject();
			}
		}
		return address;
	}

	private Date getInvoiceBatchStart(Date dateTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
	}
	
	private Date getInvoiceBatchEnd(Date dateTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		int month = cal.get(Calendar.MONTH);
		while(cal.get(Calendar.MONTH)==month) {
			cal.add(Calendar.DATE,1);
		}
		Date d = cal.getTime();
		d.setTime(d.getTime() - 1L);
		return d;
	}
	
}
