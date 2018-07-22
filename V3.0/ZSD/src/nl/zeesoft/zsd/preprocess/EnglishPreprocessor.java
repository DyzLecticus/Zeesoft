package nl.zeesoft.zsd.preprocess;

import nl.zeesoft.zsd.sequence.Analyzer;

public class EnglishPreprocessor extends PreprocessorInstance {
	@Override
	public void initialize() {
		addReplacement(" oclock"," o'clock");
		addReplacement("won't","will not");
		addReplacement("wouldn't","would not");
		addReplacement("can't","can not");
		addReplacement("couldn't","could not");
		addReplacement("don't","do not");
		addReplacement("didn't","did not");
		addReplacement("shan't","shall not");
		addReplacement("shouldn't","should not");
		addReplacement("aren't","are not");
		addReplacement("weren't","were not");
		addReplacement("hasn't","has not");
		addReplacement("hadn't","had not");
		addReplacement("isn't","is not");
		addReplacement("mustn't","must not");
		addReplacement("'ve ","  have ");
		addReplacement("'d have"," would have");
		addReplacement(" what's"," what is");
		addReplacement(" that's"," that is");
		addReplacement(" it's"," it is");
		addReplacement("What's","What is");
		addReplacement("That's","That is");
		addReplacement("It's","It is");
	}
	@Override
	public void addReplacement(String key, String value) {
		if (value.endsWith(" not") && key.endsWith("n't")) {
			super.addReplacement(key.replace("n't","nt"),value);
			super.addReplacement(Analyzer.upperCaseFirst(key.replace("n't","nt")),Analyzer.upperCaseFirst(value));
			super.addReplacement(Analyzer.upperCaseFirst(key),Analyzer.upperCaseFirst(value));
		}
		super.addReplacement(key, value);
	}
}
