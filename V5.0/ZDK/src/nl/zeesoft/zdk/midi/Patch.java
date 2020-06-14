package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

public class Patch {
	public String		name			= "New patch";
	public List<Inst>	instruments		= new ArrayList<Inst>();
	public Lfo[]		lfos			= new Lfo[3];
	
	public Patch() {
		
	}
	
	public Patch(String name) {
		this.name = name;
	}
}
