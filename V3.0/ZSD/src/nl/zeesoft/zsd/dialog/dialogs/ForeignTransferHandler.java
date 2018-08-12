package nl.zeesoft.zsd.dialog.dialogs;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.entity.UniversalCurrency;

public abstract class ForeignTransferHandler extends DialogInstanceHandler {
	public static final int		COST		= 5;
	public static final int		DURATION	= 3;
	
	@Override
	protected String initializeVariables() {
		setDialogVariableValue("cost",getExternalValueForNumber(COST));
		setDialogVariableValue("costCurrency",getExternalValueForCurrency(UniversalCurrency.CURR_EUR));
		setDialogVariableValue("duration",getExternalValueForNumber(DURATION));
		return super.initializeVariables();
	}

	private String getExternalValueForCurrency(String code) {
		return getConfig().getEntityValueTranslator().getEntityObject(getDialog().getLanguage(),BaseConfiguration.TYPE_CURRENCY).getExternalValueForInternalValue(code);
	}
}
