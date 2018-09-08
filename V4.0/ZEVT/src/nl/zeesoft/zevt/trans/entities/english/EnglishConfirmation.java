package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.EntityValueTranslator;

public class EnglishConfirmation extends EntityObject {
	public EnglishConfirmation(EntityValueTranslator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return LANG_ENG;
	}
	@Override
	public String getType() {
		return TYPE_CONFIRMATION;
	}
	@Override
	public int getMaximumSymbols() {
		return 4;
	}
	@Override
	public void initializeEntityValues() {
		addConfirmation("yes",true);
		addConfirmation("yep",true);
		addConfirmation("correct",true);
		addConfirmation("that is correct",true);
		addConfirmation("I confirm",true);
		
		addConfirmation("no",false);
		addConfirmation("nope",false);
		addConfirmation("incorrect",false);
		addConfirmation("that is incorrect",false);
		addConfirmation("that is not correct",false);
		addConfirmation("I do not confirm",false);
	}
	private void addConfirmation(String str,boolean value) {
		addEntityValue(str,"" + value,value);
	}
}
