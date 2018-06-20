package nl.zeesoft.zsmc.entity.complex;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsmc.EntityValueTranslator;
import nl.zeesoft.zsmc.SequenceMatcher;
import nl.zeesoft.zsmc.entity.EntityObject;

public abstract class ComplexObject extends EntityObject {
	public static final String					TYPE_NAME			= "NAM";
	
	private List<ComplexPattern>				patterns 			= new ArrayList<ComplexPattern>();
	private List<ComplexVariable>				variables			= new ArrayList<ComplexVariable>();
	
	private SequenceMatcher						sequenceMatcher		= new SequenceMatcher();
	

	public abstract ZStringSymbolParser translate(ZStringSymbolParser sequence);

	public ZStringSymbolParser correctAndMatch(ZStringSymbolParser sequence) {
		ZStringSymbolParser s = new ZStringSymbolParser(sequence);
		s = sequenceMatcher.correct(sequence);
		s = sequenceMatcher.match(s);
		return s;
	}

	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
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
}
