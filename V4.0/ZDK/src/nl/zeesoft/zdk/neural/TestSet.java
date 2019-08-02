package nl.zeesoft.zdk.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.functions.ZRandomize;

public class TestSet {
	public List<Test> 		tests	 		= new ArrayList<Test>();
	public float			errorTolerance	= 0.1F;
	
	public float			averageError	= 0;
	public boolean			success			= true;
	
	public void finalize() {
		averageError = 0;
		success = true;
		float total = 0;
		for (Test t: tests) {
			for (int i = 0; i<t.errors.length; i++) {
				float diff = t.errors[i];
				if (diff<0) {
					diff = diff * -1;
				}
				if (diff>errorTolerance) {
					success = false;
				}
				averageError += diff;
				total++;
			}
		}
		if (total>0) {
			averageError = averageError / total;
		}
	}
	
	public TestSet copy() {
		TestSet r = new TestSet();
		for (Test t: tests) {
			r.tests.add((Test) t.copy());
		}
		r.errorTolerance = errorTolerance;
		r.averageError = averageError;
		r.success = success;
		return r;
	}
	
	public void randomizeOrder() {
		ZRandomize rand = new ZRandomize();
		int l = tests.size();
		if (l>1) {
			for (int i = 0; i < l; i++) {
				Test t = tests.remove(i);
				tests.add(rand.getRandomInt(0,l - 1),t);
			}
		}
	}
}
