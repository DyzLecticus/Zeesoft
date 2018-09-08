package nl.zeesoft.zevt;

import nl.zeesoft.zevt.app.AppZEVT;
import nl.zeesoft.zevt.app.handler.JsonZEVTRequestHandler;
import nl.zeesoft.zevt.trans.EntityClient;
import nl.zeesoft.zevt.trans.EntityClientListener;
import nl.zeesoft.zevt.trans.EntityRequestResponse;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;

public class ZEVTConfig extends Config {
	protected void addApplications() {
		super.addApplications();
		addApplication(new AppZEVT(this));
	}
	
	public AppZEVT getZEVT() {
		AppZEVT r = null;
		AppObject app = getApplication(AppZEVT.NAME);
		if (app!=null && app instanceof AppZEVT) {
			r = (AppZEVT) app;
		}
		return r;
	}

	public void handleEntityRequest(EntityRequestResponse request,EntityClientListener listener) {
		EntityClient client = new EntityClient(this);
		client.handleRequest(request,getApplicationUrl(AppZEVT.NAME) + JsonZEVTRequestHandler.PATH,listener);
	}
}
