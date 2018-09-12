package nl.zeesoft.zspp.prepro;

import nl.zeesoft.zodb.Languages;

public class DutchPreprocessor extends LanguagePreprocessor {
	public DutchPreprocessor() {
		setLanguage(Languages.NLD);
	}
	@Override
	protected void initializeReplacements() {
		addReplacement("'s ochtends","sochtends");
		addReplacement("'s morgens","smorgens");
		addReplacement("'s middags","smiddags");
		addReplacement("'s avonds","savonds");
		addReplacement("ochtendsvroeg","ochtends vroeg");
		addReplacement("morgensvroeg","morgens vroeg");
		addReplacement("avondslaat","avonds laat");
	}
}
