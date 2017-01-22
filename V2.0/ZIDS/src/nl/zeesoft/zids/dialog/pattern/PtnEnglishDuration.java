package nl.zeesoft.zids.dialog.pattern;

import nl.zeesoft.zodb.Generic;

public class PtnEnglishDuration extends PtnObjectLiteralToValue {
	public PtnEnglishDuration() {
		super(TYPE_DURATION,"ENG");
	}
	
	@Override
	public void initializePatternStrings(PtnManager manager) {
		PtnEnglishNumber numPattern = (PtnEnglishNumber) manager.getPatternByClassName(PtnEnglishNumber.class.getName());
		if (numPattern.getNumPatternStrings()==0) {
			numPattern.initializePatternStrings(manager);
		}
		
		for (int h = 0; h<100; h++) {
			for (int m = 0; m<=59; m++) {
				String value = Generic.minStrInt(h,2) + ":" + Generic.minStrInt(m,2);
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
