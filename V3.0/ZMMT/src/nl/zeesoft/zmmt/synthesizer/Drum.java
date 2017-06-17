package nl.zeesoft.zmmt.synthesizer;

public class Drum {
	public static final	String		KICK			= "Kick";
	public static final	String		SNARE			= "Snare";
	public static final	String		HIHAT1			= "Hihat 1";
	public static final	String		HIHAT2			= "Hihat 2";
	public static final	String		CLAP			= "Clap";
	public static final	String		TOM1			= "Tom 1";
	public static final	String		TOM2			= "Tom 2";
	public static final	String		RIDE			= "Ride";
	public static final	String		CYMBAL			= "Cymbal";
	public static final	String		FX1				= "FX 1";
	public static final	String		FX2				= "FX 2";
	public static final	String		FX3				= "FX 3";
	public static final String[]	DRUMS			= {KICK,SNARE,HIHAT1,HIHAT2,CLAP,TOM1,TOM2,RIDE,CYMBAL,FX1,FX2,FX3};
	
	public static String getDrumNameForNote(int note) {
		String r = "";
		int base = 36;
		for (int d = 0; d < DRUMS.length; d++) {
			if ((base + d)==note) {
				r = DRUMS[d];
				break;
			}
		}
		return r;
	}
}
