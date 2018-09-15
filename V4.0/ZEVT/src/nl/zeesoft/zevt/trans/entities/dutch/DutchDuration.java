package nl.zeesoft.zevt.trans.entities.dutch;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zodb.lang.Languages;

public class DutchDuration extends EntityObject {
	public DutchDuration(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.NLD;
	}
	@Override
	public String getType() {
		return TYPE_DURATION;
	}
	@Override
	public int getMaximumSymbols() {
		return 5;
	}
	@Override
	public void initializeEntityValues() {
		DutchNumeric eo = (DutchNumeric) getTranslator().getEntityObject(Languages.NLD,TYPE_NUMERIC);
		if (!eo.isInitialized()) {
			eo.initialize();
		}
		
		for (int h = 0; h<100; h++) {
			for (int m = 0; m<=59; m++) {
				String value = String.format("%02d",h) + ":" + String.format("%02d",m);
				String H = eo.getExternalValueForInternalValue("" + h);
				String M = eo.getExternalValueForInternalValue("" + m);
				
				Long typeVal = ((long)h * 3600000l) + ((long)m * 60000l);
				
				String hours = "uur";
				String minutes = "minuten";
				if (m==1) {
					minutes = "minuut";
				}
				if (h==0) {
					addEntityValue(M + " " + minutes,value,typeVal);
					addEntityValue(m + " " + minutes,value,typeVal);
					if (m==15) {
						addEntityValue("een kwartier",value,typeVal);
					}
					if (m==30) {
						addEntityValue("een half uur",value,typeVal);
					}
					if (m==45) {
						addEntityValue("drie kwartier",value,typeVal);
					}
				} else {
					if (m==15) {
						addEntityValue(H + " " + hours + " en een kwartier",value,typeVal);
					}
					if (m==30) {
						if (h==1) {
							addEntityValue("anderhalf uur",value,typeVal);
						} else {
							addEntityValue(H + " en een half uur",value,typeVal);
						}
					}
					if (m==45) {
						addEntityValue(H + " " + hours + " en drie kwartier",value,typeVal);
					}
				}
				if (m==0) {
					addEntityValue(H + " " + hours,value,typeVal);
					addEntityValue(h + " " + hours,value,typeVal);
				}
				addEntityValue(H + " " + hours + " en " + M + " " + minutes,value,typeVal);
				addEntityValue(h + " " + hours + " en " + m + " " + minutes,value,typeVal);
			}
		}
	}
}