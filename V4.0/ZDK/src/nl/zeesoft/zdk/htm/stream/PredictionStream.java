package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.Predictor;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class PredictionStream extends Stream {
	public PredictionStream(Pooler pooler,Predictor predictor) {
		super(null,null);
		initialize(pooler,predictor);
	}
	
	public PredictionStream(StreamEncoder encoder,Pooler pooler,Predictor predictor) {
		super(null,null,encoder);
		initialize(pooler,predictor);
	}
	
	public PredictionStream(Messenger msgr, WorkerUnion uni,Pooler pooler,Predictor predictor) {
		super(msgr, uni);
		initialize(pooler,predictor);
	}
	
	public PredictionStream(Messenger msgr, WorkerUnion uni,StreamEncoder encoder,Pooler pooler,Predictor predictor) {
		super(msgr, uni, encoder);
		initialize(pooler,predictor);
	}
	
	public AnomalyDetector getNewAnomalyDetector() {
		AnomalyDetector r = new AnomalyDetector(getMessenger());
		addListener(r);
		return r;
	}
	
	protected void initialize(Pooler pooler,Predictor predictor) {
		addInputProcessor(pooler);
		addNextProcessor(predictor,0);
	}
}
