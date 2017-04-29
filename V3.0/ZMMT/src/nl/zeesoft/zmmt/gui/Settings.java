package nl.zeesoft.zmmt.gui;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.syntesizer.SynthesizerConfiguration;

public class Settings {
	private String						fileName					= "settings.json";
	private	String						composer					= "";
	private SynthesizerConfiguration	synthesizerConfiguration	= null;

	public Settings() {
		synthesizerConfiguration = new SynthesizerConfiguration();
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
		synthesizerConfiguration = new SynthesizerConfiguration();
		if (json.rootElement!=null) {
			for (JsElem elem: json.rootElement.children) {
				if (elem.name.equals("composer")) {
					composer = elem.value.toString();
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
}
