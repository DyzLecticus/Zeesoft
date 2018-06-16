package nl.zeesoft.zsmc.pattern;

public class UniversalAlphabetic extends EntityObject {
	@Override
	public boolean externalValuesContains(String str) {
		return isAlphabetic(str);
	}
	@Override
	public boolean internalValuesContains(String str) {
		return str.startsWith(getInternalValuePrefix()) && isAlphabetic(str.substring(getInternalValuePrefix().length()));
	}
	@Override
	public String getInternalValueForExternalValue(String str) {
		return getInternalValuePrefix() + str;
	}
	@Override
	public String getExternalValueForInternalValue(String str) {
		return str.substring(getInternalValuePrefix().length());
	}
	@Override
	public Object getTypeValueForInternalValue(String str) {
		return str.substring(getInternalValuePrefix().length());
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
}
