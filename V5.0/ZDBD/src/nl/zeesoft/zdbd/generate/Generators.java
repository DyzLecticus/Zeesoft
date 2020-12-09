package nl.zeesoft.zdbd.generate;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.PatternSequence;
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
	protected long					changed		= System.currentTimeMillis();

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
			Generator gen = getNoLock(generator.name);
			if (gen!=null) {
				generators.set(generators.indexOf(gen), generator);
			} else {
				generators.add(generator);
			}
			changed	= System.currentTimeMillis();
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
	
	public void set(String name, int index) {
		lock.lock(this);
		Generator gen = getNoLock(name);
		if (gen!=null) {
			generators.remove(gen);
			generators.set(index,gen);
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
		if (!busy.isBusy()) {
			busy.setBusy(true);
			gen = getNoLock(name);
		}
		lock.unlock(this);
		if (gen!=null) {
			gen.generatePatternSequence(network,lastIO,trainingSequence);
			lock.lock(this);
			r = gen.generatedPatternSequence.copy();
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
	
	protected Generator getNoLock(String name) {
		Generator r = null;
		for (Generator gen: generators) {
			if (gen.name.equals(name)) {
				r = gen;
			}
		}
		return r;
	}
}
