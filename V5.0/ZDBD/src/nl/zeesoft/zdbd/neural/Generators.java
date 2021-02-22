package nl.zeesoft.zdbd.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.instruments.Bass;
import nl.zeesoft.zdbd.pattern.instruments.Crash;
import nl.zeesoft.zdbd.pattern.instruments.Note;
import nl.zeesoft.zdbd.pattern.instruments.Ride;
import nl.zeesoft.zdbd.pattern.instruments.Stab;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.thread.Busy;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class Generators {
	protected Lock					lock		= new Lock();
	protected Busy					busy		= new Busy(this);
	
	protected List<Generator>		generators	= new ArrayList<Generator>();
	protected float[]				stepDelays	= null;
	protected long					changed		= System.currentTimeMillis();
	
	public Generators() {
		initializeDefaults(true,1.0F);
	}

	public void copyFrom(Generators gens) {
		lock.lock(this);
		generators.clear();
		for (Generator gen: gens.generators) {
			generators.add(gen.copy());
		}
		changed	= System.currentTimeMillis();
		lock.unlock(this);
	}
	
	public int size() {
		lock.lock(this);
		int r = generators.size();
		lock.unlock(this);
		return r;
	}
	
	public void put(Generator generator) {
		generator = generator.copy();
		if (generator.name.length()>0) {
			lock.lock(this);
			putNoLock(generator);
			lock.unlock(this);
		}
	}
	
	public Generator get(String name) {
		Generator r = null;
		lock.lock(this);
		r = getNoLock(name);
		if (r!=null) {
			r = r.copy();
		}
		lock.unlock(this);
		return r;
	}
	
	public void rename(String name, String newName) {
		if (newName.length()>0) {
			lock.lock(this);
			Generator gen = getNoLock(name);
			Generator conflict = getNoLock(newName);
			if (gen!=null && conflict==null) {
				gen.name = newName;
				changed	= System.currentTimeMillis();
			}
			lock.unlock(this);
		}
	}
	
	public void set(String name, int index) {
		lock.lock(this);
		Generator gen = getNoLock(name);
		if (gen!=null) {
			generators.remove(gen);
			generators.add(index,gen);
			changed	= System.currentTimeMillis();
		}
		lock.unlock(this);
	}
	
	public int indexOf(String name) {
		int r = -1;
		lock.lock(this);
		Generator gen = getNoLock(name);
		if (gen!=null) {
			r = generators.indexOf(gen);
		}
		lock.unlock(this);
		return r;
	}
	
	public Generator remove(String name) {
		Generator r = null;
		lock.lock(this);
		r = getNoLock(name);
		if (r!=null) {
			generators.remove(r);
			changed	= System.currentTimeMillis();
		}
		lock.unlock(this);
		return r;
	}
	
	public List<Generator> list() {
		List<Generator> r = new ArrayList<Generator>();
		lock.lock(this);
		for (Generator gen: generators) {
			r.add(gen.copy());
		}
		lock.unlock(this);
		return r;
	}
	
	public SortedMap<String,PatternSequence> getSequences() {
		SortedMap<String,PatternSequence> r = new TreeMap<String,PatternSequence>();
		lock.lock(this);
		for (Generator gen: generators) {
			if (gen.generatedPatternSequence!=null) {
				r.put(gen.name,gen.generatedPatternSequence.copy());
			}
		}
		lock.unlock(this);
		return r;
	}
	
	public void setShuffle(float[] stepDelays) {
		lock.lock(this);
		this.stepDelays = stepDelays;
		for (Generator gen: generators) {
			if (gen.generatedPatternSequence!=null) {
				gen.generatedPatternSequence.rythm.stepDelays = stepDelays; 
			}
		}
		lock.unlock(this);
	}
	
	public void clear() {
		lock.lock(this);
		generators.clear();
		changed	= System.currentTimeMillis();
		lock.unlock(this);
	}

	public long getChanged() {
		lock.lock(this);
		long r = changed;
		lock.unlock(this);
		return r;
	}
	
	public RunCode getGenerateSequenceRunCode(Network network, NetworkIO lastIO, PatternSequence trainingSequence, String name) {
		return new RunCode() {
			@Override
			protected boolean run() {
				generateSequence(network,lastIO,trainingSequence,name);
				return true;
			}
		};
	}
	
	public PatternSequence generateSequence(Network network, NetworkIO lastIO, PatternSequence trainingSequence, String name) {
		PatternSequence r = null;
		lock.lock(this);
		Generator gen = null;
		Generator copy = null;
		if (!busy.isBusy()) {
			busy.setBusy(true);
			gen = getNoLock(name);
			if (gen!=null) {
				copy = gen.copy();
			}
		}
		lock.unlock(this);
		if (copy!=null) {
			copy.generatePatternSequence(network,lastIO,trainingSequence);
			lock.lock(this);
			if (copy.generatedPatternSequence!=null) {
				gen.generatedPatternSequence = copy.generatedPatternSequence;
				if (stepDelays!=null) {
					gen.generatedPatternSequence.rythm.stepDelays = stepDelays;
					stepDelays = null;
				}
				r = gen.generatedPatternSequence;
			}
			changed = System.currentTimeMillis();
			busy.setBusy(false);
			lock.unlock(this);
		} else {
			lock.lock(this);
			busy.setBusy(false);
			lock.unlock(this);
		}
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
		Generators generators = (Generators) PersistableCollection.fromFile(path);
		if (generators!=null) {
			copyFrom(generators);
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
	
	public void initializeDefaults(boolean maintainNote, float mixEnd) {
		lock.lock(this);
		generators.clear();
		
		Generator gen = null;
		gen = new Generator();
		gen.name = "Maintain A1";
		gen.group1Distortion = 1;
		gen.group2Distortion = 1;
		gen.group3Distortion = 1;
		gen.mixEnd = mixEnd;
		if (!maintainNote) {
			gen.setMaintainInstruments();
		}
		putNoLock(gen);
		
		gen = new Generator();
		gen.name = "Maintain A2";
		gen.group1Distortion = 1;
		gen.group2Distortion = 0;
		gen.group3Distortion = 0;
		gen.mixEnd = mixEnd;
		putNoLock(gen);
		
		gen = new Generator();
		gen.name = "Maintain B1";
		gen.group1Distortion = 1;
		gen.group2Distortion = 1;
		gen.group3Distortion = 1;
		gen.mixEnd = mixEnd;
		if (maintainNote) {
			gen.setMaintainInstruments(Bass.NAME, Note.NAME, Stab.NAME);
		} else {
			gen.setMaintainInstruments(Bass.NAME, Stab.NAME);
		}
		putNoLock(gen);
		
		gen = new Generator();
		gen.name = "Maintain B2";
		gen.group1Distortion = 1;
		gen.group2Distortion = 0;
		gen.group3Distortion = 0;
		gen.mixEnd = mixEnd;
		gen.setMaintainInstruments(Bass.NAME, Note.NAME, Stab.NAME);
		putNoLock(gen);
		
		gen = new Generator();
		gen.name = "Free form";
		gen.group1Distortion = 1;
		gen.group2Distortion = 1;
		gen.group3Distortion = 1;
		gen.maintainBeat = 0;
		gen.maintainFeedback = false;
		if (maintainNote) {
			gen.setMaintainInstruments(Ride.NAME, Note.NAME);
		} else {
			gen.setMaintainInstruments(Ride.NAME);
		}
		putNoLock(gen);

		gen = new Generator();
		gen.name = "Undistorted";
		gen.group1Distortion = 0;
		gen.group2Distortion = 0;
		gen.group3Distortion = 0;
		gen.maintainBeat = 0;
		gen.setMaintainInstruments(Ride.NAME, Crash.NAME, Note.NAME);
		putNoLock(gen);
		lock.unlock(this);
	}
	
	protected void putNoLock(Generator generator) {
		Generator gen = getNoLock(generator.name);
		if (gen!=null) {
			generators.set(generators.indexOf(gen), generator);
		} else {
			generators.add(generator);
		}
		changed	= System.currentTimeMillis();
	}
	
	protected Generator getNoLock(String name) {
		Generator r = null;
		for (Generator gen: generators) {
			if (gen.name.equals(name)) {
				r = gen;
				break;
			}
		}
		return r;
	}
}
