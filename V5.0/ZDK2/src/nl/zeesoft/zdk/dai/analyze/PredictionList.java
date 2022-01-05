package nl.zeesoft.zdk.dai.analyze;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.predict.PredictionLog;

public class PredictionList extends PredictionLog {
	public PredictionList(History history, ObjMapComparator comparator) {
		setComparator(comparator);
		this.history = history;
		this.maxSize = history.maxSize;
	}
}
