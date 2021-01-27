package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.thread.Lock;

public class Arpeggiators {
	protected Lock					lock	= new Lock();
	
	protected List<Arpeggiator>		arps	= new ArrayList<Arpeggiator>();

	public Arpeggiators() {
		initializeDefaults();
	}
	
	public void put(Arpeggiator arp) {
		arp = arp.copy();
		if (arp.name.length()>0) {
			lock.lock(this);
			Arpeggiator existing = getArpeggiatorNoLock(arp.name);
			if (existing!=null) {
				arps.remove(existing);
				arps.add(arp);
			}
			lock.unlock(this);
		}
	}
	
	public Arpeggiator get(String name) {
		lock.lock(this);
		Arpeggiator r = getArpeggiatorNoLock(name);
		if (r!=null) {
			r = r.copy();
		}
		lock.unlock(this);
		return r;
	}
	
	public Arpeggiator remove(String name) {
		lock.lock(this);
		Arpeggiator r = getArpeggiatorNoLock(name);
		if (r!=null) {
			arps.remove(r);
		}
		lock.unlock(this);
		return r;
	}
	
	public List<Arpeggiator> list() {
		List<Arpeggiator> r = new ArrayList<Arpeggiator>();
		lock.lock(this);
		for (Arpeggiator arp: arps) {
			r.add(arp.copy());
		}
		lock.unlock(this);
		return r;
	}
	
	protected Arpeggiator getArpeggiatorNoLock(String name) {
		Arpeggiator r = null;
		for (Arpeggiator arp: arps) {
			if (arp.name.equals(name)) {
				r = arp;
				break;
			}
		}
		return r;
	}
	
	private void initializeDefaults() {
		arps.clear();
		arps.add(new Arpeggiator());
		
		Arpeggiator arp = new Arpeggiator();
		arp.name = "Sparse narrow even";
		arp.density = 0.25F;
		arp.maxOctave = 0;
		arp.maxSteps = 16;
		arps.add(arp);
		
		arp = new Arpeggiator();
		arp.name = "Sparse wide even";
		arp.density = 0.25F;
		arp.maxOctave = 2;
		arp.maxSteps = 16;
		arps.add(arp);
		
		arp = new Arpeggiator();
		arp.name = "Sparse wide odd";
		arp.density = 0.25F;
		arp.maxOctave = 2;
		arps.add(arp);
		
		arp = new Arpeggiator();
		arp.name = "Medium even";
		arp.density = 0.5F;
		arp.maxOctave = 1;
		arp.maxSteps = 16;
		arps.add(arp);
		
		arp = new Arpeggiator();
		arp.name = "Medium wide odd";
		arp.density = 0.5F;
		arp.maxOctave = 2;
		arp.maxSteps = 20;
		arps.add(arp);
		
		arp = new Arpeggiator();
		arp.name = "Dense even";
		arp.density = 0.8F;
		arp.maxOctave = 1;
		arp.maxSteps = 16;
		arps.add(arp);
		
		arp = new Arpeggiator();
		arp.name = "Dense wide odd";
		arp.density = 0.8F;
		arp.maxOctave = 2;
		arp.maxSteps = 20;
		arps.add(arp);
		
		arp = new Arpeggiator();
		arp.name = "Final";
		arp.minDuration = 4;
		arp.maxDuration = 4;
		arp.density = 0.0F;
		arp.maxOctave = 1;
		arp.maxSteps = 64;
		arps.add(arp);
	}
}
