package nl.zeesoft.zids.dialog.pattern;

import nl.zeesoft.zodb.Generic;


public class PtnUniversalNumber extends PtnObject {
	public PtnUniversalNumber() {
		super(TYPE_NUMBER,SPECIFIER_UNIVERSAL);
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
		return Generic.isNumeric(str);
	}

	@Override
	public void initializePatternStrings(PtnManager manager) {
		// Empty
	}
}
