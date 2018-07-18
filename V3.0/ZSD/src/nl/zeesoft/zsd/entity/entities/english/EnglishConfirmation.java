package nl.zeesoft.zsd.entity.entities.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class EnglishConfirmation extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_ENG;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_CONFIRMATION;
	}
	@Override
	public int getMaximumSymbols() {
		return 4;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);

		addConfirmation("yes",true);
		addConfirmation("yep",true);
		addConfirmation("correct",true);
		addConfirmation("that is correct",true);
		addConfirmation("I confirm",true);
		
		addConfirmation("no",false);
		addConfirmation("nope",false);
		addConfirmation("incorrect",false);
		addConfirmation("that is incorrect",false);
		addConfirmation("I do not confirm",false);
	}
	private void addConfirmation(String str,boolean value) {
		addEntityValue(str,"" + value,value);
	}
}
