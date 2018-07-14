package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.interpret.InterpreterRequest;
import nl.zeesoft.zsd.interpret.InterpreterResponse;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class SequenceInterpreter {
	private InterpreterConfiguration	configuration	= null;
	
	public SequenceInterpreter(InterpreterConfiguration c) {
		configuration = c;
	}

	public InterpreterResponse handleInterpreterRequest(InterpreterRequest request) {
		InterpreterResponse r = new InterpreterResponse(request);
		buildInterpreterResponse(r);
		return r;
	}
	
	protected void buildInterpreterResponse(InterpreterResponse r) {
		if (r.correctedInput.length()>0) {
			ZStringSymbolParser sequence = buildSequence(r);
			String language = r.request.language;
			String masterContext = r.request.masterContext;

			ZStringSymbolParser translatedPrompt = null;
			List<String> translateLanguages = new ArrayList<String>();

			// Classify language
			if (r.request.classifyLanguage) {
				r.addDebugLogLine("Classify language for sequence: ",r.correctedInput);
				List<SequenceClassifierResult> contexts = configuration.getLanguageClassifier().getContexts(r.correctedInput,true,0.0D);
				if (r.request.prompt.length()>0 && contexts.size()==0) {
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

			// Translate raw input
			translateLanguages.add(language);
			translateLanguages.add(BaseConfiguration.LANG_UNI);
			if (r.request.translateEntiyValues) {
				if (r.request.translateEntiyValues && r.request.prompt.length()>0) {
					translatedPrompt = configuration.getEntityValueTranslator().translateToInternalValues(r.request.prompt,translateLanguages,r.request.translateEntityTypes,true);
				}
				r.addDebugLogLine("Translate sequence: ",sequence);
				r.entityValueTranslation = translateEntityValues(r,translatedPrompt,sequence,translateLanguages);
				r.addDebugLogLine("Translated sequence: ",r.entityValueTranslation);
			}
			
			// Correct input
			boolean corrected = false;
			if (r.request.correctInput) {
				long stopAfterMs = r.numInputSymbols * configuration.getMaxMsPerSymbol();
				long maxCorrect = configuration.getMaxMsPerSequence();
				if (r.request.translateEntiyValues) {
					maxCorrect = (maxCorrect / 3) * 2;
				}
				if (stopAfterMs	> maxCorrect) {
					stopAfterMs = maxCorrect;
				}
				r.addDebugLogLine("Correction time limit: ","" + stopAfterMs);
				r.addDebugLogLine("Correcting sequence: ",r.correctedInput);
				String alphabet = configuration.getBase().getSupportedAlphabets().get(language);
				ZStringSymbolParser correction = configuration.getLanguageClassifier().correct(r.correctedInput,language,stopAfterMs,alphabet);
				if (!correction.equals(r.correctedInput)) {
					corrected = true;
					r.correctedInput = correction;
					r.addDebugLogLine("Corrected sequence: ",r.correctedInput);
					sequence = buildSequence(r);
				}
			}
			
			// Classify master context
			if (r.request.classifyMasterContext) {
				r.addDebugLogLine("Classify master context for sequence: ",r.correctedInput);
				List<SequenceClassifierResult> contexts = configuration.getLanguageMasterContextClassifiers().get(language).getContexts(r.correctedInput,true,0.0D);
				if (r.request.prompt.length()>0 && contexts.size()==0) {
					r.addDebugLogLine("Classify master context for sequence: ",sequence);
					contexts = configuration.getLanguageMasterContextClassifiers().get(language).getContexts(sequence,true,0.0D);
				}
				if (contexts.size()>0) {
					r.addDebugLogLine("Classified master context: ",contexts.get(0).symbol);
					r.responseMasterContexts = contexts;
					masterContext = contexts.get(0).symbol;
				}
			}
			
			// Classify context
			if (masterContext.length()>0 && r.request.classifyContext) {
				r.addDebugLogLine("Classify context for sequence: ",r.correctedInput);
				List<SequenceClassifierResult> contexts = configuration.getLanguageContextClassifiers().get(language + masterContext).getContexts(r.correctedInput,true,0.0D);
				if (r.request.prompt.length()>0 && contexts.size()==0) {
					r.addDebugLogLine("Classify context for sequence: ",sequence);
					contexts = configuration.getLanguageContextClassifiers().get(language + masterContext).getContexts(sequence,true,0.0D);
				}
				if (contexts.size()>0) {
					r.addDebugLogLine("Classified context: ",contexts.get(0).symbol);
					r.responseContexts = contexts;
				}
			}
			
			// Translate corrected input
			if (r.request.translateEntiyValues && r.request.correctInput) {
				if (!corrected) {
					r.entityValueTranslationCorrected = r.entityValueTranslation;
				} else {
					r.addDebugLogLine("Translate corrected sequence: ",sequence);
					r.entityValueTranslationCorrected = translateEntityValues(r,translatedPrompt,sequence,translateLanguages);
					r.addDebugLogLine("Translated corrected sequence: ",r.entityValueTranslationCorrected);
				}
			}
		}
	}
	
	protected InterpreterConfiguration getConfiguration() {
		return configuration;
	}
	
	private ZStringSymbolParser translateEntityValues(InterpreterResponse response,ZStringSymbolParser translatedPrompt,ZStringSymbolParser sequence,List<String> translateLanguages) {
		ZStringSymbolParser translated = configuration.getEntityValueTranslator().translateToInternalValues(sequence,translateLanguages,response.request.translateEntityTypes,true);
		if (translatedPrompt!=null) {
			translated = new ZStringSymbolParser(translated.substring(translatedPrompt.length()));
		}
		return translated;
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
