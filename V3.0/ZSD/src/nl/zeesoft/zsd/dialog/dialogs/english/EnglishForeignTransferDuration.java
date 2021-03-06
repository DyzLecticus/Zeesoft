package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.ForeignTransferDuration;

public class EnglishForeignTransferDuration extends ForeignTransferDuration {
	public EnglishForeignTransferDuration() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishForeignTransferDurationHandler.class.getName());
	}

	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Money transfer to [" + BaseConfiguration.TYPE_COUNTRY + "]. How long does that take?",getCountryOutput());
		addExample("How long does it take to transfer money to [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("How much time does it take to transfer money to [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());

		addVariable(VARIABLE_TRANSFER_TO_COUNTRY,BaseConfiguration.TYPE_COUNTRY);
	}
	
	protected String getCountryOutput() {
		return "A foreign transfer to {" + VARIABLE_TRANSFER_TO_COUNTRY + "} takes {duration} {durationDays}.";
	}
}
