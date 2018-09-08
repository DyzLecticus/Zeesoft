package nl.zeesoft.zevt.trans.entities.dutch;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.EntityValueTranslator;

public class DutchNumeric extends EntityObject {
	@Override
	public String getLanguage() {
		return LANG_NLD;
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
				addEntityValue("nul");
			} else if (i==1) {
				addEntityValue("een");
			} else if (i==2) {
				addEntityValue("twee");
			} else if (i==3) {
				addEntityValue("drie");
			} else if (i==4) {
				addEntityValue("vier");
			} else if (i==5) {
				addEntityValue("vijf");
			} else if (i==6) {
				addEntityValue("zes");
			} else if (i==7) {
				addEntityValue("zeven");
			} else if (i==8) {
				addEntityValue("acht");
			} else if (i==9) {
				addEntityValue("negen");
			} else if (i==10) {
				addEntityValue("tien");
			} else if (i==11) {
				addEntityValue("elf");
			} else if (i==12) {
				addEntityValue("twaalf");
			} else if (i==13) {
				addEntityValue("dertien");
			} else if (i==14) {
				addEntityValue("veertien");
			} else if (i==15) {
				addEntityValue("vijftien");
			} else if (i==16) {
				addEntityValue("zestien");
			} else if (i==17) {
				addEntityValue("zeventien");
			} else if (i==18) {
				addEntityValue("achttien");
			} else if (i==19) {
				addEntityValue("negentien");
			} else if (i==20) {
				addEntityValue("twintig");
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
					if (mill>1) {
						strMill = getExternalValueForInternalValue("" + mill);
					} 
					strMill += "duizend";
				}
				if (cent>0) {
					if (cent>1) {
						strCent = getExternalValueForInternalValue("" + cent);
					}
					strCent += "honderd";
				}
				if (dec==1) {
					strDec = getExternalValueForInternalValue("" + ((dec * 10) + num));
					num = 0;
				} else if (dec==2) {
					strDec = "twintig";
				} else if (dec==3) {
					strDec = "dertig";
				} else if (dec==4) {
					strDec = "veertig";
				} else if (dec==5) {
					strDec = "vijftig";
				} else if (dec==6) {
					strDec = "zestig";
				} else if (dec==7) {
					strDec = "zeventig";
				} else if (dec==8) {
					strDec = "tachtig";
				} else if (dec==9) {
					strDec = "negentig";
				}
				if (num>0) {
					strNum = getExternalValueForInternalValue("" + num);
				}
				
				if (dec>0 && num>0) {
					strDec = strNum + "en" + strDec;
					strNum = "";
				}
				
				addEntityValue(strMill + strCent + strDec + strNum,i);
			}
		}
	}
}
