package nl.zeesoft.zsd.entity.entities.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.UniversalMathematic;

public class EnglishMathematic extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_ENG;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_MATHEMATIC;
	}
	@Override
	public int getMaximumSymbols() {
		return 2;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		addSymbol("multiplied by",UniversalMathematic.MULTIPLICATION);
		addSymbol("times",UniversalMathematic.MULTIPLICATION);
		addSymbol("divided by",UniversalMathematic.DIVISION);
		addSymbol("plus",UniversalMathematic.ADDITION);
		addSymbol("minus",UniversalMathematic.SUBTRACTION);
	}
	private void addSymbol(String eVal,String iVal) {
		addEntityValue(eVal,iVal,iVal);
	}
}
