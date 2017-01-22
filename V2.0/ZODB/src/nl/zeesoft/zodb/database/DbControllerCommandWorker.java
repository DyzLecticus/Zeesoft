package nl.zeesoft.zodb.database;

import java.io.IOException;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.file.FileIO;

public final class DbControllerCommandWorker extends Worker {
	private final static String 	DB_COMMAND_FILE_NAME		= "DB_COMMAND";

	public final static String 		DB_COMMAND_STOP 			= "STOP";
	public final static String 		DB_COMMAND_UPDATE			= "UPDATE";
	public final static String 		DB_COMMAND_REVERT			= "REVERT";
	public final static String 		DB_COMMAND_SHOW_CONTROLLER	= "SHOW_CONTROLLER";
	
	protected DbControllerCommandWorker() {
		setSleep(100);
	}
	
	public void start() {
		removeCommandFile();
		Messenger.getInstance().debug(this, "Starting database command worker ...");
		super.start();
		Messenger.getInstance().debug(this, "Started database command worker");
    }
	
	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping database command worker ...");
		super.stop();
		Messenger.getInstance().debug(this, "Stopped database command worker");
		removeCommandFile();
    }

	@Override
	public void whileWorking() {
		if (FileIO.fileExists(DbConfig.getInstance().getDataDir() + DB_COMMAND_FILE_NAME)) {
			FileIO file = new FileIO();
			StringBuilder content = file.readFile(DbConfig.getInstance().getDataDir() + DB_COMMAND_FILE_NAME);
			if (content.toString().trim().equals(DB_COMMAND_STOP)) {
				removeCommandFile();
				DbController.getInstance().stop(true);
				if (Messenger.getInstance().isError()) {
					System.exit(1);
				} else {
					System.exit(0);
				}
			} else if (content.toString().trim().equals(DB_COMMAND_UPDATE)) {
				removeCommandFile();
				DbController.getInstance().updateMdlModel();
			} else if (content.toString().trim().equals(DB_COMMAND_REVERT)) {
				removeCommandFile();
				DbController.getInstance().updateDbModel();
			} else if (content.toString().trim().equals(DB_COMMAND_SHOW_CONTROLLER)) {
				removeCommandFile();
				GuiController.getInstance().initializeMainFrame(true);
			}
		}
	}
	
	public static void issueCommand(String command) {
		FileIO file = new FileIO();
		file.writeFile(DbConfig.getInstance().getDataDir() + DB_COMMAND_FILE_NAME,command);
	}
	
	private void removeCommandFile() {
		try {
			FileIO.deleteFile(DbConfig.getInstance().getDataDir() + DB_COMMAND_FILE_NAME);
		} catch (IOException e) {
			Messenger.getInstance().error(this,"Error while removing command file: " + DbConfig.getInstance().getDataDir() + DB_COMMAND_FILE_NAME + ": " + Generic.getCallStackString(e.getStackTrace(),""));
		}
	}
}	
