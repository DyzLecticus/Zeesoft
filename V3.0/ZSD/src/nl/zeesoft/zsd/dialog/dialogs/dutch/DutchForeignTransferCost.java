package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.ForeignTransferCost;

public class DutchForeignTransferCost extends ForeignTransferCost {
	public DutchForeignTransferCost() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchForeignTransferCostHandler.class.getName());
	}

	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Wat kost een overboeking naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Wat zijn de kosten van een overboeking naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Wat kost geld overboeken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Wat kost het om geld over te boeken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Wat kost geld overmaken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Wat kost het om geld over te maken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Zijn er kosten verbonden aan geld overmaken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Zijn er kosten verbonden aan geld overboeken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoeveel kost een overboeking naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoeveel kost geld overboeken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoeveel kost het om geld over te boeken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoeveel kost geld overmaken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());
		addExample("Hoeveel kost het om geld over te maken naar [" + BaseConfiguration.TYPE_COUNTRY + "]?",getCountryOutput());

		addVariable(VARIABLE_TRANSFER_TO_COUNTRY,BaseConfiguration.TYPE_COUNTRY);
	}
	
	protected String getCountryOutput() {
		return "Een overboeking naar {" + VARIABLE_TRANSFER_TO_COUNTRY + "} kost {cost} {costCurrency}.";
	}
}
