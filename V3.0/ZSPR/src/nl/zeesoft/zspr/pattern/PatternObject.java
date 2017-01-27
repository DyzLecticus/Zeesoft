package nl.zeesoft.zspr.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.thread.Locker;

/**
 * Abstract pattern object.
 */
public abstract class PatternObject extends Locker {
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
	private String					valueConcatenator	= "";
	private String					orConcatenator		= "";

	private List<String> 			patternStrings		= new ArrayList<String>();

	/**
	 * @param baseValueType The base value type specifies the primary data type this pattern will recognize
	 * @param typeSpecifier The type specifier is a string that is used to make this pattern unique among the other patterns in the pattern manager
	 */
	public PatternObject(String baseValueType, String typeSpecifier) {
		this.baseValueType = baseValueType;
		this.typeSpecifier = typeSpecifier;
	}
	
	/**
	 * Returns the value prefix used for values this pattern translates.
	 * 
	 * @return The value prefix
	 */
	public final String getValuePrefix() {
		return baseValueType + "_" + typeSpecifier;
	}
	
	/**
	 * Initialize the strings for this pattern.
	 * 
	 * @param manager The manager this pattern belongs to
	 */
	public abstract void initializePatternStrings(PatternManager manager);
	
	/**
	 * Transform a certain string to a value for this pattern.
	 * 
	 * Locking must be programmed explicitly and use the '*NoLock' methods of this class.
	 * 
	 * @param str The string to translate
	 * @return The value
	 */
	protected abstract String transformStringToValue(String str);
	
	/**
	 * Transform a certain value to a string for this pattern.
	 * 
	 * Locking must be programmed explicitly and use the '*NoLock' methods of this class.
	 * 
	 * @param str The value to translate
	 * @return The string
	 */
	protected abstract String transformValueToString(String str);
		
	/**
	 * Returns the maximum symbol sequence length this pattern will recognize.
	 * 
	 * @return The maximum symbol sequence length
	 */
	public int getMaximumSymbols() {
		return 1;
	}

	/**
	 * Returns a value translation for a certain string.
	 *  
	 * @param str The string
	 * @return The value
	 */
	public final String getValueForString(String str) {
		return getValuePrefix() + valueConcatenator + transformStringToValue(str);
	}

	/**
	 * Returns a string translation for a certain value.
	 * 
	 * @param str The value
	 * @return The string
	 */
	public final String getStringForValue(String str) {
		return transformValueToString(str.substring(getValuePrefix().length() + 1));
	}
	
	/**
	 * Returns the number of pattern strings
	 * 
	 * @return The number of pattern strings
	 */
	public final int getNumPatternStrings() {
		int r = 0;
		lockMe(this);
		r = getNumPatternStringsNoLock();
		unlockMe(this);
		return r;
	}
	
	/**
	 * Returns a specific pattern string or an empty string.
	 * 
	 * @param index The index of the pattern string
	 * @return The pattern string or an empty string
	 */
	public final String getPatternString(int index) {
		String r = "";
		lockMe(this);
		r = getPatternStringNoLock(index);
		unlockMe(this);
		return r;
	}

	/**
	 * Adds a specific pattern string to the pattern.
	 * 
	 * @param str The pattern string
	 */
	protected final void addPatternString(String str) {
		lockMe(this);
		addPatternStringNoLock(str);
		unlockMe(this);
	}

	/**
	 * Removes all pattern strings from the pattern.
	 */
	protected final void clearPatternStrings() {
		lockMe(this);
		clearPatternStringsNoLock();
		unlockMe(this);
	}

	/**
	 * Returns true if a certain string matches this pattern.
	 * 
	 * @param str The string to match
	 * @return True if the string matches this pattern
	 */
	public final boolean stringMatchesPattern(String str) {
		lockMe(this);
		boolean r = stringMatchesPatternNoLock(str);
		unlockMe(this);
		return r;
	}

	/**
	 * Returns the value concatenator for this pattern.
	 * 
	 * @return The value concatenator
	 */
	public final String getValueConcatenator() {
		String r = "";
		lockMe(this);
		r = getValueConcatenatorNoLock();
		unlockMe(this);
		return r;
	}

	/**
	 * Returns the or concatenator for this pattern.
	 * 
	 * @return The or concatenator
	 */
	public final String getOrConcatenator() {
		String r = "";
		lockMe(this);
		r = getOrConcatenatorNoLock();
		unlockMe(this);
		return r;
	}
	
	/**
	 * Sets the concatenators for this pattern.
	 * 
	 * @param value The value concatenator
	 * @param or The or concatenator
	 */
	protected void setConcatenators(String value,String or) {
		lockMe(this);
		this.valueConcatenator = value;
		this.orConcatenator = or;
		unlockMe(this);
	}

	/**
	 * Returns the base value type.
	 * 
	 * @return The base value type
	 */
	public final String getBaseValueType() {
		return baseValueType;
	}

	/**
	 * Returns the base value type specifier.
	 * 
	 * @return The base value type specifier
	 */
	public final String getTypeSpecifier() {
		return typeSpecifier;
	}
	
	protected final int getNumPatternStringsNoLock() {
		return patternStrings.size();
	}
	
	protected final int getPatternStringIndexNoLock(String str) {
		return patternStrings.indexOf(str);
	}
	
	protected final String getPatternStringNoLock(int index) {
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
	
	protected final String getValueConcatenatorNoLock() {
		return valueConcatenator;
	}

	protected final String getOrConcatenatorNoLock() {
		return orConcatenator;
	}
	
	/**
	 * Returns true if a string is alphabetic.
	 * 
	 * @param str The string
	 * @return True if the string is alphabetic
	 */
	public static final boolean isAlphabetic(String str) {
		boolean r = true;
		for (char c:str.toCharArray()) {
	        if (!Character.isAlphabetic(c)) {
	        	r = false;
	        	break;
	        }
	    }
	    return r;
	}

	/**
	 * Returns true if a string is numeric.
	 * 
	 * @param str The string
	 * @return True if the string is numeric
	 */
	public static final boolean isNumeric(String str) {
		boolean r = true;
		for (char c:str.toCharArray()) {
	        if (!Character.isDigit(c)) {
	        	r = false;
	        	break;
	        }
	    }
	    return r;
	}
}
