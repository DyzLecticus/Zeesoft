package nl.zeesoft.zids.dialog.pattern;

import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zacs.simulator.SimController;
import nl.zeesoft.zodb.Generic;


public class PtnUniversalAlphabetic extends PtnObject {
	public PtnUniversalAlphabetic() {
		super(TYPE_ALPHABETIC,SPECIFIER_UNIVERSAL);
	}
	
	@Override
	public String transformStringToValue(String str) {
		return str.replaceAll("\\|","");
	}
	
	@Override
	public String transformValueToString(String str) {
		return str;
	}

	@Override
	protected boolean stringMatchesPatternNoLock(String str) {
		Symbol symbol = SimController.getInstance().getSymbolLikeCode(str);
		return symbol==null && !Generic.stringBuilderContainsOneOfNonAllowedCharacters(new StringBuilder(str),Generic.ALPHABETIC);
	}

	@Override
	public void initializePatternStrings(PtnManager manager) {
		// Empty
	}
}
