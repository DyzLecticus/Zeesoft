package nl.zeesoft.zevt;

import nl.zeesoft.zevt.app.AppZEVT;
import nl.zeesoft.zodb.Config;

public class ZEVTConfig extends Config {
	protected void addApplications() {
		super.addApplications();
		addApplication(new AppZEVT(this));
	}
}
