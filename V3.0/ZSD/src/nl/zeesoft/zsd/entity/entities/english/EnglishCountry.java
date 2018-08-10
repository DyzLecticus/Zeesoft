package nl.zeesoft.zsd.entity.entities.english;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.sequence.Analyzer;

public class EnglishCountry extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_ENG;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_COUNTRY;
	}
	@Override
	public int getMaximumSymbols() {
		return 6;
	}
	@Override
	public String getExternalValueForInternalValue(String str) {
		String v = super.getExternalValueForInternalValue(str);
		if (v!=null && v.length()>0) {
			String[] split = v.split(" ");
			ZStringBuilder val = new ZStringBuilder();
			for (int i = 0; i<split.length; i++) {
				if (val.length()>0) {
					val.append(" ");
				}
				if (!split[i].equals("the") && !split[i].equals("and")) {
					val.append(Analyzer.upperCaseFirst(split[i]));
				} else {
					val.append(split[i]);
				}
			}
			v = val.toString();
		}
		return v;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		
		getToJsonPrefixes().add("to");
		
		addCountry("Afghanistan","AF");
		addCountry("the Aland Islands","AX");
		addCountry("Aland Islands","AX");
		addCountry("Albania","AL");
		addCountry("Algeria","DZ");
		addCountry("American Samoa","AS");
		addCountry("AndorrA","AD");
		addCountry("Angola","AO");
		addCountry("Anguilla","AI");
		addCountry("Antarctica","AQ");
		addCountry("Antigua and Barbuda","AG");
		addCountry("Argentina","AR");
		addCountry("Armenia","AM");
		addCountry("Aruba","AW");
		addCountry("Australia","AU");
		addCountry("Austria","AT");
		addCountry("Azerbaijan","AZ");
		addCountry("Bahamas","BS");
		addCountry("Bahrain","BH");
		addCountry("Bangladesh","BD");
		addCountry("Barbados","BB");
		addCountry("Belarus","BY");
		addCountry("Belgium","BE");
		addCountry("Belize","BZ");
		addCountry("Benin","BJ");
		addCountry("Bermuda","BM");
		addCountry("Bhutan","BT");
		addCountry("Bolivia","BO");
		addCountry("Bosnia and Herzegovina","BA");
		addCountry("Botswana","BW");
		addCountry("Bouvet Island","BV");
		addCountry("Brazil","BR");
		addCountry("British Indian Ocean Territory","IO");
		addCountry("Brunei Darussalam","BN");
		addCountry("Bulgaria","BG");
		addCountry("Burkina Faso","BF");
		addCountry("Burundi","BI");
		addCountry("Cambodia","KH");
		addCountry("Cameroon","CM");
		addCountry("Canada","CA");
		addCountry("Cape Verde","CV");
		addCountry("the Cayman Islands","KY");
		addCountry("Cayman Islands","KY");
		addCountry("Central African Republic","CF");
		addCountry("Chad","TD");
		addCountry("Chile","CL");
		addCountry("China","CN");
		addCountry("Christmas Island","CX");
		addCountry("the Cocos Islands","CC");
		addCountry("Cocos Islands","CC");
		addCountry("Colombia","CO");
		addCountry("Comoros","KM");
		addCountry("Congo","CG");
		addCountry("Congo","CD");
		addCountry("the Cook Islands","CK");
		addCountry("Cook Islands","CK");
		addCountry("Costa Rica","CR");
		addCountry("Cote d Ivoire","CI");
		addCountry("Croatia","HR");
		addCountry("Cuba","CU");
		addCountry("Cyprus","CY");
		addCountry("Czech Republic","CZ");
		addCountry("Denmark","DK");
		addCountry("Djibouti","DJ");
		addCountry("Dominica","DM");
		addCountry("Dominican Republic","DO");
		addCountry("Ecuador","EC");
		addCountry("Egypt","EG");
		addCountry("El Salvador","SV");
		addCountry("Equatorial Guinea","GQ");
		addCountry("Eritrea","ER");
		addCountry("Estonia","EE");
		addCountry("Ethiopia","ET");
		addCountry("the Falkland Islands","FK");
		addCountry("Falkland Islands","FK");
		addCountry("the Faroe Islands","FO");
		addCountry("Faroe Islands","FO");
		addCountry("Fiji","FJ");
		addCountry("Finland","FI");
		addCountry("France","FR");
		addCountry("French Guiana","GF");
		addCountry("French Polynesia","PF");
		addCountry("French Southern Territories","TF");
		addCountry("Gabon","GA");
		addCountry("Gambia","GM");
		addCountry("Georgia","GE");
		addCountry("Germany","DE");
		addCountry("Ghana","GH");
		addCountry("Gibraltar","GI");
		addCountry("Greece","GR");
		addCountry("Greenland","GL");
		addCountry("Grenada","GD");
		addCountry("Guadeloupe","GP");
		addCountry("Guam","GU");
		addCountry("Guatemala","GT");
		addCountry("Guernsey","GG");
		addCountry("Guinea","GN");
		addCountry("Guinea-Bissau","GW");
		addCountry("Guyana","GY");
		addCountry("Haiti","HT");
		addCountry("the Heard Island and Mcdonald Islands","HM");
		addCountry("Heard Island and Mcdonald Islands","HM");
		addCountry("Holy See","VA");
		addCountry("Vatican","VA");
		addCountry("The Vatican","VA");
		addCountry("Honduras","HN");
		addCountry("Hong Kong","HK");
		addCountry("Hungary","HU");
		addCountry("Iceland","IS");
		addCountry("India","IN");
		addCountry("Indonesia","ID");
		addCountry("Iran","IR");
		addCountry("Iraq","IQ");
		addCountry("Ireland","IE");
		addCountry("Isle of Man","IM");
		addCountry("Israel","IL");
		addCountry("Italy","IT");
		addCountry("Jamaica","JM");
		addCountry("Japan","JP");
		addCountry("Jersey","JE");
		addCountry("Jordan","JO");
		addCountry("Kazakhstan","KZ");
		addCountry("Kenya","KE");
		addCountry("Kiribati","KI");
		addCountry("North Korea","KP");
		addCountry("South Korea","KR");
		addCountry("Kuwait","KW");
		addCountry("Kyrgyzstan","KG");
		addCountry("Lao","LA");
		addCountry("Latvia","LV");
		addCountry("Lebanon","LB");
		addCountry("Lesotho","LS");
		addCountry("Liberia","LR");
		addCountry("Libyan Arab Jamahiriya","LY");
		addCountry("Liechtenstein","LI");
		addCountry("Lithuania","LT");
		addCountry("Luxembourg","LU");
		addCountry("Macao","MO");
		addCountry("Macedonia","MK");
		addCountry("Madagascar","MG");
		addCountry("Malawi","MW");
		addCountry("Malaysia","MY");
		addCountry("Maldives","MV");
		addCountry("Mali","ML");
		addCountry("Malta","MT");
		addCountry("the Marshall Islands","MH");
		addCountry("Marshall Islands","MH");
		addCountry("Martinique","MQ");
		addCountry("Mauritania","MR");
		addCountry("Mauritius","MU");
		addCountry("Mayotte","YT");
		addCountry("Mexico","MX");
		addCountry("Micronesia","FM");
		addCountry("Moldova","MD");
		addCountry("Monaco","MC");
		addCountry("Mongolia","MN");
		addCountry("Montserrat","MS");
		addCountry("Morocco","MA");
		addCountry("Mozambique","MZ");
		addCountry("Myanmar","MM");
		addCountry("Namibia","NA");
		addCountry("Nauru","NR");
		addCountry("Nepal","NP");
		addCountry("the Netherlands","NL");
		addCountry("Netherlands","NL");
		addCountry("the Netherlands Antilles","AN");
		addCountry("Netherlands Antilles","AN");
		addCountry("New Caledonia","NC");
		addCountry("New Zealand","NZ");
		addCountry("Nicaragua","NI");
		addCountry("Niger","NE");
		addCountry("Nigeria","NG");
		addCountry("Niue","NU");
		addCountry("Norfolk Island","NF");
		addCountry("the Northern Mariana Islands","MP");
		addCountry("Northern Mariana Islands","MP");
		addCountry("Norway","NO");
		addCountry("Oman","OM");
		addCountry("Pakistan","PK");
		addCountry("Palau","PW");
		addCountry("Palestinian Territory","PS");
		addCountry("Panama","PA");
		addCountry("Papua New Guinea","PG");
		addCountry("Paraguay","PY");
		addCountry("Peru","PE");
		addCountry("Philippines","PH");
		addCountry("Pitcairn","PN");
		addCountry("Poland","PL");
		addCountry("Portugal","PT");
		addCountry("Puerto Rico","PR");
		addCountry("Qatar","QA");
		addCountry("Reunion","RE");
		addCountry("Romania","RO");
		addCountry("Russian Federation","RU");
		addCountry("Rwanda","RW");
		addCountry("Saint Helena","SH");
		addCountry("Saint Kitts and Nevis","KN");
		addCountry("Saint Lucia","LC");
		addCountry("Saint Pierre and Miquelon","PM");
		addCountry("Saint Vincent and the Grenadines","VC");
		addCountry("Samoa","WS");
		addCountry("San Marino","SM");
		addCountry("Sao Tome and Principe","ST");
		addCountry("Saudi Arabia","SA");
		addCountry("Senegal","SN");
		addCountry("Serbia and Montenegro","CS");
		addCountry("Seychelles","SC");
		addCountry("Sierra Leone","SL");
		addCountry("Singapore","SG");
		addCountry("Slovakia","SK");
		addCountry("Slovenia","SI");
		addCountry("the Solomon Islands","SB");
		addCountry("Solomon Islands","SB");
		addCountry("Somalia","SO");
		addCountry("South Africa","ZA");
		addCountry("South Georgia and the South Sandwich Islands","GS");
		addCountry("Spain","ES");
		addCountry("Sri Lanka","LK");
		addCountry("Sudan","SD");
		addCountry("Suriname","SR");
		addCountry("Svalbard and Jan Mayen","SJ");
		addCountry("Swaziland","SZ");
		addCountry("Sweden","SE");
		addCountry("Switzerland","CH");
		addCountry("Syrian Arab Republic","SY");
		addCountry("Taiwan","TW");
		addCountry("Tajikistan","TJ");
		addCountry("Tanzania","TZ");
		addCountry("Thailand","TH");
		addCountry("Timor-Leste","TL");
		addCountry("Togo","TG");
		addCountry("Tokelau","TK");
		addCountry("Tonga","TO");
		addCountry("Trinidad and Tobago","TT");
		addCountry("Tunisia","TN");
		addCountry("Turkey","TR");
		addCountry("Turkmenistan","TM");
		addCountry("Turks and Caicos Islands","TC");
		addCountry("Tuvalu","TV");
		addCountry("Uganda","UG");
		addCountry("Ukraine","UA");
		addCountry("the United Arab Emirates","AE");
		addCountry("United Arab Emirates","AE");
		addCountry("the United Kingdom","GB");
		addCountry("United Kingdom","GB");
		addCountry("Great Britain","GB");
		addCountry("the UK","GB");
		addCountry("UK","GB");
		addCountry("the United States","US");
		addCountry("United States","US");
		addCountry("the US","US");
		addCountry("US","US");
		addCountry("the USA","US");
		addCountry("USA","US");
		addCountry("America","US");
		addCountry("the United States Minor Outlying Islands","UM");
		addCountry("United States Minor Outlying Islands","UM");
		addCountry("Uruguay","UY");
		addCountry("Uzbekistan","UZ");
		addCountry("Vanuatu","VU");
		addCountry("Venezuela","VE");
		addCountry("Vietnam","VN");
		addCountry("the Virgin Islands, British","VG");
		addCountry("Virgin Islands, British","VG");
		addCountry("British Virgin Islands","VG");
		addCountry("U.S. Virgin Islands","VI");
		addCountry("US Virgin Islands","VI");
		addCountry("Wallis and Futuna","WF");
		addCountry("Western Sahara","EH");
		addCountry("Yemen","YE");
		addCountry("Zambia","ZM");
		addCountry("Zimbabwe","ZW");
	}
	private void addCountry(String name,String code) {
		addEntityValue(name.toLowerCase(),code,code);
		if (name.contains("-")) {
			name = name.replaceAll("-"," ");
			addEntityValue(name.toLowerCase(),code,code);
		}
	}
}
