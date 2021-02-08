package nl.zeesoft.zdbd.midi.convertors;

public class DrumSampleConvertor {
	public int		midiNote		= 32;
	public int		velocity		= 100;
	public int		accentVelocity	= 120;
	public float	hold			= 0.9F;
	public float	accentHold		= 1.5F;
	
	public DrumSampleConvertor copy() {
		DrumSampleConvertor r = new DrumSampleConvertor();
		r.midiNote = this.midiNote;
		r.velocity = this.velocity;
		r.accentVelocity = this.accentVelocity;
		r.hold = this.hold;
		r.accentHold = this.accentHold;
		return r;
	}
}
