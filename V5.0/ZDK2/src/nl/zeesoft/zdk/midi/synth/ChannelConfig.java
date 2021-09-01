package nl.zeesoft.zdk.midi.synth;

public class ChannelConfig {
	public static final int	DEFAULT			= 63;
	
	public boolean			solo			= false;
	public boolean			mute			= false;

	public int				instrument		= 0;
	public int				volume			= 120;
	public int				attack			= DEFAULT;
	public int				decay			= DEFAULT;
	public int				release			= DEFAULT;
	
	public int				pan				= DEFAULT;
	public int				pressure		= 0;
	public int				modulation		= 0;
	public int				chorus			= 0;
	
	public int				filter			= DEFAULT;
	public int				resonance		= DEFAULT;
	public int				reverb			= 16;
	
	public int				vib_rate		= DEFAULT;
	public int				vib_depth		= DEFAULT;
	public int				vib_delay		= DEFAULT;
	
	public void copyFrom(ChannelConfig other) {
		solo = other.solo;
		mute = other.mute;
		instrument = other.instrument;
		pressure = other.pressure;
		for (int i = 0; i < ChannelControl.CONTROLS.length; i++) {
			setControlValue(ChannelControl.CONTROLS[i], other.getControlValue(ChannelControl.CONTROLS[i]));
		}
	}
	
	public int getControlValue(int control) {
		int r = DEFAULT;
		if (control==ChannelControl.VOLUME) {
			r = volume;
		} else if (control==ChannelControl.ATTACK) {
			r = attack;
		} else if (control==ChannelControl.DECAY) {
			r = decay;
		} else if (control==ChannelControl.RELEASE) {
			r = release;
		} else if (control==ChannelControl.PAN) {
			r = pan;
		} else if (control==ChannelControl.MODULATION) {
			r = modulation;
		} else {
			r = getRemainingControlValue(control);
		}
		return r;
	}
	
	public void setControlValue(int control, int value) {
		value = limitValue(value);
		if (control==ChannelControl.VOLUME) {
			volume = value;
		} else if (control==ChannelControl.ATTACK) {
			attack = value;
		} else if (control==ChannelControl.DECAY) {
			decay = value;
		} else if (control==ChannelControl.RELEASE) {
			release = value;
		} else if (control==ChannelControl.PAN) {
			pan = value;
		} else if (control==ChannelControl.MODULATION) {
			modulation = value;
		} else {
			setRemainingControlValue(control, value);
		}
	}
	
	protected int getRemainingControlValue(int control) {
		int r = DEFAULT;
		if (control==ChannelControl.CHORUS) {
			r = chorus;
		} else if (control==ChannelControl.FILTER) {
			r = filter;
		} else if (control==ChannelControl.RESONANCE) {
			r = resonance;
		} else if (control==ChannelControl.REVERB) {
			r = reverb;
		} else if (control==ChannelControl.VIB_RATE) {
			r = vib_rate;
		} else if (control==ChannelControl.VIB_DEPTH) {
			r = vib_depth;
		} else if (control==ChannelControl.VIB_DELAY) {
			r = vib_delay;
		}
		return r;
	}
	
	protected void setRemainingControlValue(int control, int value) {
		if (control==ChannelControl.CHORUS) {
			chorus = value;
		} else if (control==ChannelControl.FILTER) {
			filter = value;
		} else if (control==ChannelControl.RESONANCE) {
			resonance = value;
		} else if (control==ChannelControl.REVERB) {
			reverb = value;
		} else if (control==ChannelControl.VIB_RATE) {
			vib_rate = value;
		} else if (control==ChannelControl.VIB_DEPTH) {
			vib_depth = value;
		} else if (control==ChannelControl.VIB_DELAY) {
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
