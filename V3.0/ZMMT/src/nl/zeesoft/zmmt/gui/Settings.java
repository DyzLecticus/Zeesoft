package nl.zeesoft.zmmt.gui;

import java.io.File;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.synthesizer.SynthesizerConfiguration;

public class Settings {
	public static final String			EXTENSION_COMPOSITION		= ".ztc";
	public static final String			EXTENSION_PATCH				= ".ztp";
	public static final String			EXTENSION_MIDI				= ".mid";
			
	private static final String			WORK_DIR					= "ZeeTracker";
	private static final String			SETTINGS_JSON				= "settings.json";
	
	private	String						composer					= "";
	
	private int 						defaultBeatsPerMinute		= 128;
	private int							defaultBeatsPerBar			= 4;
	private int							defaultStepsPerBeat			= 8;
	private int							defaultBarsPerPattern		= 4;
	
	private String						workingCompositionFileName	= "";
	private String						workingTab					= "";
	private String						workingInstrument			= "";
	private int							workingCompositionPattern	= 0;

	private String						customSoundFontFileName		= "";

	private String						customFontName				= "Courier New";
	private boolean						customFontBold				= false;
	private int							customFontSize				= 14;
	
	private SortedMap<String,Integer>	keyCodeNoteNumbers			= new TreeMap<String,Integer>();
	
	private SynthesizerConfiguration	synthesizerConfiguration	= null;

	public Settings() {
		synthesizerConfiguration = new SynthesizerConfiguration();
		keyCodeNoteNumbers.put("Q",36);
		keyCodeNoteNumbers.put("2",37);
		keyCodeNoteNumbers.put("W",38);
		keyCodeNoteNumbers.put("3",39);
		keyCodeNoteNumbers.put("E",40);
		keyCodeNoteNumbers.put("R",41);
		keyCodeNoteNumbers.put("5",42);
		keyCodeNoteNumbers.put("T",43);
		keyCodeNoteNumbers.put("6",44);
		keyCodeNoteNumbers.put("Y",45);
		keyCodeNoteNumbers.put("7",46);
		keyCodeNoteNumbers.put("U",47);
		keyCodeNoteNumbers.put("I",48);
		keyCodeNoteNumbers.put("9",49);
		keyCodeNoteNumbers.put("O",50);
		keyCodeNoteNumbers.put("0",51);
		keyCodeNoteNumbers.put("P",52);
		keyCodeNoteNumbers.put("Z",48);
		keyCodeNoteNumbers.put("S",49);
		keyCodeNoteNumbers.put("X",50);
		keyCodeNoteNumbers.put("D",51);
		keyCodeNoteNumbers.put("C",52);
		keyCodeNoteNumbers.put("V",53);
		keyCodeNoteNumbers.put("G",54);
		keyCodeNoteNumbers.put("B",55);
		keyCodeNoteNumbers.put("H",56);
		keyCodeNoteNumbers.put("N",57);
		keyCodeNoteNumbers.put("J",58);
		keyCodeNoteNumbers.put("M",59);
	}

	public String toFile() {
		String err = "";
		File workDir = getWorkDir();
		if (workDir==null) {
			err = "Unable to create work directory: " + getWorkDirName();
		} else {
			err = toJson().toStringBuilderReadFormat().toFile(getWorkDirName() + "/" + SETTINGS_JSON);
		}
		return err;
	}

	public String fromFile() {
		String err = "";
		File workDir = getWorkDir();
		if (workDir==null) {
			err = "Unable to create work directory: " + getWorkDirName();
		} else {
			JsFile json = new JsFile();
			err = json.fromFile(getWorkDirName() + "/" + SETTINGS_JSON);
			if (err.length()==0) {
				fromJson(json);
			}
		}
		return err;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("composer",composer,true));
		json.rootElement.children.add(new JsElem("defaultBeatsPerMinute","" + defaultBeatsPerMinute));
		json.rootElement.children.add(new JsElem("defaultBeatsPerBar","" + defaultBeatsPerBar));
		json.rootElement.children.add(new JsElem("defaultStepsPerBeat","" + defaultStepsPerBeat));
		json.rootElement.children.add(new JsElem("defaultBarsPerPattern","" + defaultBarsPerPattern));
		json.rootElement.children.add(new JsElem("workingTab",workingTab,true));
		json.rootElement.children.add(new JsElem("workingInstrument",workingInstrument,true));
		json.rootElement.children.add(new JsElem("workingCompositionFileName",workingCompositionFileName,true));
		json.rootElement.children.add(new JsElem("workingCompositionPattern","" + workingCompositionPattern));
		json.rootElement.children.add(new JsElem("customSoundFontFileName",customSoundFontFileName,true));
		json.rootElement.children.add(new JsElem("customFontName",customFontName,true));
		json.rootElement.children.add(new JsElem("customFontBold","" + customFontBold));
		json.rootElement.children.add(new JsElem("customFontSize","" + customFontSize));
		JsElem kcnnsElem = new JsElem("keyCodeNoteNumbers");
		for (Entry<String,Integer> entry: keyCodeNoteNumbers.entrySet()) {
			kcnnsElem.children.add(new JsElem(entry.getKey(),entry.getValue().toString()));
		}
		json.rootElement.children.add(kcnnsElem);
		JsFile conf = synthesizerConfiguration.toJson();
		JsElem confElem = new JsElem("synthesizerConfiguration");
		for (JsElem conElem: conf.rootElement.children) {
			confElem.children.add(conElem);
		}
		json.rootElement.children.add(confElem);
		return json;
	}

	public void fromJson(JsFile json) {
		composer = "";
		keyCodeNoteNumbers.clear();
		synthesizerConfiguration = new SynthesizerConfiguration();
		if (json.rootElement!=null) {
			for (JsElem elem: json.rootElement.children) {
				if (elem.name.equals("composer")) {
					composer = elem.value.toString();
				} else if (elem.name.equals("defaultBeatsPerMinute")) {
					defaultBeatsPerMinute = Integer.parseInt(elem.value.toString());
				} else if (elem.name.equals("defaultBeatsPerBar")) {
					defaultBeatsPerBar = Integer.parseInt(elem.value.toString());
				} else if (elem.name.equals("defaultStepsPerBeat")) {
					defaultStepsPerBeat = Integer.parseInt(elem.value.toString());
				} else if (elem.name.equals("defaultBarsPerPattern")) {
					defaultBarsPerPattern = Integer.parseInt(elem.value.toString());
				} else if (elem.name.equals("workingTab")) {
					workingTab = elem.value.toString();
				} else if (elem.name.equals("workingInstrument")) {
					workingInstrument = elem.value.toString();
				} else if (elem.name.equals("workingCompositionFileName")) {
					workingCompositionFileName = elem.value.toString();
				} else if (elem.name.equals("workingCompositionPattern")) {
					workingCompositionPattern = Integer.parseInt(elem.value.toString());
				} else if (elem.name.equals("customSoundFontFileName")) {
					customSoundFontFileName = elem.value.toString();
				} else if (elem.name.equals("customFontName")) {
					customFontName = elem.value.toString();
				} else if (elem.name.equals("customFontBold")) {
					customFontBold = Boolean.parseBoolean(elem.value.toString());
				} else if (elem.name.equals("customFontSize")) {
					customFontSize = Integer.parseInt(elem.value.toString());
				} else if (elem.name.equals("keyCodeNoteNumbers")) {
					for (JsElem kElem: elem.children) {
						String keyCode = kElem.name;
						int noteNumber = Integer.parseInt(kElem.value.toString());
						keyCodeNoteNumbers.put(keyCode,noteNumber);
					}
				} else if (elem.name.equals("synthesizerConfiguration")) {
					JsFile conf = new JsFile();
					conf.rootElement = elem;
					synthesizerConfiguration.fromJson(conf);
				}
			}
		}
	}

	public Composition getNewComposition() {
		Composition composition = new Composition();
		composition.setComposer(composer);
		composition.setBeatsPerMinute(defaultBeatsPerMinute);
		composition.setBeatsPerBar(defaultBeatsPerBar);
		composition.setStepsPerBeat(defaultStepsPerBeat);
		composition.setBarsPerPattern(defaultBarsPerPattern);
		composition.setSynthesizerConfiguration(synthesizerConfiguration.copy());
		return composition;
	}
	
	public String getComposer() {
		return composer;
	}
	
	public void setComposer(String composer) {
		this.composer = composer;
	}

	public int getDefaultBeatsPerMinute() {
		return defaultBeatsPerMinute;
	}

	public void setDefaultBeatsPerMinute(int defaultBeatsPerMinute) {
		this.defaultBeatsPerMinute = defaultBeatsPerMinute;
	}

	public int getDefaultBeatsPerBar() {
		return defaultBeatsPerBar;
	}

	public void setDefaultBeatsPerBar(int defaultBeatsPerBar) {
		this.defaultBeatsPerBar = defaultBeatsPerBar;
	}

	public int getDefaultStepsPerBeat() {
		return defaultStepsPerBeat;
	}

	public void setDefaultStepsPerBeat(int defaultStepsPerBeat) {
		this.defaultStepsPerBeat = defaultStepsPerBeat;
	}

	public int getDefaultBarsPerPattern() {
		return defaultBarsPerPattern;
	}

	public void setDefaultBarsPerPattern(int defaultBarsPerPattern) {
		this.defaultBarsPerPattern = defaultBarsPerPattern;
	}

	public String getWorkingTab() {
		return workingTab;
	}

	public void setWorkingTab(String workingTab) {
		this.workingTab = workingTab;
	}

	public String getWorkingInstrument() {
		return workingInstrument;
	}

	public void setWorkingInstrument(String workingInstrument) {
		this.workingInstrument = workingInstrument;
	}

	public String getWorkingCompositionFileName() {
		return workingCompositionFileName;
	}

	public void setWorkingCompositionFileName(String workingCompositionFileName) {
		this.workingCompositionFileName = workingCompositionFileName;
	}

	public int getWorkingCompositionPattern() {
		return workingCompositionPattern;
	}

	public void setWorkingCompositionPattern(int workingCompositionPattern) {
		this.workingCompositionPattern = workingCompositionPattern;
	}

	public String getCustomSoundFontFileName() {
		return customSoundFontFileName;
	}

	public void setCustomSoundFontFileName(String customSoundFontFileName) {
		this.customSoundFontFileName = customSoundFontFileName;
	}

	public String getCustomFontName() {
		return customFontName;
	}

	public void setCustomFontName(String customFontName) {
		this.customFontName = customFontName;
	}

	public boolean isCustomFontBold() {
		return customFontBold;
	}

	public void setCustomFontBold(boolean customFontBold) {
		this.customFontBold = customFontBold;
	}

	public int getCustomFontSize() {
		return customFontSize;
	}

	public void setCustomFontSize(int customFontSize) {
		this.customFontSize = customFontSize;
	}

	public SortedMap<String,Integer> getKeyCodeNoteNumbers() {
		return keyCodeNoteNumbers;
	}

	public SynthesizerConfiguration getSynthesizerConfiguration() {
		return synthesizerConfiguration;
	}

	public void setSynthesizerConfiguration(SynthesizerConfiguration synthesizerConfiguration) {
		this.synthesizerConfiguration = synthesizerConfiguration;
	}
	
	public String getWorkDirName() {
		String dir = System.getProperty("user.home");
		if (dir.length()>0) {
			dir = dir + "/" + WORK_DIR;
		} else {
			dir = WORK_DIR;
		}
		return dir;
	}

	private File getWorkDir() {
		File r = new File(getWorkDirName());
		if (!r.exists()) {
			r.mkdirs();
		}
		if (!r.exists()) {
			r = null;
		}
		return r;
	}
}
