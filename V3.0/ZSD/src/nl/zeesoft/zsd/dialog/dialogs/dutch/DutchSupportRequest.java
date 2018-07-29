package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.SupportRequest;

public class DutchSupportRequest extends SupportRequest {
	private static final String		EXAMPLE_OUTPUT_LEARNING		= "Ik ben nog aan het leren.";
	private static final String		EXAMPLE_OUTPUT_NO_WORRIES	= "Geen zorgen.";
	
	public DutchSupportRequest() {
		setLanguage(BaseConfiguration.LANG_NLD);
		setHandlerClassName(DutchSupportRequestHandler.class.getName());
	}
	
	@Override
	public void initialize(EntityValueTranslator t) {
		addExample("Ik wil graag met een mens praten.","");
		addExample("Ik wil graag met een mens spreken.","");
		addExample("Ik wil worden doorverbonden met een mens.","");
		addExample("Ik wil doorverbonden worden met een mens.","");
		addExample("Kun je me doorverbinden met een mens?","");
		addExample("Kunt u me doorverbinden met een mens?","");
		addExample("Kan je me doorverbinden met een mens?","");
		addExample("Kunt u mij doorverbinden met een mens?","");
		
		addExample("Je begrijpt mij niet.",getOutputLearning());
		addExample("Jij begrijpt me niet.",getOutputLearning());
		addExample("U begrijpt me niet.",getOutputLearning());
		addExample("Je begrijpt het niet.",getOutputLearning());
		addExample("Jij begrijpt het niet.",getOutputLearning());
		addExample("U begrijpt het niet.",getOutputLearning());
		addExample("Je begrijpt mij verkeerd.",getOutputLearning());
		addExample("jij begrijpt me verkeerd.",getOutputLearning());
		addExample("U begrijpt me verkeerd.",getOutputLearning());
		addExample("Je begrijpt er niks van.",getOutputLearning());
		addExample("jij begrijpt er niks van.",getOutputLearning());
		addExample("U begrijpt er niks van.",getOutputLearning());
		addExample("Je kan me niet helpen.",getOutputLearning());
		addExample("jij kan mij niet helpen.",getOutputLearning());
		addExample("U kunt mij niet helpen.",getOutputLearning());
		
		addExample("Help.",getOutputNoWorries());
		addExample("Help!",getOutputNoWorries());

		addVariable(VARIABLE_SUPPORT_CONFIRMATION,BaseConfiguration.TYPE_CONFIRMATION);
		addVariablePrompt(VARIABLE_SUPPORT_CONFIRMATION,"Zal ik u doorverbinden met een mens?");
		addVariablePrompt(VARIABLE_SUPPORT_CONFIRMATION,"Wilt u doorverbonden worden met een mens?");
		
		addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Kan ik u ergens anders mee proberen te helpen?");
		addVariablePrompt(VARIABLE_NEXT_DIALOG,"Kan ik u op een andere manier van dienst zijn?");
	}
	
	protected String getOutputLearning() {
		return EXAMPLE_OUTPUT_LEARNING;
	}
	
	protected String getOutputNoWorries() {
		return EXAMPLE_OUTPUT_NO_WORRIES;
	}
}
