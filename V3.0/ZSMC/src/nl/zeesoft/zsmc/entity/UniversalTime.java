package nl.zeesoft.zsmc.entity;

import nl.zeesoft.zsmc.EntityValueTranslator;

public class UniversalTime extends EntityObject {
	@Override
	public String getType() {
		return TYPE_TIME;
	}
	@Override
	public String getInternalValueForExternalValue(String str) {
		return super.getInternalValueForExternalValue(correctTimeString(str));
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		for (int h = 0; h<24; h++) {
			for (int m = 0; m<60; m++) {
				for (int s = 0; s<60; s++) {
					String val = String.format("%02d",h) + ":" + String.format("%02d",m) + ":" + String.format("%02d",s);
					Long typeVal = ((long)h * 3600000l) + ((long)m * 60000l) + ((long)s * 1000l);
					addEntityValue(val,val,typeVal);
				}
			}
		}
	}

	private String correctTimeString(String str) {
		String[] split = str.split(":");
		String r = "";
		for (int i = 0; i < split.length; i++) {
			if (split[i].length()==2) {
				r += split[i]; 
			} else if (split[i].length()==1) {
				r += "0" + split[i];
			} else if (split[i].length()==0) {
				r += "00";
			}
			if (i<2) {
				r += ":";
			}
		}
		if (split.length==2) {
			r += "00";
		}
		return r;
	}
}
