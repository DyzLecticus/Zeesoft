package nl.zeesoft.zsdm.dialog.entities.dutch;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.znlb.context.ContextConfig;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.zsdm.dialog.Dialog;

public class DutchProfanity extends Dialog {
	public DutchProfanity() {
		setLanguage(Languages.NLD);
		setMasterContext(ContextConfig.MASTER_CONTEXT_GENERIC);
		setContext(ContextConfig.CONTEXT_GENERIC_PROFANITY);
	}
	@Override
	public void initialize() {
		addExample("[" + EntityObject.TYPE_PROFANITY + "].","Van zulk taalgebruik ben ik niet gediend.");
		addExample("[" + EntityObject.TYPE_PROFANITY + "].","Ik doe alsof ik dat niet heb gehoord.");
	}
}
