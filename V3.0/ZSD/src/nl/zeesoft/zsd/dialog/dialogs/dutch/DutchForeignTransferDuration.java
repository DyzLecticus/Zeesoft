package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.ForeignTransferDuration;

public class DutchForeignTransferDuration extends ForeignTransferDuration {
	public DutchForeignTransferDuration() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchForeignTransferDurationHandler.class.getName());
	}

	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Hoelang duurt een overboeking naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoelang duurt geld overboeken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoelang duurt het om geld over te boeken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoelang duurt geld overmaken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoelang duurt het om geld over te maken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoeveel tijd kost geld overboeken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoeveel tijd kost het om geld over te maken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoeveel tijd kost geld overmaken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		
		addVariable(VARIABLE_TRANSFER_TO_COUNTRY,BaseConfiguration.TYPE_COUNTRY);
	}
	
	protected String getCountryOutput() {
		return "Een overboeking naar {" + VARIABLE_TRANSFER_TO_COUNTRY + "} duurt {duration} {durationDays}.";
	}
}
