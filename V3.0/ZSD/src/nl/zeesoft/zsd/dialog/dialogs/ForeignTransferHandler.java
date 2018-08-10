package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;
import nl.zeesoft.zsd.entity.UniversalCurrency;

public abstract class ForeignTransferHandler extends DialogInstanceHandler {
	public static final int		COST		= 5;
	public static final int		DURATION	= 3;
	
	@Override
	public void buildDialogResponseOutput(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		dro.setDialogVariableValue(r,"cost",getExternalValueForNumber(COST));
		dro.setDialogVariableValue(r,"costCurrency",getExternalValueForCurrency(UniversalCurrency.CURR_EUR));
		dro.setDialogVariableValue(r,"duration",getExternalValueForNumber(DURATION));
		super.buildDialogResponseOutput(r,dro,updatedValues,promptVariable);
	}
	
	private String getExternalValueForNumber(int num) {
		return super.getConfig().getEntityValueTranslator().getEntityObject(getDialog().getLanguage(),BaseConfiguration.TYPE_NUMERIC).getExternalValueForInternalValue("" + num);
	}

	private String getExternalValueForCurrency(String code) {
		return super.getConfig().getEntityValueTranslator().getEntityObject(getDialog().getLanguage(),BaseConfiguration.TYPE_CURRENCY).getExternalValueForInternalValue(code);
	}
}
