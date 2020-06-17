package nl.zeesoft.zdk.midi;

public class Lfo {
	public static final String	SINE	= "SINE"; 
	public static final String	LINEAR	= "LINEAR"; 
	public static final String	SAW		= "SAW";
	public static final String	BINARY	= "BINARY";
	
	public String				type	= SINE;
	public int					steps	= 5;
	
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
		return r;
	}
}
