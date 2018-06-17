package nl.zeesoft.zsmc.entity;

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
		Integer r = null;
		if (internalValuesContains(str)) {
			r = Integer.parseInt(str.substring(getInternalValuePrefix().length()));
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
