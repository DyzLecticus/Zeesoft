package nl.zeesoft.zac.test;

import java.io.File;

import nl.zeesoft.zac.ZAC;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.server.SvrConfig;

public class TestZAC {

	public static void main(String[] args) {
		DbConfig.getInstance().setInstallDir("test/");
		DbConfig.getInstance().setDebug(true);
		File f = new File("test/");
		if (!f.exists()) {
			Messenger.getInstance().debug(DbConfig.getInstance(),"Creating directory: " + f.getAbsolutePath());
			f.mkdir();
		}
		if (f.exists()) {
			SvrConfig.getInstance().setPort(6556);
			SvrConfig.getInstance().setAuthorizeGetRequests(false);
			SvrConfig.getInstance().setAuthorizePostRequests(false);
			SvrConfig.getInstance().setGenerateHTTPResources(true);
			ZAC.main(args);
		}
	}

}
