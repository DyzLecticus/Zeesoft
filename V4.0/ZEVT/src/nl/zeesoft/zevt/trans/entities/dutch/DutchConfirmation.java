package nl.zeesoft.zevt.trans.entities.dutch;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zodb.Languages;

public class DutchConfirmation extends EntityObject {
	public DutchConfirmation(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.NLD;
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
		addConfirmation("ja",true);
		addConfirmation("juist",true);
		addConfirmation("dat is juist",true);
		addConfirmation("jazeker",true);
		addConfirmation("dat klopt",true);
		addConfirmation("correct",true);
		addConfirmation("dat is correct",true);
		
		addConfirmation("nee",false);
		addConfirmation("onjuist",false);
		addConfirmation("dat is onjuist",false);
		addConfirmation("dat klopt niet",false);
		addConfirmation("incorrect",false);
		addConfirmation("dat is incorrect",false);
		addConfirmation("dat is niet correct",false);
	}
	private void addConfirmation(String str,boolean value) {
		addEntityValue(str,"" + value,value);
	}
}
