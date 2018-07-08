package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.interpret.InterpreterRequest;
import nl.zeesoft.zsd.interpret.InterpreterResponse;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class SequenceInterpreter {
	private InterpreterConfiguration				configuration						= null;
	
	public SequenceInterpreter(InterpreterConfiguration c) {
		configuration = c;
	}
	
	public InterpreterResponse interpretRequest(InterpreterRequest request) {
		InterpreterResponse r = new InterpreterResponse(request);
		
		ZStringSymbolParser sequence = buildSequence(r);
		String language = request.language;
		String masterContext = request.masterContext;

		if (request.checkLanguage) {
			List<SequenceClassifierResult> contexts = configuration.getLanguageClassifier().getContexts(request.input,true,0.0D);
			if (request.prompt.length()>0 && contexts.size()==0) {
				contexts = configuration.getLanguageClassifier().getContexts(sequence,true,0.0D);
			}
			if (contexts.size()>0) {
				r.responseLanguages = contexts;
				language = contexts.get(0).symbol;
			}
		}
		if (language.length()>0) {
			if (request.correctInput) {
				ZStringSymbolParser correction = configuration.getLanguageClassifier().correct(r.correctedInput,language);
				if (!correction.equals(r.correctedInput)) {
					r.correctedInput = correction;
					sequence = buildSequence(r);
				}
			}
			if (request.checkMasterContext) {
				List<SequenceClassifierResult> contexts = configuration.getLanguageMasterContextClassifiers().get(language).getContexts(r.correctedInput,true,0.0D);
				if (request.prompt.length()>0 && contexts.size()==0) {
					contexts = configuration.getLanguageMasterContextClassifiers().get(language).getContexts(sequence,true,0.0D);
				}
				if (contexts.size()>0) {
					r.responseMasterContexts = contexts;
					masterContext = contexts.get(0).symbol;
				}
			}
			if (masterContext.length()>0) {
				if (request.checkContext) {
					List<SequenceClassifierResult> contexts = configuration.getLanguageContextClassifiers().get(language + masterContext).getContexts(r.correctedInput,true,0.0D);
					if (request.prompt.length()>0 && contexts.size()==0) {
						contexts = configuration.getLanguageContextClassifiers().get(language + masterContext).getContexts(sequence,true,0.0D);
					}
					if (contexts.size()>0) {
						r.responseContexts = contexts;
					}
				}
			}
		}
		
		if (request.translateEntiyValues) {
			List<String> languages = new ArrayList<String>(); 
			if (language.length()>0) {
				languages.add(language);
				languages.add(EntityObject.LANG_UNI);
				ZStringSymbolParser translated = configuration.getEntityValueTranslator().translateToInternalValues(sequence, languages, request.translateEntityTypes,true);
				if (request.prompt.length()>0) {
					r.entityValueTranslation = new ZStringSymbolParser(translated.split(configuration.getLanguageClassifier().getIoSeparator()).get(1));
				} else {
					r.entityValueTranslation = translated;
				}
			}
		}
		
		return r;
	}
	
	private ZStringSymbolParser buildSequence(InterpreterResponse response) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		if (response.request.prompt.length()>0) {
			r.append(response.request.prompt);
			r.append(" ");
			r.append(configuration.getLanguageClassifier().getIoSeparator());
			r.append(" ");
		}
		r.append(response.correctedInput);
		return r;
	}
}
