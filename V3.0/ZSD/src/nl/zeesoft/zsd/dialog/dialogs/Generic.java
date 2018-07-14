package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zsd.dialog.Dialog;

public abstract class Generic extends Dialog {
	public static final String	MASTER_CONTEXT_GENERIC		= "Generic";

	public Generic() {
		setMasterContext(MASTER_CONTEXT_GENERIC);
	}
}
