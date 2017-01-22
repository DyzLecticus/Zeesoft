package nl.zeesoft.zodb.database;

import java.io.IOException;
import java.util.Date;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.file.FileIO;

public final class DbControllerStatusWorker extends Worker {
	private final static String DB_STATUS_FILE_NAME		= "DB_STATUS";
	private final static String DB_STATUS_WORKING		= "WORKING:";
	
	protected DbControllerStatusWorker() {
		setSleep(100);
	}
	
	public void start() {
		Messenger.getInstance().debug(this, "Starting database status worker ...");
		super.start();
		Messenger.getInstance().debug(this, "Started database status worker");
    }
	
	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping database status worker ...");
		super.stop();
		Messenger.getInstance().debug(this, "Stopped database status worker");
		removeStatusFile();
    }

	@Override
	public void whileWorking() {
		if (!FileIO.fileExists(DbConfig.getInstance().getDataDir() + DB_STATUS_FILE_NAME)) {
			FileIO file = new FileIO();
			if (!file.writeFile(DbConfig.getInstance().getDataDir() + DB_STATUS_FILE_NAME,new StringBuilder(DB_STATUS_WORKING + (new Date()).getTime()))) {
				Messenger.getInstance().error(this,"Unable to write status file: " + DB_STATUS_FILE_NAME);
				DbController.getInstance().stop(false);
				System.exit(1);
			}
		}
	}
	
	protected boolean checkWorking() {
		boolean working = false;
		String fileName = DbConfig.getInstance().getDataDir() + DB_STATUS_FILE_NAME;
		if (FileIO.fileExists(fileName)) {
			working = true;
			removeStatusFile();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Messenger.getInstance().error(this,"Interrupted while checking status file: " + fileName);
			}
			if (!FileIO.fileExists(fileName)) {
				working = false;
			}
		}
		return working;
	}
	
	private void removeStatusFile() {
		try {
			FileIO.deleteFile(DbConfig.getInstance().getDataDir() + DB_STATUS_FILE_NAME);
		} catch (IOException e) {
			Messenger.getInstance().error(this,"Error while removing status file: " + DbConfig.getInstance().getDataDir() + DB_STATUS_FILE_NAME + ": " + Generic.getCallStackString(e.getStackTrace(),""));
		}
	}
}	
