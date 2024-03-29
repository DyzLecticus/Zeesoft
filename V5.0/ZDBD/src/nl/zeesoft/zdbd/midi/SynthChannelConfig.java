package nl.zeesoft.zdbd.midi;

public class SynthChannelConfig {
	public static int	DEFAULT			= 63;
	
	public int			channel			= 0;
	
	public boolean		solo			= false;
	public boolean		mute			= false;

	public int			instrument		= 0;
	public int			volume			= 120;
	public int			attack			= DEFAULT;
	public int			decay			= DEFAULT;
	public int			release			= DEFAULT;
	
	public int			pan				= DEFAULT;
	public int			pressure		= 0;
	public int			modulation		= 0;
	public int			chorus			= 0;
	
	public int			filter			= DEFAULT;
	public int			resonance		= DEFAULT;
	public int			reverb			= 16;
	
	public int			vib_rate		= DEFAULT;
	public int			vib_depth		= DEFAULT;
	public int			vib_delay		= DEFAULT;
	
	public void copyFrom(SynthChannelConfig cfg) {
		solo = cfg.solo;
		mute = cfg.mute;
		instrument = cfg.instrument;
		volume = cfg.volume;
		attack = cfg.attack;
		decay = cfg.decay;
		release = cfg.release;
		pan = cfg.pan;
		pressure = cfg.pressure;
		modulation = cfg.modulation;
		chorus = cfg.chorus;
		filter = cfg.filter;
		resonance = cfg.resonance;
		reverb = cfg.reverb;
		vib_rate = cfg.vib_rate;
		vib_depth = cfg.vib_depth;
		vib_delay = cfg.vib_delay;
	}
	
	public int getControlValue(int control) {
		int r = DEFAULT;
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
	
	public void setControlValue(int control, int value) {
		if (value < 0) {
			value = 0;
		} else if (value > 127) {
			value = 127;
		}
		if (control==SynthConfig.VOLUME) {
			volume = value;
		} else if (control==SynthConfig.ATTACK) {
			attack = value;
		} else if (control==SynthConfig.DECAY) {
			decay = value;
		} else if (control==SynthConfig.RELEASE) {
			release = value;
		} else if (control==SynthConfig.PAN) {
			pan = value;
		} else if (control==SynthConfig.MODULATION) {
			modulation = value;
		} else if (control==SynthConfig.CHORUS) {
			chorus = value;
		} else if (control==SynthConfig.FILTER) {
			filter = value;
		} else if (control==SynthConfig.RESONANCE) {
			resonance = value;
		} else if (control==SynthConfig.REVERB) {
			reverb = value;
		} else if (control==SynthConfig.VIB_RATE) {
			vib_rate = value;
		} else if (control==SynthConfig.VIB_DEPTH) {
			vib_depth = value;
		} else if (control==SynthConfig.VIB_DELAY) {
			vib_delay = value;
		}
	}
}
