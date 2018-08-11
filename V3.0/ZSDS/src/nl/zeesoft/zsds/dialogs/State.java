package nl.zeesoft.zsds.dialogs;

import nl.zeesoft.zsd.dialog.DialogInstance;

public class State extends DialogInstance {
	public static final String	MASTER_CONTEXT_STATE			= "State";

	public State() {
		setMasterContext(MASTER_CONTEXT_STATE);
	}
}
