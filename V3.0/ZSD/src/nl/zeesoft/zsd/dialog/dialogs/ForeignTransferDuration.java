package nl.zeesoft.zsd.dialog.dialogs;

public abstract class ForeignTransferDuration extends ForeignTransfer {
	public static final String	CONTEXT_FOREIGN_TRANSFER_DURATION		= "Duration";

	public ForeignTransferDuration() {
		setContext(CONTEXT_FOREIGN_TRANSFER_DURATION);
	}
}
