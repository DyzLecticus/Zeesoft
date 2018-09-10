package nl.zeesoft.zevt.trans;

public class UniversalNumeric extends EntityObject {
	public UniversalNumeric(Translator t) {
		super(t);
	}

	@Override
	public String getType() {
		return TYPE_NUMERIC;
	}
	
	@Override
	public String getInternalValueForExternalValue(String str) {
		String r = "";
		if (isNumeric(str)) {
			r = getInternalValuePrefix() + str;
		}
		return r;
	}
	
	@Override
	public String getExternalValueForInternalValue(String str) {
		String r = "";
		if (str.startsWith(getInternalValuePrefix()) && isNumeric(str.substring(getInternalValuePrefix().length()))) {
			r = str.substring(getInternalValuePrefix().length());
		}
		return r;
	}
	
	@Override
	public Object getTypeValueForInternalValue(String str) {
		Integer r = null;
		if (str.startsWith(getInternalValuePrefix()) && isNumeric(str.substring(getInternalValuePrefix().length()))) {
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

	@Override
	public void initializeEntityValues() {
		// Ignore
	}
}
