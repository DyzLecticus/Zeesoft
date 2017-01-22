package nl.zeesoft.zids.dialog.pattern;


public class PtnEnglishOrder2 extends PtnObjectLiteralToValue {
	public PtnEnglishOrder2() {
		super(TYPE_ORDER,"ENG2");
	}
	
	@Override
	public void initializePatternStrings(PtnManager manager) {
		PtnEnglishOrder orderPattern = (PtnEnglishOrder) manager.getPatternByClassName(PtnEnglishOrder.class.getName());
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
