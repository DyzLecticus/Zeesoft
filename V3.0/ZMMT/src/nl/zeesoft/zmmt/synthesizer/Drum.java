package nl.zeesoft.zmmt.synthesizer;

public class Drum {
	public static final	String		KICK			= "Kick";
	public static final	String		CLAP			= "Clap";
	public static final	String		SNARE			= "Snare";
	public static final	String		HIHAT1			= "Hihat 1";
	public static final	String		HIHAT2			= "Hihat 2";
	public static final	String		TOM1			= "Tom 1";
	public static final	String		TOM2			= "Tom 2";
	public static final	String		RIDE			= "Ride";
	public static final	String		CYMBAL			= "Cymbal";
	public static final	String		FX1				= "FX 1";
	public static final	String		FX2				= "FX 2";
	public static final	String		FX3				= "FX 3";
	public static final String[]	DRUMS			= {KICK,CLAP,SNARE,HIHAT1,HIHAT2,TOM1,TOM2,RIDE,CYMBAL,FX1,FX2,FX3};
	
	public static final int getExternalMidiNoteForNote(int note) {
		int r = 36;
		if (note==36) {
			r = 36;
		} else if (note==37) {
			r = 39; 
		} else if (note==38) {
			r = 40; 
		} else if (note==39) {
			r = 42;
		} else if (note==40) {
			r = 46; 
		} else if (note==41) {
			r = 41; 
		} else if (note==42) {
			r = 43; 
		} else if (note==43) {
			r = 51; 
		} else if (note==44) {
			r = 49; 
		} else if (note==45) {
			r = 76; 
		} else if (note==46) {
			r = 80; 
		} else if (note==47) {
			r = 56; 
		}
		return r;
	}
}
