package nl.zeesoft.zmmt.composition;

import nl.zeesoft.zdk.ZStringBuilder;

public class Note {
	public String	instrument			= "";
	public int		track				= 1;
	public int		step				= 1;
	public int		note				= 36;
	public boolean	accent				= false;
	public int		duration			= 1;
	public int		velocityPercentage	= 100;
	
	public Note copy() {
		Note r = new Note();
		r.instrument = instrument;
		r.track = track;
		r.step = step;
		r.note = note;
		r.accent = accent;
		r.duration = duration;
		r.velocityPercentage = velocityPercentage;
		return r;
	}
	
	@Override
	public String toString() {
		ZStringBuilder sb = new ZStringBuilder();
		int noteNum = note % 12;
		if (noteNum==0) {
			sb.append("C-");
		} else if (noteNum==1) {
			sb.append("C#");
		} else if (noteNum==2) {
			sb.append("D-");
		} else if (noteNum==3) {
			sb.append("D#");
		} else if (noteNum==4) {
			sb.append("E-");
		} else if (noteNum==5) {
			sb.append("F-");
		} else if (noteNum==6) {
			sb.append("F#");
		} else if (noteNum==7) {
			sb.append("G-");
		} else if (noteNum==8) {
			sb.append("G#");
		} else if (noteNum==9) {
			sb.append("A-");
		} else if (noteNum==10) {
			sb.append("A#");
		} else if (noteNum==11) {
			sb.append("B-");
		}
		sb.append("" + ((note - noteNum) / 12));
		if (accent) {
			sb.append("!");
		} else {
			sb.append(".");
		}
		sb.append(String.format("%03d",velocityPercentage));
		return sb.toString();
	}
}
