package nl.zeesoft.zspr.pattern.patterns.dutch;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class DutchDate extends PatternObjectLiteralToValue {
	private	Date	currentDate	= null;

	public DutchDate(Messenger msgr) {
		super(msgr,TYPE_DATE,Language.NLD);
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		DutchNumber numPattern = (DutchNumber) manager.getPatternByClassName(DutchNumber.class.getName());
		if (numPattern.getNumPatternStrings()==0) {
			numPattern.initializePatternStrings(manager);
		}
		DutchMonth monthPattern = (DutchMonth) manager.getPatternByClassName(DutchMonth.class.getName());
		if (monthPattern.getNumPatternStrings()==0) {
			monthPattern.initializePatternStrings(manager);
		}
		
		currentDate = manager.getCurrentDate();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		for (int day = 0; day<365; day++) {
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int date = cal.get(Calendar.DATE);
					
			String value = getValueFromCalendar(cal);
			
			String Y = numPattern.getPatternString(year);
			String M = monthPattern.getPatternString(month);
			String D = numPattern.getPatternString(date);
			
			addPatternStringAndValue(D + " " + M + " " + Y,value);
			addPatternStringAndValue(D + " " + M,value);
			addPatternStringAndValue(D + " " + M + " " + year,value);
			addPatternStringAndValue(date + " " + M,value);
			
			cal.add(Calendar.DATE,1);
		}
	}

	@Override
	protected boolean stringMatchesPatternNoLock(String str) {
		boolean r = false;
		if (str.equals("nu") || 
			str.equals("nu direct") || 
			str.equals("vandaag") ||
			str.equals("morgen") ||
			str.equals("overmorgen")
			) {
			r = true;
		} else {
			r = super.stringMatchesPatternNoLock(str);
		}
		return r;
	}

	@Override
	protected String transformStringToValueNoLock(String str) {
		if (str.equals("nu") || str.equals("nu direct") || str.equals("vandaag")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			str = getValueFromCalendar(cal);
		} else if (str.equals("morgen")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
			str = getValueFromCalendar(cal);
		} else if (str.equals("overmorgen")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 2);
			str = getValueFromCalendar(cal);
		} else {
			str = super.transformStringToValueNoLock(str);
		}
		return str;
	}
	
	@Override
	public int getMaximumSymbols() {
		return 3;
	}

	private String getValueFromCalendar(Calendar cal) {
		return 
			minStrInt(cal.get(Calendar.YEAR),4) + "-" + 
			minStrInt((cal.get(Calendar.MONTH) + 1),2) + "-" + 
			minStrInt(cal.get(Calendar.DATE),2);
	}
}
