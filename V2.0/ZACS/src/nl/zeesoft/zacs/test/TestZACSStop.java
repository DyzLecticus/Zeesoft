package nl.zeesoft.zacs.test;

import java.io.File;

import nl.zeesoft.zacs.ZACS;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbControllerCommandWorker;

public class TestZACSStop {

	public static void main(String[] args) {
		DbConfig.getInstance().setInstallDir("test/");
		File f = new File("test/");
		if (!f.exists()) {
			Messenger.getInstance().debug(DbConfig.getInstance(),"Creating directory: " + f.getAbsolutePath());
			f.mkdir();
		}
		if (f.exists()) {
			DbConfig.getInstance().setInstallDir("test/");
			args = new String[1];
			args[0] = DbControllerCommandWorker.DB_COMMAND_STOP;
			ZACS.main(args);
		}
	}

}
