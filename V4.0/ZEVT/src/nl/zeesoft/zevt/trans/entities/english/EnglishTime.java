package nl.zeesoft.zevt.trans.entities.english;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.lang.Languages;

public class EnglishTime extends EntityObject {
	public EnglishTime(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.ENG;
	}
	@Override
	public String getType() {
		return Types.TIME;
	}
	@Override
	public int getMaximumSymbols() {
		return 8;
	}
	@Override
	public String getInternalValueForExternalValue(String str) {
		if (str.equals("now") || str.equals("right now")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int minute = cal.get(Calendar.MINUTE);
			minute = minute - ((minute + 5) % 5);
			if (minute==60) {
				minute = 0;
				cal.add(Calendar.HOUR,1);
			}
			str = String.format("%02d",cal.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d",minute) + ":" + "00";
		} else {
			str = super.getInternalValueForExternalValue(str);
		}
		return str;
	}
	@Override
	public void initializeEntityValues() {
		EnglishNumeric eoNumeric = (EnglishNumeric) getTranslator().getEntityObject(Languages.ENG,Types.NUMERIC);
		if (!eoNumeric.isInitialized()) {
			eoNumeric.initialize();
		}
		
		addEntityValue("midnight","00:00:00",0L);

		for (int h = 0; h<24; h++) {
			String HH = String.format("%02d",h);

			String H = "";
			if (h==0) {
				H = "midnight";
			} else {
				if (h>12) {
					H = eoNumeric.getExternalValueForInternalValue("" + (h % 12));
				} else {
					H = eoNumeric.getExternalValueForInternalValue("" + h);
				}
			}

			int hn = (h + 1) % 12;
			if (hn==0) {
				hn = 12;
			}
			String HN = eoNumeric.getExternalValueForInternalValue("" + hn);
			
			String period = " in the morning";
			if (h>=17) {
				period = " in the evening";
			} else if (h>=12) {
				period = " in the afternoon";
			}

			for (int m = 0; m<60; m++) {
				String M = eoNumeric.getExternalValueForInternalValue("" + m);
				String MM = String.format("%02d",m);
				String value = HH + ":" + MM + ":" + "00";
				String minutes = "minutes";
				
				Long typeVal = ((long)h * 3600000l) + ((long)m * 60000l);
				
				if (m==0) {
					if (h>0) {
						addPatternStringAndValue(H + " o'clock",value,typeVal,period,(h>=7 && h<19));
						addPatternStringAndValue(H + " oclock",value,typeVal,period,(h>=7 && h<19));
						if (h<12) {
							addPatternStringAndValue((h % 12) + " o'clock",value,typeVal,period,(h>=7 && h<19));
							addPatternStringAndValue((h % 12) + " oclock",value,typeVal,period,(h>=7 && h<19));
						} else {
							addPatternStringAndValue(h + " o'clock",value,typeVal,period,(h>=7 && h<19));
							addPatternStringAndValue(h + " oclock",value,typeVal,period,(h>=7 && h<19));
						}
					}
				} else { 
					if (m==1) {
						minutes = "minute";
					}
					if (m == 15) {
						addPatternStringAndValue("a quarter past " + H,value,typeVal,period,(h>=7 && h<19));
					}
					if (m == 30) {
						addPatternStringAndValue("half past " + H,value,typeVal,period,(h>=7 && h<19));
					}
					if (m == 45) {
						addPatternStringAndValue("a quarter to " + HN,value,typeVal,period,(h>=7 && h<19));
					}
					addPatternStringAndValue(M + " past " + H,value,typeVal,period,(h>=7 && h<19));
					addPatternStringAndValue(M + " " + minutes + " past " + H,value,typeVal,period,(h>=7 && h<19));
					if (m % 15 == 0) {
						addPatternStringAndValue(H + " " + M,value,typeVal,period,(h>=7 && h<19));
					}
					if (m >= 45) {
						int pm = (60 - m);
						minutes = "minutes";
						if (pm==1) {
							minutes = "minute";
						}
						addPatternStringAndValue(eoNumeric.getExternalValueForInternalValue("" + pm) + " to " + HN,value,typeVal,period,(h>=7 && h<19));
						addPatternStringAndValue(eoNumeric.getExternalValueForInternalValue("" + pm) + " " + minutes + " to " + HN,value,typeVal,period,(h>=7 && h<19));
					}
				}
			}
		}
	}
	private void addPatternStringAndValue(String str,String val,Long typeVal,String period,boolean addWithoutPeriod) {
		if (addWithoutPeriod) {
			addEntityValue(str,val,typeVal);
		}
		addEntityValue(str + period,val,typeVal);
	}
}
