package nl.zeesoft.zmmt.gui;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.syntesizer.SynthesizerConfiguration;

public class Settings {
	private String						fileName					= "settings.json";
	private	String						composer					= "";
	private SynthesizerConfiguration	synthesizerConfiguration	= null;
	private SortedMap<String,Integer>	keyCodeNoteNumbers			= new TreeMap<String,Integer>();

	public Settings() {
		synthesizerConfiguration = new SynthesizerConfiguration();
		keyCodeNoteNumbers.put("Q",35);
		keyCodeNoteNumbers.put("2",36);
		keyCodeNoteNumbers.put("W",37);
		keyCodeNoteNumbers.put("3",38);
		keyCodeNoteNumbers.put("E",39);
		keyCodeNoteNumbers.put("R",40);
		keyCodeNoteNumbers.put("5",41);
		keyCodeNoteNumbers.put("T",42);
		keyCodeNoteNumbers.put("6",43);
		keyCodeNoteNumbers.put("Y",44);
		keyCodeNoteNumbers.put("7",45);
		keyCodeNoteNumbers.put("U",46);
		keyCodeNoteNumbers.put("I",47);
		keyCodeNoteNumbers.put("9",48);
		keyCodeNoteNumbers.put("O",49);
		keyCodeNoteNumbers.put("0",50);
		keyCodeNoteNumbers.put("P",51);
		keyCodeNoteNumbers.put("Z",47);
		keyCodeNoteNumbers.put("S",48);
		keyCodeNoteNumbers.put("X",49);
		keyCodeNoteNumbers.put("D",50);
		keyCodeNoteNumbers.put("C",51);
		keyCodeNoteNumbers.put("V",52);
		keyCodeNoteNumbers.put("G",53);
		keyCodeNoteNumbers.put("B",54);
		keyCodeNoteNumbers.put("H",55);
		keyCodeNoteNumbers.put("N",56);
		keyCodeNoteNumbers.put("J",57);
		keyCodeNoteNumbers.put("M",58);
	}
	
	public String fromFile() {
		String err = "";
		JsFile json = new JsFile();
		err = json.fromFile(fileName);
		return err;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("composer",composer,true));
		JsElem kcnnsElem = new JsElem("keyCodeNoteNumbers");
		for (Entry<String,Integer> entry: keyCodeNoteNumbers.entrySet()) {
			JsElem kcnnElem = new JsElem("keyCodeNoteNumber");
			kcnnElem.children.add(new JsElem("keyCode",entry.getKey(),true));
			kcnnElem.children.add(new JsElem("noteNumber","" + entry.getValue()));
			kcnnsElem.children.add(kcnnElem);
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
				} else if (elem.name.equals("keyCodeNoteNumbers")) {
					for (JsElem kElem: elem.children) {
						String keyCode = kElem.getChildByName("keyCode").value.toString();
						int noteNumber = Integer.parseInt(kElem.getChildByName("noteNumber").value.toString());
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
		composition.setSynthesizerConfiguration(synthesizerConfiguration.copy());
		return composition;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getComposer() {
		return composer;
	}
	
	public void setComposer(String composer) {
		this.composer = composer;
	}

	public SynthesizerConfiguration getSynthesizerConfiguration() {
		return synthesizerConfiguration;
	}

	public void setSynthesizerConfiguration(SynthesizerConfiguration synthesizerConfiguration) {
		this.synthesizerConfiguration = synthesizerConfiguration;
	}

	public SortedMap<String, Integer> getKeyCodeMidiNotes() {
		return keyCodeNoteNumbers;
	}
}
