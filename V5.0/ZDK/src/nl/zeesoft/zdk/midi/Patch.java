package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Instantiator;

public class Patch {
	public String			name			= "New patch";
	public List<Inst>		instruments		= new ArrayList<Inst>();
	
	public Patch() {
		
	}
	
	public Patch(String name) {
		this.name = name;
	}
	
	public Patch copy() {
		Patch r = (Patch) Instantiator.getNewClassInstance(this.getClass());
		r.name = name;
		for (Inst inst: instruments) {
			r.instruments.add(inst.copy());
		}
		return r;
	}
	
	protected boolean isLoaded() {
		boolean r = false;
		for (Inst inst: instruments) {
			if (inst.channel>=0) {
				r = true;
				break;
			}
		}
		return r;
	}
}
