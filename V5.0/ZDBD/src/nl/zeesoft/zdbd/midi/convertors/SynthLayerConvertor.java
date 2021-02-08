package nl.zeesoft.zdbd.midi.convertors;

public class SynthLayerConvertor {
	public int		channel			= 0;
	public int		baseOctave		= 2;
	public int		velocity		= 100;
	public int		accentVelocity	= 120;
	
	public SynthLayerConvertor copy() {
		SynthLayerConvertor r = new SynthLayerConvertor();
		r.channel = this.channel;
		r.baseOctave = this.baseOctave;
		r.velocity = this.velocity;
		r.accentVelocity = this.accentVelocity;
		return r;
	}
}
