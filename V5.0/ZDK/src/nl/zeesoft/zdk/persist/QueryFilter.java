package nl.zeesoft.zdk.persist;

@PersistableObject
public class QueryFilter {
	public static final String		CLASS_NAME		= "@className";
	
	public static final String		EQUALS			= "equals";
	public static final String		CONTAINS		= "contains";
	public static final String		LESS			= "less";
	public static final String		GREATER			= "greater";
	
	public static final String[]	OPERATORS		= {EQUALS,CONTAINS,LESS,GREATER};
	
	@PersistableProperty
	protected String 				propertyName	= "";
	@PersistableProperty
	protected Boolean 				invert			= false;
	@PersistableProperty
	protected String 				operator		= EQUALS;
	@PersistableProperty
	protected Object 				value			= null;
	
	public QueryFilter copy() {
		QueryFilter r = new QueryFilter();
		r.propertyName = this.propertyName;
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
