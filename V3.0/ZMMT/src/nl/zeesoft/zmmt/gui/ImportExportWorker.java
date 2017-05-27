package nl.zeesoft.zmmt.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;

public class ImportExportWorker extends Worker {
	private static final String		INITIALIZE			= "INITIALIZE";
	private static final String		LOAD_COMPOSITION	= "LOAD_COMPOSITION";
	private static final String		SAVE_COMPOSITION	= "SAVE_COMPOSITION";
	
	private Controller				controller			= null;

	private String					workingDirName		= "";
	private boolean					initializing		= false;
	private JFileChooser			fileChooser			= null;
	
	private String					action				= "";
	private	Object					actionObject		= null;
	private File					actionFile			= null;
	
	private FileFilter				compositionFilter	= null;

	public ImportExportWorker(Messenger msgr, WorkerUnion union,Controller controller) {
		super(msgr, union);
		setSleep(10);
		this.controller = controller;
		
		compositionFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				boolean accept = false;
				if (file.getAbsolutePath().toLowerCase().endsWith(Settings.EXTENSION_COMPOSITION)) {
					accept = true;
				}
				return accept;
			}
			@Override
			public String getDescription() {
				return "ZeeTracker Compositions (*.ztc)";
			}
		};
	}

	public void loadCompositionAndInitialize(File file,String workingDirName) {
		this.workingDirName = workingDirName;
		handleAction(LOAD_COMPOSITION,null,file);
	}

	public void initialize(String workingDirName) {
		boolean start = false;
		lockMe(this);
		if (fileChooser==null && !initializing) {
			initializing = true;
			action = INITIALIZE;
			actionObject = null;
			actionFile = new File(workingDirName);
			start = true;
		}
		unlockMe(this);
		if (start) {
			start();
		}
	}

	public void loadComposition() {
		handleAction(LOAD_COMPOSITION,null,null);
	}

	public void saveComposition(Composition composition,File file) {
		handleAction(SAVE_COMPOSITION,composition,file);
	}
	
	@Override
	public void whileWorking() {
		String err = "";
		if (action.equals(INITIALIZE)) {
			lockMe(this);
			if (fileChooser==null) {
				fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				if (actionFile!=null) {
					fileChooser.setCurrentDirectory(actionFile);
				}
				initializing = false;
			}
			unlockMe(this);
		} else {
			boolean selected = false;
			File file = actionFile;
			if (file==null) {
				file = chooseFile(action);
				selected = true;
			}
			if (file!=null) {
				if (action.equals(LOAD_COMPOSITION)) {
					controller.setEnabled(false);
					JsFile json = new JsFile();
					err = json.fromFile(file.getAbsolutePath());
					if (err.length()==0) {
						Composition comp = new Composition();
						comp.fromJson(json);
						actionObject = comp;
						controller.loadedComposition(file,(Composition) actionObject);
					}
					controller.setEnabled(true);
				} else if (action.equals(SAVE_COMPOSITION)) {
					if (!file.getName().endsWith(Settings.EXTENSION_COMPOSITION)) {
						file = new File(file.getAbsolutePath() + Settings.EXTENSION_COMPOSITION);
					}
					boolean confirmed = true;
					if (file.exists() && selected) {
						confirmed = controller.showConfirmMessage("Are you sure you want to overwrite the selected file?");
					}
					if (confirmed) {
						controller.setEnabled(false);
						err = ((Composition) actionObject).toJson().toFile(file.getAbsolutePath(),true);
						if (err.length()==0) {
							controller.savedComposition(file,(Composition) actionObject);
						}
						controller.setEnabled(true);
					}
				}
			}
		}
		if (err.length()>0) {
			controller.showErrorMessage(this,err);
		}
		if (workingDirName.length()>0) {
			initializing = true;
			action = INITIALIZE;
			actionObject = null;
			actionFile = new File(workingDirName);
			workingDirName = "";
		} else {
			stop();
		}
	}
	
	protected File chooseFile(String action) {
		File file = null;
		lockMe(this);
		if (fileChooser!=null) {
			if (action.equals(LOAD_COMPOSITION)) {
				fileChooser.setFileFilter(compositionFilter);
				file = controller.chooseFile(fileChooser,"Load composition");
				getFileWithExtension(file,Settings.EXTENSION_COMPOSITION);
			} else if (action.equals(SAVE_COMPOSITION)) {
				fileChooser.setFileFilter(compositionFilter);
				file = controller.chooseFile(fileChooser,"Save composition");
				getFileWithExtension(file,Settings.EXTENSION_COMPOSITION);
			}
		}
		unlockMe(this);
		return file;
	}

	private File getFileWithExtension(File file,String extension) {
		if (file!=null && !file.getAbsolutePath().toLowerCase().endsWith(extension)) {
			String fileName = file.getAbsolutePath() + extension;
			file = new File(fileName);
		}
		return file;
	}
	
	private void handleAction(String action,Object actionObject,File actionFile) {
		this.action = action;
		this.actionObject = actionObject;
		this.actionFile = actionFile;
		start();
	}
}
