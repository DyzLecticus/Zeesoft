package nl.zeesoft.zevt.trans.entities.dutch;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.EntityValueTranslator;
import nl.zeesoft.zevt.trans.UniversalMathematic;

public class DutchMathematic extends EntityObject {
	@Override
	public String getLanguage() {
		return LANG_NLD;
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
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		addSymbol("vermenigvuldigd met",UniversalMathematic.MULTIPLICATION);
		addSymbol("keer",UniversalMathematic.MULTIPLICATION);
		addSymbol("maal",UniversalMathematic.MULTIPLICATION);
		addSymbol("gedeeld door",UniversalMathematic.DIVISION);
		addSymbol("plus",UniversalMathematic.ADDITION);
		addSymbol("min",UniversalMathematic.SUBTRACTION);
	}
	private void addSymbol(String eVal,String iVal) {
		addEntityValue(eVal,iVal,iVal);
	}
}
