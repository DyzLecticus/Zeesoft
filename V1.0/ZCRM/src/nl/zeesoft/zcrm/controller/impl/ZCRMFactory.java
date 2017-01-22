package nl.zeesoft.zcrm.controller.impl;

import nl.zeesoft.zpo.controller.impl.ZPOFactory;

public class ZCRMFactory extends ZPOFactory {
	private final static String		APPLICATION_NAME					= "ZCRM";
	private final static String		APPLICATION_LABEL					= "Zeesoft Customer Relationship Manager";
	private final static String		APPLICATION_VERSION					= "1.1";
	private final static String		APPLICATION_DESCRIPTION				= 
		"The Zeesoft Customer Relationship Manager provides small business software support.\n" +
		"";
	
	public String getApplicationName() {
		return APPLICATION_NAME;
	}

	public String getApplicationLabel() {
		return APPLICATION_LABEL;
	}

	public String getApplicationVersion() {
		return APPLICATION_VERSION;
	}

	public String getApplicationDescription() {
		return APPLICATION_DESCRIPTION;
	}
}
