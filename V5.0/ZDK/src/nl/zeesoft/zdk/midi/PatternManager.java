package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.thread.Lock;

public class PatternManager {
	private Lock				lock			= new Lock();
	private List<PatchPatterns>	patchPatterns	= new ArrayList<PatchPatterns>();

	protected PatternManager(Logger logger) {
		lock.setLogger(this, logger);
	}
	
	public void setPatchPattern(String name, int index, Pattern pattern) {
		lock.lock(this);
		PatchPatterns pp = getPatchPatternsNoLock(name);
		if (pp!=null && index>=0 && index<pp.patterns.length) {
			pp.patterns[index] = pattern.copy();
		}
		lock.unlock(this);
	}
	
	public Pattern getPatchPattern(String name, int index) {
		Pattern r = null;
		lock.lock(this);
		PatchPatterns pp = getPatchPatternsNoLock(name);
		if (pp!=null && index>=0 && index<pp.patterns.length) {
			if (pp.patterns[index]!=null) {
				r = pp.patterns[index].copy();
			}
		}
		lock.unlock(this);
		return r;
	}
	
	protected void addedPatch(String name) {
		lock.lock(this);
		PatchPatterns pp = new PatchPatterns();
		pp.patchName = name;
		patchPatterns.add(pp);
		lock.unlock(this);
	}
	
	protected void renamedPatch(String name,String newName) {
		lock.lock(this);
		PatchPatterns pp = getPatchPatternsNoLock(name);
		if (pp!=null) {
			pp.patchName = newName;
		}
		lock.unlock(this);
	}
	
	protected void removedPatch(String name) {
		lock.lock(this);
		PatchPatterns pp = getPatchPatternsNoLock(name);
		if (pp!=null) {
			patchPatterns.remove(pp);
		}
		lock.unlock(this);
	}
	
	private PatchPatterns getPatchPatternsNoLock(String name) {
		PatchPatterns r = null;
		for (PatchPatterns pp: patchPatterns) {
			if (pp.patchName.equals(name)) {
				r = pp;
				break;
			}
		}
		return r;
	}
}
