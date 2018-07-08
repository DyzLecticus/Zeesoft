package nl.zeesoft.zsd.entity.dutch;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class DutchTime extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_NLD;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_TIME;
	}
	@Override
	public int getMaximumSymbols() {
		return 8;
	}
	@Override
	public String getInternalValueForExternalValue(String str) {
		if (str.equals("nu") || str.equals("nu direct")) {
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
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);

		getToJsonPrefixes().add("om");

		DutchNumeric eoNumeric = (DutchNumeric) translator.getEntityObject(BaseConfiguration.LANG_NLD,BaseConfiguration.TYPE_NUMERIC);
		if (!eoNumeric.isInitialized()) {
			eoNumeric.initialize(translator);
		}
		
		addEntityValue("middernacht","00:00:00",0l);

		for (int h = 0; h<24; h++) {
			String HH = String.format("%02d",h);

			String H = "";
			if (h==0) {
				H = "middernacht";
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
			
			String period = " sochtends";
			if (h>=17) {
				period = " savonds";
			} else if (h>=12) {
				period = " smiddags";
			}
			
			for (int m = 0; m<60; m++) {
				String M = eoNumeric.getExternalValueForInternalValue("" + m);
				String MM = String.format("%02d",m);
				String value = HH + ":" + MM + ":00";
				String minutes = "minuten";
				
				Long typeVal = ((long)h * 3600000l) + ((long)m * 60000l);
				
				if (m==0) {
					if (h>0) {
						addPatternStringAndValue(H + " uur",value,typeVal,period,(h>=7 && h<19));
						if (h<12) {
							addPatternStringAndValue((h % 12) + " uur",value,typeVal,period,(h>=7 && h<19));
						} else {
							addPatternStringAndValue(h + " uur",value,typeVal,period,(h>=7 && h<19));
						}
					}
				} else { 
					if (m==1) {
						minutes = "minuut";
					}
					if (m == 15) {
						addPatternStringAndValue("kwart over " + H,value,typeVal,period,(h>=7 && h<19));
					}
					if (m == 30) {
						addPatternStringAndValue("half " + HN,value,typeVal,period,(h>=7 && h<19));
					}
					if (m == 45) {
						addPatternStringAndValue("Kwart voor " + HN,value,typeVal,period,(h>=7 && h<19));
					}
					addPatternStringAndValue(H + " uur " + M,value,typeVal,period,(h>=7 && h<19));
					addPatternStringAndValue(M + " over " + H,value,typeVal,period,(h>=7 && h<19));
					addPatternStringAndValue(M + " " + minutes + " over " + H,value,typeVal,period,(h>=7 && h<19));
					if (m > 45) {
						int pm = (60 - m);
						minutes = "minuten";
						if (pm==1) {
							minutes = "minuut";
						}
						addPatternStringAndValue(eoNumeric.getExternalValueForInternalValue("" + pm) + " voor " + HN,value,typeVal,period,(h>=7 && h<19));
						addPatternStringAndValue(eoNumeric.getExternalValueForInternalValue("" + pm) + " " + minutes + " voor " + HN,value,typeVal,period,(h>=7 && h<19));
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
