package nl.zeesoft.zevt.trans;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class UniversalMathematic extends EntityObject {
	public static final float	RESULT_INFINITY		= 999999999.9F;
	
	public static final String	MULTIPLICATION		= "M";
	public static final String	DIVISION			= "D";
	public static final String	ADDITION			= "A";
	public static final String	SUBTRACTION			= "S";

	public static final String	EQUALS				= "EQ";
	public static final String	NOT_EQUALS			= "NE";
	public static final String	GREATER_THAN		= "GT";
	public static final String	GREATER_OR_EQUALS	= "GE";
	public static final String	LESS_THAN			= "LT";
	public static final String	LESS_OR_EQUALS		= "LE";
	
	@Override
	public String getType() {
		return TYPE_MATHEMATIC;
	}
	
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		addSymbol("*",MULTIPLICATION);
		addSymbol("/",DIVISION);
		addSymbol("+",ADDITION);
		addSymbol("-",SUBTRACTION);

		addSymbol("==",EQUALS);
		addSymbol("!=",NOT_EQUALS);
		addSymbol(">",GREATER_THAN);
		addSymbol(">=",GREATER_OR_EQUALS);
		addSymbol("<",LESS_THAN);
		addSymbol("<=",LESS_OR_EQUALS);
	}
	
	public float calculate(ZStringSymbolParser expression) {
		float r = 0F;
		
		Float f1 = null;
		Float f2 = null;
		String operator = "";
		ZStringSymbolParser remainder = new ZStringSymbolParser();
		
		boolean infinity = false;
		
		List<String> symbols = expression.toSymbolsPunctuated();
		for (String symbol: symbols) {
			Float f = null;
			try {
				f = Float.parseFloat(symbol);
			} catch(NumberFormatException e) {
				// Ignore
			}
			if (f!=null) {
				if (f1==null) {
					f1 = Float.parseFloat(symbol);
				} else if (f2==null) {
					f2 = Float.parseFloat(symbol);
				}
			} else if (
				symbol.equals(MULTIPLICATION) ||
				symbol.equals(DIVISION) ||
				symbol.equals(ADDITION) ||
				symbol.equals(SUBTRACTION)
				){
				operator = symbol;
			}
			if (operator.equals(ADDITION) || operator.equals(SUBTRACTION)) {
				if (remainder.length()>0) {
					remainder.append(" ");
				}
				if (f1!=null) {
					remainder.append("" + f1);
				} else {
					remainder.append("" + r);
				}
				remainder.append(" ");
				remainder.append("" + operator);
				
				r = 0F;
				f1 = null;
				f2 = null;
				operator = "";
			}
			if (f1!=null && f2!=null && operator.length()>0) {
				if (operator.equals(MULTIPLICATION)) {
					r = (f1 * f2);
					f1 = new Float(r);
					f2 = null;
				} else if (operator.equals(DIVISION)) {
					if (f2==0F) {
						infinity = true;
						break;
					}
					r = (f1 / f2);
					f1 = null;
					f2 = new Float(r);
				}
			}
		}

		if (!infinity) {
			if (remainder.length()>0) {
				if (f1!=null) {
					remainder.append(" ");
					remainder.append("" + f1);
				}
				r = calculateRemainder(remainder);
			} else if (f1!=null) {
				r = f1;
			}
		} else {
			r = RESULT_INFINITY;
		}
		
		return r;
	}
	
	public boolean evaluate(ZStringSymbolParser expression) {
		Boolean r = null;
		List<String> symbols = expression.toSymbolsPunctuated();
		for (String operator: getLogicalOperators()) {
			if (symbols.contains(operator)) {
				List<ZStringBuilder> exprs = expression.split(" " + operator + " ");
				if (exprs.size() == 2) {
					float f1 = calculate(new ZStringSymbolParser(exprs.get(0)));
					float f2 = calculate(new ZStringSymbolParser(exprs.get(1)));
					if (operator.equals(EQUALS)) {
						r = new Boolean(f1 == f2);
					} else if (operator.equals(NOT_EQUALS)) {
						r = new Boolean(f1 != f2);
					} else if (operator.equals(GREATER_THAN)) {
						r = new Boolean(f1 > f2);
					} else if (operator.equals(GREATER_OR_EQUALS)) {
						r = new Boolean(f1 >= f2);
					} else if (operator.equals(LESS_THAN)) {
						r = new Boolean(f1 < f2);
					} else if (operator.equals(LESS_OR_EQUALS)) {
						r = new Boolean(f1 <= f2);
					}
				}
				break;
			}
		}
		return r;
	}
	
	public static List<String> getOperators() {
		List<String> r = new ArrayList<String>();
		r.add(MULTIPLICATION);
		r.add(DIVISION);
		r.add(ADDITION);
		r.add(SUBTRACTION);
		return r;
	}
	
	public static List<String> getLogicalOperators() {
		List<String> r = new ArrayList<String>();
		r.add(EQUALS);
		r.add(NOT_EQUALS);
		r.add(GREATER_THAN);
		r.add(GREATER_OR_EQUALS);
		r.add(LESS_THAN);
		r.add(LESS_OR_EQUALS);
		return r;
	}
	
	private float calculateRemainder(ZStringSymbolParser remainder) {
		float r = 0F;

		Float f1 = null;
		Float f2 = null;
		String operator = "";
		
		List<String> symbols = remainder.toSymbolsPunctuated();
		for (String symbol: symbols) {
			Float f = null;
			try {
				f = Float.parseFloat(symbol);
			} catch(NumberFormatException e) {
				// Ignore
			}
			if (f!=null) {
				if (f1==null) {
					f1 = Float.parseFloat(symbol);
				} else if (f2==null) {
					f2 = Float.parseFloat(symbol);
				}
			} else if (
				symbol.equals(MULTIPLICATION) ||
				symbol.equals(DIVISION) ||
				symbol.equals(ADDITION) ||
				symbol.equals(SUBTRACTION)
				){
				operator = symbol;
			}
			if (operator.equals(MULTIPLICATION) || operator.equals(DIVISION)) {
				r = 0F;
				f1 = null;
				f2 = null;
				operator = "";
			}
			if (f1!=null && f2!=null && operator.length()>0) {
				if (operator.equals(ADDITION)) {
					r = (f1 + f2);
					f1 = new Float(r);
					f2 = null;
				} else if (operator.equals(SUBTRACTION)) {
					r = (f1 - f2);
					f1 = null;
					f2 = new Float(r);
				}
			}
		}
		
		return r;
	}
	
	private void addSymbol(String eVal,String iVal) {
		addEntityValue(eVal,iVal,iVal);
	}
}
