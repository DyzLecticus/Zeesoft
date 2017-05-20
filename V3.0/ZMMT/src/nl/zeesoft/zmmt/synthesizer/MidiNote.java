package nl.zeesoft.zmmt.synthesizer;

public class MidiNote {
	public String 	instrument	= "";
	public int		note		= 0;
	public int		channel		= 0;
	public int		midiNote	= 0;
	public int		velocity	= 0;
	
	public String getId() {
		return "" + channel + ":" + midiNote;
	}
}
