package nl.zeesoft.zdbd.midi;

public class SynthChannelConfig {
	public int					channel			= 0;
	
	public boolean				solo			= false;
	public boolean				mute			= false;

	public int					instrument		= 0;
	public int					volume			= 100;
	public int					attack			= 64;
	public int					decay			= 64;
	public int					release			= 64;
	
	public int					pan				= 64;
	public int					pressure		= 0;
	public int					modulation		= 0;
	public int					chorus			= 0;
	
	public int					filter			= 64;
	public int					resonance		= 64;
	public int					reverb			= 16;
	
	public int					vib_rate		= 64;
	public int					vib_depth		= 64;
	public int					vib_delay		= 64;
	
	public int getControlValue(int control) {
		int r = 64;
		if (control==SynthConfig.VOLUME) {
			r = volume;
		} else if (control==SynthConfig.ATTACK) {
			r = attack;
		} else if (control==SynthConfig.DECAY) {
			r = decay;
		} else if (control==SynthConfig.RELEASE) {
			r = release;
		} else if (control==SynthConfig.PAN) {
			r = pan;
		} else if (control==SynthConfig.MODULATION) {
			r = modulation;
		} else if (control==SynthConfig.CHORUS) {
			r = chorus;
		} else if (control==SynthConfig.FILTER) {
			r = filter;
		} else if (control==SynthConfig.RESONANCE) {
			r = resonance;
		} else if (control==SynthConfig.REVERB) {
			r = reverb;
		} else if (control==SynthConfig.VIB_RATE) {
			r = vib_rate;
		} else if (control==SynthConfig.VIB_DEPTH) {
			r = vib_depth;
		} else if (control==SynthConfig.VIB_DELAY) {
			r = vib_delay;
		}
		return r;
	}
}
