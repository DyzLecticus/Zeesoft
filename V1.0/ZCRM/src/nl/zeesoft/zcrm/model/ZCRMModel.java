package nl.zeesoft.zcrm.model;

import java.util.Calendar;
import java.util.List;

import nl.zeesoft.zadf.gui.property.PrpTextAreaStringBuffer;
import nl.zeesoft.zadf.model.DbModel;
import nl.zeesoft.zadf.model.impl.DbCollection;
import nl.zeesoft.zadf.model.impl.DbCollectionFilter;
import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zadf.model.impl.DbFetch;
import nl.zeesoft.zadf.model.impl.DbFetchFilter;
import nl.zeesoft.zadf.model.impl.DbModule;
import nl.zeesoft.zadf.model.impl.DbReferenceFilter;
import nl.zeesoft.zadf.model.impl.DbReport;
import nl.zeesoft.zcrm.batch.impl.BtcCrmFinalize;
import nl.zeesoft.zcrm.batch.impl.BtcCrmFinalizeAll;
import nl.zeesoft.zcrm.batch.impl.BtcCrmInvoice;
import nl.zeesoft.zcrm.batch.impl.BtcGenerateCompany;
import nl.zeesoft.zcrm.batch.impl.BtcGenerateNetherlands;
import nl.zeesoft.zcrm.batch.impl.BtcGenerateZeesoft;
import nl.zeesoft.zcrm.format.impl.FmtCrmContacts;
import nl.zeesoft.zcrm.format.impl.FmtCrmTaskLog;
import nl.zeesoft.zcrm.model.impl.CrmCountry;
import nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization;
import nl.zeesoft.zcrm.model.impl.CrmInvoiceBatch;
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
import nl.zeesoft.zcrm.model.impl.CrmServiceProductValueAddedTax;
import nl.zeesoft.zcrm.model.impl.CrmValueAddedTax;
import nl.zeesoft.zcrm.model.impl.CrmValueAddedTaxPercentage;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.model.MdlCollectionProperty;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.BtcProgram;
import nl.zeesoft.zodb.model.impl.BtcRepeat;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zpo.model.ZPOModel;
import nl.zeesoft.zpo.model.impl.UsrTaskLog;

public class ZCRMModel extends ZPOModel {
	public final static String 						MODULE_ZCRM 				= "ZCRM";
	
	public final static String 						MODULE_ZCRM_LABEL			= "Business";
	public final static String 						MODULE_ZCRM_DESC			= 
		"This module provides access to all customer relationship information.\n" +
		"The collections in this module are provided by the Zeesoft Customer Relationship Manager.\n" +
		"The Zeesoft Customer Relationship Manager extends the Zeesoft Personal Organizer.\n" +
		"\n" +
		"Features:\n" +
		"- Country and value added tax administration.\n" +
		"- Service and/or product price administration.\n" +
		"- People and organization contact, address, contract and delivery administration.\n" +
		"- Invoice generation and finalization by batch procedure.\n" +
		"";

	public final static int 						USER_LEVEL_CRM_ADMIN			= 100;
	public final static int 						USER_LEVEL_CRM_CONTRACT_MANAGER	= 1000;
	public final static int 						USER_LEVEL_CRM_DELIVERY_MANAGER	= 2000;
	public final static int 						USER_LEVEL_FETCH_PEOPLE			= 5000;
	
	@Override
	protected List<String> getPersistedClassNames() {
		List<String> l = super.getPersistedClassNames();
		l.add(CrmCountry.class.getName());
		l.add(CrmValueAddedTax.class.getName());
		l.add(CrmValueAddedTaxPercentage.class.getName());

		l.add(CrmServiceProduct.class.getName());
		l.add(CrmServiceProductPrice.class.getName());
		l.add(CrmServiceProductValueAddedTax.class.getName());

		l.add(CrmOrganization.class.getName());
		l.add(CrmOrganizationAddress.class.getName());
		l.add(CrmOrganizationContract.class.getName());
		l.add(CrmOrganizationContractDetail.class.getName());
		l.add(CrmOrganizationDelivery.class.getName());
		l.add(CrmOrganizationTaskLog.class.getName());

		l.add(CrmPerson.class.getName());
		l.add(CrmPersonAddress.class.getName());
		l.add(CrmPersonContract.class.getName());
		l.add(CrmPersonContractDetail.class.getName());
		l.add(CrmPersonDelivery.class.getName());
		l.add(CrmPersonTaskLog.class.getName());

		l.add(CrmInvoiceBatch.class.getName());
		l.add(CrmOrganizationInvoice.class.getName());
		l.add(CrmOrganizationInvoiceDetail.class.getName());
		l.add(CrmPersonInvoice.class.getName());
		l.add(CrmPersonInvoiceDetail.class.getName());

		l.add(CrmDeliveryOrganization.class.getName());
		return l;
	}

	@Override
	public DbUser generateInitialData(Object source) {
		DbUser admin = super.generateInitialData(source);

		QryTransaction t = null;

		// Users
		DbUser user = null;
		DbUser crmAdmin = null;
		t = new QryTransaction(admin);
		user = new DbUser();
		user.getName().setValue("crmAdmin");
		user.getPassword().setValue(DbConfig.getInstance().encodePassword(new StringBuffer("1crmAdmin!")));
		user.getLevel().setValue(USER_LEVEL_CRM_ADMIN);
		crmAdmin = user;
		t.addQuery(new QryAdd(user));
		user = new DbUser();
		user.getName().setValue("contractManager");
		user.getPassword().setValue(DbConfig.getInstance().encodePassword(new StringBuffer("1contractManager!")));
		user.getLevel().setValue(USER_LEVEL_CRM_CONTRACT_MANAGER);
		t.addQuery(new QryAdd(user));
		user = new DbUser();
		user.getName().setValue("deliveryManager");
		user.getPassword().setValue(DbConfig.getInstance().encodePassword(new StringBuffer("1deliveryManager!")));
		user.getLevel().setValue(USER_LEVEL_CRM_DELIVERY_MANAGER);
		t.addQuery(new QryAdd(user));
		DbIndex.getInstance().executeTransaction(t, source);

		BtcProgram program = null;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// Invoice batch procedure
		t = new QryTransaction(admin);
		program = new BtcProgram();
		program.getName().setValue(BtcCrmInvoice.class.getName());
		program.getDescription().setValue(
			"This program will generate invoices for all uninvoiced deliveries and logs.\n"
			);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(crmAdmin.getId().getValue());
		program.getRepeatAmount().setValue(1);
		program.getRepeat().setValue(repeats.get(BtcRepeat.MONTH).getId().getValue());
		program.getMaxLogs().setValue(100);
		t.addQuery(new QryAdd(program));

		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// Finalize batch procedure
		program = new BtcProgram();
		program.getName().setValue(BtcCrmFinalize.class.getName());
		program.getDescription().setValue(
			"This program will finalize all invoice batches checked as such.\n"
			);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(crmAdmin.getId().getValue());
		program.getRepeatAmount().setValue(15);
		program.getRepeat().setValue(repeats.get(BtcRepeat.MINUTE).getId().getValue());
		t.addQuery(new QryAdd(program));

		// Finalize all batch procedure
		program = new BtcProgram();
		program.getName().setValue(BtcCrmFinalizeAll.class.getName());
		program.getDescription().setValue(
			"This program will finalize all unfinalized invoice batches.\n"
			);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(crmAdmin.getId().getValue());
		program.getRepeatAmount().setValue(1000);
		program.getRepeat().setValue(repeats.get(BtcRepeat.YEAR).getId().getValue());
		program.getActive().setValue(false);
		t.addQuery(new QryAdd(program));
		
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// Netherlands Data Generation batch procedure
		program = new BtcProgram();
		program.getName().setValue(BtcGenerateNetherlands.class.getName());
		program.getDescription().setValue(
			"This program will generate the Netherlands, including value added tax brackets and percentages.\n" +
			"It will only do so if no CRM data exists in the database.\n"
			);
		program.getActive().setValue(false);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(crmAdmin.getId().getValue());
		program.getRepeatAmount().setValue(1000);
		program.getRepeat().setValue(repeats.get(BtcRepeat.YEAR).getId().getValue());
		t.addQuery(new QryAdd(program));

		// Zeesoft Data Generation batch procedure
		program = new BtcProgram();
		program.getName().setValue(BtcGenerateZeesoft.class.getName());
		program.getDescription().setValue(
			"This program will generate Zeesoft specific CRM data.\n" +
			"It will only do so if no CRM data exists in the database.\n"
			);
		program.getActive().setValue(false);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(crmAdmin.getId().getValue());
		program.getRepeatAmount().setValue(1000);
		program.getRepeat().setValue(repeats.get(BtcRepeat.YEAR).getId().getValue());
		t.addQuery(new QryAdd(program));
		
		// Company Data Generation batch procedure
		program = new BtcProgram();
		program.getName().setValue(BtcGenerateCompany.class.getName());
		program.getDescription().setValue(
			"This program will generate demo CRM data.\n" +
			"It will only do so if no CRM data exists in the database.\n"
			);
		program.getActive().setValue(false);
		program.getStart().setValue(cal.getTime());
		program.getExecuteAsUser().setValue(crmAdmin.getId().getValue());
		program.getRepeatAmount().setValue(1000);
		program.getRepeat().setValue(repeats.get(BtcRepeat.YEAR).getId().getValue());
		t.addQuery(new QryAdd(program));
		DbIndex.getInstance().executeTransaction(t, source);

		DbFetch usrTaskLogFetch = collectionFetches.get(UsrTaskLog.class.getName());
		DbFetch orgTaskLogFetch = collectionFetches.get(CrmOrganizationTaskLog.class.getName());
		DbFetch persTaskLogFetch = collectionFetches.get(CrmPersonTaskLog.class.getName());
		DbFetch orgFetch = collectionFetches.get(CrmOrganization.class.getName());
		DbFetch persFetch = collectionFetches.get(CrmPerson.class.getName());

		// Report formats
		t = new QryTransaction(admin);
		t.addQuery(new QryAdd(getNewReportFormat(FmtCrmTaskLog.class.getName(),"csv")));
		t.addQuery(new QryAdd(getNewReportFormat(FmtCrmContacts.class.getName(),"csv")));
		DbIndex.getInstance().executeTransaction(t, source);

		// Reports
		t = new QryTransaction(admin);
		DbReport report = new DbReport();
		report.getName().setValue(
				"CRM " + collections.get(UsrTaskLog.class.getName()).getNameSingle().getValue().toLowerCase()
				);
		report.getDescription().setValue("CRM task logs for a certain period for a certain user in CSV format.\n");
		report.getFetches().getValue().add(usrTaskLogFetch.getId().getValue());
		report.getFetches().getValue().add(orgTaskLogFetch.getId().getValue());
		report.getFetches().getValue().add(persTaskLogFetch.getId().getValue());
		report.getFormat().setValue(formats.get(FmtCrmTaskLog.class.getName()));
		report.getUserLevelVisible().setValue(DbUser.USER_LEVEL_MAX);
		t.addQuery(new QryAdd(report));

		report = new DbReport();
		report.getName().setValue(
				"CRM " + 
				collections.get(CrmOrganization.class.getName()).getNameMulti().getValue().toLowerCase() + " and " +
				collections.get(CrmPerson.class.getName()).getNameMulti().getValue().toLowerCase()
				);
		report.getDescription().setValue(report.getName().getValue() + " in CSV format.\n");
		report.getFetches().getValue().add(orgFetch.getId().getValue());
		report.getFetches().getValue().add(persFetch.getId().getValue());
		report.getFormat().setValue(formats.get(FmtCrmContacts.class.getName()));
		report.getUserLevelVisible().setValue(USER_LEVEL_FETCH_PEOPLE);
		t.addQuery(new QryAdd(report));
		DbIndex.getInstance().executeTransaction(t, source);
		
		return admin;
	}
	
	@Override
	protected void setModuleProperties(DbModule dbMod) {
		if (dbMod.getName().equals(MODULE_ZCRM)) {
			dbMod.getLabel().setValue(MODULE_ZCRM_LABEL);
			dbMod.getDescription().setValue(MODULE_ZCRM_DESC);
		} else {
			super.setModuleProperties(dbMod);
		}
	}

	@Override
	protected void setCollectionAccess(DbCollection dbCol) {
		super.setCollectionAccess(dbCol);
		if (dbCol.getName().equals(CrmInvoiceBatch.class.getName())) {
			dbCol.getUserLevelAdd().setValue(DbUser.USER_LEVEL_OFF);
		}
		if (
			(dbCol.getName().equals(CrmOrganizationInvoice.class.getName())) ||
			(dbCol.getName().equals(CrmPersonInvoice.class.getName()))
			) {
			dbCol.getUserLevelAdd().setValue(DbUser.USER_LEVEL_OFF);
			dbCol.getUserLevelRemove().setValue(DbUser.USER_LEVEL_OFF);
		}
		if (
			(dbCol.getName().equals(CrmOrganizationInvoiceDetail.class.getName())) ||
			(dbCol.getName().equals(CrmPersonInvoiceDetail.class.getName()))
			) {
			dbCol.getUserLevelAdd().setValue(DbUser.USER_LEVEL_OFF);
			dbCol.getUserLevelUpdate().setValue(DbUser.USER_LEVEL_OFF);
			dbCol.getUserLevelRemove().setValue(DbUser.USER_LEVEL_OFF);
		}
	}

	@Override
	protected void setCollectionPropertyAccess(DbCollection dbCol,DbCollectionProperty dbProp) {
		super.setCollectionPropertyAccess(dbCol, dbProp);
		if (dbCol.getName().equals(CrmValueAddedTax.class.getName())) {
			if (dbProp.getName().getValue().equals("country")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (dbCol.getName().equals(CrmValueAddedTaxPercentage.class.getName())) {
			if (dbProp.getName().getValue().equals("start")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("percentage")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("valueAddedTax")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (dbCol.getName().equals(CrmValueAddedTaxPercentage.class.getName())) {
			if (dbProp.getName().getValue().equals("start")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("percentage")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("valueAddedTax")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (
			(dbCol.getName().equals(CrmDeliveryOrganization.class.getName())) ||
			(dbCol.getName().equals(CrmOrganization.class.getName()))
			) {
			if (dbProp.getName().getValue().equals("telephone")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("email")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("website")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("accountNumber")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("chamberOfCommerceCode")) {
				dbProp.getOrderInDetail().setValue(6);
				dbProp.getOrderInFilter().setValue(6);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("employees")) {
				dbProp.getOrderInDetail().setValue(7);
				dbProp.getOrderInFilter().setValue(7);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("invoiceAddress")) {
				dbProp.getOrderInDetail().setValue(8);
				dbProp.getOrderInFilter().setValue(8);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("nextInvoiceNumber")) {
				dbProp.getOrderInDetail().setValue(-1);
				dbProp.getOrderInFilter().setValue(-1);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (dbCol.getName().equals(CrmPerson.class.getName())) {
			if (dbProp.getName().getValue().equals("name")) {
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			}
			if (dbProp.getName().getValue().equals("firstName")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("middleName")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("preposition")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("lastName")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("telephone")) {
				dbProp.getOrderInDetail().setValue(6);
				dbProp.getOrderInFilter().setValue(6);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("email")) {
				dbProp.getOrderInDetail().setValue(7);
				dbProp.getOrderInFilter().setValue(7);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("birthDate")) {
				dbProp.getOrderInDetail().setValue(8);
				dbProp.getOrderInFilter().setValue(8);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("invoiceAddress")) {
				dbProp.getOrderInDetail().setValue(9);
				dbProp.getOrderInFilter().setValue(9);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (
			(dbCol.getName().equals(CrmOrganizationAddress.class.getName())) ||
			(dbCol.getName().equals(CrmPersonAddress.class.getName()))
			) {
			if (dbProp.getName().getValue().equals("line1")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("line2")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("line3")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("postalCode")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(5);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("city")) {
				dbProp.getOrderInDetail().setValue(6);
				dbProp.getOrderInFilter().setValue(6);
				dbProp.getOrderInGrid().setValue(6);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("country")) {
				dbProp.getOrderInDetail().setValue(7);
				dbProp.getOrderInFilter().setValue(7);
				dbProp.getOrderInGrid().setValue(7);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (
			(dbCol.getName().equals(CrmOrganizationDelivery.class.getName())) ||
			(dbCol.getName().equals(CrmPersonDelivery.class.getName())) ||
			(dbCol.getName().equals(CrmOrganizationTaskLog.class.getName())) ||
			(dbCol.getName().equals(CrmPersonTaskLog.class.getName()))
			) {
			if (dbProp.getName().getValue().equals("invoiceDetail")) {
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
				dbProp.getUserLevelVisible().setValue(DbUser.USER_LEVEL_MIN);
				dbProp.getOrderInDetail().setValue(-1);
				dbProp.getOrderInFilter().setValue(50);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (
			(dbCol.getName().equals(CrmOrganizationContract.class.getName())) ||
			(dbCol.getName().equals(CrmPersonContract.class.getName()))
			) {
			if (dbProp.getName().getValue().equals("start")) {
				dbProp.getOrderInDetail().setValue(10);
				dbProp.getOrderInFilter().setValue(10);
				dbProp.getOrderInGrid().setValue(10);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("end")) {
				dbProp.getOrderInDetail().setValue(11);
				dbProp.getOrderInFilter().setValue(11);
				dbProp.getOrderInGrid().setValue(11);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (
			(dbCol.getName().equals(CrmOrganizationContractDetail.class.getName())) ||
			(dbCol.getName().equals(CrmPersonContractDetail.class.getName()))
			) {
			if (dbProp.getName().getValue().equals("contract")) {
				dbProp.getOrderInGrid().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("serviceProduct")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
			}
		}
		if (dbCol.getName().equals(CrmServiceProduct.class.getName())) {
			if (dbProp.getName().getValue().equals("service")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(2);
			}
			if (dbProp.getName().getValue().equals("organization")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(3);
			}
		}
		if (dbCol.getName().equals(CrmServiceProductPrice.class.getName())) {
			if (dbProp.getName().getValue().equals("start")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("pricePerUnit")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("costPerUnit")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("serviceProduct")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (dbCol.getName().equals(CrmServiceProductValueAddedTax.class.getName())) {
			if (dbProp.getName().getValue().equals("start")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("country")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("valueAddedTax")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("serviceProduct")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if 	(dbCol.getName().equals(CrmInvoiceBatch.class.getName())) {
			if (!dbProp.getName().equals("finalize")) {
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			}
			if (
				(dbProp.getName().getValue().equals("turnOver")) ||
				(dbProp.getName().getValue().equals("turnOverExclVAT")) ||
				(dbProp.getName().getValue().equals("cost")) ||
				(dbProp.getName().getValue().equals("profit"))
				) {
				if (dbProp.getName().getValue().equals("turnOver")) {
					dbProp.getOrderInDetail().setValue(50);
					dbProp.getOrderInFilter().setValue(50);
					dbProp.getOrderInGrid().setValue(50);
					dbProp.getOrderInSelect().setValue(-1);
				}
				if (dbProp.getName().getValue().equals("turnOverExclVAT")) {
					dbProp.getOrderInDetail().setValue(51);
					dbProp.getOrderInFilter().setValue(51);
					dbProp.getOrderInGrid().setValue(-1);
					dbProp.getOrderInSelect().setValue(-1);
				}
				if (dbProp.getName().getValue().equals("cost")) {
					dbProp.getOrderInDetail().setValue(52);
					dbProp.getOrderInFilter().setValue(52);
					dbProp.getOrderInGrid().setValue(-1);
					dbProp.getOrderInSelect().setValue(-1);
				}
				if (dbProp.getName().getValue().equals("profit")) {
					dbProp.getOrderInDetail().setValue(53);
					dbProp.getOrderInFilter().setValue(53);
					dbProp.getOrderInGrid().setValue(53);
					dbProp.getOrderInSelect().setValue(-1);
				}
			} else if (
				(dbProp.getOrderInDetail().getValue()>1) &&
				(
					(dbProp.getName().getValue().equals("start")) || 
					(dbProp.getName().getValue().equals("end")) || 
					(dbProp.getName().getValue().equals("finalize")) ||
					(dbProp.getName().getValue().equals("finalized"))
				)
				) {
				if (dbProp.getName().getValue().equals("start")) {
					dbProp.getOrderInDetail().setValue(2);
					dbProp.getOrderInFilter().setValue(2);
					dbProp.getOrderInGrid().setValue(2);
					dbProp.getOrderInSelect().setValue(-1);
				}
				if (dbProp.getName().getValue().equals("end")) {
					dbProp.getOrderInDetail().setValue(3);
					dbProp.getOrderInFilter().setValue(3);
					dbProp.getOrderInGrid().setValue(-1);
					dbProp.getOrderInSelect().setValue(-1);
				}
				if (dbProp.getName().getValue().equals("finalize")) {
					dbProp.getOrderInDetail().setValue(4);
					dbProp.getOrderInFilter().setValue(4);
					dbProp.getOrderInGrid().setValue(3);
					dbProp.getOrderInSelect().setValue(-1);
				}
				if (dbProp.getName().getValue().equals("finalized")) {
					dbProp.getOrderInDetail().setValue(5);
					dbProp.getOrderInFilter().setValue(5);
					dbProp.getOrderInGrid().setValue(4);
					dbProp.getOrderInSelect().setValue(-1);
				}
			} else if (dbProp.getOrderInDetail().getValue()>1) {
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (
			(dbCol.getName().equals(CrmOrganizationInvoice.class.getName())) ||
			(dbCol.getName().equals(CrmOrganizationInvoiceDetail.class.getName())) ||
			(dbCol.getName().equals(CrmPersonInvoice.class.getName())) ||
			(dbCol.getName().equals(CrmPersonInvoiceDetail.class.getName()))
			) {
			if (
				(!dbProp.getName().getValue().startsWith("address")) ||
				(dbProp.getName().getValue().equals("address"))
				) {
				dbProp.getUserLevelEnabled().setValue(DbUser.USER_LEVEL_OFF);
			}
			if (
				(dbProp.getName().getValue().equals("turnOver")) ||
				(dbProp.getName().getValue().equals("turnOverExclVAT")) ||
				(dbProp.getName().getValue().equals("cost")) ||
				(dbProp.getName().getValue().equals("profit"))
				) {
				if (dbProp.getName().getValue().equals("turnOver")) {
					dbProp.getOrderInDetail().setValue(50);
					dbProp.getOrderInFilter().setValue(50);
					dbProp.getOrderInGrid().setValue(50);
					dbProp.getOrderInSelect().setValue(-1);
				}
				if (dbProp.getName().getValue().equals("turnOverExclVAT")) {
					dbProp.getOrderInDetail().setValue(51);
					dbProp.getOrderInFilter().setValue(51);
					dbProp.getOrderInGrid().setValue(51);
					dbProp.getOrderInSelect().setValue(-1);
				}
				if (dbProp.getName().getValue().equals("cost")) {
					dbProp.getOrderInDetail().setValue(52);
					dbProp.getOrderInFilter().setValue(52);
					dbProp.getOrderInGrid().setValue(52);
					dbProp.getOrderInSelect().setValue(-1);
				}
				if (dbProp.getName().getValue().equals("profit")) {
					dbProp.getOrderInDetail().setValue(53);
					dbProp.getOrderInFilter().setValue(53);
					dbProp.getOrderInGrid().setValue(53);
					dbProp.getOrderInSelect().setValue(-1);
				}
			} else if (dbProp.getOrderInDetail().getValue()>1) {
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
		}
		if (
			(dbCol.getName().equals(CrmOrganizationTaskLog.class.getName())) ||
			(dbCol.getName().equals(CrmPersonTaskLog.class.getName()))
			) {
			if (dbProp.getName().getValue().equals("user")) {
				dbProp.getOrderInDetail().setValue(-1);
				dbProp.getOrderInFilter().setValue(-1);
				dbProp.getOrderInGrid().setValue(-1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("dateTime")) {
				dbProp.getOrderInDetail().setValue(1);
				dbProp.getOrderInFilter().setValue(1);
				dbProp.getOrderInGrid().setValue(1);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("duration")) {
				dbProp.getOrderInDetail().setValue(2);
				dbProp.getOrderInFilter().setValue(2);
				dbProp.getOrderInGrid().setValue(2);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("customer")) {
				dbProp.getOrderInDetail().setValue(3);
				dbProp.getOrderInFilter().setValue(3);
				dbProp.getOrderInGrid().setValue(3);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("task")) {
				dbProp.getOrderInDetail().setValue(4);
				dbProp.getOrderInFilter().setValue(4);
				dbProp.getOrderInGrid().setValue(4);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("serviceProduct")) {
				dbProp.getOrderInDetail().setValue(5);
				dbProp.getOrderInFilter().setValue(5);
				dbProp.getOrderInGrid().setValue(5);
				dbProp.getOrderInSelect().setValue(-1);
			}
			if (dbProp.getName().getValue().equals("name")) {
				dbProp.getOrderInDetail().setValue(6);
				dbProp.getOrderInFilter().setValue(6);
				dbProp.getOrderInGrid().setValue(6);
				dbProp.getOrderInSelect().setValue(1);
			}
		}
	}

	@Override
	protected String getGuiPropertyClassNameForMdlCollectionProperty(MdlCollectionProperty prop) {
		String r = super.getGuiPropertyClassNameForMdlCollectionProperty(prop); 
		if (prop.getDataTypeClassName().equals(DtStringBuffer.class.getName())) {
			if (prop.getName().equals("serviceProductDescription")) {
				r = PrpTextAreaStringBuffer.class.getName();
			}
		}
		return r;
	}

	@Override
	protected List<DbCollectionFilter> getCollectionFiltersForCollectionProperty(DbCollection dbCol, DbCollectionProperty dbProp) {
		List<DbCollectionFilter> filters = super.getCollectionFiltersForCollectionProperty(dbCol, dbProp);
		if (
			((dbCol.getName().getValue().equals(CrmOrganizationTaskLog.class.getName())) && (dbProp.getName().getValue().equals("user"))) ||
			((dbCol.getName().getValue().equals(CrmPersonTaskLog.class.getName())) && (dbProp.getName().getValue().equals("user")))
			) {
			DbCollectionFilter filter = new DbCollectionFilter();
			filter.getCollection().setValue(dbCol);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue());
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(false);
			filter.getValue().setValue(DbModel.SESSION_USER_ID);
			filters.add(filter);
		}
		if (
			((dbCol.getName().getValue().equals(CrmInvoiceBatch.class.getName())) && (dbProp.getName().getValue().equals("finalized"))) ||
			((dbCol.getName().getValue().equals(CrmOrganizationInvoice.class.getName())) && (dbProp.getName().getValue().equals("finalized"))) ||
			((dbCol.getName().getValue().equals(CrmPersonInvoice.class.getName())) && (dbProp.getName().getValue().equals("finalized")))
			) {
			DbCollectionFilter filter = new DbCollectionFilter();
			filter.getCollection().setValue(dbCol);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue());
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_EQUALS));
			filter.getMandatory().setValue(false);
			filter.getSoftware().setValue(false);
			//filter.getValue().setValue("");
			filters.add(filter);
		}
		return filters;
	}
		
	@Override
	protected List<DbReferenceFilter> getReferenceFiltersForCollectionProperty(DbCollection dbCol, DbCollectionProperty dbProp, DbCollectionProperty dbRefProp) {
		List<DbReferenceFilter> filters = super.getReferenceFiltersForCollectionProperty(dbCol, dbProp, dbRefProp);
		if (
			(dbCol.getName().getValue().equals(CrmOrganization.class.getName())) && 
			(dbProp.getName().getValue().equals("invoiceAddress")) &&
			(dbRefProp.getName().getValue().equals("organization"))
			) {
			DbReferenceFilter filter = new DbReferenceFilter();
			filter.getReference().setValue(dbProp);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue() + " -> " + dbRefProp.getName().getValue());
			filter.getProperty().setValue(dbRefProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(true);
			filter.getValue().setValue(DbModel.CONTEXT_OBJECT_PREFIX + ".id");
			filters.add(filter);
		}
		if (
			(dbCol.getName().getValue().equals(CrmPerson.class.getName())) && 
			(dbProp.getName().getValue().equals("invoiceAddress")) &&
			(dbRefProp.getName().getValue().equals("person"))
			) {
			DbReferenceFilter filter = new DbReferenceFilter();
			filter.getReference().setValue(dbProp);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue() + " -> " + dbRefProp.getName().getValue());
			filter.getProperty().setValue(dbRefProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(true);
			filter.getValue().setValue(DbModel.CONTEXT_OBJECT_PREFIX + ".id");
			filters.add(filter);
		}
		if (
			(dbCol.getName().getValue().equals(CrmServiceProductValueAddedTax.class.getName())) && 
			(dbProp.getName().getValue().equals("valueAddedTax")) &&
			(dbRefProp.getName().getValue().equals("country"))
			) {
			DbReferenceFilter filter = new DbReferenceFilter();
			filter.getReference().setValue(dbProp);
			filter.getName().setValue(dbCol.getName().getValue() + "." + dbProp.getName().getValue() + " -> " + dbRefProp.getName().getValue());
			filter.getProperty().setValue(dbRefProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getMandatory().setValue(true);
			filter.getSoftware().setValue(true);
			filter.getValue().setValue(DbModel.CONTEXT_OBJECT_PREFIX + ".country");
			filters.add(filter);
		}
		return filters;
	}

	@Override
	protected void setFetchOrderByForFetchCollectionProperty(DbFetch dbFetch, DbCollection dbCol, DbCollectionProperty dbProp) {
		if (
			(dbCol.getName().getValue().equals(CrmOrganizationTaskLog.class.getName())) ||
			(dbCol.getName().getValue().equals(CrmPersonTaskLog.class.getName())) 
			) {
			if (dbProp.getName().getValue().equals("dateTime")) {
				dbFetch.getOrderBy().setValue(dbProp);
			}
		} else {
			super.setFetchOrderByForFetchCollectionProperty(dbFetch, dbCol, dbProp);
		}
	}

	@Override
	protected void setFetchDetails(DbFetch dbFetch, DbCollection dbCol) {
		if (
			(dbCol.getName().getValue().equals(CrmOrganizationTaskLog.class.getName())) ||
			(dbCol.getName().getValue().equals(CrmPersonTaskLog.class.getName())) 
			) {
			long id = collections.get(DbUser.class.getName()).getId().getValue();
			if (!dbFetch.getEntities().getValue().contains(id)) {
				dbFetch.getEntities().getValue().add(id);
			}
		}
	}

	@Override
	protected List<DbFetchFilter> getFetchFiltersForFetchCollectionProperty(DbFetch dbFetch, DbCollection dbCol, DbCollectionProperty dbProp) {
		List<DbFetchFilter> filters = super.getFetchFiltersForFetchCollectionProperty(dbFetch, dbCol, dbProp);
		if (
			((dbCol.getName().getValue().equals(CrmOrganizationTaskLog.class.getName())) && (dbProp.getName().getValue().equals("user"))) ||
			((dbCol.getName().getValue().equals(CrmPersonTaskLog.class.getName())) && (dbProp.getName().getValue().equals("user")))
			) {
			DbFetchFilter filter = new DbFetchFilter();
			filter.getFetch().setValue(dbFetch);
			filter.getName().setValue("User");
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_CONTAINS));
			filter.getValue().setValue(DbModel.SESSION_USER_ID);
			filters.add(filter);
		}
		if (
			((dbCol.getName().getValue().equals(CrmOrganizationTaskLog.class.getName())) && (dbProp.getName().getValue().equals("dateTime"))) ||
			((dbCol.getName().getValue().equals(CrmPersonTaskLog.class.getName())) && (dbProp.getName().getValue().equals("dateTime")))
			) {
			DbFetchFilter filter = new DbFetchFilter();
			filter.getFetch().setValue(dbFetch);
			filter.getName().setValue("From");
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_GREATER_OR_EQUALS));
			filter.getValue().setValue(DbModel.DATE_THIS_YEAR);
			filters.add(filter);

			filter = new DbFetchFilter();
			filter.getFetch().setValue(dbFetch);
			filter.getName().setValue("Until");
			filter.getProperty().setValue(dbProp);
			filter.getOperator().setValue(operators.get(QryFetchCondition.OPERATOR_LESS));
			filter.getValue().setValue(DbModel.DATE_NEXT_YEAR);
			filters.add(filter);
		}
		return filters;
	}
}
