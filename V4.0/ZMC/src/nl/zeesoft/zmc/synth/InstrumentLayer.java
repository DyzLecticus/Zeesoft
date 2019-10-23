package nl.zeesoft.zmc.synth;

public class InstrumentLayer {
	public int			layerIndex			= 0;
	
	public int			midiNum				= 0;

	public int 			pressure			= 0;

	public int			modulation			= 0;
	public int			reverb				= 24;
	public int			chorus				= 0;

	public int			filter				= 64;
	public int			resonance			= 64;
	public int			attack				= 64;
	public int			decay				= 64;
	public int			release				= 64;
	public int			vibRate				= 64;
	public int			vibDepth			= 64;
	public int			vibDelay			= 64;

	public int			baseOctave			= 3;
	public int			baseVelocity		= 100;
	public int			accentVelocity		= 110;
	
	public boolean		controlModulation	= true;
	public boolean		controlFilter		= true;

	public boolean		modToChorus			= false;
	public boolean		modToResonance		= false;
	public boolean		modToVibDepth		= true;	
}
