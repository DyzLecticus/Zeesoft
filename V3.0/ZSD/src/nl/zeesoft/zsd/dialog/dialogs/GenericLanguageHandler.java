package nl.zeesoft.zsd.dialog.dialogs;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;
import nl.zeesoft.zsd.entity.EntityObject;

public abstract class GenericLanguageHandler extends DialogInstanceHandler {
	@Override
	public void buildDialogResponseOutput(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		String language = dro.values.get(GenericLanguage.VARIABLE_LANGUAGE).externalValue;
		boolean confirm = false;

		EntityObject lang = getLanguageEntity();
		ZStringBuilder languages = new ZStringBuilder();
		int i = 0;
		for (String code: getConfig().getBase().getSupportedLanguages()) {
			i++;
			if (languages.length()>0) {
				if (i==getConfig().getBase().getSupportedLanguages().size()) {
					languages.append(" ");
					languages.append(getAnd());
					languages.append(" ");
				} else {
					languages.append(", ");
				}
			}
			String extVal = lang.getExternalValueForInternalValue(lang.getInternalValuePrefix() + code);
			languages.append(extVal);
			if (language.length()>0 && extVal.equals(language)) {
				confirm = true;
			}
		}
		dro.setDialogVariableValue(r,GenericLanguage.VARIABLE_LANGUAGES,languages.toString());
		if (language.length()>0) {
			if (confirm) {
				dro.setDialogVariableValue(r,GenericLanguage.VARIABLE_CONFIRMATION,getYes());
			} else {
				dro.setDialogVariableValue(r,GenericLanguage.VARIABLE_CONFIRMATION,getNo());
			}
		}
		super.buildDialogResponseOutput(r,dro,updatedValues,promptVariable);
	}
	protected EntityObject getLanguageEntity() {
		return getConfig().getEntityValueTranslator().getEntityObject(getDialog().getLanguage(),BaseConfiguration.TYPE_LANGUAGE);
	}
	protected abstract String getAnd();
	protected abstract String getYes();
	protected abstract String getNo();
}
