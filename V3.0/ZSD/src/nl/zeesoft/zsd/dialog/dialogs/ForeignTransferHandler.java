package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public abstract class ForeignTransferHandler extends DialogInstanceHandler {
	public static final int		COST		= 5;
	public static final int		DURATION	= 3;
	
	@Override
	public void buildDialogResponseOutput(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		dro.setDialogVariableValue(r,"cost",getExternalValueForNumber(COST));
		// TODO: Add currency entity
		dro.setDialogVariableValue(r,"costCurrency","euro");
		dro.setDialogVariableValue(r,"duration",getExternalValueForNumber(DURATION));
		super.buildDialogResponseOutput(r,dro,updatedValues,promptVariable);
	}
	
	private String getExternalValueForNumber(int num) {
		return super.getConfig().getEntityValueTranslator().getEntityObject(getDialog().getLanguage(),BaseConfiguration.TYPE_NUMERIC).getExternalValueForInternalValue("" + num);
	}
}
