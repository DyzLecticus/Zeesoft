package nl.zeesoft.zmmt.syntesizer;

public class DrumConfiguration extends VelocityConfiguration {
	private	int	layer1MidiNote	= 35;
	private	int	layer2MidiNote	= 34;
	
	public DrumConfiguration copy() {
		DrumConfiguration r = new DrumConfiguration();
		r.setName(getName());

		r.setLayer1MidiNote(layer1MidiNote);
		r.setLayer1BaseVelocity(getLayer1BaseVelocity());
		r.setLayer1AccentVelocity(getLayer1AccentVelocity());
		
		r.setLayer2MidiNote(layer2MidiNote);
		r.setLayer2BaseVelocity(getLayer2BaseVelocity());
		r.setLayer2AccentVelocity(getLayer2AccentVelocity());

		return r;
	}

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
