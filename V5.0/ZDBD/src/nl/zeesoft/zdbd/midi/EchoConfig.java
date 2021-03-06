package nl.zeesoft.zdbd.midi;

public class EchoConfig {
	public boolean	active			= true;
	public int		sourceChannel	= 0;
	public int		targetChannel	= 0;
	public int		delay			= 3;
	public int		pan				= 64;
	public float	velocity		= 0.6F;
	public float	filter			= 0.80F;
	public float	reverb			= 1.25F;
	public float	chorus			= 1.25F;
	
	public EchoConfig copy() {
		EchoConfig r = new EchoConfig();
		r.active = active;
		r.sourceChannel = sourceChannel;
		r.targetChannel = targetChannel;
		r.delay = delay;
		r.pan = pan;
		r.velocity = velocity;
		r.filter = filter;
		r.reverb = reverb;
		r.chorus = chorus;
		return r;
	}
}
