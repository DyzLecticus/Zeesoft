package nl.zeesoft.zidm.dialog.pattern;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteral;

public class IndefiniteArticleEnglish extends PatternObjectLiteral {
	public static final String TYPE = "INDEFINITEARTICLE";
	
	public IndefiniteArticleEnglish(Messenger msgr) {
		super(msgr,TYPE,Language.ENG);
	}

	@Override
	public void initializePatternStrings(PatternManager manager) {
		addPatternString("a");
		addPatternString("an");
	}
}
