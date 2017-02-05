package nl.zeesoft.zspr.pattern.patterns.english;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class EnglishDuration extends PatternObjectLiteralToValue {
	public EnglishDuration(Messenger msgr) {
		super(msgr,TYPE_DURATION,Language.ENG);
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		EnglishNumber numPattern = (EnglishNumber) manager.getPatternByClassName(EnglishNumber.class.getName());
		if (numPattern.getNumPatternStrings()==0) {
			numPattern.initializePatternStrings(manager);
		}
		
		for (int h = 0; h<100; h++) {
			for (int m = 0; m<=59; m++) {
				String value = minStrInt(h,2) + ":" + minStrInt(m,2);
				String H = numPattern.getPatternString(h);
				String M = numPattern.getPatternString(m);
				
				String hours = "hours";
				if (h==1) {
					hours = "hour";
				}
				String minutes = "minutes";
				if (m==1) {
					minutes = "minute";
				}
				if (h==0) {
					addPatternStringAndValue(M + " " + minutes,value);
					addPatternStringAndValue(m + " " + minutes,value);
					if (m==30) {
						addPatternStringAndValue("half an hour",value);
					}
				} else {
					if (m==30) {
						addPatternStringAndValue(H + " and a half " + hours,value);
					}
				}
				if (m==0) {
					addPatternStringAndValue(H + " " + hours,value);
					addPatternStringAndValue(h + " " + hours,value);
				}
				addPatternStringAndValue(H + " " + hours + " and " + M + " " + minutes,value);
				addPatternStringAndValue(h + " " + hours + " and " + m + " " + minutes,value);
			}
		}
	}

	@Override
	public int getMaximumSymbols() {
		return 5;
	}
}
