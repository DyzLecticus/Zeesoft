package nl.zeesoft.zevt.trans.entities.dutch;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zevt.trans.UniversalMathematic;
import nl.zeesoft.zodb.lang.Languages;

public class DutchMathematic extends EntityObject {
	public DutchMathematic(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.NLD;
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
