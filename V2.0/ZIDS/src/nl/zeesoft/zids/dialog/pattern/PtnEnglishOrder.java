package nl.zeesoft.zids.dialog.pattern;


public class PtnEnglishOrder extends PtnObjectLiteralToValue {
	public PtnEnglishOrder() {
		super(TYPE_ORDER,"ENG");
	}
	
	@Override
	public void initializePatternStrings(PtnManager manager) {
		PtnEnglishNumber numPattern = (PtnEnglishNumber) manager.getPatternByClassName(PtnEnglishNumber.class.getName());
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
				addPatternStringAndValue("first",value);
			} else if (i==2) {
				addPatternStringAndValue("second",value);
			} else if (i==3) {
				addPatternStringAndValue("third",value);
			} else if (i==4) {
				addPatternStringAndValue("fourth",value);
			} else if (i==5) {
				addPatternStringAndValue("fifth",value);
			} else if (i==6) {
				addPatternStringAndValue("sixth",value);
			} else if (i==7) {
				addPatternStringAndValue("seventh",value);
			} else if (i==8) {
				addPatternStringAndValue("eighth",value);
			} else if (i==9) {
				addPatternStringAndValue("ninth",value);
			} else if (i==10) {
				addPatternStringAndValue("tenth",value);
			} else if (i==11) {
				addPatternStringAndValue("eleventh",value);
			} else if (i==12) {
				addPatternStringAndValue("twelfth",value);
			} else if (i==13) {
				addPatternStringAndValue("thirteenth",value);
			} else if (i==14) {
				addPatternStringAndValue("fourteenth",value);
			} else if (i==15) {
				addPatternStringAndValue("fifteenth",value);
			} else if (i==16) {
				addPatternStringAndValue("sixteenth",value);
			} else if (i==17) {
				addPatternStringAndValue("seventeenth",value);
			} else if (i==18) {
				addPatternStringAndValue("eighteenth",value);
			} else if (i==19) {
				addPatternStringAndValue("nineteenth",value);
			} else if (i==20) {
				addPatternStringAndValue("twentieth",value);
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
					strNum = getPatternString(num - 1);
				}
				
				String strDec = "";
				if (dec<=1) {
					strNum = "";
					if (dec>0||num>0) {
						strDec = getPatternString(((dec * 10) + num) - 1);
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
					strCent = numPattern.getPatternString(cent);
					if (num==0 && dec==0) {
						strCent += "hundredth";
					} else {
						strCent += "hundredand";
					}
				}
				
				String strMill = "";
				if (mill>0) {
					strMill = numPattern.getPatternString(mill);
					if (num==0 && dec==0 && cent==0) {
						strMill += "thousandth";
					} else  {
						strMill += "thousand";
					}
				}
				
				addPatternStringAndValue(strMill + strCent + strDec + strNum,value);
			}
		}
	}
}
