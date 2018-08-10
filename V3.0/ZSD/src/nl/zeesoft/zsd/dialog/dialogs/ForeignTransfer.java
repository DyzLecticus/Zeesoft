package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zsd.dialog.DialogInstance;

public abstract class ForeignTransfer extends DialogInstance {
	public static final String	MASTER_CONTEXT_FOREIGN_TRANSFER		= "ForeignTransfer";

	public static final String	VARIABLE_TRANSFER_TO_COUNTRY		= "transferToCountry";

	public ForeignTransfer() {
		setMasterContext(MASTER_CONTEXT_FOREIGN_TRANSFER);
	}
}
