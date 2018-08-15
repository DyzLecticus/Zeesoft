package nl.zeesoft.zsd.entity;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;

public class UniversalSmiley extends EntityObject {
	private int counter = 0;
	
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_SMILEY;
	}
	
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
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
