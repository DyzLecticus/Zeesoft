package nl.zeesoft.zbe.brain;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class TestCycleSet {
	public List<TestCycle>				cycles				= new ArrayList<TestCycle>();
	
	public int							successes			= 0;
	public float						averageError		= 0.0F;
	public SortedMap<Integer,Integer>	cyclesPerLevel		= new TreeMap<Integer,Integer>();
	public SortedMap<Integer,Integer>	successesPerLevel	= new TreeMap<Integer,Integer>();
	
	public boolean isSuccess(int level) {
		boolean r = successes == cycles.size();
		if (level>0) {
			for (Integer key: cyclesPerLevel.keySet()) {
				if (successesPerLevel.get(key)!=null) {
					r = successesPerLevel.get(key) == cyclesPerLevel.get(key);
					if (!r) {
						break;
					} else if (key==level) {
						break;
					}
				}
			}
		}
		return r;
	}
	
	public void finalize() {
		float totalError = 0.0F;
		float total = 0.0F;
		
		for (TestCycle tc: cycles) {
			int cycPerLevel = 1;
			if (cyclesPerLevel.get(tc.level)!=null) {
				cycPerLevel = cyclesPerLevel.get(tc.level);
				cycPerLevel++;
			}
			cyclesPerLevel.put(tc.level,cycPerLevel);
			if (tc.success) {
				successes++;
				int sucPerLevel = 1;
				if (successesPerLevel.get(tc.level)!=null) {
					sucPerLevel = successesPerLevel.get(tc.level);
					sucPerLevel++;
				}
				successesPerLevel.put(tc.level,sucPerLevel);
			}
		}
		for (TestCycle tc: cycles) {
			for (int n = 0; n < tc.outputs.length; n++) {
				total += 1.0F;
				if (!tc.success) {
					if (tc.errors[n]!=0.0F) {
						if (tc.errors[n]>0.0F) {
							totalError += tc.errors[n];
						} else {
							totalError += (tc.errors[n] * -1);
						}
					}
				}
			}
		}
		if (total>0.0F) {
			averageError = totalError / total;
		}
	}

	public TestCycleSet copy() {
		TestCycleSet r = getCopyTestCycleSet();
		for (TestCycle tc: cycles) {
			TestCycle copyTc = new TestCycle();
			tc.copy(copyTc);
			r.cycles.add(copyTc);
		}
		return r;
	}
	
	public TestCycle addTestCycle(Brain brain) {
		TestCycle r = new TestCycle();
		r.initialize(brain);
		cycles.add(r);
		return r;
	}
	
	protected TestCycleSet getCopyTestCycleSet() {
		return new TestCycleSet();
	}
}
