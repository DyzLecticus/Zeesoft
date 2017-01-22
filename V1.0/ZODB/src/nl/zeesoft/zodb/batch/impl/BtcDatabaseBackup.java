package nl.zeesoft.zodb.batch.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import nl.zeesoft.zodb.batch.BtcProgramObject;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlCollection;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.impl.BtcLog;

public class BtcDatabaseBackup extends BtcProgramObject {
	private static final int 	MAX_FETCH_NUM	= 1000; 
	
	@Override
	public String execute(BtcLog log) {
		String err = "";

		String dirName = getFullBackupDir();
		
		if (dirName.length()>0) {
			File dir = new File(dirName);
			if ((!dir.exists()) || (!dir.isDirectory()) || (!dir.canWrite())) {
				err = "Unable to write to: " + dirName;
			}
			if (err.length()==0) {
				String[] backupFiles = dir.list();
				if (backupFiles.length>0) {
					log.addLogLine("Removing previous backup from: " + dirName);
					for (int i = 0; i<backupFiles.length; i++) {
						try {
							FileObject.deleteFile(dirName + backupFiles[i]);
						} catch (IOException e) {
							err = "Unable to remove file: " + dirName + backupFiles[i];
							break;
						}
					}
				}
			}
			if (err.length()==0) {
				log.addLogLine("Creating backup in: " + dirName);
				for (MdlCollection col: DbConfig.getInstance().getModel().getCollections()) {
					createBackupFilesForCollection(col,log);
				}
			}
		} else {
			logBackupDirNotConfigured(log);
		}

		return err;
	}

	public int createBackupFilesForCollection(MdlCollection col, BtcLog log) {
		int fileNum = 0;
		boolean repeat = true;
		int start = 0;
		int objects = 0;
		Date s = new Date();
		while (repeat) {
			QryFetch fetch = new QryFetch(col.getName());
			fetch.setStartLimit(start, MAX_FETCH_NUM);
			start = start + MAX_FETCH_NUM;
			DbIndex.getInstance().executeFetch(fetch, log.getExecutingAsUser(), this);
			int size = fetch.getMainResults().getReferences().size();
			if (size>0) {
				XMLFile file = new XMLFile();
				file.setRootElement(new XMLElem("objects",null,null));
				for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
					if (ref.getDataObject() instanceof BtcLog) {
						BtcLog l = (BtcLog) ref.getDataObject();
						if (l.getStopped().getValue()==null) {
							l.getStopped().setValue(new Date());
						}
					}
					XMLFile oXml = MdlDataObject.toXml(ref.getDataObject());
					oXml.getRootElement().setParent(file.getRootElement());
					oXml.setRootElement(null);
					oXml = null;
				}
				fileNum++;
				String fileName = getFullBackupDir() + col.getName() + fileNum + ".xml";
				if (file.writeFile(fileName, file.toStringCompressed())) {
					log.addLogLine("Created backup: " + fileName);
				} else {
					log.addError("Unable to create backup: " + fileName);
				}
				file.cleanUp();

				objects = objects + size;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					repeat = false;
					break;
				}
			} else {
				repeat = false;
			}
		}
		String objs = "objects";
		if (objects==1) {
			objs = "object";
		}
		log.addLogLine("Added " + objects + " " + col.getNameSingle().toLowerCase() + " " + objs + " in " + (new Date().getTime() - s.getTime()) + " ms");
		return objects;
	}
	
	protected String getFullBackupDir() {
		return DbConfig.getInstance().getFullBackupDir();
	}

	protected void logBackupDirNotConfigured(BtcLog log) {
		log.addLogLine("Backup directory is not configured");
	}
}
