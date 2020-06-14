package nl.zeesoft.zdk.midi;

public class LfoTarget {
	public int		channel		= 0;
	public String	property		= Inst.FILTER;
	public int		percentage		= 20;
	public boolean	invert			= false;
	
	public LfoTarget() {
		
	}
	
	public LfoTarget(int channel, String property, int percentage, boolean invert) {
		this.channel = channel;
		this.property = property;
		this.percentage = percentage;
		this.invert = invert;
	}
	
	public LfoTarget copy() {
		LfoTarget r = new LfoTarget();
		r.channel = channel;
		r.property = property;
		r.percentage = percentage;
		r.invert = invert;
		return r;
	}
}
