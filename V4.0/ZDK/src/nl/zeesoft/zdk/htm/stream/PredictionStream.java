package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.Predictor;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class PredictionStream extends Stream {
	public PredictionStream(Messenger msgr, WorkerUnion uni,Pooler pooler,Predictor predictor) {
		super(msgr, uni);
		addInputProcessor(pooler);
		addNextProcessor(predictor,0);
	}
	
	public AnomalyDetector getNewAnomalyDetector() {
		AnomalyDetector r = new AnomalyDetector(getMessenger());
		addListener(r);
		return r;
	}
}
