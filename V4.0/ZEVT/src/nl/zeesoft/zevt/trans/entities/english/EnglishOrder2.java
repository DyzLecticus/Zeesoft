package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.EntityValue;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.lang.Languages;

public class EnglishOrder2 extends EntityObject {
	public EnglishOrder2(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.ENG;
	}
	@Override
	public String getType() {
		return Types.ORDER2;
	}
	@Override
	public void initializeEntityValues() {
		EnglishOrder eo = (EnglishOrder) getTranslator().getEntityObject(Languages.ENG,Types.ORDER);
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
	@Override
	protected boolean exportEntityValue(EntityValue ev) {
		boolean r = true;
		if ((Integer)ev.typeValue > 1000) {
			r = false;
		}
		return r;
	}
}
