package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

public class Instruments {
	public Bass		bass	= new Bass();
	public Stab		stab	= new Stab();
	public Arp		arp		= new Arp();
	public Drum		drum	= new Drum();
	
	public List<Instrument> list() {
		List<Instrument> r = new ArrayList<Instrument>();
		r.add(bass);
		r.add(stab);
		r.add(arp);
		r.add(drum);
		return r;
	}
}
