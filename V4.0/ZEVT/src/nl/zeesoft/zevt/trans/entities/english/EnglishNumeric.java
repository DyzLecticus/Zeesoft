package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;

public class EnglishNumeric extends EntityObject {
	public EnglishNumeric(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return LANG_ENG;
	}
	@Override
	public String getType() {
		return TYPE_NUMERIC;
	}
	@Override
	public void initializeEntityValues() {
		for (int i = 0; i<=getTranslator().getMaximumNumber(); i++) {
			if (i==0) {
				addEntityValue("zero");
			} else if (i==1) {
				addEntityValue("one");
			} else if (i==2) {
				addEntityValue("two");
			} else if (i==3) {
				addEntityValue("three");
			} else if (i==4) {
				addEntityValue("four");
			} else if (i==5) {
				addEntityValue("five");
			} else if (i==6) {
				addEntityValue("six");
			} else if (i==7) {
				addEntityValue("seven");
			} else if (i==8) {
				addEntityValue("eight");
			} else if (i==9) {
				addEntityValue("nine");
			} else if (i==10) {
				addEntityValue("ten");
			} else if (i==11) {
				addEntityValue("eleven");
			} else if (i==12) {
				addEntityValue("twelve");
			} else if (i==13) {
				addEntityValue("thirteen");
			} else if (i==14) {
				addEntityValue("fourteen");
			} else if (i==15) {
				addEntityValue("fifteen");
			} else if (i==16) {
				addEntityValue("sixteen");
			} else if (i==17) {
				addEntityValue("seventeen");
			} else if (i==18) {
				addEntityValue("eighteen");
			} else if (i==19) {
				addEntityValue("nineteen");
			} else if (i==20) {
				addEntityValue("twenty");
			} else if (i>20) {
				int num = i % 10;
				int dec = ((i - num) % 100) / 10;
				int cent = ((i - num - dec) % 1000) / 100;
				int mill = 0;
				
				if (i>999) {
					String sVal = "" + i;
					int l = sVal.length() - 3;
					mill = Integer.parseInt(sVal.substring(0,l));
				}
				
				String strNum = "";
				String strDec = "";
				String strCent = "";
				String strMill = "";
				
				if (mill>0) {
					strMill = getExternalValueForInternalValue("" + mill);
					strMill += "thousand";
				}
				if (cent>0) {
					strCent = getExternalValueForInternalValue("" + cent);
					strCent += "hundred";
				}
				if (dec==1) {
					strDec = getExternalValueForInternalValue("" + ((dec * 10) + num));
					num = 0;
				} else if (dec==2) {
					strDec = "twenty";
				} else if (dec==3) {
					strDec = "thirty";
				} else if (dec==4) {
					strDec = "fourty";
				} else if (dec==5) {
					strDec = "fifty";
				} else if (dec==6) {
					strDec = "sixty";
				} else if (dec==7) {
					strDec = "seventy";
				} else if (dec==8) {
					strDec = "eighty";
				} else if (dec==9) {
					strDec = "ninety";
				}
				if (num>0) {
					strNum = getExternalValueForInternalValue("" + num);
				}
				
				if (strCent.length()>0 && (strDec.length()>0 || strNum.length()>0)) {
					strCent += "and";
				}
				addEntityValue(strMill + strCent + strDec + strNum,i);
			}
		}
	}
}
