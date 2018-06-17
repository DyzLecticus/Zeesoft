package nl.zeesoft.zsmc.entity;

import nl.zeesoft.zsmc.EntityValueTranslator;

public class EnglishOrder2 extends EntityObject {
	@Override
	public String getLanguage() {
		return LANG_ENG;
	}
	@Override
	public String getType() {
		return TYPE_ORDER2;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		
		EnglishOrder eo = (EnglishOrder) translator.getEntityObject(LANG_ENG,TYPE_ORDER);
		if (!eo.isInitialized()) {
			eo.initialize(translator);
		}
		
		int max = translator.getMaximumOrder();
		if (translator.getMaximumOrder()>translator.getMaximumNumber()) {
			max = translator.getMaximumNumber();
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
