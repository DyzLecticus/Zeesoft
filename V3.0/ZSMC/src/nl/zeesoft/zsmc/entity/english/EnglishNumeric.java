package nl.zeesoft.zsmc.entity.english;

import nl.zeesoft.zsmc.EntityValueTranslator;
import nl.zeesoft.zsmc.entity.EntityObject;

public class EnglishNumeric extends EntityObject {
	@Override
	public String getLanguage() {
		return LANG_ENG;
	}
	@Override
	public String getType() {
		return TYPE_NUMERIC;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		for (int i = 0; i<=translator.getMaximumNumber(); i++) {
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
				int dec = ((i - num) / 10);
				
				int cent = 0;
				int mill = 0;
				
				if (dec>=100) {
					mill = (dec - (dec % 100)) / 100;
					dec = dec % 100;
				}
				if (dec>=10) {
					cent = (dec - (dec % 10)) / 10;
					dec = dec % 10;
				}

				String strNum = getExternalValueForInternalValue("" + num);
				if (num==0) {
					strNum = "";
				}
				
				String strDec = "";
				if (dec<=1) {
					strNum = "";
					if (dec>0||num>0) {
						strDec = getExternalValueForInternalValue("" +(dec * 10) + num);
					}
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
				
				String strCent = "";
				if (cent>0) {
					strCent = getExternalValueForInternalValue("" + cent) + "hundred";
					if (num>0 || dec>0) {
						strCent += "and";
					}
				}
				
				String strMill = "";
				if (mill>0) {
					strMill = getExternalValueForInternalValue("" + mill) + "thousand";
				}
				
				addEntityValue(strMill + strCent + strDec + strNum);
			}
		}
	}
}
