package nl.zeesoft.zdk.midi;

public class InstLfoSource {
	public String	property	= Inst.FILTER;
	public int		lfoIndex	= 0;
	public float	percentage	= 0.20F;
	public boolean	invert		= false;
	
	public InstLfoSource() {
		
	}
	
	public InstLfoSource(String property, int lfoIndex, float percentage, boolean invert) {
		this.property = property;
		this.lfoIndex = lfoIndex;
		this.percentage = percentage;
		this.invert = invert;
	}
	
	public InstLfoSource copy() {
		InstLfoSource r = new InstLfoSource();
		r.property = property;
		r.lfoIndex = lfoIndex;
		r.percentage = percentage;
		r.invert = invert;
		return r;
	}
}
