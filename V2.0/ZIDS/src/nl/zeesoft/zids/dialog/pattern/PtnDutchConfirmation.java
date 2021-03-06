package nl.zeesoft.zids.dialog.pattern;


public class PtnDutchConfirmation extends PtnObjectLiteralToValue {
	public PtnDutchConfirmation() {
		super(TYPE_CONFIRMATION,"NED");
	}
	
	@Override
	public void initializePatternStrings(PtnManager manager) {
		addPatternStringAndValue("ja","true");
		addPatternStringAndValue("ja bedankt","true");
		addPatternStringAndValue("ja dank je","true");
		addPatternStringAndValue("ja dank je wel","true");
		addPatternStringAndValue("ja dankjewel","true");
		addPatternStringAndValue("ja dank u","true");
		addPatternStringAndValue("ja dank u wel","true");
		addPatternStringAndValue("ja dankuwel","true");
		addPatternStringAndValue("ja , bedankt","true");
		addPatternStringAndValue("ja , dank je","true");
		addPatternStringAndValue("ja , dank je wel","true");
		addPatternStringAndValue("ja , dankjewel","true");
		addPatternStringAndValue("ja , dank u","true");
		addPatternStringAndValue("ja , dank u wel","true");
		addPatternStringAndValue("ja , dankuwel","true");

		addPatternStringAndValue("correct","true");
		addPatternStringAndValue("juist","true");
		addPatternStringAndValue("geweldig","true");
		addPatternStringAndValue("prima","true");
		addPatternStringAndValue("klopt","true");

		addPatternStringAndValue("dat is correct","true");
		addPatternStringAndValue("dat is juist","true");
		addPatternStringAndValue("dat is geweldig","true");
		addPatternStringAndValue("dat is prima","true");
		addPatternStringAndValue("dat klopt","true");
		
		addPatternStringAndValue("dat is waar","true");
		addPatternStringAndValue("dat is not onwaar","true");
		addPatternStringAndValue("waar","true");

		addPatternStringAndValue("nee","false");
		addPatternStringAndValue("nee bedankt","false");
		addPatternStringAndValue("nee dank je","false");
		addPatternStringAndValue("nee dank je wel","false");
		addPatternStringAndValue("nee dankjewel","false");
		addPatternStringAndValue("nee dank u","false");
		addPatternStringAndValue("nee dank u wel","false");
		addPatternStringAndValue("nee dankuwel","false");
		addPatternStringAndValue("nee , bedankt","false");
		addPatternStringAndValue("nee , dank je","false");
		addPatternStringAndValue("nee , dank je wel","false");
		addPatternStringAndValue("nee , dankjewel","false");
		addPatternStringAndValue("nee , dank u","false");
		addPatternStringAndValue("nee , dank u wel","false");
		addPatternStringAndValue("nee , dankuwel","false");

		addPatternStringAndValue("incorrect","false");
		addPatternStringAndValue("onjuist","false");
		addPatternStringAndValue("klopt niet","false");

		addPatternStringAndValue("dat is incorrect","false");
		addPatternStringAndValue("dat is onjuist","false");
		addPatternStringAndValue("dat klopt niet","false");

		addPatternStringAndValue("niet waar","false");
		addPatternStringAndValue("dat is niet waar","false");
		addPatternStringAndValue("dat is onwaar","false");
		addPatternStringAndValue("onwaar","false");
	}
}
