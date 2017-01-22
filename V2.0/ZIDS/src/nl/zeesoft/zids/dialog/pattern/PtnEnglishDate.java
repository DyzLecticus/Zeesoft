package nl.zeesoft.zids.dialog.pattern;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zodb.Generic;

public class PtnEnglishDate extends PtnObjectLiteralToValue {
	public PtnEnglishDate() {
		super(TYPE_DATE,"ENG");
	}
	
	@Override
	public void initializePatternStrings(PtnManager manager) {
		PtnEnglishNumber numPattern = (PtnEnglishNumber) manager.getPatternByClassName(PtnEnglishNumber.class.getName());
		if (numPattern.getNumPatternStrings()==0) {
			numPattern.initializePatternStrings(manager);
		}
		PtnEnglishOrder orderPattern = (PtnEnglishOrder) manager.getPatternByClassName(PtnEnglishOrder.class.getName());
		if (orderPattern.getNumPatternStrings()==0) {
			orderPattern.initializePatternStrings(manager);
		}
		PtnEnglishOrder2 orderPattern2 = (PtnEnglishOrder2) manager.getPatternByClassName(PtnEnglishOrder2.class.getName());
		if (orderPattern2.getNumPatternStrings()==0) {
			orderPattern2.initializePatternStrings(manager);
		}
		PtnEnglishMonth monthPattern = (PtnEnglishMonth) manager.getPatternByClassName(PtnEnglishMonth.class.getName());
		if (monthPattern.getNumPatternStrings()==0) {
			monthPattern.initializePatternStrings(manager);
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
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
			cal.setTime(new Date());
			str = getValueFromCalendar(cal);
		} else if (str.equals("tomorrow")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
			str = getValueFromCalendar(cal);
		} else if (str.equals("the day after tomorrow")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
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
			Generic.minStrInt(cal.get(Calendar.YEAR),4) + "-" + 
			Generic.minStrInt((cal.get(Calendar.MONTH) + 1),2) + "-" + 
			Generic.minStrInt(cal.get(Calendar.DATE),2);
	}
}
