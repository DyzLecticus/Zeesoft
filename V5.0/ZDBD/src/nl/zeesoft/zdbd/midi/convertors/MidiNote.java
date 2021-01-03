package nl.zeesoft.zdbd.midi.convertors;

public class MidiNote {
	public static String[]	NOTE_CODES	= {"C-","C#","D-","D#","E-","F-","F#","G-","G#","A-","A#","B-"};
	
	public int				channel		= 0;
	public int				midiNote	= 0;
	public int				velocity	= 0;
	public float			hold		= 0.9F;
}
