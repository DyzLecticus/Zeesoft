package nl.zeesoft.zspr.pattern.patterns.english;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zdk.Generic;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class EnglishTime extends PatternObjectLiteralToValue {
	public EnglishTime() {
		super(TYPE_TIME,"ENG");
	}
	
	@Override
	public int getMaximumSymbols() {
		return 8;
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		EnglishNumber numPattern = (EnglishNumber) manager.getPatternByClassName(EnglishNumber.class.getName());
		if (numPattern.getNumPatternStrings()==0) {
			numPattern.initializePatternStrings(manager);
		}

		addPatternStringAndValue("midnight","00:00:00");

		for (int h = 0; h<24; h++) {
			String HH = Generic.minStrInt(h,2);

			String H = "";
			if (h==0) {
				H = "midnight";
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
			
			String period = " in the morning";
			if (h>=17) {
				period = " in the evening";
			} else if (h>=12) {
				period = " in the afternoon";
			}

			for (int m = 0; m<60; m++) {
				String M = numPattern.transformValueToString("" + m);
				String MM = Generic.minStrInt(m,2);
				String value = HH + ":" + MM + ":" + "00";
				String minutes = "minutes";
				
				if (m==0) {
					if (h>0) {
						addPatternStringAndValue(H + " o'clock",value,period,(h>=7 && h<19));
						addPatternStringAndValue(H + " oclock",value,period,(h>=7 && h<19));
						if (h<12) {
							addPatternStringAndValue((h % 12) + " o'clock",value,period,(h>=7 && h<19));
							addPatternStringAndValue((h % 12) + " oclock",value,period,(h>=7 && h<19));
						} else {
							addPatternStringAndValue(h + " o'clock",value,period,(h>=7 && h<19));
							addPatternStringAndValue(h + " oclock",value,period,(h>=7 && h<19));
						}
					}
				} else { 
					if (m==1) {
						minutes = "minute";
					}
					if (m == 15) {
						addPatternStringAndValue("a quarter past " + H,value,period,(h>=7 && h<19));
					}
					if (m == 30) {
						addPatternStringAndValue("half past " + H,value,period,(h>=7 && h<19));
					}
					if (m == 45) {
						addPatternStringAndValue("a quarter to " + HN,value,period,(h>=7 && h<19));
					}
					addPatternStringAndValue(M + " past " + H,value,period,(h>=7 && h<19));
					addPatternStringAndValue(M + " " + minutes + " past " + H,value,period,(h>=7 && h<19));
					if (m % 15 == 0) {
						addPatternStringAndValue(H + " " + M,value,period,(h>=7 && h<19));
					}
					if (m >= 45) {
						int pm = (60 - m);
						minutes = "minutes";
						if (pm==1) {
							minutes = "minute";
						}
						addPatternStringAndValue(numPattern.transformValueToString("" + pm) + " to " + HN,value,period,(h>=7 && h<19));
						addPatternStringAndValue(numPattern.transformValueToString("" + pm) + " " + minutes + " to " + HN,value,period,(h>=7 && h<19));
					}
				}
			}
			
		}
	}

	@Override
	public boolean stringMatchesPatternNoLock(String str) {
		boolean r = false;
		if (str.equals("now") || str.equals("right now")) {
			r = true;
		} else {
			r = super.stringMatchesPatternNoLock(str);
		}
		return r;
	}

	@Override
	protected String transformStringToValueNoLock(String str) {
		if (str.equals("now") || str.equals("right now")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int minute = cal.get(Calendar.MINUTE);
			minute = minute - ((minute + 5) % 5);
			if (minute==60) {
				minute = 0;
				cal.add(Calendar.HOUR,1);
			}
			str = Generic.minStrInt(cal.get(Calendar.HOUR_OF_DAY),2) + ":" + Generic.minStrInt(minute,2) + ":00";
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
