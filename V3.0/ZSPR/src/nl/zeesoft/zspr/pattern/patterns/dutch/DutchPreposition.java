package nl.zeesoft.zspr.pattern.patterns.dutch;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class DutchPreposition extends PatternObjectLiteralToValue {
	public DutchPreposition(Messenger msgr) {
		super(msgr,TYPE_PREPOSITION,"NED");
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		for (int i = 1; i<=8; i++) {
			String value = "" + i;
			String name = "";
			if (i==1) {
				name = "van";
			} else if (i==2) {
				name = "van de";
			} else if (i==3) {
				name = "van der";
			} else if (i==4) {
				name = "van den";
			} else if (i==5) {
				name = "de";
			} else if (i==6) {
				name = "der";
			} else if (i==7) {
				name = "den";
			} else if (i==8) {
				name = "ten";
			}
			addPatternStringAndValue(name,value);
		}
	}

	@Override
	public int getMaximumSymbols() {
		return 2;
	}
}
