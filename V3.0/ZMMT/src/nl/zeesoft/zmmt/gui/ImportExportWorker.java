package nl.zeesoft.zmmt.gui;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.sequencer.CompositionToSequenceConvertor;

public class ImportExportWorker extends Worker {
	private static final String		INITIALIZE				= "INITIALIZE";
	private static final String		LOAD_COMPOSITION		= "LOAD_COMPOSITION";
	private static final String		SAVE_COMPOSITION		= "SAVE_COMPOSITION";
	private static final String		LOAD_DEMO_COMPOSITION	= "LOAD_DEMO_COMPOSITION";
	
	private Controller				controller				= null;

	private String					workingDirName			= "";
	private boolean					initializing			= false;
	private JFileChooser			fileChooser				= null;
	
	private String					action					= "";
	private	Object					actionObject			= null;
	private File					actionFile				= null;
	
	private FileFilter				compositionFilter		= null;
	private FileFilter				midiFilter				= null;

	public ImportExportWorker(Messenger msgr, WorkerUnion union,Controller controller) {
		super(msgr, union);
		setSleep(10);
		this.controller = controller;
		
		compositionFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				boolean accept = false;
				if (file.isDirectory() || file.getAbsolutePath().toLowerCase().endsWith(Settings.EXTENSION_COMPOSITION)) {
					accept = true;
				}
				return accept;
			}
			@Override
			public String getDescription() {
				return "ZeeTracker Composition (*.ztc)";
			}
		};
		midiFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				boolean accept = false;
				if (file.isDirectory() || file.getAbsolutePath().toLowerCase().endsWith(Settings.EXTENSION_MIDI)) {
					accept = true;
				}
				return accept;
			}
			@Override
			public String getDescription() {
				return "Midi file (*.mid)";
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

	public void loadDemoComposition(Settings settings) {
		handleAction(LOAD_DEMO_COMPOSITION,settings,null);
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
		} else if (
			action.equals(LOAD_COMPOSITION) || 
			action.equals(SAVE_COMPOSITION)
			) {
			boolean selected = false;
			File file = actionFile;
			if (file==null) {
				file = chooseFile(action);
				selected = true;
			}
			if (file!=null) {
				if (action.equals(LOAD_COMPOSITION)) {
					controller.setBusy(this,"Loading composition",file.getAbsolutePath());
					JsFile json = new JsFile();
					err = json.fromFile(file.getAbsolutePath());
					if (err.length()==0) {
						Composition comp = new Composition();
						comp.fromJson(json);
						controller.loadedComposition(file,comp);
					}
					controller.setDone(this);
				} else if (action.equals(SAVE_COMPOSITION)) {
					if ((!file.getName().endsWith(Settings.EXTENSION_COMPOSITION)) &&
						(!file.getName().endsWith(Settings.EXTENSION_MIDI))
						) {
						if (fileChooser.getFileFilter()==compositionFilter) {
							file = new File(file.getAbsolutePath() + Settings.EXTENSION_COMPOSITION);
						} else if (fileChooser.getFileFilter()==midiFilter) {
							file = new File(file.getAbsolutePath() + Settings.EXTENSION_MIDI);
						}
					}
					boolean confirmed = true;
					if (file.exists() && selected) {
						confirmed = controller.showConfirmMessage("Are you sure you want to overwrite the selected file?");
					}
					if (confirmed) {
						controller.setBusy(this,"Saving composition",file.getAbsolutePath());
						if (file.getName().endsWith(Settings.EXTENSION_COMPOSITION)) {
							err = ((Composition) actionObject).toJson().toFile(file.getAbsolutePath(),true);
							if (err.length()==0) {
								controller.savedComposition(file,(Composition) actionObject);
							}
						} else if (file.getName().endsWith(Settings.EXTENSION_MIDI)) {
							CompositionToSequenceConvertor convertor = new CompositionToSequenceConvertor((Composition) actionObject);
							Sequence s = convertor.getSequence(false);
		                    try {
		                        int[] fileTypes = MidiSystem.getMidiFileTypes(s);
		                        if (fileTypes.length == 0) {
		                            err = "The current MIDI system does not support this file type";
		                        } else {
									MidiSystem.write(s,fileTypes[0],file);
		                        }
							} catch (IOException e) {
								err = "Failed to write MIDI file: " + file.getAbsolutePath();
							} 
						}
						controller.setDone(this);
					}
				}
			}
		} else if (action.equals(LOAD_DEMO_COMPOSITION)) {
			controller.setBusy(this,"Loading demo composition","");
			Settings settings = (Settings) actionObject;
			Composition composition = settings.getNewComposition(true);
			controller.loadedComposition(null,composition);
			controller.setDone(this);
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
				fileChooser.removeChoosableFileFilter(midiFilter);
				file = controller.chooseFile(fileChooser,"Load composition");
				file = getFileWithExtension(file,Settings.EXTENSION_COMPOSITION);
			} else if (action.equals(SAVE_COMPOSITION)) {
				fileChooser.setFileFilter(compositionFilter);
				fileChooser.addChoosableFileFilter(midiFilter);
				file = controller.chooseFile(fileChooser,"Save composition");
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
