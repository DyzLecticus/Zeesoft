package nl.zeesoft.zspr.pattern.patterns;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;


public class UniversalTime extends PatternObject {
	public UniversalTime(Messenger msgr) {
		super(msgr,TYPE_TIME,SPECIFIER_UNIVERSAL);
	}
	
	@Override
	public String transformStringToValue(String str) {
		return correctTimeString(str);
	}
	
	@Override
	public String transformValueToString(String str) {
		return correctTimeString(str);
	}

	@Override
	protected boolean stringMatchesPatternNoLock(String str) {
		return super.stringMatchesPatternNoLock(correctTimeString(str));
	}

	@Override
	public void initializePatternStrings(PatternManager manager) {
		for (int h = 0; h<24; h++) {
			String HH = minStrInt(h,2);
			for (int m = 0; m<60; m++) {
				String MM = minStrInt(m,2);
				for (int s = 0; s<60; s++) {
					String SS = minStrInt(s,2);
					addPatternString(HH + ":" + MM + ":" + SS);
				}
			}
		}
	}

	private String correctTimeString(String str) {
		if (str.length()>=3 && str.indexOf(":")==1) {
			str = "0" + str;
		}
		if (
			(str.length()==5 && str.indexOf(":")==2) ||
			(str.length()==6 && str.indexOf(":")==2 && str.indexOf(":",3)==5)
			) {
			if (str.length()==5) {
				str += ":00";
			} else {
				str += "00";
			}
		}
		return str;
	}
}
