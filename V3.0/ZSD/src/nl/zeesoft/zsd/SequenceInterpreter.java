package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.dialog.dialogs.Generic;
import nl.zeesoft.zsd.dialog.dialogs.GenericProfanity;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.interpret.InterpreterRequest;
import nl.zeesoft.zsd.interpret.InterpreterResponse;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

/**
 * A SequenceInterpreter can be used to interpret the meaning of a sequence using several methods. 
 */
public class SequenceInterpreter {
	private InterpreterConfiguration	configuration	= null;
	
	public SequenceInterpreter(InterpreterConfiguration c) {
		configuration = c;
	}

	/**
	 * Handles an interpretation request.
	 * 
	 * @param request The interpretation request
	 * @return  The interpretation response
	 */
	public InterpreterResponse handleInterpreterRequest(InterpreterRequest request) {
		InterpreterResponse r = new InterpreterResponse(request);
		buildInterpreterResponse(r);
		return r;
	}
	
	protected void buildInterpreterResponse(InterpreterResponse r) {
		// Preprocess
		r.correctedInput = getConfiguration().getLanguagePreprocessor().process(r.request.input);
		if (r.correctedInput.length()>0) {
			ZStringSymbolParser sequence = buildSequence(r);
			String language = r.request.language;
			String masterContext = r.request.masterContext;

			ZStringSymbolParser translatedPrompt = null;
			List<String> translateLanguages = new ArrayList<String>();
			
			// Classify language
			if (r.request.classifyLanguage) {
				r.addDebugLogLine("Classify language for sequence: ",r.correctedInput);
				List<SequenceClassifierResult> contexts = getConfiguration().getLanguageClassifier().getContexts(r.correctedInput,true,0.0D);
				if (r.request.prompt.length()>0 && contexts.size()==0) {
					r.addDebugLogLine("Classify language for sequence: ",sequence);
					contexts = getConfiguration().getLanguageClassifier().getContexts(sequence,true,0.0D);
				}
				if (contexts.size()>0 && contexts.get(0).symbol.equals(BaseConfiguration.LANG_UNI)) {
					contexts.remove(0);
				}
				if (contexts.size()>0) {
					r.addDebugLogLine("Classified language: ",contexts.get(0).symbol);
					r.responseLanguages = contexts;
					language = contexts.get(0).symbol;
				}
			}
			if (language.length()==0) {
				language = getConfiguration().getBase().getPrimaryLanguage();
				r.addDebugLogLine("Selected primary language: ",language);
			}

			// Translate raw input
			translateLanguages.add(language);
			translateLanguages.add(BaseConfiguration.LANG_UNI);
			if (r.request.translateEntityValues) {
				if (r.request.translateEntityValues && r.request.prompt.length()>0) {
					translatedPrompt = getConfiguration().getEntityValueTranslator().translateToInternalValues(r.request.prompt,translateLanguages,r.request.translateEntityTypes,true);
				}
				r.addDebugLogLine("Translate sequence: ",sequence);
				r.entityValueTranslation = translateEntityValues(r,translatedPrompt,sequence,translateLanguages);
				r.addDebugLogLine("Translated sequence: ",r.entityValueTranslation);
			}
			
			// Correct input
			boolean corrected = false;
			if (r.request.correctInput) {
				ZStringSymbolParser correctionInput = null;
				if (r.request.translateEntityValues) {
					correctionInput = getInputForCorrectionFromEntityValues(r.entityValueTranslation);
				} else {
					correctionInput = new ZStringSymbolParser(r.correctedInput);
				}
				List<String> symbols = correctionInput.toSymbolsPunctuated();
				int numInputSymbols = symbols.size();
				long stopAfterMs = numInputSymbols * getConfiguration().getBase().getMaxMsInterpretPerSymbol();
				long maxCorrect = getConfiguration().getBase().getMaxMsInterpretPerSequence();
				if (r.request.translateEntityValues) {
					maxCorrect = (maxCorrect / 3) * 2;
				}
				if (stopAfterMs	> maxCorrect) {
					stopAfterMs = maxCorrect;
				}
				r.addDebugLogLine("Correction time limit: ","" + stopAfterMs);
				r.addDebugLogLine("Correcting sequence: ",correctionInput);
				String alphabet = getConfiguration().getBase().getSupportedAlphabets().get(language);
				ZStringSymbolParser correction = getConfiguration().getLanguageClassifier().correct(correctionInput,language,stopAfterMs,alphabet);
				if (!correction.equals(correctionInput)) {
					corrected = true;
					if (r.request.translateEntityValues) {
						correction = mergeCorrectionWithEntityValues(r.entityValueTranslation,correction);
					}
					r.correctedInput = correction;
					r.addDebugLogLine("Corrected sequence: ",r.correctedInput);
					sequence = buildSequence(r);
				}
			}
			
			// Translate corrected input
			if (r.request.translateEntityValues && r.request.correctInput) {
				if (!corrected) {
					r.entityValueTranslationCorrected = r.entityValueTranslation;
				} else {
					r.addDebugLogLine("Translate corrected sequence: ",sequence);
					r.entityValueTranslationCorrected = translateEntityValues(r,translatedPrompt,sequence,translateLanguages);
					r.addDebugLogLine("Translated corrected sequence: ",r.entityValueTranslationCorrected);
					sequence = buildSequence(r);
				}
			}
			
			// Classify master context
			if (r.request.classifyMasterContext) {
				r.addDebugLogLine("Classify master context for sequence: ",r.correctedInput);
				List<SequenceClassifierResult> contexts = getConfiguration().getLanguageMasterContextClassifiers().get(language).getContexts(r.correctedInput,true,0.0D);
				if (r.request.prompt.length()>0 && (contexts.size()==0 || contexts.get(0).probNormalized<r.request.classifyMasterContextThreshold)) {
					r.addDebugLogLine("Classify master context for sequence: ",sequence);
					contexts = getConfiguration().getLanguageMasterContextClassifiers().get(language).getContexts(sequence,true,0D);
				}
				if (contexts.size()>0) {
					r.responseMasterContexts = contexts;
					if (contexts.get(0).probNormalized>=r.request.classifyMasterContextThreshold) {
						r.addDebugLogLine("Classified master context: ",contexts.get(0).symbol);
						masterContext = contexts.get(0).symbol;
					}
				}
			}
			
			// Check profanity
			boolean profanity = false;
			if (r.request.checkProfanity) {
				for (SequenceClassifierResult res: r.responseMasterContexts) {
					if (res.symbol.equals(Generic.MASTER_CONTEXT_GENERIC)) {
						r.addDebugLogLine("Checking profanity for sequence: ",r.correctedInput);
						List<SequenceClassifierResult> contexts = getConfiguration().getLanguageContextClassifiers().get(language + res.symbol).getContexts(r.correctedInput,true,0D);
						for (SequenceClassifierResult resC: contexts) {
							if (resC.symbol.equals(GenericProfanity.CONTEXT_GENERIC_PROFANITY)) {
								r.responseMasterContexts.clear();
								r.responseMasterContexts.add(res);
								res.probNormalized = 1.0D;
								r.responseContexts.clear();
								r.responseContexts.add(resC);
								resC.probNormalized = 1.0D;
								profanity = true;
								break;
							}
						}
						break;
					}
				}
				if (r.responseMasterContexts.size()>0 && r.responseMasterContexts.get(0).probNormalized<r.request.classifyMasterContextThreshold) {
					r.responseMasterContexts.clear();
				}
			}
			
			// Classify context
			if (!profanity) {
				if (masterContext.length()>0 && r.request.classifyContext) {
					r.addDebugLogLine("Classify context for sequence: ",r.correctedInput);
					List<SequenceClassifierResult> contexts = getConfiguration().getLanguageContextClassifiers().get(language + masterContext).getContexts(r.correctedInput,true,r.request.classifyContextThreshold);
					if (r.request.prompt.length()>0 && contexts.size()==0) {
						r.addDebugLogLine("Classify context for sequence: ",sequence);
						contexts = getConfiguration().getLanguageContextClassifiers().get(language + masterContext).getContexts(sequence,true,0.0D);
					}
					if (contexts.size()>0) {
						r.addDebugLogLine("Classified context: ",contexts.get(0).symbol);
						r.responseContexts = contexts;
					}
				}
			}
			
		}
	}
	
	protected InterpreterConfiguration getConfiguration() {
		return configuration;
	}
	
	protected ZStringSymbolParser buildSequence(InterpreterResponse response) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		if (response.request.prompt.length()>0) {
			r.append(response.request.prompt);
			r.append(" ");
		}
		r.append(response.correctedInput);
		return r;
	}

	private ZStringSymbolParser getInputForCorrectionFromEntityValues(ZStringSymbolParser entityValueSequence) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		ZStringSymbolParser alphabetics = getConfiguration().getEntityValueTranslator().translateToExternalValues(entityValueSequence,BaseConfiguration.TYPE_ALPHABETIC,true);
		List<String> symbols = alphabetics.toSymbolsPunctuated();
		for (String symbol: symbols) {
			if (symbol.contains(getConfiguration().getEntityValueTranslator().getValueConcatenator())) {
				symbol = SymbolCorrector.PLACEHOLDER;
			}
			if (r.length()>0) {
				r.append(" ");
			} 
			r.append(symbol);
		}
		return r;
	}

	private ZStringSymbolParser mergeCorrectionWithEntityValues(ZStringSymbolParser entityValueSequence,ZStringSymbolParser correction) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		List<String> symbols = correction.toSymbolsPunctuated();
		List<String> values = entityValueSequence.toSymbolsPunctuated();
		List<String> merged = new ArrayList<String>();
		int i = 0;
		for (String symbol: symbols) {
			if (symbol.equals(SymbolCorrector.PLACEHOLDER)) {
				symbol = getConfiguration().getEntityValueTranslator().getExternalValueForInternalValues(values.get(i),"");
			}
			merged.add(symbol);
			i++;
		}
		r.fromSymbols(merged,true,true);
		return r;
	}
	
	private ZStringSymbolParser translateEntityValues(InterpreterResponse response,ZStringSymbolParser translatedPrompt,ZStringSymbolParser sequence,List<String> translateLanguages) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		ZStringSymbolParser translated = getConfiguration().getEntityValueTranslator().translateToInternalValues(sequence,translateLanguages,response.request.translateEntityTypes,true);
		if (translatedPrompt==null) {
			r = translated;
		} else {
			List<String> promptSymbols = translatedPrompt.toSymbols();
			List<String> symbols = translated.toSymbols();
			for (int i = 0; i < promptSymbols.size(); i++) {
				symbols.remove(0);
			}
			for (String symbol: symbols) {
				if (r.length()>0) {
					r.append(" ");
				}
				r.append(symbol);
			}
		}
		return r;
	}
}
