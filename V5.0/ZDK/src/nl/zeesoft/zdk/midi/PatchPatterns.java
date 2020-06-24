package nl.zeesoft.zdk.midi;

public class PatchPatterns {
	public String		patchName	= "";
	public Pattern[]	patterns	= new Pattern[100];
	
	public PatchPatterns copy() {
		PatchPatterns r = new PatchPatterns();
		r.patchName = patchName;
		for (int i = 0; i < patterns.length; i++) {
			if (patterns[i]!=null) {
				r.patterns[i] = patterns[i].copy();
			}
		}
		return r;
	}
}
