package nl.zeesoft.zevt.trans;

public class UniversalSmiley extends EntityObject {
	private int counter = 0;
	
	public UniversalSmiley(Translator t) {
		super(t);
	}

	@Override
	public String getType() {
		return TYPE_SMILEY;
	}
	
	@Override
	public void initializeEntityValues() {
		addExpressions();
	}
	
	protected void addExpressions() {
		addExpression(":");
		addExpression(";");
		addExpression("8");
		addExpression("=");
	}

	protected void addExpression(String eyes) {
		addExpression(eyes,"");
		addExpression(eyes,"-");
		addExpression(eyes,"^");
		addExpression(eyes,"o");
		addExpression(eyes,"0");
		addExpression(eyes,"O");
	}
	
	protected void addExpression(String eyes,String nose) {
		addExpressionValue(eyes + nose + ")");
		addExpressionValue("(" + nose + eyes);
		addExpressionValue(eyes + nose + "]");
		addExpressionValue("[" + nose + eyes);
		addExpressionValue(eyes + nose + "}");
		addExpressionValue("{" + nose + eyes);
	}
	
	protected void addExpressionValue(String eVal) {
		counter++;
		addEntityValue(eVal,"" + counter,true);
	}
}
