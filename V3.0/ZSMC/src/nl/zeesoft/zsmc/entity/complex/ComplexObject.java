package nl.zeesoft.zsmc.entity.complex;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsmc.EntityValueTranslator;
import nl.zeesoft.zsmc.SequenceMatcher;
import nl.zeesoft.zsmc.entity.EntityObject;

public abstract class ComplexObject extends EntityObject {
	public static final String					TYPE_NAME			= "NAM";

	private EntityValueTranslator				translator			= null;
	
	private List<ComplexPattern>				patterns 			= new ArrayList<ComplexPattern>();
	private List<ComplexVariable>				variables			= new ArrayList<ComplexVariable>();

	private SequenceMatcher						sequenceMatcher		= new SequenceMatcher();

	public abstract ZStringSymbolParser translate(ZStringSymbolParser sequence);

	public void parseVariableValuesFromSequence(ZStringSymbolParser ret,ZStringSymbolParser sequence,ZStringSymbolParser match) {
		SortedMap<String,String> values = new TreeMap<String,String>();
		if (match!=null && match.length()>0) {
			List<String> iSymbols = sequence.toSymbolsPunctuated();
			List<String> mSymbols = match.toSymbolsPunctuated();
			int mStart = -1;
			int iStart = -1;
			int i = 0;
			int m = 0;
			int seq = 0;
			for (String mSym: mSymbols) {
				for (String iSym: iSymbols) {
					if (iSym.equalsIgnoreCase(mSym)) {
						mStart = m;
						iStart = i;
						break;
					}
					i++;
				}
				m++;
				if (mStart>=0) {
					break;
				}
			}
			if (mStart>=0) {
				i = iStart;
				for (m = mStart; m < iSymbols.size(); m++) {
					String mSym = mSymbols.get(m);
					String iSym = iSymbols.get(i);
					System.out.println(" " + iSym + " equals " + mSym + "?");
					if (iSym.equalsIgnoreCase(mSym)) {
						seq++;
						i++;
					} else {
						if (seq>0 && mSym.startsWith("{") && mSym.endsWith("}")) {
							String name = mSym.substring(1,mSym.length()-1);
							ComplexVariable var = getVariableByName(name);
							if (var!=null) {
								values.put(var.name,iSym);
							}
							i++;
						} else {
							seq = 0;
						}
					}
				}
			}
		}
		
		for (ComplexVariable var: getVariables()) {
			String val = values.get(var.name);
			if (val!=null) {
				System.out.println("---> " + var.name + " = " + val);
				if (ret.length()==0) {
					ret = sequence;
				}
				String value = getInternalValueForVariable(var,val);
				System.out.println("--->! " + val + " = " + value);
				ret.replace(val,value);
				System.out.println("--->!! " + ret + " = " + val);
			}
		}
	}

	public String getInternalValueForVariable(ComplexVariable var, String value) {
		return getInternalValuePrefix() + var.name + ":" + value;
	}

	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		this.translator = translator;
		for (ComplexPattern pattern: patterns) {
			sequenceMatcher.setContext(pattern.context);
			sequenceMatcher.addSequence(pattern.pattern);
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
