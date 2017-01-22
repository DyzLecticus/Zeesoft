package nl.zeesoft.zodb.database;

import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.WorkerUnion;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.index.IdxClass;
import nl.zeesoft.zodb.database.index.IdxObject;
import nl.zeesoft.zodb.database.index.IdxUniqueConstraint;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.server.SvrController;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.file.FileIO;

/**
 * This is the main database controller
 * 
 * The handleMainMethodsArguments method is just one possible way to use this controller.
 * Use any of the public methods to construct and manage a more specific implementation.
 */
public final class DbController extends EvtEventPublisher {
	private static final String			INSTALL_SCRIPT_FILE		= "install.bat";

	public static final String			DB_INSTALLING			= "DB_INSTALLING";
	public static final String			DB_INSTALLED			= "DB_INSTALLED";
	public static final String			DB_STARTING				= "DB_STARTING";
	public static final String			DB_STARTED				= "DB_STARTED";
	public static final String			DB_STOPPING				= "DB_STOPPING";
	public static final String			DB_STOPPED				= "DB_STOPPED";
	
	public static final String			DB_INITIALIZING_MODEL	= "DB_INITIALIZING_MODEL";
	public static final String			DB_INITIALIZED_MODEL	= "DB_INITIALIZED_MODEL";

	public static final String			DB_REVERTING_MODEL		= "DB_REVERTING_MODEL";
	public static final String			DB_REVERTED_MODEL		= "DB_REVERTED_MODEL";

	public static final String			DB_UPDATING_MODEL		= "DB_UPDATING_MODEL";
	public static final String			DB_UPDATED_MODEL		= "DB_UPDATED_MODEL";

	private static DbController			controller				= null;
	private Object 						controllerIsLockedBy	= null;
	
	private DbControllerStatusWorker	statusWorker			= null;
	private DbControllerCommandWorker	commandWorker			= null;

	private boolean						stopped					= false;
	
	private DbController() {
		statusWorker = new DbControllerStatusWorker();
		commandWorker = new DbControllerCommandWorker();
	}

	public static DbController getInstance() {
		if (controller==null) {
			controller = new DbController();
		}
		return controller;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	/**
	 * if the database is installed
	 * then 
	 *     if args==null OR args[0].lenght==0 then start the database 
	 *     else if args[0].equals("STOP") then stop the database
	 *     else if args[0].equals("UPDATE") then update the abstract data model
	 *     else if args[0].equals("REVERT") then revert changes to the database data model
	 *     else if args[0].equals("SHOW_GUI") then show the database control GUI
	 * else install the database, optional arguments: [1]=jarFileName [2]=memorySettingLow [3]=memorySettingHigh
	 * 
	 * @param args The String array with arguments
	 */
	public void handleMainMethodsArguments(String[] args) {
		if (DbConfig.getInstance().fileExists()) {
			DbConfig.getInstance().unserialize();
			if (args==null || args.length==0  || args[0]==null || args[0].length()==0) {
				if (checkWorking()) {
					Messenger.getInstance().debug(this, "ZODB is already working in: " + DbConfig.getInstance().getDataDir());
					showGUI();
				} else {
					GuiController.getInstance().initializeMainFrame(false);
					GuiController.getInstance().setProgressFrameTitle("Starting database ...");
					GuiController.getInstance().showProgressFrame();
					if (start(DbConfig.getInstance().isShowGUI())) {
						if (FileIO.fileExists(INSTALL_SCRIPT_FILE)) {
							try {
								FileIO.deleteFile(INSTALL_SCRIPT_FILE);
							} catch (IOException e) {
								Messenger.getInstance().warn(this,"Unable to delete " + INSTALL_SCRIPT_FILE);
							}
						}
						checkOpenServer();
					}
					GuiController.getInstance().hideProgressFrame();
				}
			} else if (args[0].equals(DbControllerCommandWorker.DB_COMMAND_STOP)) {
				if (checkWorking()) {
					stop();
				}
			} else if (args[0].equals(DbControllerCommandWorker.DB_COMMAND_UPDATE)) {
				if (checkWorking()) {
					update();
				}
			} else if (args[0].equals(DbControllerCommandWorker.DB_COMMAND_REVERT)) {
				if (checkWorking()) {
					revert();
				}
			} else if (args[0].equals(DbControllerCommandWorker.DB_COMMAND_SHOW_CONTROLLER)) {
				if (checkWorking()) {
					showGUI();
				}
			} else {
				Messenger.getInstance().error(this, "Unhandled action: " + args[0]);
			}
		} else {
			StringBuilder errorText = DbConfig.getInstance().getModel().getErrorText();
			if (errorText.length()==0) {
				String jarFileName = "";
				String memorySetting1 = "";
				String memorySetting2 = "";
				String memorySettings = "";
				if (args!=null && args.length>1 && args[1]!=null) {
					jarFileName = args[1].toLowerCase();
				}
				if (args!=null && args.length>2 && args[2]!=null) {
					memorySetting1 = args[2];
				}
				if (args!=null && args.length>3 && args[3]!=null) {
					memorySetting2 = args[3];
				}
				if (memorySetting1.length()>0 && memorySetting2.length()>0) {
					memorySettings = memorySetting1 + " " + memorySetting2;
				}
				if (!installDefault(jarFileName,memorySettings)) {
					System.exit(1);
				} else {
					System.exit(0);
				}
			} else {
				Messenger.getInstance().start();
				Messenger.getInstance().error(this,"Model has errors:\n" + errorText);
				Messenger.getInstance().addSubscriber(new EvtEventSubscriber() {
					@Override
					public void handleEvent(EvtEvent e) {
						// Ignore
					}
				});
				Messenger.getInstance().stop();
				System.exit(1);
			}
		}
	}
	
	public boolean checkWorking() {
		boolean working = false;
		lockController(this);
		working = statusWorker.checkWorking();
		unlockController(this);
		return working;
	}

	public boolean getStopped() {
		boolean r = false;
		lockController(this);
		r = stopped;
		unlockController(this);
		return r;
	}
	
	public boolean start(boolean showProgress) {
		boolean success = false;
		if (ifLockController(this)) {
			stopped = false;
			if (showProgress) {
				Messenger.getInstance().addSubscriber(GuiController.getInstance());
			} else {
				Messenger.getInstance().addSubscriber(new EvtEventSubscriber() {
					@Override
					public void handleEvent(EvtEvent e) {
						// Ignore
					}
				});
			}
			Messenger.getInstance().start();
			publishEvent(new EvtEvent(DB_STARTING,this,null));
			Messenger.getInstance().debug(this, "Starting ZODB in: " + DbConfig.getInstance().getDataDir());
			boolean error = false;
			if (modelFileExists()) {
				statusWorker.start();
				commandWorker.start();
				DbConfig.getInstance().getModel().unserialize(MdlModel.getFullFileName());
				if (!DbConfig.getInstance().getModel().check() && DbConfig.getInstance().getModel().createDirectories(this)) {
					DbIndex.getInstance().unserialize(this);
					DbIndex.getInstance().startWorkers(true);
				} else {
					error = true;
				}
			} else {
				error = true;
			}
			if (error) {
				stop(false);
				System.exit(1);
			} else {
				success = true;
				Runtime.getRuntime().addShutdownHook(new Thread(){
					@Override
					public void run() {
						DbController.getInstance().stop(true); 
					}
				});
			}
			unlockController(this);

			// It seems certain contexts need a little pause before the database is ready
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Messenger.getInstance().error(this,"Startup pause interrupted: " + e);
			}
			publishEvent(new EvtEvent(DB_STARTED,this,success));
			
		}
		return success;
	}

	public void stop() {
		DbControllerCommandWorker.issueCommand(DbControllerCommandWorker.DB_COMMAND_STOP);		
	}

	public void update() {
		DbControllerCommandWorker.issueCommand(DbControllerCommandWorker.DB_COMMAND_UPDATE);		
	}

	public void revert() {
		DbControllerCommandWorker.issueCommand(DbControllerCommandWorker.DB_COMMAND_REVERT);		
	}

	public void showGUI() {
		if (DbConfig.getInstance().isShowGUI()) {
			DbControllerCommandWorker.issueCommand(DbControllerCommandWorker.DB_COMMAND_SHOW_CONTROLLER);
		}
	}
	
	public boolean checkOpenServer() {
		boolean error = false;
		if (DbConfig.getInstance().isOpenServer()) {
			error = !openServer();
		}
		return !error;
	}

	public boolean openServer() {
		closeServer();
		return SvrController.getInstance().open();
	}

	/**
	 * Basic installation required by the database
	 * 
	 * @param jarFileName the optional jar file name
	 * @param memorySettings the optional jar file name
	 * @return true if the installation was successful
	 */
	public boolean installDefault(String jarFileName,String memorySettings) {
		return install(jarFileName,memorySettings,false);
	}
	
	public boolean install(String jarFileName,String memorySettings,boolean databaseOnly) {
		publishEvent(new EvtEvent(DB_INSTALLING,this,null));
		if (!databaseOnly) {
			if (jarFileName.length()==0) {
				jarFileName = "zodb.jar";
			}
			if (memorySettings.length()==0) {
				memorySettings = "-Xms128m -Xmx1024m";
			}
		}
		boolean error = false;
		error = !installDatabase();
		if (!error) {
			// Debug logging for install.log
			DbConfig.getInstance().setDebug(true);
			DbConfig.getInstance().setDebugPerformance(true);
			DbConfig.getInstance().setDebugRequestLogging(true);
			DbConfig.getInstance().getCacheConfig().setDebug(true);

			if (!databaseOnly) {
				// TODO: Create UNIX style script installation
				installZODBWindowsScripts(jarFileName,memorySettings);
			}
			
			start(false);
			initializeDbModel();
			stop(false);
			
			DbConfig.getInstance().getCache().reinitialize(this);
			
			error = Messenger.getInstance().isError();
		}
		publishEvent(new EvtEvent(DB_INSTALLED,this,!error));
		return error;
	}
	
	public boolean initializeDbModel() {
		return updateDbModelPublishEvents(DB_INITIALIZING_MODEL,DB_INITIALIZED_MODEL);
	}
	
	// Called by controller command worker and protocol
	public boolean updateDbModel() {
		return updateDbModelPublishEvents(DB_REVERTING_MODEL,DB_REVERTED_MODEL);
	}
	
	// Called by controller command worker
	protected boolean updateMdlModel() {
		boolean converted = false;
		if (ifLockController(this)) {
			publishEvent(new EvtEvent(DB_UPDATING_MODEL,this,null));
	
			DbRequestQueue.getInstance().setReadOnly(true,this);
	
			MdlModel useModel = DbConfig.getInstance().getNewModel();
			List<String> originalFullNames = useModel.getFullNames();
			useModel.unserialize(MdlModel.getFullFileName());
			
			DbModelConverter converter = new DbModelConverter();
			converted = converter.updateMdlModel(useModel,originalFullNames,false);
			if (converted) {
				Messenger.getInstance().debug(this,"Updating database ...");
				if (useModel.createDirectories(this)) {
					if (addUniqeConstraintIndexes(converter.getAddIndexes())) {
						
						// Remove index directories
						for (IdxObject removeIndex: converter.getRemoveIndexes()) {
							boolean deleted = false;
							try {
								String dirName = removeIndex.getDirName();
								if (removeIndex instanceof IdxClass) {
									dirName = ((IdxClass) removeIndex).getClassDirName();
								}
								if (FileIO.fileExists(dirName)) {
									deleted = FileIO.deleteDirectory(dirName);
									if (!deleted) {
										Messenger.getInstance().error(this,"Failed to remove index directory: " + dirName);
									}
								}
							} catch (IOException e) {
								Messenger.getInstance().error(this,"Exception while removing index directories: " + e);
							}
						}
						addNonUniqeConstraintIndexes(converter.getAddIndexes());
						
						// Update working model
						useModel.serialize(MdlModel.getFullFileName());
						DbConfig.getInstance().getModel().unserialize(MdlModel.getFullFileName());
						useModel.cleanUp();
	
						Messenger.getInstance().debug(this,"Updated database");
					} else {
						Messenger.getInstance().error(this,"Failed to update database, error while updating unique indexes");
						converted = false;
					}
				} else {
					Messenger.getInstance().error(this,"Failed to update database");
					converted = false;
				}
			}
	
			boolean stoppedIndex = DbIndex.getInstance().stopWorkers(false);
	
			DbRequestQueue.getInstance().setReadOnly(false,this);
	
			if (stoppedIndex) {
				DbIndex.getInstance().restart(this);
				if (converted && converter.isRevert()) {
					converter.updateDbModel(DbConfig.getInstance().getModel());
				}
			}
			
			publishEvent(new EvtEvent(DB_UPDATED_MODEL,this,converted));
			unlockController(this);
		}
		return converted;
	}

	public void stop(boolean showProgress) {
		lockController(this);
		if (!stopped) {
			stopped = true;
			if (showProgress) {
				GuiController.getInstance().setProgressFrameTitle("Stopping database ...");
				GuiController.getInstance().showProgressFrame();
			}
			publishEvent(new EvtEvent(DB_STOPPING,this,null));
			closeServer();
			Messenger.getInstance().debug(this, "Stopping ZODB in: " + DbConfig.getInstance().getDataDir());
			DbIndex.getInstance().stopWorkers(showProgress);
			if (statusWorker.isWorking()) {
				statusWorker.stop();
			}
			if (showProgress) {
				GuiController.getInstance().incrementProgressFrameDone();
			}
			if (commandWorker.isWorking()) {
				commandWorker.stop();
			}
			if (showProgress) {
				GuiController.getInstance().incrementProgressFrameDone();
			}
			Messenger.getInstance().stop();
			if (showProgress) {
				GuiController.getInstance().incrementProgressFrameDone();
			}
			WorkerUnion.getInstance().stopWorkers(commandWorker);
			if (showProgress) {
				GuiController.getInstance().incrementProgressFrameDone();
			}
			publishEvent(new EvtEvent(DB_STOPPED,this,null));
		}
		unlockController(this);
	}

	/**************************** PRIVATE METHODS **************************/
	private synchronized boolean ifLockController(Object source) {
		boolean locked = false;
		if (!controllerIsLocked()) {
			lockController(source);
			locked = true;
		}
		return locked;
	}
	
	private synchronized void lockController(Object source) {
		int attempt = 0;
		while (controllerIsLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,Locker.getLockFailedMessage(attempt,source));
				attempt = 0;
			}
		}
		controllerIsLockedBy = source;
	}

	private synchronized void unlockController(Object source) {
		if (controllerIsLockedBy==source) {
			controllerIsLockedBy=null;
			notifyAll();
		}
	}
	
	private synchronized boolean controllerIsLocked() {
		return (controllerIsLockedBy!=null);
	}

	private boolean updateDbModelPublishEvents(String beforeEvent,String afterEvent) {
		boolean updated = false;
		if (ifLockController(this)) {
			publishEvent(new EvtEvent(beforeEvent,this,null));
			DbModelConverter converter = new DbModelConverter();
			updated = converter.updateDbModel(DbConfig.getInstance().getModel());
			publishEvent(new EvtEvent(afterEvent,this,updated));
			unlockController(this);
		}
		return updated;
	}

	private boolean closeServer() {
		boolean closed = false;
		if (SvrController.getInstance().isOpen()) {
			closed = SvrController.getInstance().close();
		}
		return closed;
	}
	
	private boolean modelFileExists() {
		boolean exists = true;
		File modelFile = new File(MdlModel.getFullFileName());
		if (!modelFile.exists()) {
			Messenger.getInstance().error(this, "Unable to find model file: " + MdlModel.getFullFileName());
			exists = false;
		}
		return exists;
	}
	
	private boolean addUniqeConstraintIndexes(List<IdxObject> indexes) {
		boolean error = false;
		for (IdxObject index: indexes) {
			if (index instanceof IdxUniqueConstraint) {
				DbIndexBuilder builder = new DbIndexBuilder(index);
				if (!builder.buildIndex()) {
					error = true;
					break;
				}
			}
		}
		return !error;
	}
	
	private boolean addNonUniqeConstraintIndexes(List<IdxObject> indexes) {
		boolean error = false;
		for (IdxObject index: indexes) {
			if (!(index instanceof IdxUniqueConstraint)) {
				DbIndexBuilder builder = new DbIndexBuilder(index);
				if (!builder.buildIndex()) {
					error = true;
					break;
				}
			}
		}
		return !error;
	}

	private boolean installDatabase() {
		boolean error = false;
		Messenger.getInstance().debug(this, "Installing ZODB in: " + DbConfig.getInstance().getDataDir());

		File confDir = new File(DbConfig.getInstance().getConfDir());
		if (!confDir.exists()) {
			confDir.mkdir();
		}
		if (!confDir.exists()) {
			Messenger.getInstance().error(this, "Unable to create configuration directory: " + DbConfig.getInstance().getConfDir());
			error = true;
		}
		if (!error) {
			File dataDir = new File(DbConfig.getInstance().getDataDir());
			if (!dataDir.exists()) {
				dataDir.mkdir();
			}
			if (!dataDir.exists()) {
				Messenger.getInstance().error(this, "Unable to create data directory: " + DbConfig.getInstance().getDataDir());
				error = true;
			}
		}
		if (!error) {
			DbConfig.getInstance().serialize();
			if (!DbConfig.getInstance().fileExists()) {
				Messenger.getInstance().error(this, "Unable to create configuration file: " + DbConfig.getInstance().getFullFileName());
				error = true;
			}
		}
		if (!error) {
			DbConfig.getInstance().getModel().serialize(MdlModel.getFullFileName());
			File modelFile = new File(MdlModel.getFullFileName());
			if (!modelFile.exists()) {
				Messenger.getInstance().error(this, "Unable to create model file: " + MdlModel.getFullFileName());
				error = true;
			}
		}
		if (!error) {
			error = !DbConfig.getInstance().getModel().createDirectories(this);
		}
		return !error;
	}
	
	/**
	 * Windows ZODB script installation
	 * 
	 * @param jarFileName the file name of the jar (see handleMainMethodsArguments)
	 * @param memorySettings optional java executable memory setting parameters 
	 */
	private void installZODBWindowsScripts(String jarFileName,String memorySettings) {
		String dir = DbConfig.getInstance().getInstallDir();
		File binDir = new File(dir + "bin/");
		File outDir = new File(dir + "out/");
		if (
			((binDir.exists() && (binDir.isDirectory())) || (binDir.mkdir())) && 
			((outDir.exists() && (outDir.isDirectory())) || (outDir.mkdir()))
			) {
			FileIO file = new FileIO();
			file.writeFile(Generic.dirName(binDir.getAbsolutePath()) + "caller.bat", getCallerScript(memorySettings,jarFileName));
			file.writeFile(Generic.dirName(binDir.getAbsolutePath()) + "background.vbs", getBackgroundScript());
			file.writeFile(dir + "start.bat", getStartScript());
			file.writeFile(dir + "stop.bat", getStopScript());
			file.writeFile(dir + "control.bat", getGUIScript());
		}
	}

	private String getCallerScript(String memorySettings,String jarFileName) {
		return
			"@ECHO OFF" + "\r\n" +
			"java " + memorySettings + " -jar lib/" + jarFileName + " %2 %3 1>out\\\"%1\".log 2>out\\\"%1\"Errors.log" + "\r\n";
	}
	
	private String getBackgroundScript() {
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

	private String getStartScript() {
		return 
			"@ECHO OFF" + "\r\n" +
			"wscript.exe bin\\background.vbs bin\\caller.bat start" + "\r\n";
	}

	private String getStopScript() {
		return 
			"@ECHO OFF" + "\r\n" +
			"wscript.exe bin\\background.vbs bin\\caller.bat stop " + DbControllerCommandWorker.DB_COMMAND_STOP + "\r\n";
	}

	private String getGUIScript() {
		return 
			"@ECHO OFF" + "\r\n" +
			"wscript.exe bin\\background.vbs bin\\caller.bat control " + DbControllerCommandWorker.DB_COMMAND_SHOW_CONTROLLER + "\r\n";
	}
}
