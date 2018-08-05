package nl.zeesoft.zsd.entity.complex.english;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;
import nl.zeesoft.zsd.entity.complex.ComplexObject;
import nl.zeesoft.zsd.entity.complex.ComplexVariable;

public class EnglishName extends ComplexObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_ENG;
	}
	
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_NAME;
	}
	
	@Override
	public double getMatchThreshold() {
		return 0.3D;
	}
	
	@Override
	public String getInternalValueForVariable(ComplexVariable var,String prefix,String value) {
		String r = "";
		if (var.name.equals(GenericHandshake.VARIABLE_FIRSTNAME) || var.name.equals(GenericHandshake.VARIABLE_LASTNAME)) {
			r = value.substring(0,1).toUpperCase() + value.substring(1);
			r = super.getInternalValueForVariable(var,prefix,r);
		} else {
			r = super.getInternalValueForVariable(var,prefix,value);
		}
		return r;
	}
	
	@Override
	public void initialize(EntityValueTranslator translator) {
		addVariable(GenericHandshake.VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_ALPHABETIC);
		addVariable(GenericHandshake.VARIABLE_PREPOSITION,BaseConfiguration.TYPE_PREPOSITION);
		addVariable(GenericHandshake.VARIABLE_LASTNAME,BaseConfiguration.TYPE_ALPHABETIC);
		
		addGenericPatterns("{firstName} {preposition} {lastName}");
		addGenericPatterns("{firstName} {lastName}");
		addGenericPatterns("{firstName}");
		
		addSpecificPatterns("firstname", "{firstName}");
		addSpecificPatterns("lastname", "{preposition} {lastName}");
		addSpecificPatterns("lastname", "{lastName}");
		
		super.initialize(translator);
	}
	
	private void addGenericPatterns(String variables) {
		addPattern(new ZStringSymbolParser("My name is " + variables + "."),"");
		addPattern(new ZStringSymbolParser("I am " + variables + "."),"");
		
		addPattern(new ZStringSymbolParser("What is your name? " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Who are you? " + variables + "."),"");
		
		addPattern(new ZStringSymbolParser("What is your name? My name is " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Who are you? My name is " + variables + "."),"");
		
		addPattern(new ZStringSymbolParser("What is your name? I am " + variables + "."),"");
		addPattern(new ZStringSymbolParser("Who are you? I am " + variables + "."),"");
		
		addPattern(new ZStringSymbolParser(variables + " is my name."),"");
		addPattern(new ZStringSymbolParser(variables + " am I."),"");
	}
	
	private void addSpecificPatterns(String name, String variables) {
		addPattern(new ZStringSymbolParser("What is your " + name + "? " + variables + "."),"");
		
		addPattern(new ZStringSymbolParser("What is your " + name + "? My " + name + " is " + variables + "."),"");
		
		addPattern(new ZStringSymbolParser(variables + " is my " + name + "."),"");
	}
}
