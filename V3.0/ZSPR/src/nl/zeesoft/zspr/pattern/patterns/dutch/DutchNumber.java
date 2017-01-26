package nl.zeesoft.zspr.pattern.patterns.dutch;

import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObjectLiteral;

public class DutchNumber extends PatternObjectLiteral {
	public DutchNumber() {
		super(TYPE_NUMBER,"NED");
	}
	
	@Override
	public void initializePatternStrings(PatternManager manager) {
		for (int i = 0; i<=manager.getMaximumNumber(); i++) {
			if (i==0) {
				addPatternString("nul");
			} else if (i==1) {
				addPatternString("een");
			} else if (i==2) {
				addPatternString("twee");
			} else if (i==3) {
				addPatternString("drie");
			} else if (i==4) {
				addPatternString("vier");
			} else if (i==5) {
				addPatternString("vijf");
			} else if (i==6) {
				addPatternString("zes");
			} else if (i==7) {
				addPatternString("zeven");
			} else if (i==8) {
				addPatternString("acht");
			} else if (i==9) {
				addPatternString("negen");
			} else if (i==10) {
				addPatternString("tien");
			} else if (i==11) {
				addPatternString("elf");
			} else if (i==12) {
				addPatternString("twaalf");
			} else if (i==13) {
				addPatternString("dertien");
			} else if (i==14) {
				addPatternString("veertien");
			} else if (i==15) {
				addPatternString("vijftien");
			} else if (i==16) {
				addPatternString("zestien");
			} else if (i==17) {
				addPatternString("zeventien");
			} else if (i==18) {
				addPatternString("achttien");
			} else if (i==19) {
				addPatternString("negentien");
			} else if (i==20) {
				addPatternString("twintig");
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

				String strNum = getPatternString(num);
				if (num==0) {
					strNum = "";
				}
				
				String strDec = "";
				if (dec<=1) {
					strNum = "";
					if (dec>0||num>0) {
						strDec = getPatternString((dec * 10) + num);
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
						strCent = getPatternString(cent) + "honderd";
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
						strMill = getPatternString(mill) + "duizend";
					}
				}
				
				addPatternString(strMill + strCent + strDec + strNum);
			}
		}
	}
}
