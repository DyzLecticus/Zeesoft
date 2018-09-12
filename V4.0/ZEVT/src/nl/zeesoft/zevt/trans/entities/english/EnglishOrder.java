package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zodb.Languages;

public class EnglishOrder extends EntityObject {
	public EnglishOrder(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.ENG;
	}
	@Override
	public String getType() {
		return TYPE_ORDER;
	}
	@Override
	public void initializeEntityValues() {
		EnglishNumeric eo = (EnglishNumeric) getTranslator().getEntityObject(Languages.ENG,TYPE_NUMERIC);
		if (!eo.isInitialized()) {
			eo.initialize();
		}
		
		int max = getTranslator().getMaximumOrder();
		if (getTranslator().getMaximumOrder()>getTranslator().getMaximumNumber()) {
			max = getTranslator().getMaximumNumber();
		}

		for (int i = 1; i<=max; i++) {
			String value = "" + i;
			if (i==1) {
				addEntityValue("first",value,i);
			} else if (i==2) {
				addEntityValue("second",value,i);
			} else if (i==3) {
				addEntityValue("third",value,i);
			} else if (i==4) {
				addEntityValue("fourth",value,i);
			} else if (i==5) {
				addEntityValue("fifth",value,i);
			} else if (i==6) {
				addEntityValue("sixth",value,i);
			} else if (i==7) {
				addEntityValue("seventh",value,i);
			} else if (i==8) {
				addEntityValue("eighth",value,i);
			} else if (i==9) {
				addEntityValue("ninth",value,i);
			} else if (i==10) {
				addEntityValue("tenth",value,i);
			} else if (i==11) {
				addEntityValue("eleventh",value,i);
			} else if (i==12) {
				addEntityValue("twelfth",value,i);
			} else if (i==13) {
				addEntityValue("thirteenth",value,i);
			} else if (i==14) {
				addEntityValue("fourteenth",value,i);
			} else if (i==15) {
				addEntityValue("fifteenth",value,i);
			} else if (i==16) {
				addEntityValue("sixteenth",value,i);
			} else if (i==17) {
				addEntityValue("seventeenth",value,i);
			} else if (i==18) {
				addEntityValue("eighteenth",value,i);
			} else if (i==19) {
				addEntityValue("nineteenth",value,i);
			} else if (i==20) {
				addEntityValue("twentieth",value,i);
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

				String strNum = "";
				if (num>0) {
					strNum = getExternalValueForInternalValue("" + num);
				}
				
				String strDec = "";
				if (dec<=1) {
					strNum = "";
					if (dec>0||num>0) {
						strDec = getExternalValueForInternalValue("" + ((dec * 10) + num));
					}
				} else if (dec==2) {
					if (num==0) {
						strDec = "twentieth";
					} else {
						strDec = "twenty";
					}
				} else if (dec==3) {
					if (num==0) {
						strDec = "thirtieth";
					} else {
						strDec = "thirty";
					}
				} else if (dec==4) {
					if (num==0) {
						strDec = "fourtieth";
					} else {
						strDec = "fourty";
					}
				} else if (dec==5) {
					if (num==0) {
						strDec = "fiftieth";
					} else {
						strDec = "fifty";
					}
				} else if (dec==6) {
					if (num==0) {
						strDec = "sixtieth";
					} else {
						strDec = "sixty";
					}
				} else if (dec==7) {
					if (num==0) {
						strDec = "seventieth";
					} else {
						strDec = "seventy";
					}
				} else if (dec==8) {
					if (num==0) {
						strDec = "eightieth";
					} else {
						strDec = "eighty";
					}
				} else if (dec==9) {
					if (num==0) {
						strDec = "ninetieth";
					} else {
						strDec = "ninety";
					}
				}
				
				String strCent = "";
				if (cent>0) {
					strCent = eo.getExternalValueForInternalValue("" + cent);
					if (num==0 && dec==0) {
						strCent += "hundredth";
					} else {
						strCent += "hundredand";
					}
				}
				
				String strMill = "";
				if (mill>0) {
					strMill = eo.getExternalValueForInternalValue("" + mill);
					if (num==0 && dec==0 && cent==0) {
						strMill += "thousandth";
					} else  {
						strMill += "thousand";
					}
				}

				addEntityValue(strMill + strCent + strDec + strNum,value,i);
			}
		}
	}
}
