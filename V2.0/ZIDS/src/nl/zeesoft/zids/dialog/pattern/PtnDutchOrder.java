package nl.zeesoft.zids.dialog.pattern;


public class PtnDutchOrder extends PtnObjectLiteralToValue {
	public PtnDutchOrder() {
		super(TYPE_ORDER,"NED");
	}
	
	@Override
	public void initializePatternStrings(PtnManager manager) {
		PtnDutchNumber numPattern = (PtnDutchNumber) manager.getPatternByClassName(PtnDutchNumber.class.getName());
		if (numPattern.getNumPatternStrings()==0) {
			numPattern.initializePatternStrings(manager);
		}
		
		int max = manager.getMaximumOrder();
		if (manager.getMaximumOrder()>manager.getMaximumNumber()) {
			max = manager.getMaximumNumber();
		}

		for (int i = 1; i<=max; i++) {
			String value = "" + i;
			if (i==1) {
				addPatternStringAndValue("eerste",value);
			} else if (i==2) {
				addPatternStringAndValue("tweede",value);
			} else if (i==3) {
				addPatternStringAndValue("derde",value);
			} else if (i==4) {
				addPatternStringAndValue("vierde",value);
			} else if (i==5) {
				addPatternStringAndValue("vijfde",value);
			} else if (i==6) {
				addPatternStringAndValue("zesde",value);
			} else if (i==7) {
				addPatternStringAndValue("zevende",value);
			} else if (i==8) {
				addPatternStringAndValue("achtste",value);
			} else if (i==9) {
				addPatternStringAndValue("negende",value);
			} else if (i==10) {
				addPatternStringAndValue("tiende",value);
			} else if (i==11) {
				addPatternStringAndValue("elfde",value);
			} else if (i==12) {
				addPatternStringAndValue("twaalfde",value);
			} else if (i==13) {
				addPatternStringAndValue("dertiende",value);
			} else if (i==14) {
				addPatternStringAndValue("veertiende",value);
			} else if (i==15) {
				addPatternStringAndValue("vijftiende",value);
			} else if (i==16) {
				addPatternStringAndValue("zestiende",value);
			} else if (i==17) {
				addPatternStringAndValue("zeventiende",value);
			} else if (i==18) {
				addPatternStringAndValue("achttiende",value);
			} else if (i==19) {
				addPatternStringAndValue("negentiende",value);
			} else if (i==20) {
				addPatternStringAndValue("twintigste",value);
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
					strNum = numPattern.getPatternString(num);
				}
				
				String strDec = "";
				if (dec<=1) {
					strNum = "";
					if (dec>0||num>0) {
						strDec = getPatternString(((dec * 10) + num) - 1);
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
					strCent = numPattern.getPatternString(cent);
					if (num==0 && dec==0) {
						strCent += "honderdste";
					} else {
						strCent += "honderd";
					}
				}
				
				String strMill = "";
				if (mill>0) {
					strMill = numPattern.getPatternString(mill);
					if (num==0 && dec==0 && cent==0) {
						strMill += "duizendste";
					} else  {
						strMill += "duizend";
					}
				}
				
				addPatternStringAndValue(strMill + strCent + strDec + strNum,value);
			}
		}
	}
}
