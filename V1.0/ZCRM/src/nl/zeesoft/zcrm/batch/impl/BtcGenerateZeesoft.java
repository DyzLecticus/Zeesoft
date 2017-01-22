package nl.zeesoft.zcrm.batch.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zcrm.model.impl.CrmCountry;
import nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization;
import nl.zeesoft.zcrm.model.impl.CrmOrganization;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationContract;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationContractDetail;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationTaskLog;
import nl.zeesoft.zcrm.model.impl.CrmPerson;
import nl.zeesoft.zcrm.model.impl.CrmPersonAddress;
import nl.zeesoft.zcrm.model.impl.CrmPersonDelivery;
import nl.zeesoft.zcrm.model.impl.CrmServiceProduct;
import nl.zeesoft.zcrm.model.impl.CrmServiceProductPrice;
import nl.zeesoft.zcrm.model.impl.CrmServiceProductValueAddedTax;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zpo.model.impl.UsrTask;

public class BtcGenerateZeesoft extends BtcGenerateObject {
	@Override
	public String execute(BtcLog log) {
		String err = "";
		
		if (getCrmEntityObjectCount(log.getExecutingAsUser())==0) {
			QryTransaction t = null;

			// Task
			t = new QryTransaction(log.getExecutingAsUser());
			UsrTask task = new UsrTask();
			task.getName().setValue("Develop Zeesoft product line");
			task.getUser().setValue(DbConfig.getInstance().getModel().getAdminUser(this));
			task.getDescription().setValue(
				"Create a Pure Java database.\n" +
				"Use the database to create an application development framework.\n" +
				"Use the application development framework to create Zeesoft business applications.\n" +
				"Publish te Zeesoft product line on GitHub.\n"
				);
			t.addQuery(new QryAdd(task));
			DbIndex.getInstance().executeTransaction(t, this);
			
			CrmCountry nl = generateCountry(log.getExecutingAsUser(),"Netherlands","NL",new BigDecimal("21.00"),new BigDecimal("6.00"));
			
			log.addLogLine("Added country " + nl.getName().getValue());
			
			// People
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 1979);
			cal.set(Calendar.MONTH, Calendar.OCTOBER);
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
	
			Date bd = cal.getTime();
			
			t = new QryTransaction(log.getExecutingAsUser());
			CrmPerson andre = new CrmPerson();
			andre.getDescription().setValue(
				"André van der Zee is the person that designed and wrote this software.\n"
				);
			andre.getFirstName().setValue("André");
			andre.getMiddleName().setValue("Michel");
			andre.getPreposition().setValue("van der");
			andre.getLastName().setValue("Zee");
			andre.getBirthDate().setValue(bd);
			andre.getTelephone().setValue("0031652327758");
			andre.getEmail().setValue("dyz@xs4all.nl");
			t.addQuery(new QryAdd(andre));
			DbIndex.getInstance().executeTransaction(t, this);

			log.addLogLine("Added person: " + andre.getName().getValue());
	
			t = new QryTransaction(log.getExecutingAsUser());
			CrmPersonAddress andreAddress = new CrmPersonAddress();
			andreAddress.getPerson().setValue(andre);
			andreAddress.getName().setValue("Woonadres");
			andreAddress.getLine1().setValue("Paramaribostraat");
			andreAddress.getLine2().setValue("9");
			andreAddress.getPostalCode().setValue("2315VD");
			andreAddress.getCity().setValue("Leiden");
			andreAddress.getCountry().setValue(nl);
			t.addQuery(new QryAdd(andreAddress));
			DbIndex.getInstance().executeTransaction(t, this);

			log.addLogLine("Added address for person: " + andre.getName().getValue());
			
			// Organizations
			t = new QryTransaction(log.getExecutingAsUser());
			CrmDeliveryOrganization zeesoft = new CrmDeliveryOrganization();
			zeesoft.getName().setValue("Zeesoft");
			zeesoft.getDescription().setValue(
				"Zeesoft is the company that created this software.\n"+
				"Zeesoft was founded by André van der Zee.\n"
				);
			zeesoft.getAccountNumber().setValue("NL.BANK.123.456.789");
			zeesoft.getChamberOfCommerceCode().setValue("NLKVK123");
			zeesoft.getTelephone().setValue("0031652327758");
			zeesoft.getEmail().setValue("dyz@xs4all.nl");
			zeesoft.getWebsite().setValue("dyz.home.xs4all.nl");
			zeesoft.getEmployees().getValue().add(andre.getId().getValue());
			t.addQuery(new QryAdd(zeesoft));
			DbIndex.getInstance().executeTransaction(t, this);

			log.addLogLine("Added delivery organization: " + zeesoft.getName().getValue());
			
			t = new QryTransaction(log.getExecutingAsUser());
			CrmOrganization zeesoftCommunity = new CrmOrganization();
			zeesoftCommunity.getName().setValue("Zeesoft Community");
			zeesoftCommunity.getDescription().setValue("Demo Zeesoft Customer");
			t.addQuery(new QryAdd(zeesoftCommunity));
			DbIndex.getInstance().executeTransaction(t, this);

			log.addLogLine("Added organization: " + zeesoftCommunity.getName().getValue());
	
			// Services and products
			t = new QryTransaction(log.getExecutingAsUser());
			CrmServiceProduct servDev = new CrmServiceProduct();
			servDev.getOrganization().setValue(zeesoft);
			servDev.getName().setValue("Software development");
			servDev.getDescription().setValue("One hour of software development.\n");
			t.addQuery(new QryAdd(servDev));
	
			CrmServiceProduct prodZODB = new CrmServiceProduct();
			prodZODB.getOrganization().setValue(zeesoft);
			prodZODB.getName().setValue("Zeesoft Object Database");
			prodZODB.getDescription().setValue(ZCRMModel.MODULE_ZODB_DESC);
			prodZODB.getService().setValue(false);
			t.addQuery(new QryAdd(prodZODB));
	
			CrmServiceProduct prodZADF = new CrmServiceProduct();
			prodZADF.getOrganization().setValue(zeesoft);
			prodZADF.getName().setValue("Zeesoft Application Development Framework");
			prodZADF.getDescription().setValue(ZCRMModel.MODULE_ZADF_DESC);
			prodZADF.getService().setValue(false);
			t.addQuery(new QryAdd(prodZADF));

			CrmServiceProduct prodZPO = new CrmServiceProduct();
			prodZPO.getOrganization().setValue(zeesoft);
			prodZPO.getName().setValue("Zeesoft Personal Organizer");
			prodZPO.getDescription().setValue(ZCRMModel.MODULE_ZPO_DESC);
			prodZPO.getService().setValue(false);
			t.addQuery(new QryAdd(prodZPO));
			
			CrmServiceProduct prodZCRM = new CrmServiceProduct();
			prodZCRM.getOrganization().setValue(zeesoft);
			prodZCRM.getName().setValue("Zeesoft Customer Relationship Manager");
			prodZCRM.getDescription().setValue(ZCRMModel.MODULE_ZCRM_DESC);
			prodZCRM.getService().setValue(false);
			t.addQuery(new QryAdd(prodZCRM));
			DbIndex.getInstance().executeTransaction(t, this);

			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,2014);
			cal.set(Calendar.MONTH,0);
			cal.set(Calendar.DATE,1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			t = new QryTransaction(log.getExecutingAsUser());
			CrmServiceProductPrice servicePrice = new CrmServiceProductPrice();
			servicePrice.getStart().setValue(cal.getTime());
			servicePrice.getServiceProduct().setValue(servDev);
			servicePrice.getPricePerUnit().setValue(new BigDecimal("80.00"));
			servicePrice.getCostPerUnit().setValue(new BigDecimal("50.00"));
			t.addQuery(new QryAdd(servicePrice));

			CrmServiceProductPrice productPrice = new CrmServiceProductPrice();
			productPrice.getServiceProduct().setValue(prodZODB);
			productPrice.getStart().setValue(cal.getTime());
			productPrice.getPricePerUnit().setValue(new BigDecimal("0.00"));
			productPrice.getCostPerUnit().setValue(new BigDecimal("0.00"));
			t.addQuery(new QryAdd(productPrice));

			productPrice = new CrmServiceProductPrice();
			productPrice.getServiceProduct().setValue(prodZADF);
			productPrice.getStart().setValue(cal.getTime());
			productPrice.getPricePerUnit().setValue(new BigDecimal("0.00"));
			productPrice.getCostPerUnit().setValue(new BigDecimal("0.00"));
			t.addQuery(new QryAdd(productPrice));
			
			productPrice = new CrmServiceProductPrice();
			productPrice.getServiceProduct().setValue(prodZPO);
			productPrice.getStart().setValue(cal.getTime());
			productPrice.getPricePerUnit().setValue(new BigDecimal("0.00"));
			productPrice.getCostPerUnit().setValue(new BigDecimal("0.00"));
			t.addQuery(new QryAdd(productPrice));

			productPrice = new CrmServiceProductPrice();
			productPrice.getServiceProduct().setValue(prodZCRM);
			productPrice.getStart().setValue(cal.getTime());
			productPrice.getPricePerUnit().setValue(new BigDecimal("0.00"));
			productPrice.getCostPerUnit().setValue(new BigDecimal("0.00"));
			t.addQuery(new QryAdd(productPrice));
			DbIndex.getInstance().executeTransaction(t, this);
			
			t = new QryTransaction(log.getExecutingAsUser());
			CrmServiceProductValueAddedTax serviceVAT = new CrmServiceProductValueAddedTax();
			serviceVAT.getStart().setValue(cal.getTime());
			serviceVAT.getServiceProduct().setValue(servDev);
			serviceVAT.getCountry().setValue(nl);
			serviceVAT.getValueAddedTax().setValue(valueAddedTaxes.get("Default"));
			t.addQuery(new QryAdd(serviceVAT));

			CrmServiceProductValueAddedTax productVAT = new CrmServiceProductValueAddedTax();
			productVAT.getServiceProduct().setValue(prodZODB);
			productVAT.getStart().setValue(cal.getTime());
			productVAT.getCountry().setValue(nl);
			productVAT.getValueAddedTax().setValue(valueAddedTaxes.get("Default"));
			t.addQuery(new QryAdd(productVAT));

			productVAT = new CrmServiceProductValueAddedTax();
			productVAT.getServiceProduct().setValue(prodZADF);
			productVAT.getStart().setValue(cal.getTime());
			productVAT.getCountry().setValue(nl);
			productVAT.getValueAddedTax().setValue(valueAddedTaxes.get("Default"));
			t.addQuery(new QryAdd(productVAT));
			
			productVAT = new CrmServiceProductValueAddedTax();
			productVAT.getServiceProduct().setValue(prodZPO);
			productVAT.getStart().setValue(cal.getTime());
			productVAT.getCountry().setValue(nl);
			productVAT.getValueAddedTax().setValue(valueAddedTaxes.get("Default"));
			t.addQuery(new QryAdd(productVAT));

			productVAT = new CrmServiceProductValueAddedTax();
			productVAT.getServiceProduct().setValue(prodZCRM);
			productVAT.getStart().setValue(cal.getTime());
			productVAT.getCountry().setValue(nl);
			productVAT.getValueAddedTax().setValue(valueAddedTaxes.get("Default"));
			t.addQuery(new QryAdd(productVAT));
			DbIndex.getInstance().executeTransaction(t, this);

			log.addLogLine("Added services and products for delivery organization: " + zeesoft.getName().getValue());

			// Contracts
			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2006);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
	
			Date start = cal.getTime();
			
			t = new QryTransaction(log.getExecutingAsUser());
			CrmOrganizationContract contract = new CrmOrganizationContract();
			contract.getName().setValue("Zeesoft product line development");
			contract.getDescription().setValue("Discount on one hour of software development.\n");
			contract.getOrganization().setValue(zeesoft);
			contract.getCustomer().setValue(zeesoftCommunity);
			contract.getStart().setValue(start);
			t.addQuery(new QryAdd(contract));
			DbIndex.getInstance().executeTransaction(t, this);
	
			t = new QryTransaction(log.getExecutingAsUser());
			CrmOrganizationContractDetail contractDetail = new CrmOrganizationContractDetail();
			contractDetail.getContract().setValue(contract);
			contractDetail.getServiceProduct().setValue(servDev);
			contractDetail.getName().setValue("Zeesoft product line development");
			contractDetail.getPricePerUnit().setValue(new BigDecimal("0.00"));
			t.addQuery(new QryAdd(contractDetail));
			DbIndex.getInstance().executeTransaction(t, this);

			log.addLogLine("Added contract for delivery organization: " + zeesoft.getName().getValue() + ", customer: " + zeesoftCommunity.getName().getValue());
	
			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2015);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			Date dd = cal.getTime();
						
			t = new QryTransaction(log.getExecutingAsUser());
			CrmPersonDelivery ZODBProduct = new CrmPersonDelivery();
			ZODBProduct.getCustomer().setValue(andre);
			ZODBProduct.getServiceProduct().setValue(prodZODB);
			ZODBProduct.getName().setValue("ZODB delivery");
			ZODBProduct.getDeliveryDateTime().setValue(dd);
			ZODBProduct.getUnits().setValue(1);
			t.addQuery(new QryAdd(ZODBProduct));
	
			CrmPersonDelivery ZADFProduct = new CrmPersonDelivery();
			ZADFProduct.getCustomer().setValue(andre);
			ZADFProduct.getServiceProduct().setValue(prodZADF);
			ZADFProduct.getName().setValue("ZADF delivery");
			ZADFProduct.getDeliveryDateTime().setValue(dd);
			ZADFProduct.getUnits().setValue(1);
			t.addQuery(new QryAdd(ZADFProduct));

			CrmPersonDelivery ZPOProduct = new CrmPersonDelivery();
			ZPOProduct.getCustomer().setValue(andre);
			ZPOProduct.getServiceProduct().setValue(prodZPO);
			ZPOProduct.getName().setValue("ZPO delivery");
			ZPOProduct.getDeliveryDateTime().setValue(dd);
			ZPOProduct.getUnits().setValue(1);
			t.addQuery(new QryAdd(ZPOProduct));
			
			CrmPersonDelivery ZCRMProduct = new CrmPersonDelivery();
			ZCRMProduct.getCustomer().setValue(andre);
			ZCRMProduct.getServiceProduct().setValue(prodZCRM);
			ZCRMProduct.getName().setValue("ZCRM delivery");
			ZCRMProduct.getDeliveryDateTime().setValue(dd);
			ZCRMProduct.getUnits().setValue(1);
			t.addQuery(new QryAdd(ZCRMProduct));
			DbIndex.getInstance().executeTransaction(t, this);
			
			log.addLogLine("Added deliveries for delivery organization: " + zeesoft.getName().getValue());
			
			// Task logs
			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2014);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 20);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			t = new QryTransaction(log.getExecutingAsUser());
			for (int i = 0; i < 365; i++) {
				CrmOrganizationTaskLog taskLog = new CrmOrganizationTaskLog();
				taskLog.getTask().setValue(task);
				taskLog.getUser().setValue(task.getUser().getValue());
				taskLog.getCustomer().setValue(zeesoftCommunity);
				taskLog.getServiceProduct().setValue(servDev);
				taskLog.getDateTime().setValue(cal.getTime());
				taskLog.getDuration().setValue(new BigDecimal("4.00"));
				t.addQuery(new QryAdd(taskLog));
				cal.add(Calendar.DATE, 1);
			}
			DbIndex.getInstance().executeTransaction(t, this);
			
			log.addLogLine("Added task logs for customer: " + zeesoftCommunity.getName().getValue());
		}
		return err;
	}
	
}
