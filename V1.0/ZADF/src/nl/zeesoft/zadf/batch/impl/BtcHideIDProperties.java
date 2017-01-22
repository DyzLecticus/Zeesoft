package nl.zeesoft.zadf.batch.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zadf.model.impl.DbCollectionProperty;
import nl.zeesoft.zodb.batch.BtcProgramObject;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.BtcLog;

public class BtcHideIDProperties extends BtcProgramObject {
	@Override
	public String execute(BtcLog log) {
		String err = "";

		for (String propertyName: getPropertyNames()) {
			QryTransaction t = new QryTransaction(log.getExecutingAsUser());
			QryFetch fetch = new QryFetch(DbCollectionProperty.class.getName());
			fetch.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,QryFetchCondition.OPERATOR_EQUALS,new DtString(propertyName)));
			DbIndex.getInstance().executeFetch(fetch, log.getExecutingAsUser(), this);
			for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
				DbCollectionProperty dbProp = (DbCollectionProperty) ref.getDataObject();
				dbProp.getOrderInDetail().setValue(getOrderForProperty(propertyName));
				dbProp.getOrderInFilter().setValue(getOrderForProperty(propertyName));
				if (updateGridAndSelectOrder()) {
					dbProp.getOrderInGrid().setValue(getOrderForProperty(propertyName));
					dbProp.getOrderInSelect().setValue(getOrderForProperty(propertyName));
				}
				t.addQuery(new QryUpdate(dbProp));
			}
			DbIndex.getInstance().executeTransaction(t, this);
			if (getTransactionErrors(t, log)) {
				err = "An error occured while updating the collection properties";
				break;
			}
		}

		return err;
	}
	
	public boolean updateGridAndSelectOrder() {
		return true;
	}
	
	public List<String> getPropertyNames() {
		List<String> props = new ArrayList<String>();
		props.add(MdlObject.PROPERTY_ID);
		return props;
	}
	
	public int getOrderForProperty(String propertyName) {
		return -1;
	}
}
