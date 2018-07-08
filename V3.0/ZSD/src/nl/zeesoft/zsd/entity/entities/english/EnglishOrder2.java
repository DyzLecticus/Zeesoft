package nl.zeesoft.zsd.entity.entities.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class EnglishOrder2 extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_ENG;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_ORDER2;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		
		EnglishOrder eo = (EnglishOrder) translator.getEntityObject(BaseConfiguration.LANG_ENG,BaseConfiguration.TYPE_ORDER);
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
