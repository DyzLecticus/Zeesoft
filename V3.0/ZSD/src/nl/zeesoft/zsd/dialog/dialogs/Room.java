package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zsd.dialog.DialogInstance;

public abstract class Room extends DialogInstance {
	public static final String	MASTER_CONTEXT_ROOM		= "Room";

	public Room() {
		setMasterContext(MASTER_CONTEXT_ROOM);
	}
}
