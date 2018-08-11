package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.ForeignTransferQnA;

public class EnglishForeignTransferQnA extends ForeignTransferQnA {
	public EnglishForeignTransferQnA() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishForeignTransferQnAHandler.class.getName());
	}

	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("What does money transfer to a foreign country cost?",getCostOutput());
		addExample("What does it cost to transfer money to a foreign country?",getCostOutput());
		addExample("What are the costs of transferring money to a foreign country?",getCostOutput());
		addExample("How much does it cost to transfer money to a foreign country?",getCostOutput());
		addExample("Are there any costs attached to transfer money to a foreign country?",getCostOutput());
		addExample("What does a transfer abroad cost?",getCostOutput());
		addExample("What does it cost to transfer money abroad?",getCostOutput());
		addExample("How much does it cost to transfer abroad?",getCostOutput());
		addExample("Are there any costs attached to transferring money abroad?",getCostOutput());

		addExample("How long does foreign transfer take?",getDurationOutput());
		addExample("How long does it take to transfer money to a foreign country?",getDurationOutput());
		addExample("How much time does it take to transfer money to a foreign country?",getDurationOutput());
		addExample("How much time does foreign transfer take?",getDurationOutput());
		addExample("How long does transfer abroad take?",getDurationOutput());
		addExample("How long does it take to transfer money abroad?",getDurationOutput());
		addExample("How much time does it take to transfer money abroad?",getDurationOutput());
		addExample("How much time does transfer abroad take?",getDurationOutput());
	}
	
	protected String getCostOutput() {
		return "A foreign transfer costs {cost} {costCurrency}.";
	}
	
	protected String getDurationOutput() {
		return "A foreign transfer takes {duration} {durationDays}.";
	}
}
