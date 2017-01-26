package nl.zeesoft.zspr.pattern.patterns;

import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;

public class UniversalAlphabetic extends PatternObject {
	public UniversalAlphabetic() {
		super(TYPE_ALPHABETIC,SPECIFIER_UNIVERSAL);
	}
	
	@Override
	public String transformStringToValue(String str) {
		return str.replaceAll("\\|","");
	}
	
	@Override
	public String transformValueToString(String str) {
		return str;
	}

	@Override
	protected boolean stringMatchesPatternNoLock(String str) {
		return isAlphabetic(str);
	}

	@Override
	public void initializePatternStrings(PatternManager manager) {
		// Empty
	}
}
