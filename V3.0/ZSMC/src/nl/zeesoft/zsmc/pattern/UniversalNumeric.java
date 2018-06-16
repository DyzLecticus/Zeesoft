package nl.zeesoft.zsmc.pattern;

public class UniversalNumeric extends EntityObject {
	@Override
	public String getType() {
		return TYPE_NUMERIC;
	}
	@Override
	public boolean externalValuesContains(String str) {
		return isNumeric(str);
	}
	@Override
	public boolean internalValuesContains(String str) {
		return str.startsWith(getInternalValuePrefix()) && isNumeric(str.substring(getInternalValuePrefix().length()));
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
