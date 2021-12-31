package nl.zeesoft.zdk.dai.recognize;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.History;
import nl.zeesoft.zdk.dai.MapPrediction;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.Prediction;

public class PatternRecognizer {
	public List<ListPatternRecognizer>	patternRecognizers	= new ArrayList<ListPatternRecognizer>();

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (ListPatternRecognizer lpr: patternRecognizers) {
			if (str.length()>0) {
				str.append("\n");
			}
			str.append(lpr.toString());
		}
		return str.toString();
	}
	
	public void generateDefaultPatternRecognizers() {
		generatePatternRecognizers(4, 8);
	}

	public void generatePatternRecognizers(int num, int depth) {
		ListPatternRecognizer pr = new ListPatternRecognizer();
		for (int i = 0; i < depth; i++) {
			pr.indexes.add(i);
		}
		patternRecognizers.add(pr);
		patternRecognizers.add(new FibonacciPatternRecognizer(depth));
		for (int i = 2; i < num; i++) {
			patternRecognizers.add(new PowerPatternRecognizer(depth, i));
		}
	}

	public List<Integer> getCombinedIndexes() {
		List<Integer> r = new ArrayList<Integer>();
		for (ListPatternRecognizer lpr: patternRecognizers) {
			for (Integer index: lpr.indexes) {
				if (!r.contains(index)) {
					r.add(index);
				}
			}
		}
		return r;
	}
	
	public void detectPatterns(ObjMapList history, ObjMapComparator comparator) {
		detectPatterns(history, comparator, 0);
	}

	public void detectPatterns(ObjMapList history, ObjMapComparator comparator, int maxDepth) {
		if (maxDepth<=0) {
			maxDepth = history.list.size();
		}
		for (ListPatternRecognizer lpr: patternRecognizers) {
			lpr.calculatePatternSimilarity(history, comparator, maxDepth);
		}
	}
	
	public Prediction getPrediction(History history, PatternRecognizer patternRecognizer) {
		Prediction r = new Prediction();
		addMapPredictions(r, history, patternRecognizer);
		r.calculatePredictedMap();
		return r;
	}

	public void addMapPredictions(Prediction prediction, History history, PatternRecognizer patternRecognizer) {
		if (history.list.size()==1) {
			MapPrediction mp = new MapPrediction(history.list.get(0));
			mp.confidence = 1.0F;
			mp.votes = 1;
		} else {
			addListPatternRecognizerPredictions(prediction, history, patternRecognizer);
		}
	}
	
	public void addListPatternRecognizerPredictions(Prediction prediction, History history, PatternRecognizer patternRecognizer) {
		int total = 0;
		for (ListPatternRecognizer lpr: patternRecognizer.patternRecognizers) {
			for (Integer index: lpr.startIndexes) {
				ObjMap predictedMap = history.list.get(index - 1);
				MapPrediction mp = prediction.getOrAddMapPrediction(predictedMap);
				mp.votes++;
				mp.confidence += (lpr.similarity / (float) lpr.startIndexes.size());
				total++;
			}
		}
		if (total>0) {
			for (MapPrediction mp: prediction.mapPredictions) {
				mp.confidence = ((float)mp.votes / (float)total) * (mp.confidence / (float)mp.votes);
			}
		}
	}
}
