package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

public class Pattern {
	public int 					steps	= 16;
	public int 					lanes	= 1;
	public List<PatternNote>	notes	= new ArrayList<PatternNote>();
	
	public Pattern copy() {
		Pattern r = new Pattern();
		r.steps = steps;
		r.lanes = lanes;
		for (PatternNote note: notes) {
			if (note.step>=0 && note.step<steps && note.lane>=0 && note.lane<lanes) {
				r.notes.add(note);
			}
		}
		return r;
	}
}
