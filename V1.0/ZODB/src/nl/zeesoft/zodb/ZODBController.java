package nl.zeesoft.zodb;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import nl.zeesoft.zodb.batch.BtcBatchWorker;
import nl.zeesoft.zodb.client.ClConfig;
import nl.zeesoft.zodb.client.ClControlSession;
import nl.zeesoft.zodb.client.ClFactory;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbCacheSaveWorker;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbControlServerWorker;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbIndexPreloadManager;
import nl.zeesoft.zodb.database.DbIndexSaveWorker;
import nl.zeesoft.zodb.database.DbSessionServerWorker;
import nl.zeesoft.zodb.database.DbWhiteListWorker;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.impl.BtcProgram;
import nl.zeesoft.zodb.model.impl.DbSession;
import nl.zeesoft.zodb.protocol.PtcObject;

/**
 * This class is designed to be extended as a main entry point for specific client/server implementations.
 */
public class ZODBController implements EvtEventSubscriber {
	public static final int		KEY_LENGTH			= 128;
	
	public static final String 	DATA_DIR_NAME		= "DATA_DIR_NAME";

	private static final String ACTION_DESCRIBE		= "DESCRIBE"; 
	private static final String ACTION_INSTALL 		= "INSTALL"; 
	private static final String ACTION_CHECK_LOCK	= "CHECK_LOCK"; 
	private static final String ACTION_START		= "START"; 
	private static final String ACTION_STOP		 	= "STOP"; 
	private static final String ACTION_RESTORE	 	= "RESTORE"; 
	private static final String ACTION_UPDATE	 	= "UPDATE";
	
	private String				action				= "";
	private String 				dir 				= Generic.dirName(new File("").getAbsolutePath()); 

	private	ClControlSession 	controlSession 		= null;

	public ZODBController(String[] args) {
		if (args.length>=1) {
			action = args[0];
		}

		if ((action.equals(ACTION_INSTALL)) && (args.length>=2)) {
			dir = Generic.dirName(new File(args[1]).getAbsolutePath());
		}

		if (action.length()>0) {
			DbConfig.getInstance().setInstallDir(getDir());
		}

		if (action.equals(ACTION_DESCRIBE)) {
			describe();
		} else if (action.equals(ACTION_INSTALL)) {
			install();
		} else if (action.equals(ACTION_RESTORE)) {
			restore();
		} else if (action.equals(ACTION_UPDATE)) {
			if (args.length>=2) {
				update(args[1]);
			} else {
				Messenger.getInstance().error(this, "Update requires a backup dir parameter");
			}
		} else if (action.equals(ACTION_CHECK_LOCK)) {
			if (FileObject.fileExists(getLockFileName())) {
				startControlSession();
			}
		} else if (action.equals(ACTION_START)) {
			if (FileObject.fileExists(getLockFileName())) {
				startControlSession();
			} else {
				startServer();
			}
		} else if (action.equals(ACTION_STOP)) {
			startControlSession();
		}
	}

	protected void initializeDBConfig() {
		DbConfig.getInstance().setEncryptionKey(Generic.generateNewKey(KEY_LENGTH));
		DbConfig.getInstance().setModelClassName(ZODBModel.class.getName());
	}	

	protected void describe() {
		initializeDBConfig();
		DbConfig.getInstance().setDebug(true);
		FileObject file = new FileObject();
		file.writeFile(getDir() + "model.txt", DbConfig.getInstance().getModel().getDescription());
	}

	protected void install() {
		initializeDBConfig();

		if (DbConfig.getInstance().getEncryptionKey().length()<KEY_LENGTH) {
			DbConfig.getInstance().setEncryptionKey(Generic.generateNewKey(KEY_LENGTH));
		}
		
		DbFactory dbFac = new DbFactory();
		dbFac.buildDatabase(dbFac);
		
		ClFactory clFac = new ClFactory();
		clFac.buildClient();
		
		File libDir = new File(getDir() + "lib/");
		File binDir = new File(getDir() + "bin/");
		File outDir = new File(getDir() + "out/");
		if (
			((libDir.exists() && (libDir.isDirectory())) || (libDir.mkdir())) && 
			((binDir.exists() && (binDir.isDirectory())) || (binDir.mkdir())) && 
			((outDir.exists() && (outDir.isDirectory())) || (outDir.mkdir()))
			) {
			FileObject file = new FileObject();
			file.writeFile(Generic.dirName(binDir.getAbsolutePath()) + "caller.bat", getCallerScript());
			file.writeFile(Generic.dirName(binDir.getAbsolutePath()) + "background.vbs", getBackgroundScript());
			file.writeFile(Generic.dirName(binDir.getAbsolutePath()) + "checkLock.bat", getCheckLockScript());
			file.writeFile(Generic.dirName(binDir.getAbsolutePath()) + "stopServer.bat", getStopWaitScript());
			file.writeFile(Generic.dirName(binDir.getAbsolutePath()) + "restore.bat", getRestoreScript());
			file.writeFile(Generic.dirName(binDir.getAbsolutePath()) + "update.bat", getUpdateScript());
			file.writeFile(getDir() + "startServer.bat", getStartScript());
			file.writeFile(getDir() + "stopServer.bat", getStopScript());
		}
	}

	protected void restore() {
		DbConfig.getInstance().unserialize();
		if (FileObject.fileExists(getLockFileName())) {
			Messenger.getInstance().error(this, "Data directory is locked");
		} else {
			DbFactory dbFac = new DbFactory();
			dbFac.restoreDatabaseFromBackup(dbFac);
		}
	}

	protected void update(String dataDirName) {
		DbConfig.getInstance().unserialize();
		if (FileObject.fileExists(getLockFileName())) {
			Messenger.getInstance().error(this, "Data directory is locked");
		} else {
			DbFactory dbFac = new DbFactory();
			dbFac.updateDatabaseFromBackup(dataDirName,dbFac);
		}
	}

	protected void startControlSession() {
		DbConfig.getInstance().unserialize();
		
		ClFactory clFac = new ClFactory();
		clFac.loadClient();
		
		ClSessionManager.getInstance().addSubscriber(this);
		ClSessionManager.getInstance().initializeNewControlSession();
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			if (action.equals(ACTION_START)) {
				Messenger.getInstance().error(this, "Server is already running");
				ClSessionManager.getInstance().stopSession(controlSession.getSessionId());
			} else if (action.equals(ACTION_CHECK_LOCK)) {
				System.exit(0);
			} else {
				ClRequest r = (ClRequest) e.getValue();
				if (r.getActionResponse().startsWith(PtcObject.AUTHORIZE_SESSION_SUCCESS)) {
					Messenger.getInstance().debug(this, "Authorized for action: " + action);
					ClRequest sr = null;
					if (action.equals(ACTION_STOP)) {
						sr = controlSession.getNewStopZODBProgramRequest();
					}
					if (sr!=null) {
						sr.addSubscriber(this);
						controlSession.getRequestQueue().addRequest(sr, this);
					}
				} else if (r.getActionResponse().startsWith(PtcObject.AUTHORIZE_SESSION_FAILED)) {
					Messenger.getInstance().error(this, "Failed to authorize for action: " + action);
					ClSessionManager.getInstance().stopSession(controlSession.getSessionId());
				} else {
					Messenger.getInstance().error(this, "Unexpected response for action: " + action + ", response: " + r.getActionResponse());
					ClSessionManager.getInstance().stopSession(controlSession.getSessionId());
				}
			}
		} else if (e.getType().equals(ClSessionManager.LOST_CONNECTION_TO_SERVER)) {
			System.exit(0);
		} else if (e.getType().equals(ClSessionManager.FAILED_TO_CONNECT_TO_SERVER)) {
			if (
				(action.equals(ACTION_START)) ||
				(action.equals(ACTION_CHECK_LOCK))
				) {
				Messenger.getInstance().debug(this, "Unlocking data directory because server is not running.");
				unlockDataDir();
				if (action.equals(ACTION_START)) {
					startServer();
				} else if (action.equals(ACTION_CHECK_LOCK)) {
					System.exit(0);
				}
			}
		} else if (e.getType().equals(ClSessionManager.UNABLE_TO_DECODE_SERVER_RESPONSE)) {
			Messenger.getInstance().debug(this,ClSessionManager.UNABLE_TO_DECODE_SERVER_RESPONSE);
			System.exit(0);
		} else if (e.getType().equals(ClSessionManager.SERVER_STOPPED_SESSION)) {
			Messenger.getInstance().debug(this,ClSessionManager.SERVER_STOPPED_SESSION);
			System.exit(0);
		} else if (e.getType().equals(ClSessionManager.FAILED_TO_OBTAIN_SESSION)) {
			Messenger.getInstance().debug(this,ClSessionManager.FAILED_TO_OBTAIN_SESSION);
			System.exit(0);
		} else if (e.getType().equals(ClSessionManager.OBTAINED_SESSION)) {
			if (e.getValue()!=null) {
				controlSession = (ClControlSession) e.getValue();
				ClRequest r = controlSession.getNewAuthorizationRequest(ClConfig.getInstance().getUserName(),ClConfig.getInstance().getUserPassword());
				r.addSubscriber(this);
				controlSession.getRequestQueue().addRequest(r, this);

				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						ClSessionManager.getInstance().stopSession(controlSession.getSessionId());
					}
				});
			}
		}
	}

	protected void startServer() {
		DbConfig.getInstance().unserialize();
		String err = lockDataDir();
		if (err.length()==0) {
			DbFactory dbFac = new DbFactory();
			dbFac.loadDatabase(dbFac);
		
			DbCacheSaveWorker.getInstance().start();
			DbIndexSaveWorker.getInstance().start();
			DbIndexPreloadManager.getInstance().start();
			endOpenSessions();
			endStartedBatches();
			DbWhiteListWorker.getInstance().start();
			DbControlServerWorker.getInstance().start();
			DbSessionServerWorker.getInstance().start();
			if (DbConfig.getInstance().isStartBatches()) {
				BtcBatchWorker.getInstance().start();
			}
	
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						if (DbSessionServerWorker.getInstance().isWorking()) {
							DbSessionServerWorker.getInstance().stop();
						}
						if (DbControlServerWorker.getInstance().isWorking()) {
							DbControlServerWorker.getInstance().stop();
						}
						if (BtcBatchWorker.getInstance().isWorking()) {
							BtcBatchWorker.getInstance().stop();
						}
						if (DbWhiteListWorker.getInstance().isWorking()) {
							DbWhiteListWorker.getInstance().stop();
						}
						if (DbIndexPreloadManager.getInstance().isWorking()) {
							DbIndexPreloadManager.getInstance().stop();
						}
						if (DbIndexSaveWorker.getInstance().isWorking()) {
							DbIndexSaveWorker.getInstance().stop();
						}
						if (DbCacheSaveWorker.getInstance().isWorking()) {
							DbCacheSaveWorker.getInstance().stop();
						}
						WorkerUnion.getInstance().stopWorkers();
					} catch (Exception e) {
						String callStack = Generic.getCallStackString(e.getStackTrace(),"");
						Messenger.getInstance().error(this, "An error occured in the shutdown hook: " + e.getMessage() + "\n" + callStack);
					} finally {
						unlockDataDir();
					}
				}
			});
		}
	}

	protected void endOpenSessions() {
		QryFetch fetch = new QryFetch(DbSession.class.getName());
		fetch.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,QryFetchCondition.OPERATOR_EQUALS,new DtString(DbSession.OPEN)));
		DbIndex.getInstance().executeFetch(fetch, DbConfig.getInstance().getModel().getAdminUser(this), this);
		if (fetch.getMainResults().getReferences().size()>0) {
			QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(this));
			for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
				DbSession session = (DbSession) ref.getDataObject();
				session.getName().setValue(DbSession.CLOSED);
				session.getEnded().setValue(new Date());
				t.addQuery(new QryUpdate(session));
			}
			DbIndex.getInstance().executeTransaction(t, this);
			Messenger.getInstance().debug(this, "Closed " + t.getUpdatedIdList().size() + " sessions");
		}
	}

	protected void endStartedBatches() {
		QryFetch fetch = new QryFetch(BtcProgram.class.getName());
		fetch.addCondition(new QryFetchCondition("executing",QryFetchCondition.OPERATOR_EQUALS,new DtBoolean(true)));
		DbIndex.getInstance().executeFetch(fetch, DbConfig.getInstance().getModel().getAdminUser(this), this);
		if (fetch.getMainResults().getReferences().size()>0) {
			QryTransaction t = new QryTransaction(DbConfig.getInstance().getModel().getAdminUser(this));
			for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
				BtcProgram program = (BtcProgram) ref.getDataObject();
				program.getExecuting().setValue(false);
				t.addQuery(new QryUpdate(program));
			}
			DbIndex.getInstance().executeTransaction(t, this);
			Messenger.getInstance().debug(this, "Stopped " + t.getUpdatedIdList().size() + " batch programs");
		}
	}

	protected String getCheckLockScript() {
		return
			"cd ../" + "\r\n" +
			"bin\\java -jar lib/" + getJarFileName() + " " + ACTION_CHECK_LOCK + "\r\n";
	}

	protected String getStopWaitScript() {
		return
			"cd ../" + "\r\n" +
			"bin\\java -jar lib/" + getJarFileName() + " " + ACTION_STOP + "\r\n";
	}

	protected String getRestoreScript() {
		return
			"cd ../" + "\r\n" +
			"bin\\java " + getMemorySettings() + " -jar lib/" + getJarFileName() + " " + ACTION_RESTORE + " 1>bin\\restore.log \r\n";
	}

	protected String getUpdateScript() {
		return
			"cd ../" + "\r\n" +
			"bin\\java " + getMemorySettings() + " -jar lib/" + getJarFileName() + " " + ACTION_UPDATE + " " + DATA_DIR_NAME + " 1>bin\\update.log \r\n";
	}

	protected String getStartScript() {
		return
			//"cd " + getDir() + "\r\n" +
			"wscript.exe bin\\background.vbs bin\\caller.bat Server START" + "\r\n";
	}

	protected String getStopScript() {
		return 
			//"cd " + getDir() + "\r\n" +
			"wscript.exe bin\\background.vbs bin\\caller.bat Stop STOP" + "\r\n";
	}

	protected String getJarFileName() {
		return "zodb.jar";
	}

	protected String getMemorySettings() {
		return "-Xms128m -Xmx1024m";
	}

	protected String getCallerScript() {
		return
			"@ECHO OFF" + "\r\n" +
			"bin\\java " + getMemorySettings() + " -jar lib/" + getJarFileName() + " %2 %3 1>out\\\"%1\".log 2>out\\\"%1\"Errors.log" + "\r\n";
	}
	
	protected String getBackgroundScript() {
		return 
			"set args = WScript.Arguments" + "\r\n" +
			"num = args.Count" + "\r\n" +
			"if num = 0 then" + "\r\n" +
			"	WScript.Echo \"Usage: [CScript | WScript] background.vbs aScript.bat <some script arguments>\"" + "\r\n" +
			"	WScript.Quit 1" + "\r\n" +
			"end if" + "\r\n" +
			"sargs = \"\"" + "\r\n" +
			"if num > 1 then" + "\r\n" +
			"	sargs = \" \"" + "\r\n" +
			"	for k = 1 to num - 1" + "\r\n" +
			"		anArg = args.Item(k)" + "\r\n" +
			"		sargs = sargs & anArg & \" \"" + "\r\n" +
			"	next" + "\r\n" +
			"end if" + "\r\n" +
			"Set WshShell = WScript.CreateObject(\"WScript.Shell\")" + "\r\n" +
			"WshShell.Run \"\"\"\" & WScript.Arguments(0) & \"\"\"\" & sargs, 0, False" + "\r\n"
			;
	}

	/**
	 * @return the action
	 */
	protected String getAction() {
		return action;
	}

	/**
	 * @return the dir
	 */
	protected String getDir() {
		return dir;
	}

	/**
	 * @return the controlSession
	 */
	protected ClControlSession getControlSession() {
		return controlSession;
	}
	
	private String getLockFileName() {
		return DbConfig.getInstance().getFullDataDir() + ".lock";
	}
	
	private String lockDataDir() {
		String err = "";
		String fileName = getLockFileName();
		FileObject fObj = new FileObject();
		if (!FileObject.fileExists(fileName)) {
			boolean locked = fObj.writeFile(fileName, "LOCKED");
			if (locked) {
				Messenger.getInstance().debug(this,"Locked data directory");
			} else {
				err = "Unable to lock data directory";
				Messenger.getInstance().error(this, err);
			}
		} else {
			err = "Data directory is already locked";
			Messenger.getInstance().error(this, err);
		}
		return err;
	}
	
	private String unlockDataDir() {
		String err = "";
		String fileName = DbConfig.getInstance().getFullDataDir() + ".lock";
		if (FileObject.fileExists(fileName)) {
			try {
				FileObject.deleteFile(fileName);
				Messenger.getInstance().debug(this,"Unlocked data directory");
			} catch (IOException e) {
				String callStack = Generic.getCallStackString(e.getStackTrace(),"");
				err = "Unable to unlock data directory: " + e.getMessage() + "\n" + callStack;
				Messenger.getInstance().error(this, err);
			}
		} else {
			err = "Data directory is not locked";
			Messenger.getInstance().error(this, err);
		}
		return err;
	}
}
