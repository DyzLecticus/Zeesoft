package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
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
		
		if (r.correctedInput.length()>0) {
			ZStringSymbolParser sequence = buildSequence(r);
			String language = request.language;
			String masterContext = request.masterContext;
	
			if (request.classifyLanguage) {
				r.addDebugLogLine("Classify language for sequence: ",r.correctedInput);
				List<SequenceClassifierResult> contexts = configuration.getLanguageClassifier().getContexts(r.correctedInput,true,0.0D);
				if (request.prompt.length()>0 && contexts.size()==0) {
					r.addDebugLogLine("Classify language for sequence: ",sequence);
					contexts = configuration.getLanguageClassifier().getContexts(sequence,true,0.0D);
				}
				if (contexts.size()>0) {
					r.addDebugLogLine("Classified language: ",contexts.get(0).symbol);
					r.responseLanguages = contexts;
					language = contexts.get(0).symbol;
				}
			}
			if (language.length()==0) {
				language = configuration.getBase().getPrimaryLanguage();
				r.addDebugLogLine("Selected primary language: ",language);
			}
			if (request.correctInput) {
				long stopAfterMs = r.numInputSymbols * configuration.getMaxMsPerSymbol();
				long maxCorrect = configuration.getMaxMsPerSequence();
				if (request.translateEntiyValues) {
					maxCorrect = (maxCorrect / 3) * 2;
				}
				if (stopAfterMs	> maxCorrect) {
					stopAfterMs = maxCorrect;
				}
				r.addDebugLogLine("Correction time limit: ","" + stopAfterMs);
				r.addDebugLogLine("Correcting sequence: ",r.correctedInput);
				ZStringSymbolParser correction = configuration.getLanguageClassifier().correct(r.correctedInput,language,stopAfterMs);
				if (!correction.equals(r.correctedInput)) {
					r.correctedInput = correction;
					r.addDebugLogLine("Corrected sequence: ",r.correctedInput);
					sequence = buildSequence(r);
				}
			}
			if (request.classifyMasterContext) {
				r.addDebugLogLine("Classify master context for sequence: ",r.correctedInput);
				List<SequenceClassifierResult> contexts = configuration.getLanguageMasterContextClassifiers().get(language).getContexts(r.correctedInput,true,0.0D);
				if (request.prompt.length()>0 && contexts.size()==0) {
					r.addDebugLogLine("Classify master context for sequence: ",sequence);
					contexts = configuration.getLanguageMasterContextClassifiers().get(language).getContexts(sequence,true,0.0D);
				}
				if (contexts.size()>0) {
					r.addDebugLogLine("Classified master context: ",contexts.get(0).symbol);
					r.responseMasterContexts = contexts;
					masterContext = contexts.get(0).symbol;
				}
			}
			if (masterContext.length()>0) {
				if (request.classifyContext) {
					r.addDebugLogLine("Classify context for sequence: ",r.correctedInput);
					List<SequenceClassifierResult> contexts = configuration.getLanguageContextClassifiers().get(language + masterContext).getContexts(r.correctedInput,true,0.0D);
					if (request.prompt.length()>0 && contexts.size()==0) {
						r.addDebugLogLine("Classify context for sequence: ",sequence);
						contexts = configuration.getLanguageContextClassifiers().get(language + masterContext).getContexts(sequence,true,0.0D);
					}
					if (contexts.size()>0) {
						r.addDebugLogLine("Classified context: ",contexts.get(0).symbol);
						r.responseContexts = contexts;
					}
				}
			}
			if (request.translateEntiyValues) {
				List<String> languages = new ArrayList<String>(); 
				languages.add(language);
				languages.add(BaseConfiguration.LANG_UNI);
				r.addDebugLogLine("Translate sequence: ",sequence);
				ZStringSymbolParser translated = configuration.getEntityValueTranslator().translateToInternalValues(sequence,languages,request.translateEntityTypes,true);
				if (request.prompt.length()>0) {
					ZStringSymbolParser translatedPrompt = configuration.getEntityValueTranslator().translateToInternalValues(request.prompt,languages,request.translateEntityTypes,true);
					r.entityValueTranslation = new ZStringSymbolParser(translated.substring(translatedPrompt.length()));
				} else {
					r.entityValueTranslation = translated;
				}
				r.addDebugLogLine("Translated sequence: ",r.entityValueTranslation);
			}
		}
		
		return r;
	}
	
	private ZStringSymbolParser buildSequence(InterpreterResponse response) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		if (response.request.prompt.length()>0) {
			r.append(response.request.prompt);
			r.append(" ");
		}
		r.append(response.correctedInput);
		return r;
	}
}
