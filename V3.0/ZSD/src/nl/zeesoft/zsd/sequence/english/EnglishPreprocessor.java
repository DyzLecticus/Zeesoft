package nl.zeesoft.zsd.sequence.english;

import nl.zeesoft.zsd.sequence.SequencePreprocessor;

public class EnglishPreprocessor extends SequencePreprocessor {
	@Override
	public void initialize() {
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
	}
	@Override
	public void addReplacement(String key, String value) {
		if (value.endsWith(" not")) {
			if (key.endsWith("n't")) {
				super.addReplacement(key.replace("n't","nt"),value);
			}
			if (key.endsWith("ouldn't")) {
				super.addReplacement(key.replace("ouldn't","ould've"),value.replace(" not"," have"));
			}
		}
		super.addReplacement(key, value);
	}
	
}
