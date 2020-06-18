package nl.zeesoft.zdk.midi;

public class PatternNote extends NoteObject {
	public int step		= 0;
	public int lane		= 1;
	public int duration	= 1;
	
	@Override
	public PatternNote copy() {
		PatternNote r = (PatternNote) super.copy();
		r.step = step;
		r.lane = lane;
		r.duration = duration;
		return r;
	}
}
