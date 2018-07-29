package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zsd.dialog.DialogInstance;

public abstract class Support extends DialogInstance {
	public static final String	MASTER_CONTEXT_SUPPORT		= "Support";

	public Support() {
		setMasterContext(MASTER_CONTEXT_SUPPORT);
	}
}
