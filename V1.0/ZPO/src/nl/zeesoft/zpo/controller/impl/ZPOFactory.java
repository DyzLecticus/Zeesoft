package nl.zeesoft.zpo.controller.impl;

import nl.zeesoft.zadf.controller.impl.ZADFFactory;

public class ZPOFactory extends ZADFFactory {
	private final static String		APPLICATION_NAME					= "ZPO";
	private final static String		APPLICATION_LABEL					= "Zeesoft Personal Organizer";
	private final static String		APPLICATION_VERSION					= "1.1";
	private final static String		APPLICATION_DESCRIPTION				= 
		"The Zeesoft Personal Organizer provides administration of tasks and notes.\n" +
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
