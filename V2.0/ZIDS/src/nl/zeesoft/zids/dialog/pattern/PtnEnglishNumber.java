package nl.zeesoft.zids.dialog.pattern;


public class PtnEnglishNumber extends PtnObjectLiteral {
	public PtnEnglishNumber() {
		super(TYPE_NUMBER,"ENG");
	}
	
	@Override
	public void initializePatternStrings(PtnManager manager) {
		for (int i = 0; i<=manager.getMaximumNumber(); i++) {
			if (i==0) {
				addPatternString("zero");
			} else if (i==1) {
				addPatternString("one");
			} else if (i==2) {
				addPatternString("two");
			} else if (i==3) {
				addPatternString("three");
			} else if (i==4) {
				addPatternString("four");
			} else if (i==5) {
				addPatternString("five");
			} else if (i==6) {
				addPatternString("six");
			} else if (i==7) {
				addPatternString("seven");
			} else if (i==8) {
				addPatternString("eight");
			} else if (i==9) {
				addPatternString("nine");
			} else if (i==10) {
				addPatternString("ten");
			} else if (i==11) {
				addPatternString("eleven");
			} else if (i==12) {
				addPatternString("twelve");
			} else if (i==13) {
				addPatternString("thirteen");
			} else if (i==14) {
				addPatternString("fourteen");
			} else if (i==15) {
				addPatternString("fifteen");
			} else if (i==16) {
				addPatternString("sixteen");
			} else if (i==17) {
				addPatternString("seventeen");
			} else if (i==18) {
				addPatternString("eighteen");
			} else if (i==19) {
				addPatternString("nineteen");
			} else if (i==20) {
				addPatternString("twenty");
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
					strCent = getPatternString(cent) + "hundred";
					if (num>0 || dec>0) {
						strCent += "and";
					}
				}
				
				String strMill = "";
				if (mill>0) {
					strMill = getPatternString(mill) + "thousand";
				}
				
				addPatternString(strMill + strCent + strDec + strNum);
			}
		}
	}
}
