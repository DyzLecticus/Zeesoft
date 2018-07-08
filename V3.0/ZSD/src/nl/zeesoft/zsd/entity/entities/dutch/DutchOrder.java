package nl.zeesoft.zsd.entity.entities.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class DutchOrder extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_NLD;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_ORDER;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		
		DutchNumeric eo = (DutchNumeric) translator.getEntityObject(BaseConfiguration.LANG_NLD,BaseConfiguration.TYPE_NUMERIC);
		if (!eo.isInitialized()) {
			eo.initialize(translator);
		}
		
		int max = translator.getMaximumOrder();
		if (translator.getMaximumOrder()>translator.getMaximumNumber()) {
			max = translator.getMaximumNumber();
		}

		for (int i = 1; i<=max; i++) {
			String value = "" + i;
			if (i==1) {
				addEntityValue("eerste",value,i);
			} else if (i==2) {
				addEntityValue("tweede",value,i);
			} else if (i==3) {
				addEntityValue("derde",value,i);
			} else if (i==4) {
				addEntityValue("vierde",value,i);
			} else if (i==5) {
				addEntityValue("vijfde",value,i);
			} else if (i==6) {
				addEntityValue("zesde",value,i);
			} else if (i==7) {
				addEntityValue("zevende",value,i);
			} else if (i==8) {
				addEntityValue("achtste",value,i);
			} else if (i==9) {
				addEntityValue("negende",value,i);
			} else if (i==10) {
				addEntityValue("tiende",value,i);
			} else if (i==11) {
				addEntityValue("elfde",value,i);
			} else if (i==12) {
				addEntityValue("twaalfde",value,i);
			} else if (i==13) {
				addEntityValue("dertiende",value,i);
			} else if (i==14) {
				addEntityValue("veertiende",value,i);
			} else if (i==15) {
				addEntityValue("vijftiende",value,i);
			} else if (i==16) {
				addEntityValue("zestiende",value,i);
			} else if (i==17) {
				addEntityValue("zeventiende",value,i);
			} else if (i==18) {
				addEntityValue("achttiende",value,i);
			} else if (i==19) {
				addEntityValue("negentiende",value,i);
			} else if (i==20) {
				addEntityValue("twintigste",value,i);
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
					strNum = eo.getExternalValueForInternalValue("" + num);
				}
				
				String strDec = "";
				if (dec<=1) {
					strNum = "";
					if (dec>0||num>0) {
						strDec = getExternalValueForInternalValue("" + ((dec * 10) + num));
					}
				} else if (dec==2) {
					strDec = "twintigste";
				} else if (dec==3) {
					strDec = "dertigste";
				} else if (dec==4) {
					strDec = "veertigste";
				} else if (dec==5) {
					strDec = "vijftigste";
				} else if (dec==6) {
					strDec = "zestigste";
				} else if (dec==7) {
					strDec = "zeventigste";
				} else if (dec==8) {
					strDec = "tachtigste";
				} else if (dec==9) {
					strDec = "negentigste";
				}
				if (dec>1 && num>0) {
					strDec = strNum + "en" + strDec;
					strNum = "";
				}

				String strCent = "";
				if (cent>0) {
					strCent = eo.getExternalValueForInternalValue("" + cent);
					if (num==0 && dec==0) {
						strCent += "honderdste";
					} else {
						strCent += "honderd";
					}
				}
				
				String strMill = "";
				if (mill>0) {
					strMill = eo.getExternalValueForInternalValue("" + mill);
					if (num==0 && dec==0 && cent==0) {
						strMill += "duizendste";
					} else  {
						strMill += "duizend";
					}
				}

				addEntityValue(strMill + strCent + strDec + strNum,value,i);
			}
		}
	}
}
