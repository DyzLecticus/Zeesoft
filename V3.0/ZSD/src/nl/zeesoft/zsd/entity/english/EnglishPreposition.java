package nl.zeesoft.zsd.entity.english;

import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class EnglishPreposition extends EntityObject {
	@Override
	public String getLanguage() {
		return LANG_ENG;
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
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		
		for (int i = 1; i<=4; i++) {
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
			}
			addEntityValue(name,value,name);
		}
	}
}
