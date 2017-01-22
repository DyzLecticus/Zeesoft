package nl.zeesoft.zodb.batch.impl;

import java.io.File;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.impl.BtcLog;

public class BtcDatabaseBackupRemote extends BtcDatabaseBackup {
	@Override
	public String execute(BtcLog log) {
		String err = "";
		
		File dir = new File(DbConfig.getInstance().getRemoteBackupDir());
		if ((!dir.exists()) || (!dir.isDirectory()) || (!dir.canWrite())) {
			err = "Unable to write to: " + DbConfig.getInstance().getRemoteBackupDir();
		}
		if (err.length()==0) {
			dir = new File(getFullBackupDir());
			if (!dir.exists()) {
				dir.mkdir();
			}
			XMLFile f = new XMLFile();
			f.setRootElement(new XMLElem("config",null,null));
			new XMLElem("encryptionKey",Generic.compress(new StringBuffer(DbConfig.getInstance().getEncryptionKey())),f.getRootElement());
			f.writeFile(DbConfig.getInstance().getRemoteBackupDir()+ "DbConfig.xml",f.toStringReadFormat());
			err = super.execute(log);
		}
		return err;
	}

	@Override
	protected String getFullBackupDir() {
		return Generic.dirName(DbConfig.getInstance().getRemoteBackupDir() + "backup/");
	}
	@Override
	protected void logBackupDirNotConfigured(BtcLog log) {
		log.addLogLine("Remote backup directory is not configured");
	}
}
