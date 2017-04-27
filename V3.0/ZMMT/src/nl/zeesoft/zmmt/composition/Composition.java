package nl.zeesoft.zmmt.composition;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zmmt.syntesizer.Synthesizer;

public class Composition {
	private String		composer		= "";
	private String		name			= "";
	private int 		beatsPerMinute	= 120;
	private int			beatsPerBar		= 4;
	private int			stepsPerBeat	= 8;
	
	private Synthesizer	synthesizer		= null;
	
	public Composition() {
		synthesizer = new Synthesizer();
		synthesizer.initialize();
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("composer",composer,true));
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("beatsPerMinute","" + beatsPerMinute));
		json.rootElement.children.add(new JsElem("beatsPerBar","" + beatsPerBar));
		json.rootElement.children.add(new JsElem("stepsPerBeat","" + stepsPerBeat));
		return json;
	}

	public void fromJson(JsFile json) {
		for (JsElem cElem: json.rootElement.children) {
			if (cElem.name.equals("composer")) {
				composer = cElem.value.toString();
			} else if (cElem.name.equals("name")) {
				name = cElem.value.toString();
			} else if (cElem.name.equals("beatsPerMinute")) {
				beatsPerMinute = Integer.parseInt(cElem.value.toString());
			} else if (cElem.name.equals("beatsPerBar")) {
				beatsPerBar = Integer.parseInt(cElem.value.toString());
			} else if (cElem.name.equals("stepsPerBeat")) {
				stepsPerBeat = Integer.parseInt(cElem.value.toString());
			}
		}
	}

	public int getStepsPerBar() {
		return (beatsPerBar * stepsPerBeat);
	}
	
	public long getMsForStep(int number) {
		return (60000 / beatsPerMinute) / stepsPerBeat;
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

	public Synthesizer getSynthesizer() {
		return synthesizer;
	}
}
