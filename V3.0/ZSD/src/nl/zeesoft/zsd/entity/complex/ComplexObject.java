package nl.zeesoft.zsd.entity.complex;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequenceMatcher;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.UniversalAlphabetic;
import nl.zeesoft.zsd.sequence.SequenceMatcherResult;

public abstract class ComplexObject extends EntityObject {
	private EntityValueTranslator				translator			= null;
	
	private List<ComplexPattern>				patterns 			= new ArrayList<ComplexPattern>();
	private List<ComplexVariable>				variables			= new ArrayList<ComplexVariable>();

	private SequenceMatcher						sequenceMatcher		= new SequenceMatcher();
	
	private UniversalAlphabetic					ua					= null;

	public double getMatchThreshold() {
		return 0D;
	}
	
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser entityValueSequence) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		ZStringSymbolParser matchSequence = getTranslator().translateToExternalValues(entityValueSequence,BaseConfiguration.TYPE_ALPHABETIC,true);
		List<SequenceMatcherResult> matches = getSequenceMatcher().getMatches(matchSequence,"",true,getMatchThreshold());
		for (SequenceMatcherResult match: matches) {
			List<ComplexVariableReplacement> replacements = getMatchOverlayReplacements(matchSequence.toSymbolsPunctuated(),match.result.symbols);
			if (replacements.size()>0) {
				List<String> evSymbols = entityValueSequence.toSymbolsPunctuated();
				int i = 0;
				for (String symbol: evSymbols) {
					for(ComplexVariableReplacement rep: replacements) {
						if (rep.index==i) {
							symbol = symbol + getTranslator().getOrConcatenator() + rep.value;
						}
					}
					i++;
					if (r.length()>0) {
						r.append(" ");
					}
					r.append(symbol);
				}
				break;
			}
		}
		return r;
	}

	private List<ComplexVariableReplacement> getMatchOverlayReplacements(List<String> matchSymbols,List<String> overlaySymbols) {
		List<ComplexVariableReplacement> r = new ArrayList<ComplexVariableReplacement>();
		int iStart = -1;
		int oStart = -1;
		String first = overlaySymbols.get(0);
		if (first.startsWith("{") && first.endsWith("}")) {
			iStart = 0;
			oStart = 0;
		} else {
			int i = 0;
			for (String iSym: matchSymbols) {
				int o = 0;
				for (String oSym: overlaySymbols) {
					if (oSym.startsWith("{") && oSym.endsWith("}")) {
						break;
					} else if (iSym.equalsIgnoreCase(oSym)) {
						oStart = o;
						iStart = i;
						break;
					}
					o++;
				}
				if (iStart>=0) {
					break;
				}
				i++;
			}
		}
		if (iStart>=0) {
			int matchVars = 0;
			int i = iStart;
			for (int o = oStart; o < overlaySymbols.size(); o++) {
				String oSym = overlaySymbols.get(o);
				String iSym = matchSymbols.get(i);
				if (oSym.startsWith("{") && oSym.endsWith("}")) {
					String name = oSym.substring(1,oSym.length()-1);
					ComplexVariable var = getVariableByName(name);
					if (var!=null) {
						matchVars++;
						String value = getTranslator().getInternalValueFromInternalValues(iSym,var.entityValueType);
						if (value.length()==0 &&
							var.entityValueType.equals(BaseConfiguration.TYPE_ALPHABETIC) &&
							UniversalAlphabetic.isAlphabetic(iSym)
							) {
							value = ua.getInternalValuePrefix() + iSym;
						}
						if (value.length()>0) {
							String[] split = value.split(getTranslator().getValueConcatenator());
							value = getInternalValueForVariable(var,split[0],split[1]);
							ComplexVariableReplacement rep = new ComplexVariableReplacement();
							rep.variable = var;
							rep.value = value;
							rep.index = i;
							r.add(rep);
						}
					}
				}
				i++;
				if (i>=matchSymbols.size()) {
					break;
				}
			}
			if (r.size()!=matchVars) {
				r.clear();
			}
		}
		
		return r;
	}
	
	public String getInternalValueForVariable(ComplexVariable var,String prefix, String value) {
		return getInternalValuePrefix() + var.name + getTranslator().getValueConcatenator() + prefix + getTranslator().getValueConcatenator() + value;
	}

	@Override
	public String getExternalValueForInternalValue(String str) {
		String r = "";
		if (str.startsWith(getInternalValuePrefix())) {
			str = str.substring(getInternalValuePrefix().length());
			String[] elems = str.split(getTranslator().getValueConcatenator());
			if (elems.length>=2) {
				String name = elems[0];
				ComplexVariable var = getVariableByName(name);
				if (var!=null) {
					String value = str.substring(name.length() + 1);
					if (var.entityValueType.equals(BaseConfiguration.TYPE_ALPHABETIC)) {
						r = value;
					} else {
						r = getTranslator().getExternalValueForInternalValues(value,var.entityValueType);
					}
				}
			}
		}
		return r;
	}
	
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		this.translator = translator;
		this.ua = (UniversalAlphabetic) translator.getEntityObject(BaseConfiguration.LANG_UNI,BaseConfiguration.TYPE_ALPHABETIC);
		for (ComplexPattern pattern: patterns) {
			List<String> contextSymbols = new ArrayList<String>();
			contextSymbols.add(pattern.context);
			if (!contextSymbols.contains("")) {
				contextSymbols.add("");
			}
			sequenceMatcher.addSymbols(pattern.pattern.toSymbolsPunctuated(),contextSymbols);
		}
		sequenceMatcher.calculateProb();
	}
	
	public void addPattern(ZStringSymbolParser pattern,String context) {
		ComplexPattern pat = new ComplexPattern();
		pat.pattern = pattern;
		pat.context = context;
		patterns.add(pat);
	}

	public void addVariable(String name, String type) {
		ComplexVariable var = new ComplexVariable();
		var.name = name;
		var.entityValueType = type;
		variables.add(var);
	}
	
	public ComplexVariable getVariableByName(String name) {
		ComplexVariable r = null;
		for (ComplexVariable var: variables) {
			if (var.name.equals(name)) {
				r = var;
				break;
			}
		}
		return r;
	}

	public EntityValueTranslator getTranslator() {
		return translator;
	}
	
	public List<ComplexPattern> getPatterns() {
		return patterns;
	}

	public List<ComplexVariable> getVariables() {
		return variables;
	}
	
	public SequenceMatcher getSequenceMatcher() {
		return sequenceMatcher;
	}
}
