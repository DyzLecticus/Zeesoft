package nl.zeesoft.zadf.batch.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.model.MdlObject;

public class BtcShowCreatedAndChangedProperties extends BtcHideIDProperties {
	@Override
	public List<String> getPropertyNames() {
		List<String> props = new ArrayList<String>();
		props.add(MdlObject.PROPERTY_CREATEDBY);
		props.add(MdlObject.PROPERTY_CREATEDON);
		props.add(MdlObject.PROPERTY_CHANGEDBY);
		props.add(MdlObject.PROPERTY_CHANGEDON);
		return props;
	}
	
	@Override
	public int getOrderForProperty(String propertyName) {
		int order = 1000;
		if (propertyName.equals(MdlObject.PROPERTY_CREATEDBY)) {
			order = 1000;
		} else if (propertyName.equals(MdlObject.PROPERTY_CREATEDON)) {
			order = 1001;
		} else if (propertyName.equals(MdlObject.PROPERTY_CHANGEDBY)) {
			order = 1002;
		} else if (propertyName.equals(MdlObject.PROPERTY_CHANGEDON)) {
			order = 1003;
		}
		return order;
	}

	@Override
	public boolean updateGridAndSelectOrder() {
		return false;
	}
}
