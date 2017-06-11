package nl.zeesoft.zmmt.composition;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Pattern {
	private int				number		= 0;
	private int 			bars		= 0;
	private List<Note>		notes		= new ArrayList<Note>();
	private List<Control>	controls	= new ArrayList<Control>();
	
	public Pattern copy() {
		Pattern copy = new Pattern();
		copy.setNumber(number);
		copy.setBars(bars);
		for (Note note: notes) {
			copy.getNotes().add(note.copy());
		}
		for (Control control: controls) {
			copy.getControls().add(control.copy());
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
			JsElem notesElem = new JsElem("notes",true);
			json.rootElement.children.add(notesElem);
			for (Note note: notes) {
				JsElem noteElem = new JsElem();
				notesElem.children.add(noteElem);
				noteElem.children.add(new JsElem("i",note.instrument,true));
				noteElem.children.add(new JsElem("t","" + note.track));
				noteElem.children.add(new JsElem("s","" + note.step));
				noteElem.children.add(new JsElem("n","" + note.note));
				noteElem.children.add(new JsElem("d","" + note.duration));
				noteElem.children.add(new JsElem("a","" + note.accent));
				noteElem.children.add(new JsElem("v","" + note.velocityPercentage));
			}
		}
		if (controls.size()>0) {
			JsElem ctrlsElem = new JsElem("controls",true);
			json.rootElement.children.add(ctrlsElem);
			for (Control ctrl: controls) {
				JsElem ctrlElem = new JsElem();
				ctrlsElem.children.add(ctrlElem);
				ctrlElem.children.add(new JsElem("i",ctrl.instrument,true));
				ctrlElem.children.add(new JsElem("c","" + ctrl.control));
				ctrlElem.children.add(new JsElem("s","" + ctrl.step));
				ctrlElem.children.add(new JsElem("p","" + ctrl.percentage));
			}
		}
		return json;
	}

	public void fromJson(JsFile json) {
		notes.clear();
		controls.clear();
		for (JsElem elem: json.rootElement.children) {
			if (elem.name.equals("number")) {
				number = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("bars")) {
				bars = Integer.parseInt(elem.value.toString());
			} else if (elem.name.equals("notes") || elem.name.equals("steps")) { // TODO: remove steps as option
				for (JsElem stepElem: elem.children) {
					Note note = new Note();
					for (JsElem valElem: stepElem.children) {
						if (valElem.name.equals("i")) {
							note.instrument = valElem.value.toString();
						} else if (valElem.name.equals("t")) {
							note.track = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("s")) {
							note.step = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("d")) {
							note.duration = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("n")) {
							note.note = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("a")) {
							note.accent = Boolean.parseBoolean(valElem.value.toString());
						} else if (valElem.name.equals("v")) {
							note.velocityPercentage = Integer.parseInt(valElem.value.toString());
						}
					}
					notes.add(note);
				}
			} else if (elem.name.equals("controls")) {
				for (JsElem ctrlElem: elem.children) {
					Control ctrl = new Control();
					for (JsElem valElem: ctrlElem.children) {
						if (valElem.name.equals("i")) {
							ctrl.instrument = valElem.value.toString();
						} else if (valElem.name.equals("c")) {
							ctrl.control = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("s")) {
							ctrl.step = Integer.parseInt(valElem.value.toString());
						} else if (valElem.name.equals("p")) {
							ctrl.percentage = Integer.parseInt(valElem.value.toString());
						}
					}
					controls.add(ctrl);
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

	public List<Control> getControls() {
		return controls;
	}
}
