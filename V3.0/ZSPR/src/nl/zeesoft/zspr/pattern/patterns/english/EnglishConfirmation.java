package nl.zeesoft.zspr.pattern.patterns.english;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class EnglishConfirmation extends PatternObjectLiteralToValue {
	public EnglishConfirmation(Messenger msgr) {
		super(msgr,TYPE_CONFIRMATION,"ENG");
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		addPatternStringAndValue("yes","true");
		addPatternStringAndValue("yes thanks","true");
		addPatternStringAndValue("yes thank you","true");
		addPatternStringAndValue("yes , thanks","true");
		addPatternStringAndValue("yes , thank you","true");
		
		addPatternStringAndValue("yep","true");
		addPatternStringAndValue("correct","true");
		addPatternStringAndValue("cool","true");
		addPatternStringAndValue("check","true");

		addPatternStringAndValue("excellent","true");
		addPatternStringAndValue("awesome","true");
		addPatternStringAndValue("that is excellent","true");
		addPatternStringAndValue("that is awesome","true");
		addPatternStringAndValue("that's excellent","true");
		addPatternStringAndValue("that's awesome","true");

		addPatternStringAndValue("that is true","true");
		addPatternStringAndValue("that is not false","true");
		addPatternStringAndValue("that's true","true");
		addPatternStringAndValue("that's not false","true");
		addPatternStringAndValue("true","true");
		
		addPatternStringAndValue("no","false");
		addPatternStringAndValue("no thanks","false");
		addPatternStringAndValue("no thank you","false");
		addPatternStringAndValue("no , thanks","false");
		addPatternStringAndValue("no , thank you","false");
		
		addPatternStringAndValue("nope","false");
		addPatternStringAndValue("incorrect","false");

		addPatternStringAndValue("that is incorrect","false");
		addPatternStringAndValue("that's incorrect","false");

		addPatternStringAndValue("that is not true","false");
		addPatternStringAndValue("that is false","false");
		addPatternStringAndValue("that's not true","false");
		addPatternStringAndValue("that's false","false");
		addPatternStringAndValue("false","false");
	}
}
