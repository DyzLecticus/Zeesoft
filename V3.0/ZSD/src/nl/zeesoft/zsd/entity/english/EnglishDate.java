package nl.zeesoft.zsd.entity.english;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class EnglishDate extends EntityObject {
	private	Date	currentDate	= null;
	@Override
	public String getLanguage() {
		return LANG_ENG;
	}
	@Override
	public String getType() {
		return TYPE_DATE;
	}
	@Override
	public int getMaximumSymbols() {
		return 4;
	}
	@Override
	public String getInternalValueForExternalValue(String str) {
		if (str.equals("now") || str.equals("right now") || str.equals("today")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			str = getInternalValuePrefix() + getValueFromCalendar(cal);
		} else if (str.equals("yesterday")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
			str = getInternalValuePrefix() + getValueFromCalendar(cal);
		} else if (str.equals("tomorrow")) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
			str = getInternalValuePrefix() + getValueFromCalendar(cal);
		} else if (str.equals("the day after tomorrow")) {
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
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);

		EnglishNumeric eoNumeric = (EnglishNumeric) translator.getEntityObject(LANG_ENG,TYPE_NUMERIC);
		if (!eoNumeric.isInitialized()) {
			eoNumeric.initialize(translator);
		}
		
		EnglishOrder eoOrder = (EnglishOrder) translator.getEntityObject(LANG_ENG,TYPE_ORDER);
		if (!eoOrder.isInitialized()) {
			eoOrder.initialize(translator);
		}

		EnglishOrder2 eoOrder2 = (EnglishOrder2) translator.getEntityObject(LANG_ENG,TYPE_ORDER2);
		if (!eoOrder2.isInitialized()) {
			eoOrder2.initialize(translator);
		}
		
		EnglishMonth eoMonth = (EnglishMonth) translator.getEntityObject(LANG_ENG,TYPE_MONTH);
		if (!eoMonth.isInitialized()) {
			eoMonth.initialize(translator);
		}

		currentDate = translator.getCurrentDate();
		
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
			String DO = eoOrder.getExternalValueForInternalValue("" + date);
			String DO2 = eoOrder2.getExternalValueForInternalValue("" + date);

			addEntityValue(M + " " + DO + " " + Y,value,cal.getTime());
			addEntityValue("the " + DO + " of " + M + " " + Y,value,cal.getTime());
			addEntityValue(M + " " + DO,value,cal.getTime());
			addEntityValue("the " + DO + " of " + M,value,cal.getTime());

			addEntityValue(M + " " + DO2 + " " + year,value,cal.getTime());
			addEntityValue("the " + DO2 + " of " + M + " " + year,value,cal.getTime());
			addEntityValue(M + " " + DO2,value,cal.getTime());
			addEntityValue("the " + DO2 + " of " + M,value,cal.getTime());
			
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
