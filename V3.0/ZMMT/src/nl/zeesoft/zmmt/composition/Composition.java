package nl.zeesoft.zmmt.composition;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zmmt.syntesizer.SynthesizerConfiguration;

public class Composition {
	private String						composer						= "";
	private String						name							= "";
	private int 						beatsPerMinute					= 128;
	private int							beatsPerBar						= 4;
	private int							stepsPerBeat					= 8;
	
	private SynthesizerConfiguration	synthesizerConfiguration		= null;
	
	private List<Pattern>				patterns						= new ArrayList<Pattern>();
	
	public Composition() {
		synthesizerConfiguration = new SynthesizerConfiguration();
	}
	
	public Composition copy() {
		Composition copy = new Composition();
		copy.fromJson(toJson());
		return copy;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("composer",composer,true));
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("beatsPerMinute","" + beatsPerMinute));
		json.rootElement.children.add(new JsElem("beatsPerBar","" + beatsPerBar));
		json.rootElement.children.add(new JsElem("stepsPerBeat","" + stepsPerBeat));
		JsFile conf = synthesizerConfiguration.toJson();
		JsElem confElem = new JsElem("synthesizerConfiguration");
		for (JsElem conElem: conf.rootElement.children) {
			confElem.children.add(conElem);
		}
		json.rootElement.children.add(confElem);
		if (patterns.size()>0) {
			JsElem ptnsElem = new JsElem("patterns",true);
			json.rootElement.children.add(ptnsElem);
			for (Pattern pattern: patterns) {
				ptnsElem.children.add(pattern.toJson().rootElement);
			}
		}
		return json;
	}

	public void fromJson(JsFile json) {
		patterns.clear();
		for (JsElem elem: json.rootElement.children) {
			if (elem.name.equals("composer")) {
				composer = elem.value.toString();
			} else if (elem.name.equals("name")) {
				name = elem.value.toString();
			} else if (elem.name.equals("beatsPerMinute")) {
				beatsPerMinute = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("beatsPerBar")) {
				beatsPerBar = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("stepsPerBeat")) {
				stepsPerBeat = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("synthesizerConfiguration")) {
				JsFile conf = new JsFile();
				conf.rootElement = elem;
				synthesizerConfiguration.fromJson(conf);
			} else if (elem.name.equals("patterns")) {
				for (JsElem ptnElem: elem.children) {
					JsFile ptn = new JsFile();
					ptn.rootElement = ptnElem;
					Pattern pattern = new Pattern();
					pattern.fromJson(ptn);
					patterns.add(pattern);
				}
			}
		}
	}

	public int getStepsPerBar() {
		return (beatsPerBar * stepsPerBeat);
	}
	
	public long getMsForStep(int number) {
		return (60000 / beatsPerMinute) / stepsPerBeat;
	}

	public int getStepsForBars(int bars) {
		return (bars * getStepsPerBar());
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBeatsPerMinute() {
		return beatsPerMinute;
	}

	public void setBeatsPerMinute(int beatsPerMinute) {
		this.beatsPerMinute = beatsPerMinute;
	}

	public int getBeatsPerBar() {
		return beatsPerBar;
	}

	public void setBeatsPerBar(int beatsPerBar) {
		this.beatsPerBar = beatsPerBar;
	}

	public int getStepsPerBeat() {
		return stepsPerBeat;
	}

	public void setStepsPerBeat(int stepsPerBeat) {
		this.stepsPerBeat = stepsPerBeat;
	}

	public SynthesizerConfiguration getSynthesizerConfiguration() {
		return synthesizerConfiguration;
	}

	public void setSynthesizerConfiguration(SynthesizerConfiguration synthesizerConfiguration) {
		this.synthesizerConfiguration = synthesizerConfiguration;
	}

	public List<Pattern> getPatterns() {
		return patterns;
	}
}
