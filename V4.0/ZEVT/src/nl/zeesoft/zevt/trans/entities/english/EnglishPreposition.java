package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.znlb.lang.Languages;

public class EnglishPreposition extends EntityObject {
	public EnglishPreposition(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.ENG;
	}
	@Override
	public String getType() {
		return TYPE_PREPOSITION;
	}
	@Override
	public int getMaximumSymbols() {
		return 2;
	}
	@Override
	public void initializeEntityValues() {
		for (int i = 1; i<=5; i++) {
			String value = "" + i;
			String name = "";
			if (i==1) {
				name = "of";
			} else if (i==2) {
				name = "of the";
			} else if (i==3) {
				name = "from";
			} else if (i==4) {
				name = "from the";
			} else if (i==5) {
				name = "the";
			}
			addEntityValue(name,value,name);
		}
	}
}
