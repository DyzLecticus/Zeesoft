package nl.zeesoft.zsd.entity.entities.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class DutchConfirmation extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_NLD;
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
