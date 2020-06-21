package nl.zeesoft.zdk.midi;

public class InstGroup {
	protected String	name		= "";
	protected int[]		channels	= new int[1];
	
	protected InstGroup copy() {
		InstGroup r = new InstGroup();
		r.name = name;
		r.channels = new int[channels.length];
		for (int i = 0; i < channels.length; i++) {
			r.channels[i] = channels[i];
		}
		return r;
	}
}
