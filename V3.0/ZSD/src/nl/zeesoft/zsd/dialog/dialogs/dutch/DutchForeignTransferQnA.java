package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.ForeignTransferQnA;

public class DutchForeignTransferQnA extends ForeignTransferQnA {
	public DutchForeignTransferQnA() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchForeignTransferQnAHandler.class.getName());
	}

	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Een buitenlandoverboeking. Wat kost dat?",getCostOutput());
		addExample("Wat kost een buitenlandoverboeking?",getCostOutput());
		addExample("Wat kost een overboeking naar het buitenland?",getCostOutput());
		addExample("Wat zijn de kosten van een overboeking naar het buitenland?",getCostOutput());
		addExample("Wat zijn de kosten van een buitenlandoverboeking?",getCostOutput());
		addExample("Wat kost geld overboeken naar het buitenland?",getCostOutput());
		addExample("Wat kost het om geld over te boeken naar het buitenland?",getCostOutput());
		addExample("Wat kost geld overmaken naar het buitenland?",getCostOutput());
		addExample("Wat kost het om geld over te maken naar het buitenland?",getCostOutput());
		addExample("Zijn er kosten verbonden aan een buitenlandoverboeking?",getCostOutput());
		addExample("Zijn er kosten verbonden aan buitenlandoverboekingen?",getCostOutput());
		addExample("Zijn er kosten verbonden aan geld overmaken naar het buitenland?",getCostOutput());
		addExample("Zijn er kosten verbonden aan geld overboeken naar het buitenland?",getCostOutput());
		addExample("Hoeveel kost een buitenlandoverboeking?",getCostOutput());
		addExample("Hoeveel kosten buitenlandoverboekingen?",getCostOutput());
		addExample("Hoeveel kost een overboeking naar het buitenland?",getCostOutput());
		addExample("Hoeveel kost geld overboeken naar het buitenland?",getCostOutput());
		addExample("Hoeveel kost het om geld over te boeken naar het buitenland?",getCostOutput());
		addExample("Hoeveel kost geld overmaken naar het buitenland?",getCostOutput());
		addExample("Hoeveel kost het om geld over te maken naar het buitenland?",getCostOutput());

		addExample("Een buitenlandoverboeking. Hoe lang duurt dat?",getDurationOutput());
		addExample("Hoelang duurt een buitenlandoverboeking?",getDurationOutput());
		addExample("Hoelang duren buitenlandoverboekingen?",getDurationOutput());
		addExample("Hoelang duurt een overboeking naar het buitenland?",getDurationOutput());
		addExample("Hoelang duurt geld overboeken naar het buitenland?",getDurationOutput());
		addExample("Hoelang duurt het om geld over te boeken naar het buitenland?",getDurationOutput());
		addExample("Hoelang duurt geld overmaken naar het buitenland?",getDurationOutput());
		addExample("Hoelang duurt het om geld over te maken naar het buitenland?",getDurationOutput());
		addExample("Hoeveel tijd kost een buitenlandoverboeking?",getDurationOutput());
		addExample("Hoeveel tijd kosten buitenlandoverboekingen?",getDurationOutput());
		addExample("Hoeveel tijd kost geld overboeken naar het buitenland?",getDurationOutput());
		addExample("Hoeveel tijd kost het om geld over te maken naar het buitenland?",getDurationOutput());
		addExample("Hoeveel tijd kost geld overmaken naar het buitenland?",getDurationOutput());
	}
	
	protected String getCostOutput() {
		return "Een overboeking naar het buitenland kost {cost} {costCurrency}.";
	}

	protected String getDurationOutput() {
		return "Een overboeking naar het buitenland duurt {duration} {durationDays}.";
	}
}
