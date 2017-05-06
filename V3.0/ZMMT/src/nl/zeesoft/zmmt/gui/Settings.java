package nl.zeesoft.zmmt.gui;

import java.io.File;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.syntesizer.SynthesizerConfiguration;

public class Settings {
	private static final String			WORKDIR						= "ZeeTracker";
	private static final String			SETTINGS_JSON				= "settings.json";
	
	private	String						composer					= "";
	
	private int 						defaultBeatsPerMinute		= 128;
	private int							defaultBeatsPerBar			= 4;
	private int							defaultStepsPerBeat			= 8;
	private int							defaultPatternBars			= 4;
	
	private String						workingCompositionFileName	= "";
	private String						workingTab					= "";
	private String						workingInstrument			= "";
	private String						workingPattern				= "";
	
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
		err = toJson().toStringBuilderReadFormat().toFile(getWorkDirName() + "/" + SETTINGS_JSON);
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
			fromJson(json);
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
		json.rootElement.children.add(new JsElem("defaultPatternBars","" + defaultPatternBars));
		json.rootElement.children.add(new JsElem("workingCompositionFileName",workingCompositionFileName,true));
		json.rootElement.children.add(new JsElem("workingTab",workingTab,true));
		json.rootElement.children.add(new JsElem("workingInstrument",workingInstrument,true));
		json.rootElement.children.add(new JsElem("workingPattern",workingPattern,true));
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
				} else if (elem.name.equals("defaultPatternBars")) {
					defaultPatternBars = Integer.parseInt(elem.value.toString());
				} else if (elem.name.equals("workingCompositionFileName")) {
					workingCompositionFileName = elem.value.toString();
				} else if (elem.name.equals("workingTab")) {
					workingTab = elem.value.toString();
				} else if (elem.name.equals("workingInstrument")) {
					workingInstrument = elem.value.toString();
				} else if (elem.name.equals("workingPattern")) {
					workingPattern = elem.value.toString();
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

	public int getDefaultPatternBars() {
		return defaultPatternBars;
	}

	public void setDefaultPatternBars(int defaultPatternBars) {
		this.defaultPatternBars = defaultPatternBars;
	}

	public String getWorkingCompositionFileName() {
		return workingCompositionFileName;
	}

	public void setWorkingCompositionFileName(String workingCompositionFileName) {
		this.workingCompositionFileName = workingCompositionFileName;
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

	public String getWorkingPattern() {
		return workingPattern;
	}

	public void setWorkingPattern(String workingPattern) {
		this.workingPattern = workingPattern;
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

	private String getWorkDirName() {
		String dir = System.getProperty("user.home");
		if (dir.length()>0) {
			dir = dir + "/" + WORKDIR;
		} else {
			dir = WORKDIR;
		}
		return dir;
	}
}
