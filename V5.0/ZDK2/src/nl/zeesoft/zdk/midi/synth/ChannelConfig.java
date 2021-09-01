package nl.zeesoft.zdk.midi.synth;

public class ChannelConfig {
	public static final int			DEFAULT			= 63;
	
	public static final int			VOLUME			= 7;
	public static final int			ATTACK			= 73;
	public static final int			DECAY			= 75;
	public static final int			RELEASE			= 72;
	public static final int			PAN				= 10;
	public static final int			MODULATION		= 1;
	public static final int			CHORUS			= 93;
	public static final int			FILTER			= 74;
	public static final int			RESONANCE		= 71;
	public static final int			REVERB			= 91;
	public static final int			VIB_RATE		= 76;
	public static final int			VIB_DEPTH		= 77;
	public static final int			VIB_DELAY		= 78;

	public static final int[]		CONTROLS		= {
		VOLUME,
		ATTACK,
		DECAY,
		RELEASE,
		PAN,
		MODULATION,
		CHORUS,
		FILTER,
		RESONANCE,
		REVERB,
		VIB_RATE,
		VIB_DEPTH,
		VIB_DELAY,
	};
	
	public static final String[]	CONTROL_NAMES	= {
		"Volume",
		"Attack",
		"Decay",
		"Release",
		"Pan",
		"Modulation",
		"Chorus",
		"Filter",
		"Resonance",
		"Reverb",
		"Vibrato rate",
		"Vibrato depth",
		"Vibrato delay",
	};
	
	public int						channel			= 0;
	
	public boolean					solo			= false;
	public boolean					mute			= false;

	public int						instrument		= 0;
	public int						volume			= 120;
	public int						attack			= DEFAULT;
	public int						decay			= DEFAULT;
	public int						release			= DEFAULT;
	
	public int						pan				= DEFAULT;
	public int						pressure		= 0;
	public int						modulation		= 0;
	public int						chorus			= 0;
	
	public int						filter			= DEFAULT;
	public int						resonance		= DEFAULT;
	public int						reverb			= 16;
	
	public int						vib_rate		= DEFAULT;
	public int						vib_depth		= DEFAULT;
	public int						vib_delay		= DEFAULT;
	
	public void copyFrom(ChannelConfig cfg) {
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
		if (control==VOLUME) {
			r = volume;
		} else if (control==ATTACK) {
			r = attack;
		} else if (control==DECAY) {
			r = decay;
		} else if (control==RELEASE) {
			r = release;
		} else if (control==PAN) {
			r = pan;
		} else if (control==MODULATION) {
			r = modulation;
		} else {
			r = getRemainingControlValue(control);
		}
		return r;
	}
	
	public void setControlValue(int control, int value) {
		value = limitValue(value);
		if (control==VOLUME) {
			volume = value;
		} else if (control==ATTACK) {
			attack = value;
		} else if (control==DECAY) {
			decay = value;
		} else if (control==RELEASE) {
			release = value;
		} else if (control==PAN) {
			pan = value;
		} else if (control==MODULATION) {
			modulation = value;
		} else {
			setRemainingControlValue(control, value);
		}
	}
	
	protected int getRemainingControlValue(int control) {
		int r = DEFAULT;
		if (control==CHORUS) {
			r = chorus;
		} else if (control==FILTER) {
			r = filter;
		} else if (control==RESONANCE) {
			r = resonance;
		} else if (control==REVERB) {
			r = reverb;
		} else if (control==VIB_RATE) {
			r = vib_rate;
		} else if (control==VIB_DEPTH) {
			r = vib_depth;
		} else if (control==VIB_DELAY) {
			r = vib_delay;
		}
		return r;
	}
	
	protected void setRemainingControlValue(int control, int value) {
		if (control==CHORUS) {
			chorus = value;
		} else if (control==FILTER) {
			filter = value;
		} else if (control==RESONANCE) {
			resonance = value;
		} else if (control==REVERB) {
			reverb = value;
		} else if (control==VIB_RATE) {
			vib_rate = value;
		} else if (control==VIB_DEPTH) {
			vib_depth = value;
		} else if (control==VIB_DELAY) {
			vib_delay = value;
		}
	}
	
	protected static int limitValue(int value) {
		if (value < 0) {
			value = 0;
		} else if (value > 127) {
			value = 127;
		}
		return value;
	}
}
