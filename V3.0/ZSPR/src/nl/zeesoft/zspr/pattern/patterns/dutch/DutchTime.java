package nl.zeesoft.zspr.pattern.patterns.dutch;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class DutchTime extends PatternObjectLiteralToValue {
	public DutchTime(Messenger msgr) {
		super(msgr,TYPE_TIME,Language.NLD);
	}
	
	@Override
	public int getMaximumSymbols() {
		return 8;
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		DutchNumber numPattern = (DutchNumber) manager.getPatternByClassName(DutchNumber.class.getName());
		if (numPattern.getNumPatternStrings()==0) {
			numPattern.initializePatternStrings(manager);
		}

		addPatternStringAndValue("middernacht","00:00:00");

		for (int h = 0; h<24; h++) {
			String HH = minStrInt(h,2);

			String H = "";
			if (h==0) {
				H = "middernacht";
			} else {
				if (h>12) {
					H = numPattern.transformValueToString("" + (h % 12));
				} else {
					H = numPattern.transformValueToString("" + h);
				}
			}

			int hn = (h + 1) % 12;
			if (hn==0) {
				hn = 12;
			}
			String HN = numPattern.transformValueToString("" + hn);
			
			String period = " 's ochtends";
			if (h>=17) {
				period = " 's avonds";
			} else if (h>=12) {
				period = " 's middags";
			}
			
			for (int m = 0; m<60; m++) {
				String M = numPattern.transformValueToString("" + m);
				String MM = minStrInt(m,2);
				String value = HH + ":" + MM + ":00";
				String minutes = "minuten";
				
				if (m==0) {
					if (h>0) {
						addPatternStringAndValue(H + " uur",value,period,(h>=7 && h<19));
						if (h<12) {
							addPatternStringAndValue((h % 12) + " uur",value,period,(h>=7 && h<19));
						} else {
							addPatternStringAndValue(h + " uur",value,period,(h>=7 && h<19));
						}
					}
				} else { 
					if (m==1) {
						minutes = "minuut";
					}
					if (m == 15) {
						addPatternStringAndValue("kwart over " + H,value,period,(h>=7 && h<19));
					}
					if (m == 30) {
						addPatternStringAndValue("half " + HN,value,period,(h>=7 && h<19));
					}
					if (m == 45) {
						addPatternStringAndValue("Kwart voor " + HN,value,period,(h>=7 && h<19));
					}
					addPatternStringAndValue(H + " uur " + M,value,period,(h>=7 && h<19));
					addPatternStringAndValue(M + " over " + H,value,period,(h>=7 && h<19));
					addPatternStringAndValue(M + " " + minutes + " over " + H,value,period,(h>=7 && h<19));
					if (m > 45) {
						int pm = (60 - m);
						minutes = "minuten";
						if (pm==1) {
							minutes = "minuut";
						}
						addPatternStringAndValue(numPattern.transformValueToString("" + pm) + " voor " + HN,value,period,(h>=7 && h<19));
						addPatternStringAndValue(numPattern.transformValueToString("" + pm) + " " + minutes + " voor " + HN,value,period,(h>=7 && h<19));
					}
				}
			}
			
		}
	}

	@Override
	protected String transformStringToValueNoLock(String str) {
		if (str.equals("nu") || str.equals("nu direct")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int minute = cal.get(Calendar.MINUTE);
			minute = minute - ((minute + 5) % 5);
			if (minute==60) {
				minute = 0;
				cal.add(Calendar.HOUR,1);
			}
			str = minStrInt(cal.get(Calendar.HOUR_OF_DAY),2) + ":" + minStrInt(minute,2) + ":" + "00";
		} else {
			str = super.transformStringToValueNoLock(str);
		}
		return str;
	}

	private void addPatternStringAndValue(String str,String val,String period,boolean addWithoutPeriod) {
		if (addWithoutPeriod) {
			addPatternStringAndValue(str,val);
		}
		addPatternStringAndValue(str + period,val);
	}
}
