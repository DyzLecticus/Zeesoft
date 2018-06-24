package nl.zeesoft.zsmc.entity.complex.dutch;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsmc.EntityValueTranslator;
import nl.zeesoft.zsmc.entity.complex.ComplexObject;
import nl.zeesoft.zsmc.entity.complex.ComplexVariable;

public class DutchName extends ComplexObject {
	@Override
	public String getLanguage() {
		return LANG_NLD;
	}
	
	@Override
	public String getType() {
		return TYPE_NAME;
	}
	
	@Override
	public String getInternalValueForVariable(ComplexVariable var, String value) {
		String r = super.getInternalValueForVariable(var, value);
		if (var.name.equals("firstName") || var.name.equals("lastName")) {
			r = value.substring(0,1).toUpperCase() + value.substring(1);
			r = getInternalValuePrefix() + var.name + ":" + r;
		}
		return r;
	}
	
	@Override
	public void initialize(EntityValueTranslator translator) {
		addVariable("firstName",TYPE_ALPHABETIC);
		addVariable("preposition",TYPE_PREPOSITION);
		addVariable("lastName",TYPE_ALPHABETIC);
		
		addPatterns("{firstName} {preposition} {lastName}");
		addPatterns("{firstName} {lastName}");
		addPatterns("{firstName}");
		
		super.initialize(translator);
	}
	
	private void addPatterns(String variables) {
		addPattern(new ZStringSymbolParser("Mijn naam is " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Ik heet " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Ik ben " + variables + "."),"");

		addPattern(new ZStringSymbolParser("Hoe heet je? " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Hoe heet jij? " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wie ben jij? " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wie bent u? " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wat is je naam? " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wat is jouw naam? " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wat is uw naam? " + variables + "."),"");

		addPattern(new ZStringSymbolParser("Hoe heet je? Ik heet " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Hoe heet jij? Ik heet " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wie ben jij? Ik ben " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wie bent u? Ik ben " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wat is je naam? Mijn naam is " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wat is jouw naam? Mijn naam is " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Wat is uw naam? Mijn naam is " + variables + "."),"");
	}
}
