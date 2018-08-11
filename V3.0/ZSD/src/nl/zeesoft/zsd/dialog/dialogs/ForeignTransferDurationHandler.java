package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public abstract class ForeignTransferDurationHandler extends ForeignTransferHandler {
	@Override
	public void buildDialogResponseOutput(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		DialogVariableValue dvv = dro.values.get(ForeignTransfer.VARIABLE_TRANSFER_TO_COUNTRY);
		if (dvv==null || dvv.externalValue.length()==0) {
			dro.output = new ZStringSymbolParser();
		} else {
			dro.setDialogVariableValue(r,"durationDays",getDays());
			super.buildDialogResponseOutput(r,dro,updatedValues,promptVariable);
		}
	}
	protected abstract String getDays();
}
