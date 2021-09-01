package nl.zeesoft.zdk.midi.synth;

public class ChannelControl {
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
}
