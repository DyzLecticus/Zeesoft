package nl.zeesoft.zsd.entity.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class DutchPreposition extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_NLD;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_PREPOSITION;
	}
	@Override
	public int getMaximumSymbols() {
		return 2;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		
		for (int i = 1; i<=8; i++) {
			String value = "" + i;
			String name = "";
			if (i==1) {
				name = "van";
			} else if (i==2) {
				name = "van de";
			} else if (i==3) {
				name = "van der";
			} else if (i==4) {
				name = "van den";
			} else if (i==5) {
				name = "de";
			} else if (i==6) {
				name = "der";
			} else if (i==7) {
				name = "den";
			} else if (i==8) {
				name = "ten";
			}
			addEntityValue(name,value,name);
		}
	}
}
