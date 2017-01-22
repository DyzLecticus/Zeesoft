package nl.zeesoft.zids.dialog.pattern;


public class PtnEnglishMonth extends PtnObjectLiteralToValue {
	public PtnEnglishMonth() {
		super(TYPE_MONTH,"ENG");
	}
	
	@Override
	public void initializePatternStrings(PtnManager manager) {
		for (int i = 1; i<=12; i++) {
			String value = "" + i;
			String name = "";
			if (i==1) {
				name = "january";
			} else if (i==2) {
				name = "february";
			} else if (i==3) {
				name = "march";
			} else if (i==4) {
				name = "april";
			} else if (i==5) {
				name = "may";
			} else if (i==6) {
				name = "june";
			} else if (i==7) {
				name = "july";
			} else if (i==8) {
				name = "august";
			} else if (i==9) {
				name = "september";
			} else if (i==10) {
				name = "october";
			} else if (i==11) {
				name = "november";
			} else if (i==12) {
				name = "december";
			}
			addPatternStringAndValue(name,value);
		}
	}
}
