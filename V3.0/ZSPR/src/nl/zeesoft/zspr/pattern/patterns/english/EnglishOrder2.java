package nl.zeesoft.zspr.pattern.patterns.english;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class EnglishOrder2 extends PatternObjectLiteralToValue {
	public EnglishOrder2(Messenger msgr) {
		super(msgr,TYPE_ORDER,"ENG2");
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		EnglishOrder orderPattern = (EnglishOrder) manager.getPatternByClassName(EnglishOrder.class.getName());
		if (orderPattern.getNumPatternStrings()==0) {
			orderPattern.initializePatternStrings(manager);
		}
		
		int max = manager.getMaximumOrder();
		if (manager.getMaximumOrder()>manager.getMaximumNumber()) {
			max = manager.getMaximumNumber();
		}

		for (int i = 1; i<=max; i++) {
			String value = "" + i;
			String num = value;
			String suffix = orderPattern.getPatternString(i - 1);
			suffix = suffix.substring(suffix.length() - 2);
			addPatternStringAndValue(num + suffix,value);
		}
	}
}
