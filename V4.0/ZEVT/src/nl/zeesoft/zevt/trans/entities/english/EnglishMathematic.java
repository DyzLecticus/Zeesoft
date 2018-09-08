package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.EntityValueTranslator;
import nl.zeesoft.zevt.trans.UniversalMathematic;

public class EnglishMathematic extends EntityObject {
	public EnglishMathematic(EntityValueTranslator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return LANG_ENG;
	}
	@Override
	public String getType() {
		return TYPE_MATHEMATIC;
	}
	@Override
	public int getMaximumSymbols() {
		return 2;
	}
	@Override
	public void initializeEntityValues() {
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
