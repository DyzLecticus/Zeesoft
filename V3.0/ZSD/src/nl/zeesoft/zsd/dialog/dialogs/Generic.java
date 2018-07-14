package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zsd.dialog.DialogInstance;

public abstract class Generic extends DialogInstance {
	public static final String	MASTER_CONTEXT_GENERIC		= "Generic";

	public Generic() {
		setMasterContext(MASTER_CONTEXT_GENERIC);
	}
}
