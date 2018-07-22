package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.dialogs.GenericMathHandler;
import nl.zeesoft.zsd.entity.EntityObject;

public class DutchGenericMathHandler extends GenericMathHandler {
	@Override
	protected EntityObject getNumericEntity() {
		return getConfig().getEntityValueTranslator().getEntityObject(BaseConfiguration.LANG_NLD,BaseConfiguration.TYPE_NUMERIC);
	}
	
	@Override
	protected String getExactly() {
		return "precies";
	}
	
	@Override
	protected String getAbout() {
		return "ongeveer";
	}

	@Override
	protected String getInfinity() {
		return "oneindig";
	}
}
