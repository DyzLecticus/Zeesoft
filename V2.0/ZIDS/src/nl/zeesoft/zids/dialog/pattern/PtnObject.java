package nl.zeesoft.zids.dialog.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Locker;

public abstract class PtnObject extends Locker {
	public final static	String		TYPE_ALPHABETIC		= "ALPHABETIC";
	public final static	String		TYPE_NUMBER			= "NUMBER";
	public final static	String		TYPE_TIME			= "TIME";
	public final static	String		TYPE_ORDER			= "ORDER";
	public final static	String		TYPE_MONTH			= "MONTH";
	public final static	String		TYPE_DATE			= "DATE";
	public final static	String		TYPE_DURATION		= "DURATION";
	public final static	String		TYPE_CONFIRMATION	= "CONFIRMATION";
	public final static	String		TYPE_PREPOSITION	= "PREPOSITION";
	public final static	String		TYPE_OBJECT			= "OBJECT";

	public final static	String[]	TYPES				= {TYPE_ALPHABETIC,TYPE_NUMBER,TYPE_TIME,TYPE_ORDER,TYPE_MONTH,TYPE_DATE,TYPE_DURATION,TYPE_CONFIRMATION,TYPE_PREPOSITION,TYPE_OBJECT};
	
	public final static	String		SPECIFIER_UNIVERSAL	= "UNI";

	private String					baseValueType		= "";
	private String					typeSpecifier		= "";

	private List<String> 			patternStrings		= new ArrayList<String>();

	public PtnObject(String baseValueType, String typeSpecifier) {
		this.baseValueType = baseValueType;
		this.typeSpecifier = typeSpecifier;
	}

	/**
	 * @return the baseValueType
	 */
	public final String getBaseValueType() {
		return baseValueType;
	}

	/**
	 * @return the typeSpecifier
	 */
	public final String getTypeSpecifier() {
		return typeSpecifier;
	}

	public final String getValuePrefix() {
		return baseValueType + "_" + typeSpecifier;
	}
	
	public abstract void initializePatternStrings(PtnManager manager);
	
	public abstract String transformStringToValue(String str);
	public abstract String transformValueToString(String str);
		
	public final String getValueForString(String str) {
		return getValuePrefix() + ":" + transformStringToValue(str);
	}

	public final String getStringForValue(String str) {
		return transformValueToString(str.substring(getValuePrefix().length() + 1));
	}
	
	public final List<String> getPatternStrings() {
		lockMe(this);
		List<String> r = getPatternStringsNoLock();
		unlockMe(this);
		return r;
	}

	protected final int getNumPatternStrings() {
		int r = 0;
		lockMe(this);
		r = getNumPatternStringsNoLock();
		unlockMe(this);
		return r;
	}

	protected final int getPatternStringIndex(String str) {
		int r = -1;
		lockMe(this);
		r = getPatternStringIndexNoLock(str);
		unlockMe(this);
		return r;
	}
	
	protected final String getPatternString(int index) {
		String r = "";
		lockMe(this);
		r = getPatternStringNoLock(index);
		unlockMe(this);
		return r;
	}

	protected final void addPatternString(String str) {
		lockMe(this);
		addPatternStringNoLock(str);
		unlockMe(this);
	}

	protected final void clearPatternStrings() {
		lockMe(this);
		clearPatternStringsNoLock();
		unlockMe(this);
	}

	public final boolean stringMatchesPattern(String str) {
		lockMe(this);
		boolean r = stringMatchesPatternNoLock(str);
		unlockMe(this);
		return r;
	}
	
	/************ DO NOT EXPOSE NO LOCK VARIATIONS *************/ 
	
	protected List<String> getPatternStringsNoLock() {
		return new ArrayList<String>(patternStrings);
	}

	protected int getNumPatternStringsNoLock() {
		return patternStrings.size();
	}
	
	protected int getPatternStringIndexNoLock(String str) {
		return patternStrings.indexOf(str);
	}
	
	protected String getPatternStringNoLock(int index) {
		return patternStrings.get(index);
	}

	protected void addPatternStringNoLock(String str) {
		patternStrings.add(str);
	}

	protected void clearPatternStringsNoLock() {
		patternStrings.clear();
	}
	
	protected boolean stringMatchesPatternNoLock(String str) {
		return patternStrings.contains(str.toLowerCase());
	}

	public int getMaximumSymbols() {
		return 1;
	}
}
