package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;

public class EnglishOrder2 extends EntityObject {
	public EnglishOrder2(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return LANG_ENG;
	}
	@Override
	public String getType() {
		return TYPE_ORDER2;
	}
	@Override
	public void initializeEntityValues() {
		EnglishOrder eo = (EnglishOrder) getTranslator().getEntityObject(LANG_ENG,TYPE_ORDER);
		if (!eo.isInitialized()) {
			eo.initialize();
		}
		
		int max = getTranslator().getMaximumOrder();
		if (getTranslator().getMaximumOrder()>getTranslator().getMaximumNumber()) {
			max = getTranslator().getMaximumNumber();
		}

		for (int i = 1; i<=max; i++) {
			String value = "" + i;
			String num = value;
			String suffix = eo.getExternalValueForInternalValue("" + i);
			suffix = suffix.substring(suffix.length() - 2);
			addEntityValue(num + suffix,value,i);
		}
	}
}
