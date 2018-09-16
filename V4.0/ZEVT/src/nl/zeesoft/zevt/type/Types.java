package nl.zeesoft.zevt.type;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Types {
	public static final String		ALPHABETIC		= "ABC";
	public static final String		NUMERIC			= "NUM";
	public static final String		MATHEMATIC		= "MTH";
	public static final String		ORDER			= "ORD";
	public static final String		ORDER2			= "OR2";
	public static final String		DURATION		= "DUR";
	public static final String		MONTH			= "MNT";
	public static final String		DATE			= "DAT";
	public static final String		TIME			= "TIM";
	public static final String		PREPOSITION		= "PRE";
	public static final String		COUNTRY			= "CNT";
	public static final String		LANGUAGE		= "LNG";
	public static final String		CURRENCY		= "CUR";
	public static final String		PROFANITY		= "PRF";
	public static final String		CONFIRMATION	= "CNF";
	public static final String		SMILEY			= "SML";
	public static final String		FROWNY			= "FRN";
	
	public static final String[]	TYPES			= {
				ALPHABETIC
				,Types.NUMERIC
				,Types.MATHEMATIC		
				,Types.ORDER			
				,Types.ORDER2			
				,Types.DURATION		
				,Types.MONTH			
				,Types.DATE			
				,Types.TIME			
				,Types.PREPOSITION	
				,Types.COUNTRY		
				,Types.LANGUAGE		
				,Types.CURRENCY		
				,Types.PROFANITY		
				,Types.CONFIRMATION	
				,Types.SMILEY			
				,Types.FROWNY			
				};
	
	public static final String[]	TYPE_NAMES		= {
				"Alphabetic"
				,"Numeric"
				,"Mathematic"		
				,"Order"			
				,"Order2"			
				,"Duration"		
				,"Month"			
				,"Date"			
				,"Time"			
				,"Preposition"	
				,"Country"		
				,"Language"		
				,"Currency"		
				,"Profanity"		
				,"Confirmation"	
				,"Smiley"			
				,"Frowny"			
				};
	
	private List<String>			typeCodes		= new ArrayList<String>();		
	private List<String>			typeNames		= new ArrayList<String>();		
	
	public void initialize() {
		for (int i = 0; i < TYPES.length; i++) {
			typeCodes.add(TYPES[i]);
			typeNames.add(TYPE_NAMES[i]);
		}
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem typesElem = new JsElem("types",true);
		json.rootElement.children.add(typesElem);
		int i = 0;
		for (String code: typeCodes) {
			JsElem typeElem = new JsElem();
			typesElem.children.add(typeElem);
			typeElem.children.add(new JsElem("code",code,true));
			typeElem.children.add(new JsElem("name",typeNames.get(i),true));
			i++;
		}
		return json;
	}
	
	public List<String> getCodes() {
		return typeCodes;
	}
	
	public List<String> getNames() {
		return typeNames;
	}
	
	public String getNameForCode(String code) {
		String r = "";
		int i = typeCodes.indexOf(code);
		if (i>=0) {
			r = typeNames.get(i);
		}
		return r;
	}
}
