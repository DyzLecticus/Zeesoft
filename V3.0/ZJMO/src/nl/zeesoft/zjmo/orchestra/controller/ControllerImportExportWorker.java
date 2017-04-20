package nl.zeesoft.zjmo.orchestra.controller;

import java.io.File;

import javax.swing.JFileChooser;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraGenerator;

public class ControllerImportExportWorker extends Worker {
	public static final String		INITIALIZE			= "INITIALIZE";
	public static final String		IMPORT_CHANGES		= "IMPORT_CHANGES";
	public static final String		EXPORT_CHANGES		= "EXPORT_CHANGES";
	public static final String		GENERATE			= "GENERATE";

	private OrchestraController 	controller 			= null;
	private JFileChooser			fileChooser 		= null;

	private String					action				= "";
	private	Orchestra				orchestraUpdate		= null;

	public ControllerImportExportWorker(Messenger msgr, WorkerUnion union,OrchestraController controller) {
		super(msgr, union);
		setSleep(1000);
		this.controller = controller;
	}

	public void initialize() {
		handleAction(INITIALIZE,null);
	}

	public void handleAction(String action,Orchestra orchestraUpdate) {
		this.action = action;
		if (orchestraUpdate!=null) {
			this.orchestraUpdate = orchestraUpdate.getCopy(false);
		} else {
			this.orchestraUpdate = null;
		}
		start();
	}
	
	@Override
	public void whileWorking() {
		String err = "";
		if (action.equals(INITIALIZE)) {
			if (fileChooser==null) {
				fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("."));
			}
		} else {
			File file = chooseFile(action);
			if (file!=null) {
				if (action.equals(IMPORT_CHANGES)) {
					JsFile json = new JsFile();
					ZStringBuilder oriJson = orchestraUpdate.toJson(false).toStringBuilderReadFormat();
					ZStringBuilder newJson = null;
					err = json.fromFile(file.getAbsolutePath());
					if (err.length()==0) {
						orchestraUpdate.fromJson(json);
						newJson = orchestraUpdate.toJson(false).toStringBuilderReadFormat();
						if (newJson.equals(oriJson)) {
							err = "No changes to import";
						}
					}
					if (err.length()==0) {
						controller.importedOrchestraUpdate(orchestraUpdate);
					}
				} else if (action.equals(EXPORT_CHANGES)) {
					boolean confirmed = true;
					if (file.exists()) {
						confirmed = controller.showConfirmMessage("Are you sure you want to overwrite the selected file?");
					}
					if (confirmed) {
						orchestraUpdate.toJson(false).toFile(file.getAbsolutePath(),true);
					}
				} else if (action.equals(GENERATE)) {
					boolean confirmed = true;
					File check = new File(file.getAbsolutePath() + "/orchestra.json");
					if (check.exists()) {
						confirmed = controller.showConfirmMessage("Are you sure you want to overwrite the selected orchestra?");
					}
					if (confirmed) {
						OrchestraGenerator generator = orchestraUpdate.getNewGenerator();
						err = generator.generate(orchestraUpdate,file);
					}
				}
			}
		}
		if (err.length()>0) {
			controller.showErrorMessage(err);
		} else {
			controller.completedImportExportAction(orchestraUpdate,action);
		}
		stop();
	}
	
	protected File chooseFile(String action) {
		File file = null;
		String title = "";
		if (action.equals(IMPORT_CHANGES)) {
			title = "Import";
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		} else if (action.equals(EXPORT_CHANGES)) {
			title = "Export";
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		} else if (action.equals(GENERATE)) {
			title = "Generate";
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		int choice = fileChooser.showDialog(controller.getMainFrame(),title);
		if (choice==JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		return file;
	}
}
