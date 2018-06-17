package nl.zeesoft.zsmc.entity.dutch;

import nl.zeesoft.zsmc.EntityValueTranslator;
import nl.zeesoft.zsmc.entity.EntityObject;

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
						strDec = getExternalValueForInternalValue("" + (dec * 10) + num);
					}
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
				if (dec>1 && num>0) {
					strDec = strNum + "en" + strDec;
					strNum = "";
				}
				
				String strCent = "";
				if (cent>0) {
					if (cent==1) {
						strCent = "honderd";
					} else {
						strCent = getExternalValueForInternalValue("" + cent) + "honderd";
					}
					if (dec==0 && num==0) {
						strDec = "";
						strNum = "";
					}
				}
				
				String strMill = "";
				if (mill>0) {
					if (mill==1) {
						strMill = "duizend";
					} else {
						strMill = getExternalValueForInternalValue("" + mill) + "duizend";
					}
				}
				
				// TODO: Fix dutch numeric
				if (i>1900 && i<3000) {
					System.out.println(i + " = " + strMill + strCent + strDec + strNum);
				}
				addEntityValue(strMill + strCent + strDec + strNum,i);
			}
		}
	}
}
