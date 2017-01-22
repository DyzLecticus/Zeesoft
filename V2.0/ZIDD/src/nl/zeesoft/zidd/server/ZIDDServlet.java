package nl.zeesoft.zidd.server;

import nl.zeesoft.zidd.database.model.ZIDDDataGenerator;
import nl.zeesoft.zidd.database.model.ZIDDModel;
import nl.zeesoft.zidd.server.requesthandler.RhGetAlexa;
import nl.zeesoft.zids.database.model.ZIDSDataGenerator;
import nl.zeesoft.zids.server.ZIDSServlet;

@SuppressWarnings("serial")
public class ZIDDServlet extends ZIDSServlet {
	@Override
	protected String getModelClassName() {
		return ZIDDModel.class.getName();
	}
	@Override
	protected ZIDSDataGenerator getNewDataGenerator() {
		return new ZIDDDataGenerator();
	}
	@Override
	protected void addRequestHandlers() {
		super.addRequestHandlers();
		addRequestHandler(new RhGetAlexa());
	}
	@Override
	protected String getTitle() {
		return "ZIDD";
	}
	@Override
	protected String getBackgroundColor() {
		return "#C2DFFF";
	}
}
