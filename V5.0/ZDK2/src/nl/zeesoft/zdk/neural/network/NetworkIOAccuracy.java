package nl.zeesoft.zdk.neural.network;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.HistoricalFloat;
import nl.zeesoft.zdk.str.StrUtil;

public class NetworkIOAccuracy {
	protected SortedMap<String,HistoricalFloat>		accuracies	= null;
	
	protected NetworkIOAccuracy(NetworkIOAnalyzer analyzer) {
		this.accuracies = new TreeMap<String,HistoricalFloat>(analyzer.accuracies);
	}
	
	public SortedMap<String,HistoricalFloat> getAccuracies() {
		return accuracies;
	}
	
	public float getAverage() {
		float r = 0.0F;
		for (HistoricalFloat accuracy: accuracies.values()) {
			r += accuracy.getAverage();
		}
		if (accuracies.size()>0) {
			r = r / (float)accuracies.size();
		}
		return r;
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		StrUtil.appendLine(r, "Average: ");
		r.append(getAverage());
		for (Entry<String,HistoricalFloat> entry: accuracies.entrySet()) {
			StrUtil.appendLine(r, entry.getKey());
			r.append(": ");
			r.append(entry.getValue().getAverage());
		}
		return r.toString();
	}
}
