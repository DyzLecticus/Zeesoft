package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class Arpeggiators {
	protected Lock					lock	= new Lock();
	
	protected List<Arpeggiator>		arps	= new ArrayList<Arpeggiator>();
	protected long					changed	= System.currentTimeMillis();

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
			}
			arps.add(arp);
			changed	= System.currentTimeMillis();
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
	
	public void rename(String name, String newName) {
		if (newName.length()>0) {
			lock.lock(this);
			Arpeggiator arp = getArpeggiatorNoLock(name);
			Arpeggiator conflict = getArpeggiatorNoLock(newName);
			if (arp!=null && conflict==null) {
				arp.name = newName;
				changed	= System.currentTimeMillis();
			}
			lock.unlock(this);
		}
	}
	
	public Arpeggiator remove(String name) {
		lock.lock(this);
		Arpeggiator r = getArpeggiatorNoLock(name);
		if (r!=null) {
			arps.remove(r);
			changed	= System.currentTimeMillis();
		}
		lock.unlock(this);
		return r;
	}
	
	public List<Arpeggiator> list() {
		List<Arpeggiator> r = new ArrayList<Arpeggiator>();
		lock.lock(this);
		SortedMap<String,Arpeggiator> map = new TreeMap<String,Arpeggiator>();
		for (Arpeggiator arp: arps) {
			map.put(arp.name,arp.copy());
		}
		for (Entry<String,Arpeggiator> entry: map.entrySet()) {
			r.add(entry.getValue());
		}
		lock.unlock(this);
		return r;
	}

	public long getChanged() {
		lock.lock(this);
		long r = changed;
		lock.unlock(this);
		return r;
	}
	
	public RunCode getFromFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				fromFile(path);
				return true;
			}
		};
	}
	
	public void fromFile(String path) {
		Arpeggiators arpeggiators = (Arpeggiators) PersistableCollection.fromFile(path);
		if (arpeggiators!=null) {
			lock.lock(this);
			arps.clear();
			for (Arpeggiator arp: arpeggiators.list()) {
				arps.add(arp);
			}
			changed = arpeggiators.changed;
			lock.unlock(this);
		}
	}
	
	public RunCode getToFileRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				toFile(path);
				return true;
			}
		};
	}
	
	public void toFile(String path) {
		PersistableCollection.toFile(this, path);
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
		
		Arpeggiator arp = new Arpeggiator();
		arp.name = "Even sparse narrow";
		arp.density = 0.25F;
		arp.maxOctave = 0;
		arp.maxSteps = 16;
		arps.add(arp);

		arp = new Arpeggiator();
		arp.name = "Even medium";
		arp.density = 0.50F;
		arp.maxOctave = 1;
		arp.maxSteps = 16;
		arps.add(arp);

		arp = new Arpeggiator();
		arp.name = "Odd medium";
		arp.density = 0.50F;
		arp.maxOctave = 1;
		arp.maxSteps = 12;
		arps.add(arp);

		arp = new Arpeggiator();
		arp.name = "Odd dense medium ";
		arp.density = 0.75F;
		arp.maxOctave = 1;
		arp.maxSteps = 12;
		arps.add(arp);

		arp = new Arpeggiator();
		arp.name = "Odd dense wide ";
		arp.density = 0.75F;
		arp.maxOctave = 2;
		arp.maxSteps = 12;
		arps.add(arp);

		arp = new Arpeggiator();
		arp.name = "Even dense medium ";
		arp.density = 0.75F;
		arp.maxOctave = 1;
		arp.maxSteps = 16;
		arps.add(arp);
	}
}
