package nl.zeesoft.zcrm.batch.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zcrm.model.impl.CrmCountry;
import nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization;
import nl.zeesoft.zcrm.model.impl.CrmOrganization;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationAddress;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationDelivery;
import nl.zeesoft.zcrm.model.impl.CrmOrganizationTaskLog;
import nl.zeesoft.zcrm.model.impl.CrmPerson;
import nl.zeesoft.zcrm.model.impl.CrmPersonAddress;
import nl.zeesoft.zcrm.model.impl.CrmPersonDelivery;
import nl.zeesoft.zcrm.model.impl.CrmPersonTaskLog;
import nl.zeesoft.zcrm.model.impl.CrmServiceProduct;
import nl.zeesoft.zcrm.model.impl.CrmServiceProductPrice;
import nl.zeesoft.zcrm.model.impl.CrmServiceProductValueAddedTax;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.impl.BtcLog;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zpo.model.impl.UsrTask;

public class BtcGenerateCompany extends BtcGenerateObject {
	private static final int NUM_EMPLOYEES							= 40;

	private static final int NUM_PEOPLE 							= 2000;
	private static final int NUM_ORGANIZATIONS 						= 2000;
	
	private static final int NUM_SERVICE_DELIVERIES_PEOPLE 			= 80000;
	private static final int NUM_SERVICE_DELIVERIES_ORGANIZATIONS 	= 80000;
	
	@Override
	public String execute(BtcLog log) {
		String err = "";
		
		if (getCrmEntityObjectCount(log.getExecutingAsUser())==0) {
			QryTransaction t = null;
			
			List<DbUser> users = new ArrayList<DbUser>();
			t = new QryTransaction(log.getExecutingAsUser());
			for (int i = 0; i< NUM_EMPLOYEES; i++) {
				DbUser user = new DbUser();
				user.getName().setValue("demoUser" + Generic.minStrInt(i,4));
				user.getPassword().setValue(DbConfig.getInstance().encodePassword(new StringBuffer("1demoUser!")));
				user.getLevel().setValue(ZCRMModel.USER_LEVEL_FETCH_PEOPLE);
				users.add(user);
				t.addQuery(new QryAdd(user));
			}
			DbIndex.getInstance().executeTransaction(t, this);
			
			List<UsrTask> tasks = new ArrayList<UsrTask>();
			t = new QryTransaction(log.getExecutingAsUser());
			for (DbUser user: users) {
				UsrTask task = new UsrTask();
				task.getName().setValue("Work for customer");
				task.getUser().setValue(user);
				task.getDescription().setValue(
					"Work for customer.\n"
					);
				tasks.add(task);
				t.addQuery(new QryAdd(task));
			}
			DbIndex.getInstance().executeTransaction(t, this);
			
			CrmCountry nl = generateCountry(log.getExecutingAsUser(),"Netherlands","NL",new BigDecimal("21.00"),new BigDecimal("6.00"));
			
			log.addLogLine("Added country " + nl.getName().getValue());
	
			Calendar cal = Calendar.getInstance();

			// Delivery organization
			t = new QryTransaction(log.getExecutingAsUser());
			CrmDeliveryOrganization organization = new CrmDeliveryOrganization();
			organization.getName().setValue("Company");
			organization.getDescription().setValue(
				"This is a generated delivery organization.\n"
				);
			organization.getAccountNumber().setValue("NL.BANK.123.456.789");
			organization.getChamberOfCommerceCode().setValue("NLKVK123");
			organization.getTelephone().setValue("0123456789");
			organization.getEmail().setValue("info@company.nl");
			organization.getWebsite().setValue("www.company.nl");
			t.addQuery(new QryAdd(organization));
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " delivery organization");

			// Services and products
			t = new QryTransaction(log.getExecutingAsUser());
			CrmServiceProduct service = new CrmServiceProduct();
			service.getOrganization().setValue(organization);
			service.getName().setValue("Service");
			service.getDescription().setValue("One hour of service.\n");
			t.addQuery(new QryAdd(service));
	
			CrmServiceProduct product = new CrmServiceProduct();
			product.getOrganization().setValue(organization);
			product.getName().setValue("Product");
			product.getDescription().setValue("One amazing product.\n");
			product.getService().setValue(false);
			t.addQuery(new QryAdd(product));
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " services and products");

			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,2000);
			cal.set(Calendar.MONTH,0);
			cal.set(Calendar.DATE,1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			t = new QryTransaction(log.getExecutingAsUser());
			CrmServiceProductPrice servicePrice = new CrmServiceProductPrice();
			servicePrice.getStart().setValue(cal.getTime());
			servicePrice.getServiceProduct().setValue(service);
			servicePrice.getPricePerUnit().setValue(new BigDecimal("80.00"));
			servicePrice.getCostPerUnit().setValue(new BigDecimal("50.00"));
			t.addQuery(new QryAdd(servicePrice));

			CrmServiceProductPrice productPrice = new CrmServiceProductPrice();
			productPrice.getServiceProduct().setValue(product);
			productPrice.getStart().setValue(cal.getTime());
			productPrice.getPricePerUnit().setValue(new BigDecimal("20.00"));
			productPrice.getCostPerUnit().setValue(new BigDecimal("5.00"));
			t.addQuery(new QryAdd(productPrice));
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " service and product prices");
			
			t = new QryTransaction(log.getExecutingAsUser());
			CrmServiceProductValueAddedTax serviceVAT = new CrmServiceProductValueAddedTax();
			serviceVAT.getStart().setValue(cal.getTime());
			serviceVAT.getServiceProduct().setValue(service);
			serviceVAT.getCountry().setValue(nl);
			serviceVAT.getValueAddedTax().setValue(valueAddedTaxes.get("Default"));
			t.addQuery(new QryAdd(serviceVAT));

			CrmServiceProductValueAddedTax productVAT = new CrmServiceProductValueAddedTax();
			productVAT.getServiceProduct().setValue(product);
			productVAT.getStart().setValue(cal.getTime());
			productVAT.getCountry().setValue(nl);
			productVAT.getValueAddedTax().setValue(valueAddedTaxes.get("Default"));
			t.addQuery(new QryAdd(productVAT));
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " service and product value added taxes");

			// Employees
			List<CrmPerson> employees = new ArrayList<CrmPerson>();
			t = new QryTransaction(log.getExecutingAsUser());
			for (int i = 0; i < NUM_EMPLOYEES; i++) {
				cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, (1960 + (i % 30)));
				cal.set(Calendar.MONTH, (Calendar.JANUARY + (i % 10)));
				cal.set(Calendar.DATE, 1 + (i % 30));
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
		
				Date bd = cal.getTime();
				
				CrmPerson person = new CrmPerson();
				person.getDescription().setValue(
					"This is a generated employee.\n"
					);
				person.getFirstName().setValue("employee" + Generic.minStrInt(i,4));
				person.getLastName().setValue("worker" + Generic.minStrInt(i,4));
				person.getBirthDate().setValue(bd);
				person.getTelephone().setValue("012345678" + (i % 10));
				person.getEmail().setValue("email@provider.nl");
				t.addQuery(new QryAdd(person));
				employees.add(person);
			}
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " employees");
			
			List<Long> employeeIdList = t.getAddedIdList();

			// Update delivery organization
			t = new QryTransaction(log.getExecutingAsUser());
			organization.getEmployees().setValue(employeeIdList);
			t.addQuery(new QryUpdate(organization));
			DbIndex.getInstance().executeTransaction(t, this);
			
			// People
			List<CrmPerson> people = new ArrayList<CrmPerson>();
			t = new QryTransaction(log.getExecutingAsUser());
			for (int i = 0; i < NUM_PEOPLE; i++) {
				cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, (1960 + (i % 30)));
				cal.set(Calendar.MONTH, (Calendar.JANUARY + (i % 10)));
				cal.set(Calendar.DATE, 1 + (i % 30));
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
		
				Date bd = cal.getTime();
				
				CrmPerson person = new CrmPerson();
				person.getDescription().setValue(
					"This is a generated person.\n"
					);
				person.getFirstName().setValue("firstName" + Generic.minStrInt(i,4));
				person.getMiddleName().setValue("middleName" + Generic.minStrInt(i,4));
				person.getPreposition().setValue("prep" + Generic.minStrInt(i,4));
				person.getLastName().setValue("lastName" + Generic.minStrInt(i,4));
				person.getBirthDate().setValue(bd);
				person.getTelephone().setValue("012345678" + (i % 10));
				person.getEmail().setValue("email@provider.nl");
				t.addQuery(new QryAdd(person));
				people.add(person);
			}
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " people");
			
			// Organizations
			List<CrmOrganization> organizations = new ArrayList<CrmOrganization>();
			t = new QryTransaction(log.getExecutingAsUser());
			for (int i = 0; i < NUM_ORGANIZATIONS; i++) {
				CrmPerson employee = null;
				if (i < NUM_PEOPLE) {
					employee = people.get(i);
				}
				CrmOrganization org = new CrmOrganization();
				org.getName().setValue("Organization" + Generic.minStrInt(i,4));
				org.getDescription().setValue(
					"This is a generated organization.\n"
					);
				org.getAccountNumber().setValue("NL.BANK.123.456.78" + (i % 10));
				org.getChamberOfCommerceCode().setValue("NLKVK12" + (i % 10));
				org.getTelephone().setValue("012345678" + (i % 10));
				org.getEmail().setValue("info@organization" + Generic.minStrInt(i,4) + ".nl");
				org.getWebsite().setValue("www.organization" + Generic.minStrInt(i,4) + ".nl");
				if (employee!=null) {
					org.getEmployees().getValue().add(employee.getId().getValue());
				}
				t.addQuery(new QryAdd(org));
				organizations.add(org);
			}
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " organizations");

			// Adresses 
			t = new QryTransaction(log.getExecutingAsUser());
			for (CrmPerson person: employees) {
				CrmPersonAddress personAddress = new CrmPersonAddress();
				personAddress.getPerson().setValue(person);
				personAddress.getName().setValue("Default address");
				personAddress.getLine1().setValue("Street");
				personAddress.getLine2().setValue("House number");
				personAddress.getPostalCode().setValue("PostalCode");
				personAddress.getCity().setValue("City");
				personAddress.getCountry().setValue(nl);
				t.addQuery(new QryAdd(personAddress));
			}
			for (CrmPerson person: people) {
				CrmPersonAddress personAddress = new CrmPersonAddress();
				personAddress.getPerson().setValue(person);
				personAddress.getName().setValue("Default address");
				personAddress.getLine1().setValue("Street");
				personAddress.getLine2().setValue("House number");
				personAddress.getPostalCode().setValue("PostalCode");
				personAddress.getCity().setValue("City");
				personAddress.getCountry().setValue(nl);
				t.addQuery(new QryAdd(personAddress));
			}
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " addresses");

			t = new QryTransaction(log.getExecutingAsUser());
			for (CrmOrganization org: organizations) {
				CrmOrganizationAddress organizationAddress = new CrmOrganizationAddress();
				organizationAddress.getOrganization().setValue(org);
				organizationAddress.getName().setValue("Default address");
				organizationAddress.getLine1().setValue("Street");
				organizationAddress.getLine2().setValue("House number");
				organizationAddress.getPostalCode().setValue("PostalCode");
				organizationAddress.getCity().setValue("City");
				organizationAddress.getCountry().setValue(nl);
				t.addQuery(new QryAdd(organizationAddress));
			}
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " addresses");

			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2009);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			t = new QryTransaction(log.getExecutingAsUser());
			for (CrmPerson pers: people) {
				cal.add(Calendar.DATE, 1);
				Date dd = cal.getTime();
				CrmPersonDelivery productDelivery = new CrmPersonDelivery();
				productDelivery.getCustomer().setValue(pers);
				productDelivery.getServiceProduct().setValue(product);
				productDelivery.getDeliveryDateTime().setValue(dd);
				productDelivery.getUnits().setValue(1);
				t.addQuery(new QryAdd(productDelivery));
			}
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " product deliveries");

			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2009);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			t = new QryTransaction(log.getExecutingAsUser());
			for (CrmOrganization org: organizations) {
				cal.add(Calendar.DATE, 1);
				Date dd = cal.getTime();
				CrmOrganizationDelivery productDelivery = new CrmOrganizationDelivery();
				productDelivery.getCustomer().setValue(org);
				productDelivery.getServiceProduct().setValue(product);
				productDelivery.getDeliveryDateTime().setValue(dd);
				productDelivery.getUnits().setValue(1);
				t.addQuery(new QryAdd(productDelivery));
			}
			DbIndex.getInstance().executeTransaction(t, this);
			log.addLogLine("Added " + t.getAddedIdList().size() + " product deliveries");

			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2000);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date start = cal.getTime();
			
			int i = 0;
			for (DbUser user: users) {
				UsrTask task = tasks.get(i);
				CrmOrganization org = organizations.get(i);

				cal = Calendar.getInstance();
				cal.setTime(start);
				
				t = new QryTransaction(log.getExecutingAsUser());
				for (int ii = 0; ii < (NUM_SERVICE_DELIVERIES_ORGANIZATIONS / (NUM_EMPLOYEES / 2)); ii++) {
					cal.add(Calendar.DATE, 1);
					if ((cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY) &&
						(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY)
						) {
						CrmOrganizationTaskLog taskLog = new CrmOrganizationTaskLog();
						taskLog.getTask().setValue(task);
						taskLog.getUser().setValue(user);
						taskLog.getCustomer().setValue(org);
						taskLog.getServiceProduct().setValue(service);
						taskLog.getDateTime().setValue(cal.getTime());
						taskLog.getDuration().setValue(new BigDecimal("8.00"));
						t.addQuery(new QryAdd(taskLog));
					}
				}
				DbIndex.getInstance().executeTransaction(t, this);
				log.addLogLine("Added " + t.getAddedIdList().size() + " task logs for customer: " + org.getName().getValue());
				i++;
				if (i>=20) {
					break;
				}
			}

			i = 0;
			for (DbUser user: users) {
				if (i>=20) {
					UsrTask task = tasks.get(i);
					CrmPerson pers = people.get((i - 20));
	
					cal = Calendar.getInstance();
					cal.setTime(start);
					
					t = new QryTransaction(log.getExecutingAsUser());
					for (int ii = 0; ii < (NUM_SERVICE_DELIVERIES_PEOPLE / (NUM_EMPLOYEES / 2)); ii++) {
						cal.add(Calendar.DATE, 1);
						if ((cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY) &&
							(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY)
							) {
							CrmPersonTaskLog taskLog = new CrmPersonTaskLog();
							taskLog.getTask().setValue(task);
							taskLog.getUser().setValue(user);
							taskLog.getCustomer().setValue(pers);
							taskLog.getServiceProduct().setValue(service);
							taskLog.getDateTime().setValue(cal.getTime());
							taskLog.getDuration().setValue(new BigDecimal("8.00"));
							t.addQuery(new QryAdd(taskLog));
						}
					}
					DbIndex.getInstance().executeTransaction(t, this);
					log.addLogLine("Added " + t.getAddedIdList().size() + " task logs for customer: " + pers.getName().getValue());
				}
				i++;
			}

		}
		return err;
	}
	
}
