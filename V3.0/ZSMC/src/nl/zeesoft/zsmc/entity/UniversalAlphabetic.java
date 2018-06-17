package nl.zeesoft.zsmc.entity;

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
		String r = "";
		if (externalValuesContains(str)) {
			r = getInternalValuePrefix() + str;
		}
		return r;
	}
	@Override
	public String getExternalValueForInternalValue(String str) {
		String r = "";
		if (internalValuesContains(str)) {
			r = str.substring(getInternalValuePrefix().length());
		}
		return r;
	}
	@Override
	public Object getTypeValueForInternalValue(String str) {
		String r = "";
		if (internalValuesContains(str)) {
			r = str.substring(getInternalValuePrefix().length());
		}
		return r;
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
