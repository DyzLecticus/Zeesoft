package nl.zeesoft.zsds.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsds.dialogs.StateAccuracy;

public class EnglishStateAccuracy extends StateAccuracy {
	private static final String EXAMPLE_OUTPUT_ACCURACY = "My current test results indicate that I am {exactly} {percentage} percent accurate.";
	
	public EnglishStateAccuracy() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishStateAccuracyHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		super.initialize(t);
		addExample("How accurate are you?",EXAMPLE_OUTPUT_ACCURACY);
		addExample("How precise are you?",EXAMPLE_OUTPUT_ACCURACY);
		addExample("What are your most recent test results?",EXAMPLE_OUTPUT_ACCURACY);
	}
}
