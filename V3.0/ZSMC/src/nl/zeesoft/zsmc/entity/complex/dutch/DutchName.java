package nl.zeesoft.zsmc.entity.complex.dutch;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsmc.EntityValueTranslator;
import nl.zeesoft.zsmc.entity.complex.ComplexObject;

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
	public ZStringSymbolParser translate(ZStringSymbolParser sequence) {
		ZStringSymbolParser s = new ZStringSymbolParser();
		ZStringSymbolParser t = correctAndMatch(sequence);
		if (t.length()>0) {
			
		}
		return s;
	}

	@Override
	public void initialize(EntityValueTranslator translator) {
		addVariable("{firstName}",TYPE_ALPHABETIC);
		addVariable("{preposition}",TYPE_ALPHABETIC);
		addVariable("{lastName}",TYPE_ALPHABETIC);
		
		addPatterns("{firstName} {preposition} {lastName}");
		addPatterns("{firstName} {lastName}");
		addPatterns("{lastName} , {firstName} {preposition}");
		addPatterns("{lastName} , {firstName} ");
		
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
