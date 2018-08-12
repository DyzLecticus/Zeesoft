package nl.zeesoft.zsd.dialog.dialogs;

public abstract class ForeignTransferQnAHandler extends ForeignTransferHandler {
	@Override
	protected String initializeVariables() {
		setDialogVariableValue("durationDays",getDays());
		return super.initializeVariables();
	}
	protected abstract String getDays();
}
