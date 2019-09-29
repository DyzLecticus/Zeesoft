package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.htm.proc.BufferedPredictor;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class BufferedPredictionStream extends PredictionStream {
	public BufferedPredictionStream(Pooler pooler,BufferedPredictor predictor) {
		super(pooler, predictor);
	}

	public BufferedPredictionStream(StreamEncoder encoder,Pooler pooler,BufferedPredictor predictor) {
		super(encoder, pooler, predictor);
	}
	
	public BufferedPredictionStream(Messenger msgr, WorkerUnion uni,Pooler pooler,BufferedPredictor predictor) {
		super(msgr, uni, pooler, predictor);
	}

	public BufferedPredictionStream(Messenger msgr, WorkerUnion uni,StreamEncoder encoder,Pooler pooler,BufferedPredictor predictor) {
		super(msgr, uni, encoder, pooler, predictor);
	}

	public ValuePredictor getNewValuePredictor() {
		return getNewValuePredictor(StreamEncoder.VALUE_KEY);
	}
	
	public ValuePredictor getNewValuePredictor(String valueKey) {
		ValuePredictor r = new ValuePredictor(this,valueKey);
		addListener(r);
		return r;
	}
	
	public ValueAnomalyDetector getNewValueAnomalyDetector() {
		return getNewValueAnomalyDetector(StreamEncoder.VALUE_KEY);
	}

	public ValueAnomalyDetector getNewValueAnomalyDetector(String valueKey) {
		ValueAnomalyDetector r = new ValueAnomalyDetector(this,valueKey);
		addListener(r);
		return r;
	}
}
