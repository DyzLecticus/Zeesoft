package nl.zeesoft.zdk.im.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.im.ObjectArray;
import nl.zeesoft.zdk.im.ObjectArrayList;

public class PatternRecognizerResult {
	public float								similarity		= 0F; 
	public List<Integer>						startIndexes	= new ArrayList<Integer>();
	public List<PatternRecognizerPrediction>	predictions		= new ArrayList<PatternRecognizerPrediction>();
	
	public void addPredictions(ObjectArrayList history, float similarity) {
		int total = 0;
		for (Integer index: startIndexes) {
			ObjectArray predictedInput = history.list.get(index - 1);
			PatternRecognizerPrediction prediction = getOrAddPrediction(predictedInput);
			prediction.votes++;
			total++;
		}
		if (total>0) {
			for (PatternRecognizerPrediction p: predictions) {
				p.confidence = ((float)p.votes / (float)total) * similarity;
			}
		}
	}

	public PatternRecognizerPrediction getOrAddPrediction(ObjectArray predictedInput) {
		PatternRecognizerPrediction r = getPrediction(predictedInput);
		if (r==null) {
			r = new PatternRecognizerPrediction();
			r.predictedInput = predictedInput;
			predictions.add(r);
		}
		return r;
	}

	public PatternRecognizerPrediction getPrediction(ObjectArray predictedInput) {
		PatternRecognizerPrediction r = null;
		for (PatternRecognizerPrediction p: predictions) {
			if (p.predictedInput.equals(predictedInput)) {
				r = p;
				break;
			}
		}
		return r;
	}
}
