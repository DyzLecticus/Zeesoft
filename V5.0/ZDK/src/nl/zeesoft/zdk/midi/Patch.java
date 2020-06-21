package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

public class Patch {
	public String			name			= "New patch";
	public List<Inst>		instruments		= new ArrayList<Inst>();
	public List<Pattern>	patterns		= new ArrayList<Pattern>();
	
	public Patch() {
		
	}
	
	public Patch(String name) {
		this.name = name;
	}
	
	public Patch copy() {
		Patch r = new Patch();
		r.name = name;
		for (Inst inst: instruments) {
			r.instruments.add(inst.copy());
		}
		for (Pattern pat: patterns) {
			r.patterns.add(pat.copy());
		}
		return r;
	}
}
