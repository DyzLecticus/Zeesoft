package nl.zeesoft.zids.dialog.pattern;

import nl.zeesoft.zodb.Generic;


public abstract class PtnObjectLiteral extends PtnObject {
	public PtnObjectLiteral(String baseValueType,String typeSpecifier) {
		super(baseValueType,typeSpecifier);
	}
	
	@Override
	public final String transformStringToValue(String str) {
		lockMe(this);
		String r = transformStringToValueNoLock(str);
		unlockMe(this);
		return r;
	}
	
	@Override
	public final String transformValueToString(String str) {
		lockMe(this);
		String r = transformValueToStringNoLock(str);
		unlockMe(this);
		return r;
	}

	protected String transformStringToValueNoLock(String str) {
		int index = getPatternStringIndexNoLock(str);
		return "" + index;
	}
	
	protected String transformValueToStringNoLock(String str) {
		if (!Generic.isNumeric(str)) {
			str = "";
		} else {
			int val = Integer.parseInt(str);
			if (val>=0 && val<getNumPatternStringsNoLock()) {
				str = getPatternStringNoLock(val);
			} else {
				str = "";
			}
		}
		return str;
	}
}
