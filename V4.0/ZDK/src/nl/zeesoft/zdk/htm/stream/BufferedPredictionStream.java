package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.htm.proc.BufferedPredictor;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class BufferedPredictionStream extends PredictionStream {
	public BufferedPredictionStream(Messenger msgr, WorkerUnion uni,Pooler pooler,BufferedPredictor predictor) {
		super(msgr, uni, pooler, predictor);
	}

	public ValueAnomalyDetector getNewValueAnomalyDetector(String valueKey) {
		ValueAnomalyDetector r = new ValueAnomalyDetector(getMessenger(),valueKey);
		addListener(r);
		return r;
	}
}
