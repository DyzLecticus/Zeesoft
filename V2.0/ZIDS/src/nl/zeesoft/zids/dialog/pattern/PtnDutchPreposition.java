package nl.zeesoft.zids.dialog.pattern;


public class PtnDutchPreposition extends PtnObjectLiteralToValue {
	public PtnDutchPreposition() {
		super(TYPE_PREPOSITION,"NED");
	}
	
	@Override
	public void initializePatternStrings(PtnManager manager) {
		for (int i = 1; i<=8; i++) {
			String value = "" + i;
			String name = "";
			if (i==1) {
				name = "van";
			} else if (i==2) {
				name = "van de";
			} else if (i==3) {
				name = "van der";
			} else if (i==4) {
				name = "van den";
			} else if (i==5) {
				name = "de";
			} else if (i==6) {
				name = "der";
			} else if (i==7) {
				name = "den";
			} else if (i==8) {
				name = "ten";
			}
			addPatternStringAndValue(name,value);
		}
	}

	@Override
	public int getMaximumSymbols() {
		return 2;
	}
}
