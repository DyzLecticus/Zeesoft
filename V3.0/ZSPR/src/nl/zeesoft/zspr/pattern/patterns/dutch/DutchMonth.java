package nl.zeesoft.zspr.pattern.patterns.dutch;

import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class DutchMonth extends PatternObjectLiteralToValue {
	public DutchMonth() {
		super(TYPE_MONTH,"NED");
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		for (int i = 1; i<=12; i++) {
			String value = "" + i;
			String name = "";
			if (i==1) {
				name = "januari";
			} else if (i==2) {
				name = "februari";
			} else if (i==3) {
				name = "maart";
			} else if (i==4) {
				name = "april";
			} else if (i==5) {
				name = "mei";
			} else if (i==6) {
				name = "juni";
			} else if (i==7) {
				name = "juli";
			} else if (i==8) {
				name = "augustus";
			} else if (i==9) {
				name = "september";
			} else if (i==10) {
				name = "oktober";
			} else if (i==11) {
				name = "november";
			} else if (i==12) {
				name = "december";
			}
			addPatternStringAndValue(name,value);
		}
	}
}
