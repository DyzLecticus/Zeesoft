package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.ForeignTransferCost;

public class EnglishForeignTransferCost extends ForeignTransferCost {
	public EnglishForeignTransferCost() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishForeignTransferCostHandler.class.getName());
	}

	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("What does money transfer to a foreign country cost?",getDefaultOutput());
		addExample("What does it cost to transfer money to a foreign country?",getDefaultOutput());
		addExample("What are the costs of transferring money to a foreign country?",getDefaultOutput());
		addExample("How much does ot cost to transfer money to a foreign country?",getDefaultOutput());
		addExample("Are there any costs attached to transfer money to a foreign country?",getDefaultOutput());
		addExample("What does a transfer abroad cost?",getDefaultOutput());
		addExample("What does it cost to transfer money abroad?",getDefaultOutput());
		addExample("How much does ot cost to transfer abroad?",getDefaultOutput());
		addExample("Are there any costs attached to transferring money abroad?",getDefaultOutput());

		addExample("What does money transfer to [" + BaseConfiguration.TYPE_COUNTRY + "] cost?",getCountryOutput());
		addExample("What does it cost to transfer money to [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("What are the costs of transferring money to [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("How much does ot cost to transfer money to [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Are there any costs attached to transfer money to [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());

		addVariable(VARIABLE_TRANSFER_TO_COUNTRY,BaseConfiguration.TYPE_COUNTRY);
	}
	
	protected String getDefaultOutput() {
		return "A foreign transfer costs {cost} {costCurrency}.";
	}
	
	protected String getCountryOutput() {
		return "A foreign transfer to {" + VARIABLE_TRANSFER_TO_COUNTRY + "} costs {cost} {costCurrency}.";
	}
}
