package nl.zeesoft.zsc.analyze;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerVectorizer {
	private List<AnalyzerVectorizerWorker>		workers		= new ArrayList<AnalyzerVectorizerWorker>();
	
	public AnalyzerVectorizer(SymbolAnalyzer analyzer, int numWorkers) {
		for (int w = 0; w < numWorkers; w++) {
			AnalyzerVectorizerWorker worker = new AnalyzerVectorizerWorker(analyzer);
			workers.add(worker);
		}
		int i = 0;
		for (String symbol: analyzer.getKnownSymbols()) {
			int index = (i % numWorkers);
			workers.get(index).symbols.add(symbol);
			i++;
		}
	}
	
	public void vectorize() {
		for (AnalyzerVectorizerWorker worker: workers) {
			worker.start();
		}
	}
	
	public boolean isDone() {
		boolean r = true;
		for (AnalyzerVectorizerWorker worker: workers) {
			if (worker.isWorking()) {
				r = false;
				break;
			}
		}
		return r;
	}
}
