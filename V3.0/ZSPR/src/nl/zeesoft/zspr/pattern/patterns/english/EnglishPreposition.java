package nl.zeesoft.zspr.pattern.patterns.english;

import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class EnglishPreposition extends PatternObjectLiteralToValue {
	public EnglishPreposition() {
		super(TYPE_PREPOSITION,"ENG");
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		for (int i = 1; i<=4; i++) {
			String value = "" + i;
			String name = "";
			if (i==1) {
				name = "of";
			} else if (i==2) {
				name = "of the";
			} else if (i==3) {
				name = "from";
			} else if (i==4) {
				name = "from the";
			}
			addPatternStringAndValue(name,value);
		}
	}

	@Override
	public int getMaximumSymbols() {
		return 2;
	}
}
