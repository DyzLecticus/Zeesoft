package nl.zeesoft.zdk.dai.recognize;

public class PowerPatternRecognizer extends ListPatternRecognizer {
	public PowerPatternRecognizer(int depth) {
		int i = 0;
		for (int d = 0; d < depth; d++) {
			indexes.add(i);
			if (i==0) {
				i = 1;
			} else {
				i = i * 2;
			}
		}
	}
}
