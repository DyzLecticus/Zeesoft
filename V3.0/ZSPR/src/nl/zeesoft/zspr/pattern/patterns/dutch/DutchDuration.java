package nl.zeesoft.zspr.pattern.patterns.dutch;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class DutchDuration extends PatternObjectLiteralToValue {
	public DutchDuration(Messenger msgr) {
		super(msgr,TYPE_DURATION,Language.NLD);
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		DutchNumber numPattern = (DutchNumber) manager.getPatternByClassName(DutchNumber.class.getName());
		if (numPattern.getNumPatternStrings()==0) {
			numPattern.initializePatternStrings(manager);
		}
		
		for (int h = 0; h<100; h++) {
			for (int m = 0; m<=59; m++) {
				String value = minStrInt(h,2) + ":" + minStrInt(m,2);
				String H = numPattern.getPatternString(h);
				String M = numPattern.getPatternString(m);
				
				String hours = "uur";
				String minutes = "minuten";
				if (m==1) {
					minutes = "minuut";
				}
				if (h==0) {
					addPatternStringAndValue(M + " " + minutes,value);
					addPatternStringAndValue(m + " " + minutes,value);
					if (m==15) {
						addPatternStringAndValue("een kwartier",value);
					}
					if (m==30) {
						addPatternStringAndValue("een half uur",value);
					}
					if (m==45) {
						addPatternStringAndValue("drie kwartier",value);
					}
				} else {
					if (m==15) {
						addPatternStringAndValue(H + " " + hours + " en een kwartier",value);
					}
					if (m==30) {
						if (h==1) {
							addPatternStringAndValue("anderhalf uur",value);
						} else {
							addPatternStringAndValue(H + " en een half uur",value);
						}
					}
					if (m==45) {
						addPatternStringAndValue(H + " " + hours + " en drie kwartier",value);
					}
				}
				if (m==0) {
					addPatternStringAndValue(H + " " + hours,value);
					addPatternStringAndValue(h + " " + hours,value);
				}
				addPatternStringAndValue(H + " " + hours + " en " + M + " " + minutes,value);
				addPatternStringAndValue(h + " " + hours + " en " + m + " " + minutes,value);
			}
		}
	}

	@Override
	public int getMaximumSymbols() {
		return 5;
	}
}
