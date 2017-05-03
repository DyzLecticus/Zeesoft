package nl.zeesoft.zmmt.syntesizer;

public class DrumConfiguration extends VelocityConfiguration {
	private	int	layer1MidiNote	= 35;
	private	int	layer2MidiNote	= 34;
	
	public int getLayer1MidiNote() {
		return layer1MidiNote;
	}
	
	public void setLayer1MidiNote(int note) {
		this.layer1MidiNote = note;
	}

	public int getLayer2MidiNote() {
		return layer2MidiNote;
	}

	public void setLayer2MidiNote(int note) {
		this.layer2MidiNote = note;
	}
}
