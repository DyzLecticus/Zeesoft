package nl.zeesoft.zspr.pattern.patterns;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;

public class UniversalNumber extends PatternObject {
	public UniversalNumber(Messenger msgr) {
		super(msgr,TYPE_NUMBER,SPECIFIER_UNIVERSAL);
	}
	
	@Override
	public String transformStringToValue(String str) {
		return str;
	}
	
	@Override
	public String transformValueToString(String str) {
		return str;
	}

	@Override
	protected boolean stringMatchesPatternNoLock(String str) {
		return isNumeric(str);
	}

	@Override
	public void initializePatternStrings(PatternManager manager) {
		// Empty
	}
}
