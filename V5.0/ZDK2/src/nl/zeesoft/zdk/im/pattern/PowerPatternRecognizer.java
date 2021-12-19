package nl.zeesoft.zdk.im.pattern;

public class PowerPatternRecognizer extends PatternRecognizer {
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
