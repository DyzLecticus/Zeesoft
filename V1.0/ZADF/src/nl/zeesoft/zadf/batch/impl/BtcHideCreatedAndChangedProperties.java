package nl.zeesoft.zadf.batch.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.model.MdlObject;

public class BtcHideCreatedAndChangedProperties extends BtcHideIDProperties {
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
		return -1;
	}

	@Override
	public boolean updateGridAndSelectOrder() {
		return false;
	}
}
