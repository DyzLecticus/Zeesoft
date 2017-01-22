package nl.zeesoft.zcrm;

import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zcrm.controller.impl.ZCRMFactory;
import nl.zeesoft.zcrm.model.ZCRMModel;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zpo.ZPOController;

public class ZCRMController extends ZPOController {
	private static final String JAR_FILE_NAME = "zcrm.jar"; 
	
	public ZCRMController(String[] args) {
		super(args);
	}

	@Override
	protected void initializeDBConfig() {
		super.initializeDBConfig();
		DbConfig.getInstance().setPort(7654);
		DbConfig.getInstance().setModelClassName(ZCRMModel.class.getName());
	}	

	@Override
	protected void initializeGuiConfig() {
		super.initializeGuiConfig();
		GuiConfig.getInstance().setFactoryClassName(ZCRMFactory.class.getName());
	}	

	@Override
	protected String getJarFileName() {
		return JAR_FILE_NAME;
	}
}
