package nl.zeesoft.zmmt.synthesizer;

public class DrumConfiguration extends BaseConfiguration {
	private boolean	muted					= false;

	private	int		layer1MidiNote			= 35;
	private int 	layer1BaseVelocity		= 100;
	private	int		layer1AccentVelocity	= 110;

	private	int		layer2MidiNote			= 34;
	private int 	layer2BaseVelocity		= 100;
	private	int		layer2AccentVelocity	= 110;
	
	public DrumConfiguration copy() {
		DrumConfiguration r = new DrumConfiguration();
		r.setName(getName());

		r.setMuted(muted);
		
		r.setLayer1MidiNote(layer1MidiNote);
		r.setLayer1BaseVelocity(layer1BaseVelocity);
		r.setLayer1AccentVelocity(layer1AccentVelocity);
		
		r.setLayer2MidiNote(layer2MidiNote);
		r.setLayer2BaseVelocity(layer2BaseVelocity);
		r.setLayer2AccentVelocity(layer2AccentVelocity);

		return r;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public int getLayer1MidiNote() {
		return layer1MidiNote;
	}
	
	public void setLayer1MidiNote(int note) {
		this.layer1MidiNote = note;
	}

	public int getLayer1BaseVelocity() {
		return layer1BaseVelocity;
	}
	
	public void setLayer1BaseVelocity(int baseVelocity) {
		this.layer1BaseVelocity = baseVelocity;
	}
	
	public int getLayer1AccentVelocity() {
		return layer1AccentVelocity;
	}
	
	public void setLayer1AccentVelocity(int accentVelocity) {
		this.layer1AccentVelocity = accentVelocity;
	}

	public int getLayer2MidiNote() {
		return layer2MidiNote;
	}

	public void setLayer2MidiNote(int note) {
		this.layer2MidiNote = note;
	}

	public int getLayer2BaseVelocity() {
		return layer2BaseVelocity;
	}

	public void setLayer2BaseVelocity(int baseVelocity) {
		this.layer2BaseVelocity = baseVelocity;
	}

	public int getLayer2AccentVelocity() {
		return layer2AccentVelocity;
	}

	public void setLayer2AccentVelocity(int accentVelocity) {
		this.layer2AccentVelocity = accentVelocity;
	}
}
