package nl.zeesoft.zmmt.syntesizer;

public class Instrument {
	public static final	String		SYNTH_BASS1			= "Synth bass 1";
	public static final	String		SYNTH_BASS2			= "Synth bass 2";
	public static final	String		SYNTH_BASS3			= "Synth bass 3";
	public static final	String		SYNTH1				= "Synth 1";
	public static final	String		SYNTH2				= "Synth 2";
	public static final	String		SYNTH3				= "Synth 3";
	public static final	String		BASS				= "Bass";
	public static final	String		PIANO				= "Piano";
	public static final	String		HARP				= "Harp";
	public static final	String		DRUMS				= "Drums";
	public static final	String		STRINGS1			= "Strings 1";
	public static final	String		STRINGS2			= "Strings 2";
	public static final String[]	INSTRUMENTS			= {SYNTH_BASS1,SYNTH_BASS2,SYNTH_BASS3,SYNTH1,SYNTH2,SYNTH3,BASS,PIANO,HARP,DRUMS,STRINGS1,STRINGS2};
	
	public static final int getMidiChannelForInstrument(String name) {
		int r = 0;
		for (int i = 0; i < INSTRUMENTS.length; i++) {
			if (INSTRUMENTS[i].equals(name)) {
				r = i;
				break;
			}
		}
		return r;
	}
}
