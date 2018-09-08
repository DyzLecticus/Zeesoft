package nl.zeesoft.zevt.trans.entities.dutch;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.EntityValueTranslator;

public class DutchDate extends EntityObject {
	private	Date	currentDate	= null;
	public DutchDate(EntityValueTranslator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return LANG_NLD;
	}
	@Override
	public String getType() {
		return TYPE_DATE;
	}
	@Override
	public int getMaximumSymbols() {
		return 3;
	}
	@Override
	public String getInternalValueForExternalValue(String str) {
		if (str.equals("nu") || str.equals("nu direct") || str.equals("vandaag")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			str = getInternalValuePrefix() + getValueFromCalendar(cal);
		} else if (str.equals("gisteren")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
			str = getInternalValuePrefix() + getValueFromCalendar(cal);
		} else if (str.equals("morgen")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
			str = getInternalValuePrefix() + getValueFromCalendar(cal);
		} else if (str.equals("overmorgen")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 2);
			str = getInternalValuePrefix() + getValueFromCalendar(cal);
		} else {
			str = super.getInternalValueForExternalValue(str);
		}
		return str;
	}
	@Override
	public void initializeEntityValues() {
		DutchNumeric eoNumeric = (DutchNumeric) getTranslator().getEntityObject(LANG_NLD,TYPE_NUMERIC);
		if (!eoNumeric.isInitialized()) {
			eoNumeric.initialize();
		}
		
		DutchMonth eoMonth = (DutchMonth) getTranslator().getEntityObject(LANG_NLD,TYPE_MONTH);
		if (!eoMonth.isInitialized()) {
			eoMonth.initialize();
		}

		currentDate = getTranslator().getCurrentDate();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		cal.add(Calendar.DATE,-1);
		for (int day = 0; day<366; day++) {
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int date = cal.get(Calendar.DATE);
					
			String value = getValueFromCalendar(cal);
			
			String Y = eoNumeric.getExternalValueForInternalValue("" + year);
			String M = eoMonth.getExternalValueForInternalValue("" + (month + 1));
			String D = eoNumeric.getExternalValueForInternalValue("" + date);
			
			addEntityValue(D + " " + M + " " + Y,value,cal.getTime());
			addEntityValue(D + " " + M,value,cal.getTime());
			addEntityValue(D + " " + M + " " + year,value,cal.getTime());
			addEntityValue(date + " " + M,value,cal.getTime());
			
			cal.add(Calendar.DATE,1);
		}
	}
	private String getValueFromCalendar(Calendar cal) {
		return 
			String.format("%04d",cal.get(Calendar.YEAR)) + "-" + 
			String.format("%02d",(cal.get(Calendar.MONTH) + 1)) + "-" + 
			String.format("%02d",cal.get(Calendar.DATE));
	}
}
