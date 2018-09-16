package nl.zeesoft.zsdm.dialog.dialogs.dutch;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class DutchGenericProfanity extends DutchGeneric {
	public DutchGenericProfanity() {
		setContext(ContextConfig.CONTEXT_GENERIC_PROFANITY);
	}
	@Override
	public void initialize() {
		addExample("[" + Types.PROFANITY + "].","Van zulk taalgebruik ben ik niet gediend.");
		addExample("[" + Types.PROFANITY + "].","Ik doe alsof ik dat niet heb gehoord.");
	}
}
