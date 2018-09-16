package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.lang.Languages;

public class EnglishDuration extends EntityObject {
	public EnglishDuration(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.ENG;
	}
	@Override
	public String getType() {
		return Types.DURATION;
	}
	@Override
	public int getMaximumSymbols() {
		return 5;
	}
	@Override
	public void initializeEntityValues() {
		EnglishNumeric eo = (EnglishNumeric) getTranslator().getEntityObject(Languages.ENG,Types.NUMERIC);
		if (!eo.isInitialized()) {
			eo.initialize();
		}

		for (int h = 0; h<100; h++) {
			for (int m = 0; m<=59; m++) {
				String value = String.format("%02d",h) + ":" + String.format("%02d",m);
				String H = eo.getExternalValueForInternalValue("" + h);
				String M = eo.getExternalValueForInternalValue("" + m);
				
				Long typeVal = ((long)h * 3600000l) + ((long)m * 60000l);
				
				String hours = "hours";
				if (h==1) {
					hours = "hour";
				}
				String minutes = "minutes";
				if (m==1) {
					minutes = "minute";
				}
				if (h==0) {
					addEntityValue(M + " " + minutes,value,typeVal);
					addEntityValue(m + " " + minutes,value,typeVal);
					if (m==30) {
						addEntityValue("half an hour",value,typeVal);
					}
				} else {
					if (m==30) {
						addEntityValue(H + " and a half " + hours,value,typeVal);
					}
				}
				if (m==0) {
					addEntityValue(H + " " + hours,value,typeVal);
					addEntityValue(h + " " + hours,value,typeVal);
				}
				addEntityValue(H + " " + hours + " and " + M + " " + minutes,value,typeVal);
				addEntityValue(h + " " + hours + " and " + m + " " + minutes,value,typeVal);
			}
		}
	}
}
