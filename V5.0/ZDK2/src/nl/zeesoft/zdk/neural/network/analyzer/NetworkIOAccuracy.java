package nl.zeesoft.zdk.neural.network.analyzer;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.str.StrUtil;

public class NetworkIOAccuracy {
	public int							total			= 0;
	public List<IOAccuracy>				accuracies		= new ArrayList<IOAccuracy>();
	
	protected IOAccuracyCalculator		calculator		= new IOAccuracyCalculator();
	protected IOAccuracyRmseCalculator	rmseCalculator	= new IOAccuracyRmseCalculator();
	protected IOAccuracyMapeCalculator	mapeCalculator	= new IOAccuracyMapeCalculator();
	
	public NetworkIOAccuracy() {
		
	}
	
	public NetworkIOAccuracy(NetworkIOAnalyzer analyzer, int max) {
		int start = 0;
		int end = analyzer.networkIO.size();
		int capacity = end;
		if (max>0) {
			capacity = max;
			start = end - max;
			if (start<0) {
				start = 0;
			}
		}
		total = end - start;
		calculator.calculateAverageAccuracies(analyzer, start, end, capacity, this);
		rmseCalculator.calculateAverageRmses(analyzer, start, end, this);
		mapeCalculator.calculateAverageMapes(analyzer, start, end, this);
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		for (IOAccuracy acc: accuracies) {
			StrUtil.appendLine(r, acc.name);
			r.append(": ");
			r.append(acc.accuracy);
			r.append(" (");
			r.append(acc.rootMeanSquaredError);
			r.append(")");
		}
		return r.toString();
	}
	
	public IOAccuracy getAverage() {
		return getOrCreateIOAccuracy(IOAccuracy.AVERAGE);
	}
	
	public IOAccuracy getIOAccuracy(String name) {
		IOAccuracy r = null;
		for (IOAccuracy accuracy: accuracies) {
			if (accuracy.name.equals(name)) {
				r = accuracy;
				break;
			}
		}
		return r;
	}	
	
	protected IOAccuracy getOrCreateIOAccuracy(String name) {
		IOAccuracy r = getIOAccuracy(name);
		if (r==null) {
			r = addIOAccuracy(name);
		}
		return r;
	}
	
	protected IOAccuracy addIOAccuracy(String name) {
		IOAccuracy r = new IOAccuracy();
		r.name = name;
		accuracies.add(r);
		return r;
	}
}
