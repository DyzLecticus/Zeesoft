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
	
}
