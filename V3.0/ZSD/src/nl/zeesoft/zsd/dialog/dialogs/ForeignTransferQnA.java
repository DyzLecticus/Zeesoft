package nl.zeesoft.zsd.dialog.dialogs;

public abstract class ForeignTransferQnA extends ForeignTransfer {
	public static final String	CONTEXT_FOREIGN_TRANSFER_QNA		= "QuestionAndAnswer";;

	public ForeignTransferQnA() {
		setContext(CONTEXT_FOREIGN_TRANSFER_QNA);
	}
}
