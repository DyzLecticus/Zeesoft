package nl.zeesoft.zevt.trans;

import nl.zeesoft.zevt.type.Types;

public class UniversalFrowny extends UniversalSmiley {
	public UniversalFrowny(Translator t) {
		super(t);
	}

	@Override
	public String getType() {
		return Types.FROWNY;
	}
	
	@Override
	protected void addExpression(String eyes,String nose) {
		addExpressionValue(eyes + nose + "(");
		addExpressionValue(")" + nose + eyes);
		addExpressionValue(eyes + nose + "[");
		addExpressionValue("]" + nose + eyes);
		addExpressionValue(eyes + nose + "{");
		addExpressionValue("}" + nose + eyes);
	}
}
