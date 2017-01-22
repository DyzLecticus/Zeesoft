package nl.zeesoft.zadf;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.ZODBController;
import nl.zeesoft.zodb.client.ClConfig;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.FileObject;

public class ZADFController extends ZODBController {
	private static final String ACTION_START_MAIN_GUI 		= "START_GUI";
	private static final String ACTION_START_CONTROL_GUI 	= "START_CONTROL";

	private static final String JAR_FILE_NAME				= "zadf.jar"; 
	
	private JFileChooser		fileChooser					= null;

	public ZADFController(String[] args) {
		super(args);
		if (getAction().equals(ACTION_START_MAIN_GUI)) {
			startMainGui();
		} else if (getAction().equals(ACTION_START_CONTROL_GUI)) {
			startControlGui();
		}
	}

	@Override
	protected void initializeDBConfig() {
		DbConfig.getInstance().setPort(5432);
		DbConfig.getInstance().setEncryptionKey(Generic.generateNewKey(1024));
		DbConfig.getInstance().setModelClassName(ZADFModel.class.getName());
	}	

	protected void initializeGuiConfig() {
		GuiConfig.getInstance().setFactoryClassName(ZADFFactory.class.getName());
	}	
	
	@Override
	protected void install() {
		super.install();
		installGui();
	}

	@Override
	protected String getJarFileName() {
		return JAR_FILE_NAME;
	}

	@Override
	protected void update(String dataDirName) {
		if (dataDirName.equals(DATA_DIR_NAME)) {
			if (fileChooser==null) {
				fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			}
			FileFilter filter = new FileFilter() {
				@Override
				public boolean accept(File file) {
					boolean returnVal = false;
					if (file.getName().endsWith("DbConfig.xml")) {
						returnVal = true;
					}
					return returnVal;
				}
				@Override
				public String getDescription() {
					return "DbConfig.xml";
				}
			}; 
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showSaveDialog(null);
			if (returnVal==JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				dataDirName = Generic.dirName(selectedFile.getParent());
				super.update(dataDirName);
			}
		} else {
			super.update(dataDirName);
		}
	}
	
	public void installGui() {
		initializeGuiConfig();
		
		File dataDir = new File(DbConfig.getInstance().getFullDataDir());
		File iconDir = new File(GuiConfig.getInstance().getFullIconDir());
		if (
			((dataDir.exists() && (dataDir.isDirectory())) || (dataDir.mkdir())) && 
			((iconDir.exists() && (iconDir.isDirectory())) || (iconDir.mkdir())) 
			) {
			DbConfig.getInstance().serialize();
			ClConfig.getInstance().serialize();
			GuiConfig.getInstance().serialize();
						
			FileObject file = new FileObject();
			file.writeFile(DbConfig.getInstance().getInstallDir() + "startGUI.bat", getStartGuiScript());
			file.writeFile(DbConfig.getInstance().getInstallDir() + "startControl.bat", getStartControlScript());

			// Client
			File clDir = new File(Generic.dirName(DbConfig.getInstance().getInstallDir() + "client/"));
			if ((clDir.exists()) && (clDir.isDirectory())) {
				String installDir = DbConfig.getInstance().getInstallDir();
				String userName = ClConfig.getInstance().getUserName();
				StringBuffer userPassword = ClConfig.getInstance().getUserPassword();
				
				DbConfig.getInstance().setInstallDir(installDir + "client/");
				ClConfig.getInstance().setUserName("");
				ClConfig.getInstance().setUserPassword(new StringBuffer());
				
				File clDataDir = new File(DbConfig.getInstance().getFullDataDir());
				File clIconDir = new File(GuiConfig.getInstance().getFullIconDir());
				File clBinDir = new File(Generic.dirName(DbConfig.getInstance().getInstallDir() + "bin"));
				File clOutDir = new File(Generic.dirName(DbConfig.getInstance().getInstallDir() + "out"));
				if (
					((clDataDir.exists() && (clDataDir.isDirectory())) || (clDataDir.mkdir())) && 
					((clIconDir.exists() && (clIconDir.isDirectory())) || (clIconDir.mkdir())) && 
					((clBinDir.exists() && (clBinDir.isDirectory())) || (clBinDir.mkdir())) &&
					((clOutDir.exists() && (clOutDir.isDirectory())) || (clOutDir.mkdir())) 
					) {
					DbConfig.getInstance().serialize();
					ClConfig.getInstance().serialize();
					GuiConfig.getInstance().serialize();
	
					file.writeFile(Generic.dirName(clBinDir.getAbsolutePath()) + "caller.bat", getCallerScript());
					file.writeFile(Generic.dirName(clBinDir.getAbsolutePath()) + "background.vbs", getBackgroundScript());
					
					file.writeFile(DbConfig.getInstance().getInstallDir() + "startGUI.bat", getStartGuiScript());
					file.writeFile(DbConfig.getInstance().getInstallDir() + "startControl.bat", getStartControlScript());
				}
				
				DbConfig.getInstance().setInstallDir(installDir);
				ClConfig.getInstance().setUserName(userName);
				ClConfig.getInstance().setUserPassword(userPassword);
			}
		}
	}
	
	protected void startMainGui() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				GuiController.getInstance().stopWorking();
			}
		});
		GuiController.getInstance().startMainGui();
	}

	protected void startControlGui() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				GuiController.getInstance().stopWorking();
			}
		});
		GuiController.getInstance().startControlGui();
	}

	protected String getStartGuiScript() {
		return
			//"cd " + DbConfig.getInstance().getInstallDir() + "\r\n" +
			"wscript.exe bin\\background.vbs bin\\caller.bat GUI START_GUI" + "\r\n";
	}

	protected String getStartControlScript() {
		return
			//"cd " + DbConfig.getInstance().getInstallDir() + "\r\n" +
			"wscript.exe bin\\background.vbs bin\\caller.bat Control START_CONTROL" + "\r\n";
	}
}
