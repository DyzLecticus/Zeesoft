package nl.zeesoft.zpo;

import nl.zeesoft.zadf.ZADFController;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zpo.controller.impl.ZPOFactory;
import nl.zeesoft.zpo.model.ZPOModel;

public class ZPOController extends ZADFController {
	private static final String JAR_FILE_NAME = "zpo.jar"; 
	
	public ZPOController(String[] args) {
		super(args);
	}

	@Override
	protected void initializeDBConfig() {
		super.initializeDBConfig();
		DbConfig.getInstance().setPort(6543);
		DbConfig.getInstance().setModelClassName(ZPOModel.class.getName());
	}	

	@Override
	protected void initializeGuiConfig() {
		super.initializeGuiConfig();
		GuiConfig.getInstance().setFactoryClassName(ZPOFactory.class.getName());
	}	

	@Override
	protected String getJarFileName() {
		return JAR_FILE_NAME;
	}
}
