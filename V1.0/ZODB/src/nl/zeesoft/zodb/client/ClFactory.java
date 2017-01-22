package nl.zeesoft.zodb.client;

import java.util.Date;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;

public final class ClFactory {

	public void buildClient() {
		Date start = new Date();
		
		DbConfig.getInstance().serialize();
		ClConfig.getInstance().serialize();
		
		long time = ((new Date()).getTime() - start.getTime());
		Messenger.getInstance().debug(this, "Client build in: " + DbConfig.getInstance().getInstallDir() + " took: " + time + " ms");
	}
	
	public void loadClient() {
		Date start = new Date();
		
		DbConfig.getInstance().unserialize();
		ClConfig.getInstance().unserialize();
		
		DbConfig.getInstance().getModel().getCrc();
		
		long time = ((new Date()).getTime() - start.getTime());
		Messenger.getInstance().debug(this, "Client load from: " + DbConfig.getInstance().getInstallDir() + " took: " + time + " ms");
	}

}
