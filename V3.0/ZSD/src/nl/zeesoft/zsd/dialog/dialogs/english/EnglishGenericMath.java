package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericMath;

public class EnglishGenericMath extends GenericMath {
	public EnglishGenericMath() {
		setLanguage(BaseConfiguration.LANG_ENG);
		setHandlerClassName(EnglishGenericMathHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("How much is [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","{exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM]?","That is {exact} {result}.");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM]?","That is {exact} {result}. ");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","That is {exact} {result}. ");
		addExample("How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?","That is {exact} {result}. ");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"What else can I do for you?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Is there anything else I can help you with?");

		addVariable(VARIABLE_NUMBER1,BaseConfiguration.TYPE_NUMERIC);
		addVariable(VARIABLE_NUMBER2,BaseConfiguration.TYPE_NUMERIC);
		addVariable(VARIABLE_NUMBER3,BaseConfiguration.TYPE_NUMERIC);
		addVariable(VARIABLE_NUMBER4,BaseConfiguration.TYPE_NUMERIC);
		addVariable(VARIABLE_NUMBER5,BaseConfiguration.TYPE_NUMERIC);

		addVariable(VARIABLE_OPERATOR1,BaseConfiguration.TYPE_MATHEMATIC);
		addVariable(VARIABLE_OPERATOR2,BaseConfiguration.TYPE_MATHEMATIC);
		addVariable(VARIABLE_OPERATOR3,BaseConfiguration.TYPE_MATHEMATIC);
		addVariable(VARIABLE_OPERATOR4,BaseConfiguration.TYPE_MATHEMATIC);
	}
}
