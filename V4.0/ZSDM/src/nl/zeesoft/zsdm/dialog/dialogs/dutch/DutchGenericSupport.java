package nl.zeesoft.zsdm.dialog.dialogs.dutch;

import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.context.ContextConfig;

public class DutchGenericSupport extends DutchGeneric {
	private static final String		EXAMPLE_OUTPUT_CONTACT		= "Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}.";
	private static final String		EXAMPLE_OUTPUT_LEARNING		= "Ik ben nog aan het leren.";
	private static final String		EXAMPLE_OUTPUT_NO_WORRIES	= "Geen zorgen.";
	
	public DutchGenericSupport() {
		setContext(ContextConfig.CONTEXT_GENERIC_SUPPORT);
		// TODO: setHandlerClassName(DutchSupportRequestHandler.class.getName());
	}
	
	@Override
	public void initialize() {
		addExamplesForFilterContext(FILTER_CONTEXT_NO_TRANSFER,getOutputContact());
		addExamplesForFilterContext(FILTER_CONTEXT_TRANSFER,"");

		addVariable(VARIABLE_SUPPORT_CONFIRMATION,Types.CONFIRMATION);
		addVariablePrompt(VARIABLE_SUPPORT_CONFIRMATION,"Zal ik u doorverbinden met een mens?");
		addVariablePrompt(VARIABLE_SUPPORT_CONFIRMATION,"Wilt u doorverbonden worden met een mens?");
		
		addNextDialogVariable();
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Kan ik u ergens anders mee proberen te helpen?");
		addVariablePrompt(VARIABLE_NAME_NEXT_DIALOG,"Kan ik u op een andere manier van dienst zijn?");
	}
	
	protected void addExamplesForFilterContext(String filterContext,String outputContact) {
		addExample("Ik wil graag met een mens praten.",outputContact,filterContext);
		addExample("Ik wil graag met een mens spreken.",outputContact,filterContext);
		addExample("Ik wil worden doorverbonden met een mens.",outputContact,filterContext);
		addExample("Ik wil doorverbonden worden met een mens.",outputContact,filterContext);
		addExample("Kun je me doorverbinden met een mens?",outputContact,filterContext);
		addExample("Kunt u me doorverbinden met een mens?",outputContact,filterContext);
		addExample("Kan je mij doorverbinden met een mens?",outputContact,filterContext);
		addExample("Kunt u mij doorverbinden met een mens?","",filterContext);
		
		String append = "";
		if (outputContact.length()>0) {
			append = " " + outputContact;
		}
		
		addExample("[" + Types.FROWNY + "].",getOutputLearning() + append,filterContext);
		addExample("Zucht.",getOutputLearning() + append,filterContext);
		addExample("Je begrijpt mij niet.",getOutputLearning() + append,filterContext);
		addExample("Jij begrijpt me niet.",getOutputLearning() + append,filterContext);
		addExample("U begrijpt me niet.",getOutputLearning() + append,filterContext);
		addExample("Je begrijpt het niet.",getOutputLearning() + append,filterContext);
		addExample("Jij begrijpt het niet.",getOutputLearning() + append,filterContext);
		addExample("U begrijpt het niet.",getOutputLearning() + append,filterContext);
		addExample("Je begrijpt mij verkeerd.",getOutputLearning() + append,filterContext);
		addExample("jij begrijpt me verkeerd.",getOutputLearning() + append,filterContext);
		addExample("U begrijpt me verkeerd.",getOutputLearning() + append,filterContext);
		addExample("Je begrijpt er niks van.",getOutputLearning() + append,filterContext);
		addExample("jij begrijpt er niks van.",getOutputLearning() + append,filterContext);
		addExample("U begrijpt er niks van.",getOutputLearning() + append,filterContext);
		addExample("Je kan me niet helpen.",getOutputLearning() + append,filterContext);
		addExample("jij kan mij niet helpen.",getOutputLearning() + append,filterContext);
		addExample("U kunt mij niet helpen.",getOutputLearning() + append,filterContext);
		
		addExample("Help.",getOutputNoWorries() + append,filterContext);
		addExample("Help!",getOutputNoWorries() + append,filterContext);
	}
	
	protected String getOutputContact() {
		return EXAMPLE_OUTPUT_CONTACT;
	}
	
	protected String getOutputLearning() {
		return EXAMPLE_OUTPUT_LEARNING;
	}
	
	protected String getOutputNoWorries() {
		return EXAMPLE_OUTPUT_NO_WORRIES;
	}
}
