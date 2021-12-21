package nl.zeesoft.zdk.im;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.im.pattern.PatternRecognizerPrediction;
import nl.zeesoft.zdk.im.pattern.PatternRecognizerResult;
import nl.zeesoft.zdk.str.StrUtil;

public class MachinePrediction {
	public List<PatternRecognizerResult>	results				= null;
	
	public List<PropertySupport>			propertySupport		= new ArrayList<PropertySupport>();
	public ObjectArray						prediction			= null;
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if (results!=null && results.size()>0) {
			for (PatternRecognizerResult result: results) {
				StrUtil.appendLine(str, "Pattern recognizer: " + result.patternRecognizer.indexes + ", similarity: " + result.similarity);
				for (PatternRecognizerPrediction prediction: result.predictions) {
					StrUtil.appendLine(str, " -> " + prediction.predictedInput + ", votes: " + prediction.votes + " = " + prediction.confidence);
				}
			}
			ObjectArray prediction = getPrediction();
			StrUtil.appendLine(str, "Property support: ");
			int maxIndex = getMaxIndex(propertySupport);
			for (int i = 0; i <= maxIndex; i++) {
				StrUtil.appendLine(str, " -> Property: " + i);
				List<PropertySupport> indexSupport = getSupport(propertySupport, i);
				for (PropertySupport support: indexSupport) {
					StrUtil.appendLine(str, "   -> Value: " + support.value + ", support: " + support.support);
				}
			}
			StrUtil.appendLine(str, "Combined prediction: " + prediction);
		}
		return str.toString();
	}
	
	public ObjectArray getPrediction() {
		if (prediction==null && results!=null) {
			calculateSupport();
			calculatePrediction();
		}
		return prediction;
	}
	
	public int getMaxIndex(List<PropertySupport> list) {
		int r = -1;
		for (PropertySupport support: list) {
			if (support.index > r) {
				r = support.index;
			}
		}
		return r;
	}

	public void calculatePrediction() {
		int maxIndex = getMaxIndex(propertySupport);
		prediction = new ObjectArray((maxIndex + 1));
		for (int i = 0; i <= maxIndex; i++) {
			PropertySupport winner = selectWinner(i);
			if (winner!=null) {
				prediction.objects[i] = winner.value;
			}
		}
	}

	public PropertySupport selectWinner(int index) {
		PropertySupport r = null;
		float max = 0F;
		List<PropertySupport> winners = new ArrayList<PropertySupport>();
		List<PropertySupport> indexSupport = getSupport(propertySupport, index);
		for (PropertySupport support: indexSupport) {
			if (support.support > max) {
				max = support.support;
				winners.clear();
			}
			if (support.support == max) {
				winners.add(support);
			}
		}
		if (winners.size()>0) {
			r = winners.get(0);
		}
		return r;
	}
	
	public List<PropertySupport> getSupport(List<PropertySupport> list, int index) {
		List<PropertySupport> r = new ArrayList<PropertySupport>();
		for (PropertySupport support: list) {
			if (support.index == index) {
				r.add(support);
			}
		}
		return r;
	}

	public PropertySupport getSupport(List<PropertySupport> list, int index, Object value) {
		PropertySupport r = null;
		for (PropertySupport support: list) {
			if (support.index == index && (support.value==value || (support.value != null && support.value.equals(value)))) {
				r = support;
				break;
			}
		}
		return r;
	}

	public PropertySupport getOrAddSupport(List<PropertySupport> list, int index, Object value) {
		PropertySupport r = getSupport(list, index, value);
		if (r==null) {
			r = new PropertySupport(index, value);
			list.add(r);
		}
		return r;
	}

	public void calculateSupport() {
		propertySupport.clear();
		int maxIndex = -1;
		for (PatternRecognizerResult result: results) {
			for (PatternRecognizerPrediction prediction: result.predictions) {
				Object[] values = prediction.predictedInput.objects;
				for (int i = 0; i < values.length; i++) {
					PropertySupport support = getOrAddSupport(propertySupport, i, values[i]);
					support.support += (result.similarity * prediction.confidence);
					if (i > maxIndex) {
						maxIndex = i;
					}
				}
			}
		}
	}
}
