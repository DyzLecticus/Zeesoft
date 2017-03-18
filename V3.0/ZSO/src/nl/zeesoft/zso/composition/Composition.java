package nl.zeesoft.zso.composition;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Composition {
	private String		name			= "";
	private boolean		loop			= true;
	private int 		beatsPerMinute	= 120;
	private int			beatsPerBar		= 4;
	private int			stepsPerBeat	= 4;
	
	private List<Step>	steps			= new ArrayList<Step>();
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("loop","" + loop,true));
		json.rootElement.children.add(new JsElem("beatsPerMinute","" + beatsPerMinute));
		json.rootElement.children.add(new JsElem("beatsPerBar","" + beatsPerBar));
		json.rootElement.children.add(new JsElem("stepsPerBeat","" + stepsPerBeat));
		if (steps.size()>0) {
			JsElem stepsElem = new JsElem("steps",true);
			json.rootElement.children.add(stepsElem);
			for (Step step: steps) {
				JsElem stepElem = new JsElem();
				stepsElem.children.add(stepElem);
				stepElem.children.add(new JsElem("positionName",step.getPositionName(),true));
				stepElem.children.add(new JsElem("bar","" + step.getBar()));
				stepElem.children.add(new JsElem("number","" + step.getNumber()));
				stepElem.children.add(new JsElem("startMs","" + step.getStartMs()));
				if (step.getDurationMs()>0) {
					stepElem.children.add(new JsElem("durationMs","" + step.getDurationMs()));
				}
			}
		}
		return json;
	}

	public void fromJson(JsFile json) {
		steps.clear();
		for (JsElem cElem: json.rootElement.children) {
			if (cElem.name.equals("name")) {
				name = cElem.value.toString();
			} else if (cElem.name.equals("loop")) {
				loop = Boolean.parseBoolean(cElem.value.toString());
			} else if (cElem.name.equals("beatsPerMinute")) {
				beatsPerMinute = Integer.parseInt(cElem.value.toString());
			} else if (cElem.name.equals("beatsPerBar")) {
				beatsPerBar = Integer.parseInt(cElem.value.toString());
			} else if (cElem.name.equals("stepsPerBeat")) {
				stepsPerBeat = Integer.parseInt(cElem.value.toString());
			} else if (cElem.name.equals("steps")) {
				for (JsElem stepElem: cElem.children) {
					String positionName = "";
					int bar = 0;
					int number = 0;
					long startMs = 0;
					long durationMs = 0;
					for (JsElem vElem: stepElem.children) {
						if (vElem.name.equals("positionName")) {
							positionName = vElem.value.toString(); 
						} else if (vElem.name.equals("bar")) {
							bar = Integer.parseInt(vElem.value.toString()); 
						} else if (vElem.name.equals("number")) {
							number = Integer.parseInt(vElem.value.toString()); 
						} else if (vElem.name.equals("startMs")) {
							startMs = Long.parseLong(vElem.value.toString()); 
						} else if (vElem.name.equals("durationMs")) {
							durationMs = Long.parseLong(vElem.value.toString()); 
						}
					}
					if (positionName.length()>0 && bar>0 && number>0) {
						steps.add(new Step(positionName,bar,number,startMs,durationMs));
					}
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
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

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
}
