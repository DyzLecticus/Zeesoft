package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zsd.dialog.DialogObject;

public abstract class Generic extends DialogObject {
	public static final String	MASTER_CONTEXT_GENERIC		= "Generic";

	public Generic() {
		setMasterContext(MASTER_CONTEXT_GENERIC);
	}
}
