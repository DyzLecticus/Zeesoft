package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A ValueClassifier is used to listen to ClassificationStream output for Classifications.
 * It combines these into a list for its listeners. 
 */
public class ValueClassifier extends StreamListenerObject {
	private	List<ValueClassifierListener>	listeners		= new ArrayList<ValueClassifierListener>();

	public ValueClassifier(ClassificationStream stream) {
		super(stream);
	}
	
	public void addListener(ValueClassifierListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}

	@Override
	public void processedResult(Stream stream, StreamResult result) {
		List<Classification> classifications = new ArrayList<Classification>();
		if (result.outputSDRs.size()>3) {
			for (int i = 3; i < result.outputSDRs.size(); i++) {
				SDR outputSDR = result.outputSDRs.get(i);
				if (outputSDR instanceof DateTimeSDR) {
					DateTimeSDR classificationSDR = (DateTimeSDR) outputSDR;
					if (classificationSDR.keyValues.containsKey(Classifier.CLASSIFICATION_KEY)) {
						Classification classification = (Classification) classificationSDR.keyValues.get(Classifier.CLASSIFICATION_KEY);
						classifications.add(classification);
					}
				}
			}
			lockMe(this);
			List<ValueClassifierListener> list = new ArrayList<ValueClassifierListener>(listeners);
			unlockMe(this);
			for (ValueClassifierListener listener: list) {
				listener.classifiedValue(result, classifications);
			}
		}
	}
}
