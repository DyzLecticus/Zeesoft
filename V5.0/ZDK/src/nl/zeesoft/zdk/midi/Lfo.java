package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

public class Lfo {
	public static final String	LINEAR	= "LINEAR"; 
	public static final String	BINARY	= "BINARY";
	
	public String				type	= LINEAR;
	public int					steps	= 5;
	public List<LfoTarget>		targets	= new ArrayList<LfoTarget>();
	
	public Lfo() {
		
	}
	
	public Lfo(String type) {
		this.type = type;
		if (type.equals(BINARY)) {
			steps = 2;
		}
	}
	
	public Lfo copy() {
		Lfo r = new Lfo();
		r.type = type;
		r.steps = steps;
		for (LfoTarget target: targets) {
			r.targets.add(target.copy());
		}
		return r;
	}
	
	public void addTarget(int channel, String instProperty, int percentage) {
		addTarget(channel, instProperty, percentage, false);
	}
	
	public void addTarget(int channel, String instProperty, int percentage, boolean invert) {
		targets.add(new LfoTarget(channel, instProperty, percentage, invert));
	}
}
