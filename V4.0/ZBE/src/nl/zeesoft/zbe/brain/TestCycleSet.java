package nl.zeesoft.zbe.brain;

import java.util.ArrayList;
import java.util.List;

public class TestCycleSet {
	public List<Cycle>	cycles			= new ArrayList<Cycle>();
	
	public int			successes		= 0;
	public float		averageError	= 0.0F;
	
	public boolean isSuccess() {
		return successes == cycles.size();
	}
	
	public void finalize() {
		float totalError = 0.0F;
		float total = 0.0F;
		for (Cycle cycle: cycles) {
			if (cycle instanceof TestCycle) {
				TestCycle tc = (TestCycle) cycle;
				if (tc.success) {
					successes++;
				}
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
		}
		if (total>0.0F) {
			averageError = totalError / total;
		}
	}

	public TestCycleSet copy() {
		TestCycleSet r = getCopyTestCycleSet();
		for (Cycle tc: cycles) {
			TestCycle copyTc = new TestCycle();
			tc.copy(copyTc);
			r.cycles.add(copyTc);
		}
		return r;
	}
	
	protected TestCycleSet getCopyTestCycleSet() {
		return new TestCycleSet();
	}
}
