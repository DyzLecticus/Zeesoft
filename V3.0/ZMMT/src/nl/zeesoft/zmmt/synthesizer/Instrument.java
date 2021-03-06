package nl.zeesoft.zmmt.synthesizer;

import java.awt.Color;

public class Instrument {
	public static final	String		BASS1				= "Bass 1";
	public static final	String		BASS2				= "Bass 2";
	public static final	String		BASS3				= "Bass 3";
	public static final	String		SYNTH1				= "Synth 1";
	public static final	String		SYNTH2				= "Synth 2";
	public static final	String		SYNTH3				= "Synth 3";
	public static final	String		LEAD				= "Lead";
	public static final	String		STRINGS				= "Strings";
	public static final	String		DRUMS				= "Drums";
	public static final	String		ECHO				= "Echo";
	public static final String[]	INSTRUMENTS			= {BASS1,BASS2,BASS3,SYNTH1,SYNTH2,SYNTH3,LEAD,DRUMS,STRINGS,ECHO};
	public static final String[]	INSTRUMENT_SHORTS	= {"BS1","BS2","BS3","SN1","SN2","SN3","LD","DRM","STR","ECH"};
	
	public static int getMidiChannelForInstrument(String name,int layer) {
		int r = -1;
		if (layer==2 && name.equals(ECHO)) {
			r = 15;
		} else if (layer==1) {
			if (name.equals(BASS1)) {
				r = 10;
			} else if (name.equals(SYNTH1)) {
				r = 11;
			} else if (name.equals(LEAD)) {
				r = 12;
			} else if (name.equals(DRUMS)) {
				r = 9;
			} else if (name.equals(STRINGS)) {
				r = 13;
			} else if (name.equals(ECHO)) {
				r = 14;
			}
		} else {
			if (name.equals(DRUMS)) {
				r = 9;
			} else if (name.equals(ECHO)) {
				r = 8;
			} else if (name.equals(STRINGS)) {
				r = 7;
			} else {
				r = getIndexForInstrument(name);
			}
		}
		return r;
	}

	public static Color getColorForInstrument(String name) {
		Color r = null;
		if (name.equals(BASS1)) {
			r = new Color(153,204,255);
		} else if (name.equals(BASS2)) {
			r = new Color(153,153,255);
		} else if (name.equals(BASS3)) {
			r = new Color(204,153,255);
		} else if (name.equals(SYNTH1)) {
			r = new Color(255,153,255);
		} else if (name.equals(SYNTH2)) {
			r = new Color(255,153,178);
		} else if (name.equals(SYNTH3)) {
			r = new Color(255,102,153);
		} else if (name.equals(LEAD)) {
			r = new Color(255,102,102);
		} else if (name.equals(STRINGS)) {
			r = new Color(255,255,102);
		} else if (name.equals(DRUMS)) {
			r = new Color(255,178,102);
		} else if (name.equals(ECHO)) {
			r = new Color(178,255,102);
		}
		return r;
	}
	
	public static int getIndexForInstrument(String name) {
		int r = -1;
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			if (Instrument.INSTRUMENTS[i].equals(name)) {
				r = i;
				break;
			}
		}
		return r;
	}
}
