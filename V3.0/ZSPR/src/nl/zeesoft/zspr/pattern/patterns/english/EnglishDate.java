package nl.zeesoft.zspr.pattern.patterns.english;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteralToValue;

public class EnglishDate extends PatternObjectLiteralToValue {
	private	Date	currentDate	= null;
	
	public EnglishDate(Messenger msgr) {
		super(msgr,TYPE_DATE,Language.ENG);
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		EnglishNumber numPattern = (EnglishNumber) manager.getPatternByClassName(EnglishNumber.class.getName());
		if (numPattern.getNumPatternStrings()==0) {
			numPattern.initializePatternStrings(manager);
		}
		EnglishOrder orderPattern = (EnglishOrder) manager.getPatternByClassName(EnglishOrder.class.getName());
		if (orderPattern.getNumPatternStrings()==0) {
			orderPattern.initializePatternStrings(manager);
		}
		EnglishOrder2 orderPattern2 = (EnglishOrder2) manager.getPatternByClassName(EnglishOrder2.class.getName());
		if (orderPattern2.getNumPatternStrings()==0) {
			orderPattern2.initializePatternStrings(manager);
		}
		EnglishMonth monthPattern = (EnglishMonth) manager.getPatternByClassName(EnglishMonth.class.getName());
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
			String DO = orderPattern.getPatternString(date - 1);
			String DO2 = orderPattern2.getPatternString(date - 1);

			addPatternStringAndValue(M + " " + DO + " " + Y,value);
			addPatternStringAndValue("the " + DO + " of " + M + " " + Y,value);
			addPatternStringAndValue(M + " " + DO,value);
			addPatternStringAndValue("the " + DO + " of " + M,value);

			addPatternStringAndValue(M + " " + DO2 + " " + year,value);
			addPatternStringAndValue("the " + DO2 + " of " + M + " " + year,value);
			addPatternStringAndValue(M + " " + DO2,value);
			addPatternStringAndValue("the " + DO2 + " of " + M,value);
			
			cal.add(Calendar.DATE,1);
		}
	}

	@Override
	protected boolean stringMatchesPatternNoLock(String str) {
		boolean r = false;
		if (str.equals("now") || 
			str.equals("right now") || 
			str.equals("today") ||
			str.equals("tomorrow") ||
			str.equals("the day after tomorrow")
			) {
			r = true;
		} else {
			r = super.stringMatchesPatternNoLock(str);
		}
		return r;
	}

	@Override
	protected String transformStringToValueNoLock(String str) {
		if (str.equals("now") || str.equals("right now") || str.equals("today")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			str = getValueFromCalendar(cal);
		} else if (str.equals("tomorrow")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
			str = getValueFromCalendar(cal);
		} else if (str.equals("the day after tomorrow")) {
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
		return 4;
	}
	
	private String getValueFromCalendar(Calendar cal) {
		return 
			minStrInt(cal.get(Calendar.YEAR),4) + "-" + 
			minStrInt((cal.get(Calendar.MONTH) + 1),2) + "-" + 
			minStrInt(cal.get(Calendar.DATE),2);
	}
}
