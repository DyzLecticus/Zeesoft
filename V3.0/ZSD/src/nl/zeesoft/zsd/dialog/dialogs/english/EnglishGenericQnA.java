package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericQnA;

public class EnglishGenericQnA extends GenericQnA {
	private static final String 		SUPPORT	= "I can help with general questions, booking rooms and questions about room bookings.";
	private static final String 		SELF	= "I am an artificially intelligent personal assistant.";
	private static final String 		GOAL	= "My goal is to understand and help people.";
	private static final String 		CREATOR	= "My software was written by André van der Zee.";
	
	public EnglishGenericQnA() {
		setLanguage(BaseConfiguration.LANG_ENG);
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Who is André van der Zee?","André van der Zee is a software engineer from Leiden, The Netherlands.");
		
		addExample("What is the answer to the ultimate question of life, the universe, and everything?","Fourtytwo.");
		
		addExample("How can you help me?",SUPPORT);
		addExample("What can you do for me?",SUPPORT);
		addExample("How can you assist me?",SUPPORT);
		addExample("How can you support me?",SUPPORT);
		addExample("How can you be of service to me?",SUPPORT);
		
		addExample("What are you?",SELF);
		addExample("Can you describe yourself?",SELF);
		addExample("How would you describe yourself?",SELF);
		
		addExample("Who wrote your software?",CREATOR);
		addExample("Who programmed you?",CREATOR);
		addExample("Who created you?",CREATOR);
		addExample("Who made you?",CREATOR);
		addExample("Who built you?",CREATOR);

		addExample("What is your goal?",GOAL);
		addExample("What are your goals?",GOAL);
		addExample("Why do you exist?",GOAL);
		addExample("What is your purpose?",GOAL);
		addExample("What is the purpose of your existence?",GOAL);
		addExample("Why were you created?",GOAL);
		addExample("Why were you built?",GOAL);
	}
}
