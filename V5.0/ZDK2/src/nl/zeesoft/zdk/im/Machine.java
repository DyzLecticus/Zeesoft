package nl.zeesoft.zdk.im;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.im.pattern.PatternRecognizer;
import nl.zeesoft.zdk.im.pattern.PatternRecognizerResult;

public class Machine {
	public SimilarityCalculator 	calculator			= new SimilarityCalculator();
	public ObjectArrayList			history				= new ObjectArrayList();
	public List<PatternRecognizer>	patternRecognizers	= new ArrayList<PatternRecognizer>();
	
	public void generatePatternRecognizers(int num, int depth) {
		for (int i = 0; i < num; i++) {
			PatternRecognizer pr = new PatternRecognizer();
			for (int d = 0; d < depth * (i + 1); d += (i+1)) {
				pr.indexes.add(d);
			}
			patternRecognizers.add(pr);
		}
	}

	public void addInput(ObjectArray input) {
		history.add(input);
	}
	
	public List<PatternRecognizerResult> runPatternRecognizers(float minimumSimilarity) {
		List<PatternRecognizerResult> r = new ArrayList<PatternRecognizerResult>();
		for (PatternRecognizer pr: patternRecognizers) {
			PatternRecognizerResult result = pr.calculatePatternSimilarity(calculator, history);
			if (result.similarity>=minimumSimilarity) {
				result.addPredictions(history, result.similarity);
				r.add(result);
			}
		}
		return r;
	}
}
