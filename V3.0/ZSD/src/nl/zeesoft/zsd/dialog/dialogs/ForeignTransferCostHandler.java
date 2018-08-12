package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public abstract class ForeignTransferCostHandler extends ForeignTransferHandler {
	@Override
	protected void setPrompt(String promptVariable) {
		DialogVariableValue dvv = getResponseOutput().values.get(ForeignTransfer.VARIABLE_TRANSFER_TO_COUNTRY);
		if (dvv==null || dvv.externalValue.length()==0) {
			getResponseOutput().output = new ZStringSymbolParser();
		} else {
			super.setPrompt(promptVariable);
		}
	}
}
