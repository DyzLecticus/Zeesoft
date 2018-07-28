package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.dialogs.GenericMathHandler;
import nl.zeesoft.zsd.entity.EntityObject;

public class EnglishGenericMathHandler extends GenericMathHandler {
	@Override
	protected EntityObject getNumericEntity() {
		return getConfig().getEntityValueTranslator().getEntityObject(BaseConfiguration.LANG_ENG,BaseConfiguration.TYPE_NUMERIC);
	}
	
	@Override
	protected String getExactly() {
		return "exactly";
	}
	
	@Override
	protected String getAbout() {
		return "about";
	}

	@Override
	protected String getMinus() {
		return "minus";
	}
	
	@Override
	protected String getInfinity() {
		return "infinity";
	}
}
