package nl.zeesoft.zsd.entity.complex;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequenceMatcher;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.UniversalAlphabetic;
import nl.zeesoft.zsd.sequence.SequenceMatcherResult;

public abstract class ComplexObject extends EntityObject {
	public static final String					TYPE_NAME			= "NAM";

	private EntityValueTranslator				translator			= null;
	
	private List<ComplexPattern>				patterns 			= new ArrayList<ComplexPattern>();
	private List<ComplexVariable>				variables			= new ArrayList<ComplexVariable>();

	private SequenceMatcher						sequenceMatcher		= new SequenceMatcher();
	
	private UniversalAlphabetic					ua					= null;

	public double getMatchThreshold() {
		return 0.7D;
	}
	
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser entityValueSequence) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		ZStringSymbolParser matchSequence = getTranslator().translateToExternalValues(entityValueSequence,TYPE_ALPHABETIC,true);
		List<SequenceMatcherResult> matches = getSequenceMatcher().getMatches(matchSequence,"",true,getMatchThreshold());
		for (SequenceMatcherResult match: matches) {
			ZStringSymbolParser parsed = parseVariableValuesFromSequence(matchSequence,match);
			if (parsed.length()>0) {
				parsed = getTranslator().translateToInternalValues(parsed,LANG_UNI,TYPE_ALPHABETIC,false);
				List<String> mergedSyms = new ArrayList<String>();
				List<String> parsedSyms = parsed.toSymbolsPunctuated();
				int i = 0;
				for (String sym: entityValueSequence.toSymbolsPunctuated()) {
					String parse = parsedSyms.get(i);
					if (parse.startsWith(getInternalValuePrefix())) {
						mergedSyms.add(sym + getTranslator().getOrConcatenator() + parse);
					} else {
						mergedSyms.add(sym);
					}
					i++;
				}
				r.fromSymbols(mergedSyms,false,false);
				break;
			}
		}
		return r;
	}

	public ZStringSymbolParser parseVariableValuesFromSequence(ZStringSymbolParser matchSequence,SequenceMatcherResult match) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		SortedMap<String,String> values = new TreeMap<String,String>();
		SortedMap<String,String> replaces = new TreeMap<String,String>();
		int matchVars = 0;
		List<String> iSymbols = matchSequence.toSymbolsPunctuated();
		List<String> mSymbols = match.result.symbols;
		int iStart = -1;
		int mStart = -1;
		int i = 0;
		int m = 0;
		for (String iSym: iSymbols) {
			for (String mSym: mSymbols) {
				if (mSym.startsWith("{") && mSym.endsWith("}")) {
					mStart = 0;
					iStart = 0;
					break;
				} else if (iSym.equalsIgnoreCase(mSym)) {
					mStart = m;
					iStart = i;
					break;
				}
				m++;
			}
			if (iStart>=0) {
				break;
			}
			i++;
		}
		if (iStart>=0) {
			i = iStart;
			for (m = mStart; m < mSymbols.size(); m++) {
				String mSym = mSymbols.get(m);
				String iSym = iSymbols.get(i);
				if (iSym.equalsIgnoreCase(mSym)) {
					i++;
				} else {
					if (mSym.startsWith("{") && mSym.endsWith("}")) {
						String name = mSym.substring(1,mSym.length()-1);
						ComplexVariable var = getVariableByName(name);
						if (var!=null) {
							matchVars++;
							String value = getTranslator().getInternalValueFromInternalValues(iSym,var.entityValueType);
							if (value.length()==0 &&
								var.entityValueType.equals(TYPE_ALPHABETIC) &&
								UniversalAlphabetic.isAlphabetic(iSym)
								) {
								value = ua.getInternalValuePrefix() + iSym;
							}
							if (value.length()>0) {
								values.put(var.name,value);
								replaces.put(var.name,iSym);
							}
						}
						i++;
					}
				}
				if (i>=iSymbols.size()) {
					break;
				}
			}
		}
		
		if (matchVars==values.size()) {
			for (ComplexVariable var: getVariables()) {
				String val = values.get(var.name);
				if (val!=null) {
					String replace = replaces.get(var.name);
					if (r.length()==0) {
						r = matchSequence;
					}
					String value = getInternalValueForVariable(var,val);
					r.replace(replace,value);
				}
			}
		}
		
		return r;
	}

	public String getInternalValueForVariable(ComplexVariable var, String value) {
		return getInternalValuePrefix() + var.name + ":" + value;
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
					if (var.entityValueType.equals(TYPE_ALPHABETIC)) {
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
		this.ua = (UniversalAlphabetic) translator.getEntityObject(LANG_UNI,TYPE_ALPHABETIC);
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
