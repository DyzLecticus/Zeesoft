package nl.zeesoft.zsd.dialog.dialogs;

public abstract class GenericCancel extends Generic {
	public static final String	CONTEXT_GENERIC_CANCEL		= "Cancel";

	public GenericCancel() {
		setContext(CONTEXT_GENERIC_CANCEL);
	}
}
