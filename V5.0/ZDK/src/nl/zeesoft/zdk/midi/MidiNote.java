package nl.zeesoft.zdk.midi;

import nl.zeesoft.zdk.Str;

public class MidiNote {
	private static final String[]	NOTES 			= {"C-","C#","D-","D#","E-","F-","F#","G-","G#","A-","A#","B-"};
	
	public int						channel			= 0;
	public int						octave			= 3;
	public int						octaveNote		= 0;
	public int						velocity		= -1;
	public int						delaySteps		= 0;
	
	@Override
	public String toString() {
		Str str = new Str(NOTES[octaveNote]);
		str.sb().append(octave);
		if (velocity>0) {
			str.sb().append(":");
			str.sb().append(String.format("%03d", velocity));
		}
		return str.toString();
	}
	
	public void fromString(String str) {
		if (str.length()>=2) {
			String not = str.substring(0,2);
			for (int i = 0; i < NOTES.length; i++) {
				if (NOTES[i].equals(not)) {
					octaveNote = i;
					break;
				}
			}
			if (str.length()>=3) {
				String oct = str.substring(2,3);
				try {
					octave = Integer.parseInt(oct);
				} catch(NumberFormatException e) {
					// Ignore
				}
				if (str.length()>=7) {
					String vel = str.substring(3,7);
					try {
						velocity = Integer.parseInt(vel);
					} catch(NumberFormatException e) {
						// Ignore
					}
				}
			}
		}
	}
	
	public int getMidiNoteNum() {
		return (octave * 12) + octaveNote; 
	}
}
