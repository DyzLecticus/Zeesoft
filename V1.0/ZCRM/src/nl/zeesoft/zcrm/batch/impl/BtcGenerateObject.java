package nl.zeesoft.zcrm.batch.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zcrm.model.impl.CrmCountry;
import nl.zeesoft.zcrm.model.impl.CrmDeliveryOrganization;
import nl.zeesoft.zcrm.model.impl.CrmOrganization;
import nl.zeesoft.zcrm.model.impl.CrmPerson;
import nl.zeesoft.zcrm.model.impl.CrmValueAddedTax;
import nl.zeesoft.zcrm.model.impl.CrmValueAddedTaxPercentage;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.batch.BtcProgramObject;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryObject;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.model.impl.DbUser;

public abstract class BtcGenerateObject extends BtcProgramObject {
	protected SortedMap<String,CrmValueAddedTax> valueAddedTaxes = new TreeMap<String,CrmValueAddedTax>();
	
	protected int getCrmEntityObjectCount(DbUser user) {
		QryFetch fetchCountries = new QryFetch(CrmCountry.class.getName());
		QryFetch fetchOrganizations = new QryFetch(CrmOrganization.class.getName());
		QryFetch fetchPeople = new QryFetch(CrmPerson.class.getName());
		QryFetch fetchDeliveryOrganizations = new QryFetch(CrmDeliveryOrganization.class.getName());
		QryFetchList fl = new QryFetchList(user);
		fl.addQuery(fetchCountries);
		fl.addQuery(fetchOrganizations);
		fl.addQuery(fetchPeople);
		fl.addQuery(fetchDeliveryOrganizations);
		for (QryObject qry: fl.getQueries()) {
			QryFetch f = (QryFetch) qry;
			f.setType(QryFetch.TYPE_COUNT_REFERENCES);
		}
		DbIndex.getInstance().executeFetchList(fl,this);
		int count = 0;
		for (QryObject qry: fl.getQueries()) {
			QryFetch f = (QryFetch) qry;
			count = count + f.getCount();
		}
		return count;
	}
	
	protected CrmCountry generateCountry(DbUser user, String name, String code, BigDecimal defaultPercentage, BigDecimal lowPercentage) {
		QryTransaction t = null;

		// Countries
		t = new QryTransaction(user);
		CrmCountry country = new CrmCountry();
		country.getName().setValue(name);
		country.getCode().setValue(code);
		t.addQuery(new QryAdd(country));
		DbIndex.getInstance().executeTransaction(t, this);
		
		// Value added taxes
		t = new QryTransaction(user);
		CrmValueAddedTax vatDefault = new CrmValueAddedTax();
		vatDefault.getName().setValue("Default");
		vatDefault.getCountry().setValue(country);
		t.addQuery(new QryAdd(vatDefault));
		valueAddedTaxes.put(vatDefault.getName().getValue(), vatDefault);
		CrmValueAddedTax vatLow = null;
		if (lowPercentage.compareTo(Generic.ZERO_VALUE_BIG_DECIMAL) > 0) {
			vatLow = new CrmValueAddedTax();
			vatLow.getName().setValue("Low");
			vatLow.getCountry().setValue(country);
			t.addQuery(new QryAdd(vatLow));
			valueAddedTaxes.put(vatLow.getName().getValue(), vatLow);
		}
		CrmValueAddedTax vatZero = new CrmValueAddedTax();
		vatZero.getName().setValue("Zero");
		vatZero.getCountry().setValue(country);
		t.addQuery(new QryAdd(vatZero));
		valueAddedTaxes.put(vatZero.getName().getValue(), vatZero);
		DbIndex.getInstance().executeTransaction(t, this);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,2000);
		cal.set(Calendar.MONTH,0);
		cal.set(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		
		t = new QryTransaction(user);
		CrmValueAddedTaxPercentage vatDefaultPercentage = new CrmValueAddedTaxPercentage();
		vatDefaultPercentage.getValueAddedTax().setValue(vatDefault);
		vatDefaultPercentage.getStart().setValue(cal.getTime());
		vatDefaultPercentage.getPercentage().setValue(defaultPercentage);
		t.addQuery(new QryAdd(vatDefaultPercentage));
		if (vatLow!=null) {
			CrmValueAddedTaxPercentage vatLowPercentage = new CrmValueAddedTaxPercentage();
			vatLowPercentage.getValueAddedTax().setValue(vatLow);
			vatLowPercentage.getStart().setValue(cal.getTime());
			vatLowPercentage.getPercentage().setValue(lowPercentage);
			t.addQuery(new QryAdd(vatLowPercentage));
		}
		CrmValueAddedTaxPercentage vatZeroPercentage = new CrmValueAddedTaxPercentage();
		vatZeroPercentage.getValueAddedTax().setValue(vatZero);
		vatZeroPercentage.getStart().setValue(cal.getTime());
		vatZeroPercentage.getPercentage().setValue(new BigDecimal("0.00"));
		t.addQuery(new QryAdd(vatZeroPercentage));
		DbIndex.getInstance().executeTransaction(t, this);
		
		return country;
	}
}
