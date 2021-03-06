package nl.zeesoft.zmmt.composition;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zmmt.synthesizer.SynthesizerConfiguration;

public class Composition {
	public static final int				TRACKS							= 32;
	public static final int				RESOLUTION						= 960;
	
	private String						composer						= "";
	private String						name							= "";

	private int 						beatsPerMinute					= 128;
	private int							beatsPerBar						= 4;
	private int							stepsPerBeat					= 8;
	private int							barsPerPattern					= 4;
	
	private SynthesizerConfiguration	synthesizerConfiguration		= null;
	
	private List<Pattern>				patterns						= new ArrayList<Pattern>();

	private List<Integer>				sequence						= new ArrayList<Integer>();

	public Composition() {
		synthesizerConfiguration = new SynthesizerConfiguration();
	}
	
	public Composition copy() {
		Composition copy = new Composition();
		copy.setComposer(composer);
		copy.setName(name);
		copy.setBeatsPerMinute(beatsPerMinute);
		copy.setBeatsPerBar(beatsPerBar);
		copy.setStepsPerBeat(stepsPerBeat);
		copy.setBarsPerPattern(barsPerPattern);
		copy.setSynthesizerConfiguration(synthesizerConfiguration.copy());
		for (Pattern pat: patterns) {
			copy.getPatterns().add(pat.copy());
		}
		for (Integer num: sequence) {
			copy.getSequence().add(new Integer(num));
		}
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
		json.rootElement.children.add(new JsElem("barsPerPattern","" + barsPerPattern));
		JsFile conf = synthesizerConfiguration.toJson();
		JsElem confElem = new JsElem("instruments");
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
		if (sequence.size()>0) {
			JsElem seqElem = new JsElem("sequence");
			json.rootElement.children.add(seqElem);
			int i = 0;
			for (Integer num: sequence) {
				seqElem.children.add(new JsElem("" + i,"" + num));
				i++;
			}
		}
		return json;
	}

	public void fromJson(JsFile json) {
		patterns.clear();
		sequence.clear();
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
			} else if (elem.name.equals("barsPerPattern")) {
				barsPerPattern = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("instruments")) {
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
			} else if (elem.name.equals("sequence")) {
				for (JsElem numElem: elem.children) {
					sequence.add(Integer.parseInt(numElem.value.toString()));
				}
			}
		}
	}

	public int getStepsPerBar() {
		return (beatsPerBar * stepsPerBeat);
	}
	
	public long getMsPerStep() {
		return (60000 / beatsPerMinute) / stepsPerBeat;
	}

	public int getTicksPerStep() {
		return (RESOLUTION / stepsPerBeat);
	}

	public int getStepsForPattern(Pattern p) {
		int r = getBarsPerPattern() * getStepsPerBar();
		if (p.getBars()>0) {
			r = p.getBars() * getStepsPerBar();
		}
		return r;
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

	public int getBarsPerPattern() {
		return barsPerPattern;
	}

	public void setBarsPerPattern(int barsPerPattern) {
		this.barsPerPattern = barsPerPattern;
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

	public List<Integer> getSequence() {
		return sequence;
	}

	public Pattern getPattern(int number) {
		Pattern r = null;
		for (Pattern pattern: patterns) {
			if (pattern.getNumber()==number) {
				r = pattern;
				break;
			}
		}
		return r;
	}
}
