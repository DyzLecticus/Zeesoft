package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zsd.dialog.dialogs.Generic;
import nl.zeesoft.zsd.dialog.dialogs.GenericProfanity;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.interpret.InterpreterRequest;
import nl.zeesoft.zsd.interpret.InterpreterResponse;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

/**
 * A SequenceInterpreter can be used to interpret the meaning of a sequence using several methods. 
 */
public class SequenceInterpreter {
	private Messenger					messenger		= null;
	private InterpreterConfiguration	configuration	= null;
	
	public SequenceInterpreter(InterpreterConfiguration c) {
		configuration = c;
	}

	public SequenceInterpreter(Messenger msgr,InterpreterConfiguration c) {
		messenger = msgr;
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

	public Messenger getMessenger() {
		return messenger;
	}
	
	protected void buildInterpreterResponse(InterpreterResponse r) {
		// Preprocess
		r.correctedInput = getConfiguration().getLanguagePreprocessor().process(r.request.input);
		if (r.correctedInput.length()>0) {
			ZStringSymbolParser promptAndInput = getAddPromptToInput(r.request.prompt,r.correctedInput);
			
			String language = r.request.language;
			String masterContext = r.request.masterContext;
			
			// Classify language
			if (r.request.classifyLanguage) {
				r.addDebugLogLine("Classify language for sequence: ",r.correctedInput);
				List<SequenceClassifierResult> contexts = getConfiguration().getLanguageClassifier().getContexts(r.correctedInput,true,0.0D);
				if (r.request.prompt.length()>0 && contexts.size()==0) {
					r.addDebugLogLine("Classify language for sequence: ",promptAndInput);
					contexts = getConfiguration().getLanguageClassifier().getContexts(promptAndInput,true,0.0D);
				}
				if (contexts.size()>0 && contexts.get(0).symbol.equals(BaseConfiguration.LANG_UNI)) {
					contexts.remove(0);
				}
				if (contexts.size()>0) {
					r.addDebugLogLine("Classified language: ",contexts.get(0).symbol);
					if (r.request.language.length()>0 && !contexts.get(0).symbol.equals(r.request.language)) {
						List<SequenceClassifierResult> contextsTest = new ArrayList<SequenceClassifierResult>(contexts);
						for (SequenceClassifierResult context: contextsTest) {
							if (context.symbol.equals(r.request.language) &&
								(contexts.get(0).probNormalized - context.probNormalized)<r.request.minLanguageChangeDifference
								) {
								contexts.clear();
								contexts.add(context);
								r.addDebugLogLine("Selected language: ",context.symbol);
								break;
							}
						}
					}
					r.classifiedLanguages = contexts;
					language = contexts.get(0).symbol;
				}
			}
			if (language.length()==0) {
				language = getConfiguration().getBase().getPrimaryLanguage();
				r.addDebugLogLine("Selected primary language: ",language);
			}

			// Translate raw input
			List<String> translateLanguages = new ArrayList<String>(getConfiguration().getBase().getSupportedLanguages());
			ZStringSymbolParser translatedPrompt = null;
			if (!translateLanguages.contains(BaseConfiguration.LANG_UNI)) {
				translateLanguages.add(BaseConfiguration.LANG_UNI);
			}
			if (r.request.translateEntityValues) {
				if (r.request.prompt.length()>0) {
					translatedPrompt = getConfiguration().getEntityValueTranslator().translateToInternalValues(r.request.prompt,translateLanguages,r.request.translateEntityTypes,true);
				}
				r.addDebugLogLine("Translate sequence: ",r.correctedInput);
				r.entityValueTranslation = translateEntityValues(r,translatedPrompt,promptAndInput,translateLanguages);
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
					promptAndInput = getAddPromptToInput(r.request.prompt,r.correctedInput);
				}
			}
			
			// Translate corrected input
			if (r.request.translateEntityValues && r.request.correctInput) {
				if (!corrected) {
					r.entityValueTranslationCorrected = r.entityValueTranslation;
				} else {
					r.addDebugLogLine("Translate corrected sequence: ",r.correctedInput);
					r.entityValueTranslationCorrected = translateEntityValues(r,translatedPrompt,promptAndInput,translateLanguages);
					r.addDebugLogLine("Translated corrected sequence: ",r.entityValueTranslationCorrected);
				}
			}
			
			ZStringSymbolParser promptAndInputClassification = null;
			if (r.request.translateEntityValues) {
				if (r.request.correctInput) {
					r.classificationSequence = getInputForClassifierFromEntityValues(r.entityValueTranslationCorrected);
				} else {
					r.classificationSequence = getInputForClassifierFromEntityValues(r.entityValueTranslation);
				}
				promptAndInputClassification = getAddPromptToInput(r.request.prompt,r.classificationSequence);
			} else {
				r.classificationSequence = r.correctedInput;
				promptAndInputClassification = promptAndInput;
			}
			
			// Check profanity
			boolean profanity = false;
			if (r.request.checkProfanity) {
				String pmc = getProfanityMasterContext(language);
				String pc = getProfanityContext(language);
				if (pmc.length()>0 && pc.length()>0) {
					r.addDebugLogLine("Checking profanity for sequence: ",r.classificationSequence);
					List<SequenceClassifierResult> contexts = getConfiguration().getLanguageContextClassifiers().get(language + pmc).getContexts(r.classificationSequence,true,0D);
					for (SequenceClassifierResult resC: contexts) {
						if (resC.symbol.equals(pc)) {
							SequenceClassifierResult res = new SequenceClassifierResult();
							res.symbol = Generic.MASTER_CONTEXT_GENERIC;
							res.probNormalized = 1.0D;
							r.classifiedMasterContexts.clear();
							r.classifiedMasterContexts.add(res);
							r.classifiedContexts.clear();
							r.classifiedContexts.add(resC);
							resC.probNormalized = 1.0D;
							profanity = true;
							break;
						}
					}
				} else {
					r.addDebugLogLine("Unable to check profanity for language: ",language);
				}
			}
			
			if (!profanity) {
				// Classify master context
				if (r.request.classifyMasterContext) {
					List<SequenceClassifierResult> contexts = null;
					if (!r.classificationSequence.equals(r.correctedInput)) {
						r.addDebugLogLine("Classify master context for input sequence: ",r.correctedInput);
						List<SequenceClassifierResult> contextsRaw = getConfiguration().getLanguageMasterContextClassifiers().get(language).getContexts(r.correctedInput,true,r.request.classifyMasterContextThreshold);
						r.addDebugLogLine("Classify master context for classification sequence: ",promptAndInputClassification);
						contexts = getConfiguration().getLanguageMasterContextClassifiers().get(language).getContexts(promptAndInputClassification,true,r.request.classifyMasterContextThreshold);
						if (contextsRaw.size()>0 && contexts.size()>0 && contextsRaw.get(0).probNormalized>=contexts.get(0).probNormalized) {
							r.addDebugLogLine("Selected input sequence master context classification.");
							contexts = contextsRaw;
						}
					} else {
						r.addDebugLogLine("Classify master context for input sequence: ",r.correctedInput);
						contexts = getConfiguration().getLanguageMasterContextClassifiers().get(language).getContexts(r.correctedInput,true,r.request.classifyMasterContextThreshold);
					}
					if (r.request.prompt.length()>0 && contexts.size()==0) {
						r.addDebugLogLine("Classify master context for input sequence: ",promptAndInput);
						contexts = getConfiguration().getLanguageMasterContextClassifiers().get(language).getContexts(promptAndInput,true,r.request.classifyMasterContextThreshold);
					}
					if (contexts.size()>0) {
						for (SequenceClassifierResult context: contexts) {
							if (configuration.getBase().getSupportedMasterContexts().get(language).contains(context.symbol)) {
								r.classifiedMasterContexts = contexts;
								r.addDebugLogLine("Classified master context: ",contexts.get(0).symbol);
								masterContext = context.symbol;
								break;
							} else {
								r.addDebugLogLine("Classified unsupported master context: ",context.symbol);
							}
						}
					}
				}

				// Classify context
				if (masterContext.length()>0 && r.request.classifyContext) {
					if (configuration.getBase().getSupportedMasterContexts().get(language).contains(masterContext)) {
						List<SequenceClassifierResult> contexts = null;
						if (!r.classificationSequence.equals(r.correctedInput)) {
							r.addDebugLogLine("Classify context for input sequence: ",r.correctedInput);
							List<SequenceClassifierResult> contextsRaw = getConfiguration().getLanguageContextClassifiers().get(language + masterContext).getContexts(r.correctedInput,true,r.request.classifyContextThreshold);
							r.addDebugLogLine("Classify context for classification sequence: ",promptAndInputClassification);
							contexts = getConfiguration().getLanguageContextClassifiers().get(language + masterContext).getContexts(promptAndInputClassification,true,r.request.classifyContextThreshold);
							if (contextsRaw.size()>0 && contexts.size()>0 && contextsRaw.get(0).probNormalized>=contexts.get(0).probNormalized) {
								r.addDebugLogLine("Selected input sequence context classification.");
								contexts = contextsRaw;
								r.classificationSequence = r.correctedInput;
							}
						} else {
							r.addDebugLogLine("Classify context for input sequence: ",r.correctedInput);
							contexts = getConfiguration().getLanguageContextClassifiers().get(language + masterContext).getContexts(r.correctedInput,true,r.request.classifyContextThreshold);
						}
						if (r.request.prompt.length()>0 && contexts.size()==0) {
							r.addDebugLogLine("Classify context for sequence: ",promptAndInput);
							contexts = getConfiguration().getLanguageContextClassifiers().get(language + masterContext).getContexts(promptAndInput,true,r.request.classifyContextThreshold);
						}
						if (contexts.size()>0) {
							r.addDebugLogLine("Classified context: ",contexts.get(0).symbol);
							r.classifiedContexts = contexts;
						}
					} else {
						r.addDebugLogLine("Master context is not supported: ",masterContext);
					}
				}
			}
			
		}
	}
	
	protected InterpreterConfiguration getConfiguration() {
		return configuration;
	}
	
	protected String getProfanityMasterContext(String language) {
		return Generic.MASTER_CONTEXT_GENERIC;
	}
	
	protected String getProfanityContext(String language) {
		return GenericProfanity.CONTEXT_GENERIC_PROFANITY;
	}
	
	private ZStringSymbolParser getAddPromptToInput(ZStringSymbolParser prompt, ZStringSymbolParser input) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		if (prompt.length()>0) {
			r.append(prompt);
			r.append(" ");
		}
		r.append(input);
		return r;
	}

	private ZStringSymbolParser getInputForClassifierFromEntityValues(ZStringSymbolParser entityValueSequence) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		ZStringSymbolParser alphabetics = getConfiguration().getEntityValueTranslator().translateToExternalValues(entityValueSequence,BaseConfiguration.TYPE_ALPHABETIC,true);
		List<String> symbols = alphabetics.toSymbolsPunctuated();
		List<String> symbolsNew = new ArrayList<String>();
		String valConCat = getConfiguration().getEntityValueTranslator().getValueConcatenator();
		for (String symbol: symbols) {
			if (symbol.contains(valConCat)) {
				String val = getConfiguration().getEntityValueTranslator().getInternalValueFromInternalValues(symbol,"");
				if (val.length()>0) {
					String prefix = val.split(valConCat)[0] + valConCat;
					EntityObject eo = getConfiguration().getEntityValueTranslator().getEntityObject(prefix);
					symbol = "[" + eo.getType() + "]";
				}
			}
			symbolsNew.add(symbol);
		}
		r.fromSymbols(symbolsNew,true,true);
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
			if (symbol.length()>0) {
				merged.add(symbol);
			}
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
