package nl.zeesoft.zmmt.composition;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Pattern {
	private int				pattern		= 1;
	private int 			bars		= 8;
	private List<Step>		steps		= new ArrayList<Step>();
	
	public Pattern copy() {
		Pattern copy = new Pattern();
		copy.fromJson(toJson());
		return copy;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("pattern","" + pattern));
		json.rootElement.children.add(new JsElem("bars","" + bars));
		if (steps.size()>0) {
			JsElem stepsElem = new JsElem("steps",true);
			json.rootElement.children.add(stepsElem);
			for (Step step: steps) {
				JsElem stepElem = new JsElem();
				stepsElem.children.add(stepElem);
				stepElem.children.add(new JsElem("instrument",step.instrument,true));
				stepElem.children.add(new JsElem("track","" + step.track));
				stepElem.children.add(new JsElem("step","" + step.step));
				stepElem.children.add(new JsElem("length","" + step.step));
				stepElem.children.add(new JsElem("note","" + step.note));
				stepElem.children.add(new JsElem("accent","" + step.accent));
			}
		}
		return json;
	}

	public void fromJson(JsFile json) {
		steps.clear();
		for (JsElem elem: json.rootElement.children) {
			if (elem.name.equals("pattern")) {
				pattern = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("bars")) {
				bars = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("steps")) {
				for (JsElem stepElem: elem.children) {
					Step step = new Step();
					for (JsElem valElem: stepElem.children) {
						if (valElem.name.equals("instrument")) {
							step.instrument = valElem.value.toString();
						} else if (valElem.name.equals("track")) {
							step.track = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("step")) {
							step.step = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("length")) {
							step.length = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("note")) {
							step.note = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("accent")) {
							step.accent = Boolean.parseBoolean(valElem.value.toString());
						}
					}
					steps.add(step);
				}
			}
		}
	}

	public int getPattern() {
		return pattern;
	}

	public void setPattern(int pattern) {
		this.pattern = pattern;
	}

	public int getBars() {
		return bars;
	}

	public void setBars(int bars) {
		this.bars = bars;
	}

	public List<Step> getSteps() {
		return steps;
	}
}
