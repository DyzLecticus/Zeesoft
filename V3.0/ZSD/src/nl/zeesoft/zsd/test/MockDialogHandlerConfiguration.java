package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;

public class MockDialogHandlerConfiguration extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockDialogHandlerConfiguration*.");
	}

	@Override
	protected Object initialzeMock() {
		DialogHandlerConfiguration config = new DialogHandlerConfiguration();
		config.getBase().setBaseDir("resources/");
		config.setEntityValueTranslator(new FixedDateEntityValueTranslator());

		InterpreterConfiguration iConfig = TestInterpreterConfiguration.getConfig(getTester());
		if (iConfig!=null) {
			config.setEntityValueTranslator(iConfig.getEntityValueTranslator());
			config.setLanguagePreprocessor(iConfig.getLanguagePreprocessor());
			config.setLanguageClassifier(iConfig.getLanguageClassifier());
			for (String language: config.getBase().getSupportedLanguages()) {
				config.getLanguageMasterContextClassifiers().put(language,iConfig.getLanguageMasterContextClassifiers().get(language));
				for (String masterContext: config.getBase().getSupportedMasterContexts().get(language)) {
					config.getLanguageContextClassifiers().put(language + masterContext,iConfig.getLanguageContextClassifiers().get(language + masterContext));
				}
			}
		}
		
		return config;
	}
}
