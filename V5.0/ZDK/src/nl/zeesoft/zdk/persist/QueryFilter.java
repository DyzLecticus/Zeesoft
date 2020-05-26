package nl.zeesoft.zdk.persist;

@PersistableObject
public class QueryFilter {
	protected static final String	CLASS_NAME				= "@className";
	
	protected static final String	EQUALS					= "equals";
	protected static final String	CONTAINS				= "contains";
	protected static final String	LESS					= "less";
	protected static final String	GREATER					= "greater";
	
	protected static final String[]	OPERATORS				= {EQUALS,CONTAINS,LESS,GREATER};
	
	@PersistableProperty
	protected String 				methodOrPropertyName	= "";
	@PersistableProperty
	protected Boolean 				invert					= false;
	@PersistableProperty
	protected String 				operator				= EQUALS;
	@PersistableProperty
	protected Object 				value					= null;
	
	public QueryFilter copy() {
		QueryFilter r = new QueryFilter();
		r.methodOrPropertyName = this.methodOrPropertyName;
		r.invert = this.invert;
		r.operator = this.operator;
		r.value = this.value;
		return r;
	}
	
	public static boolean isValidOperator(String operator) {
		boolean r = false;
		if (operator!=null) {
			r = false;
			for (int i = 0; i < OPERATORS.length; i++) {
				if (operator.equals(OPERATORS[i])) {
					r = true;
					break;
				}
			}
		}
		return r;
	}
}
