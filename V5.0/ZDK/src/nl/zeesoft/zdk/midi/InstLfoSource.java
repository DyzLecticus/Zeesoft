package nl.zeesoft.zdk.midi;

public class InstLfoSource {
	public String	property		= Inst.FILTER;
	public int		lfoIndex		= 0;
	public int		percentage		= 20;
	public boolean	invert			= false;
	
	public InstLfoSource() {
		
	}
	
	public InstLfoSource(String property, int lfoIndex, int percentage, boolean invert) {
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
