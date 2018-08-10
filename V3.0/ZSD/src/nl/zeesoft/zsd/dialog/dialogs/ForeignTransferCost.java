package nl.zeesoft.zsd.dialog.dialogs;

public abstract class ForeignTransferCost extends ForeignTransfer {
	public static final String	CONTEXT_FOREIGN_TRANSFER_COST		= "Cost";

	public ForeignTransferCost() {
		setContext(CONTEXT_FOREIGN_TRANSFER_COST);
	}
}
