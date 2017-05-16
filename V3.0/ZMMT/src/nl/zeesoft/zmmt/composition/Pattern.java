package nl.zeesoft.zmmt.composition;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Pattern {
	private int				number		= 0;
	private int 			bars		= 0;
	private List<Note>		notes		= new ArrayList<Note>();
	
	public Pattern copy() {
		Pattern copy = new Pattern();
		copy.setNumber(number);
		copy.setBars(bars);
		for (Note note: notes) {
			copy.getNotes().add(note.copy());
		}
		return copy;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("number","" + number));
		if (bars>0) {
			json.rootElement.children.add(new JsElem("bars","" + bars));
		}
		if (notes.size()>0) {
			JsElem stepsElem = new JsElem("steps",true);
			json.rootElement.children.add(stepsElem);
			for (Note note: notes) {
				JsElem stepElem = new JsElem();
				stepsElem.children.add(stepElem);
				stepElem.children.add(new JsElem("instrument",note.instrument,true));
				stepElem.children.add(new JsElem("track","" + note.track));
				stepElem.children.add(new JsElem("step","" + note.step));
				stepElem.children.add(new JsElem("note","" + note.note));
				stepElem.children.add(new JsElem("duration","" + note.duration));
				stepElem.children.add(new JsElem("accent","" + note.accent));
				stepElem.children.add(new JsElem("velocityPercentage","" + note.velocityPercentage));
			}
		}
		return json;
	}

	public void fromJson(JsFile json) {
		notes.clear();
		for (JsElem elem: json.rootElement.children) {
			if (elem.name.equals("number")) {
				number = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("bars")) {
				bars = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("steps")) {
				for (JsElem stepElem: elem.children) {
					Note note = new Note();
					for (JsElem valElem: stepElem.children) {
						if (valElem.name.equals("instrument")) {
							note.instrument = valElem.value.toString();
						} else if (valElem.name.equals("track")) {
							note.track = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("step")) {
							note.step = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("duration")) {
							note.duration = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("note")) {
							note.note = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("accent")) {
							note.accent = Boolean.parseBoolean(valElem.value.toString());
						} else if (valElem.name.equals("velocityPercentage")) {
							note.velocityPercentage = Integer.parseInt(valElem.value.toString());
						}
					}
					notes.add(note);
				}
			}
		}
	}

	public Note getNote(int track, int step, int duration) {
		Note r = null;
		int endStep = step + (duration - 1);
		for (Note n: notes) {
			int noteEndStep = n.step + (n.duration - 1);
			if (n.track==track && 
				(n.step<=endStep && noteEndStep>=step)
				) {
				r = n;
				break;
			}
		}
		return r;
	}

	public List<Note> getTrackNotes(int track, int step, int duration) {
		List<Note> r = new ArrayList<Note>();
		int endStep = step + (duration - 1);
		for (Note n: notes) {
			int noteEndStep = n.step + (n.duration - 1);
			if (n.track==track && 
				(n.step<=endStep && noteEndStep>=step)
				) {
				int addIndex = 0;
				int i = 0;
				for (Note note: r) {
					if (note.step<n.step) {
						addIndex = i + 1;
					}
					i++;
				}
				r.add(addIndex,n);
			}
		}
		return r;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int pattern) {
		this.number = pattern;
	}

	public int getBars() {
		return bars;
	}

	public void setBars(int bars) {
		this.bars = bars;
	}

	public List<Note> getNotes() {
		return notes;
	}
}
