package nl.zeesoft.zsds.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsds.dialogs.StateAccuracy;

public class DutchStateAccuracy extends StateAccuracy {
	private static final String EXAMPLE_OUTPUT_ACCURACY = "Mijn meest recente test resultaten geven aan dat ik {exact} {percentage} procent nauwkeurig ben.";
	
	public DutchStateAccuracy() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchStateAccuracyHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		super.initialize(t);
		addExample("Hoe accuraat ben je?",EXAMPLE_OUTPUT_ACCURACY);
		addExample("Hoe nauwkeurig ben je?",EXAMPLE_OUTPUT_ACCURACY);
		addExample("Wat zijn je meest recente test resultaten?",EXAMPLE_OUTPUT_ACCURACY);
	}
}
