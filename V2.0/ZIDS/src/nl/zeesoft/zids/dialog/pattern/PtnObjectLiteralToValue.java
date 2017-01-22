package nl.zeesoft.zids.dialog.pattern;

import java.util.ArrayList;
import java.util.List;

public abstract class PtnObjectLiteralToValue extends PtnObjectLiteral {
	private List<String> valuesForPatterns = new ArrayList<String>();
	
	public PtnObjectLiteralToValue(String baseValueType,String typeSpecifier) {
		super(baseValueType,typeSpecifier);
	}

	@Override
	protected String transformStringToValueNoLock(String str) {
		int val = Integer.parseInt(super.transformStringToValueNoLock(str));
		if (val>=0 && val<valuesForPatterns.size()) {
			str = valuesForPatterns.get(val);
		}
		return str;
	}

	@Override
	protected String transformValueToStringNoLock(String str) {
		int val = valuesForPatterns.indexOf(str);
		if (val>=0 && val<getNumPatternStringsNoLock()) {
			str = getPatternStringNoLock(val);
		}
		return str;
	}

	protected final void addPatternStringAndValue(String str,String val) {
		lockMe(this);
		addPatternStringAndValueNoLock(str,val);
		unlockMe(this);
	}

	protected void addPatternStringAndValueNoLock(String str,String val) {
		addPatternStringNoLock(str);
		valuesForPatterns.add(val);
	}
		
	@Override
	protected void clearPatternStringsNoLock() {
		super.clearPatternStringsNoLock();
		valuesForPatterns.clear();
	}
}
