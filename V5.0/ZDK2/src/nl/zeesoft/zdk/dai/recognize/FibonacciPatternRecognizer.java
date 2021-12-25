package nl.zeesoft.zdk.dai.recognize;

public class FibonacciPatternRecognizer extends ListPatternRecognizer {
	public FibonacciPatternRecognizer(int depth) {
		int i = 0;
		for (int d = 0; d <= depth; d++) {
			indexes.add(i);
			if (i==0) {
				i = 1;
			} else {
				i += indexes.get(d - 1);
			}
		}
		if (indexes.size()>=2) {
			indexes.remove(1);
		}
	}
}
