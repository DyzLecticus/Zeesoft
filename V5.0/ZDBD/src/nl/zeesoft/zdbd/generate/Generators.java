package nl.zeesoft.zdbd.generate;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.thread.Lock;

public class Generators {
	private Lock					lock		= new Lock();
	private List<GeneratorIO>		generators	= new ArrayList<GeneratorIO>();
	
	public int size() {
		lock.lock(this);
		int r = generators.size();
		lock.unlock(this);
		return r;
	}
	
	public void add(GeneratorIO generator) {
		add(-1,generator);
	}
	
	public void add(int index, GeneratorIO generator) {
		lock.lock(this);
		if (generator!=null) {
			if (index>=0 && index<generators.size()) {
				generators.add(index,generator.copy());
			} else {
				generators.add(generator.copy());
			}
		}
		lock.unlock(this);
	}
	
	public GeneratorIO get(int index) {
		GeneratorIO r = null;
		lock.lock(this);
		if (index<generators.size()) {
			r = generators.get(index).copy();
		}
		lock.unlock(this);
		return r;
	}
	
	public void set(int index, GeneratorIO generator) {
		lock.lock(this);
		if (index>=0 && index<generators.size()) {
			generators.set(index,generator.copy());
		}
		lock.unlock(this);
	}
	
	public GeneratorIO remove(int index) {
		GeneratorIO r = null;
		lock.lock(this);
		if (index<generators.size()) {
			r = generators.remove(index);
		}
		lock.unlock(this);
		return r;
	}
	
	public void clear() {
		lock.lock(this);
		generators.clear();
		lock.unlock(this);
	}
}
