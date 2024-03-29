package nl.zeesoft.zsd.entity.entities.dutch;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.sequence.Analyzer;

public class DutchCountry extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_NLD;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_COUNTRY;
	}
	@Override
	public int getMaximumSymbols() {
		return 8;
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
				if (!split[i].equals("de") && !split[i].equals("het") && !split[i].equals("van") && !split[i].equals("en")) {
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
		
		getToJsonPrefixes().add("naar");
		
		addCountry("Afghanistan","AF");
		addCountry("Aland","AX");
		addCountry("Albani�","AL");
		addCountry("Algerije","DZ");
		addCountry("de Amerikaanse Maagdeneilanden","VI");
		addCountry("Amerikaanse Maagdeneilanden","VI");
		addCountry("Amerikaans-Samoa","AS");
		addCountry("Andorra","AD");
		addCountry("Angola","AO");
		addCountry("Anguilla","AI");
		addCountry("Antarctica","AQ");
		addCountry("Antigua en Barbuda","AG");
		addCountry("Argentini�","AR");
		addCountry("Armeni�","AM");
		addCountry("Aruba","AW");
		addCountry("Australi�","AU");
		addCountry("Azerbeidzjan","AZ");
		addCountry("Bahamas","BS");
		addCountry("Bahrein","BH");
		addCountry("Bangladesh","BD");
		addCountry("Barbados","BB");
		addCountry("Belgi�","BE");
		addCountry("Belize","BZ");
		addCountry("Benin","BJ");
		addCountry("Bermuda","BM");
		addCountry("Bhutan","BT");
		addCountry("Bolivia","BO");
		addCountry("Bonaire, Sint Eustatius en Saba","BQ");
		addCountry("Bonaire","BQ");
		addCountry("Sint Eustatius","BQ");
		addCountry("Saba","BQ");
		addCountry("Bosni� en Herzegovina","BA");
		addCountry("Botswana","BW");
		addCountry("Bouvet","BV");
		addCountry("Brazili�","BR");
		addCountry("de Britse Maagdeneilanden","VG");
		addCountry("Britse Maagdeneilanden","VG");
		addCountry("Brits Indische Oceaanterritorium","IO");
		addCountry("Brunei","BN");
		addCountry("Bulgarije","BG");
		addCountry("Burkina Faso","BF");
		addCountry("Burundi","BI");
		addCountry("Cambodja","KH");
		addCountry("Canada","CA");
		addCountry("de Centraal-Afrikaanse Republiek","CF");
		addCountry("Centraal-Afrikaanse Republiek","CF");
		addCountry("Chili","CL");
		addCountry("China","CN");
		addCountry("Christmaseiland","CX");
		addCountry("de Cocoseilanden","CC");
		addCountry("Cocoseilanden","CC");
		addCountry("Colombia","CO");
		addCountry("Comoren","KM");
		addCountry("Congo-Brazzaville","CG");
		addCountry("Congo-Kinshasa","CD");
		addCountry("de Cookeilanden","CK");
		addCountry("Cookeilanden","CK");
		addCountry("Costa Rica","CR");
		addCountry("Cuba","CU");
		addCountry("Cura�ao","CW");
		addCountry("Cyprus","CY");
		addCountry("Denemarken","DK");
		addCountry("Djibouti","DJ");
		addCountry("Dominica","DM");
		addCountry("Dominicaanse Republiek","DO");
		addCountry("Duitsland","DE");
		addCountry("Ecuador","EC");
		addCountry("Egypte","EG");
		addCountry("El Salvador","SV");
		addCountry("Equatoriaal-Guinea","GQ");
		addCountry("Eritrea","ER");
		addCountry("Estland","EE");
		addCountry("Ethiopi�","ET");
		addCountry("Faer�er","FO");
		addCountry("de Falklandeilanden","FK");
		addCountry("Falklandeilanden","FK");
		addCountry("Fiji","FJ");
		addCountry("Filipijnen","PH");
		addCountry("Finland","FI");
		addCountry("Frankrijk","FR");
		addCountry("Franse Zuidelijke en Antarctische Gebieden","TF");
		addCountry("Frans-Guyana","GF");
		addCountry("Frans-Polynesi�","PF");
		addCountry("Gabon","GA");
		addCountry("Gambia","GM");
		addCountry("Georgi�","GE");
		addCountry("Ghana","GH");
		addCountry("Gibraltar","GI");
		addCountry("Grenada","GD");
		addCountry("Griekenland","GR");
		addCountry("Groenland","GL");
		addCountry("Guadeloupe","GP");
		addCountry("Guam","GU");
		addCountry("Guatemala","GT");
		addCountry("Guernsey","GG");
		addCountry("Guinee","GN");
		addCountry("Guinee-Bissau","GW");
		addCountry("Guyana","GY");
		addCountry("Ha�ti","HT");
		addCountry("de Heard en McDonaldeilanden","HM");
		addCountry("Heard en McDonaldeilanden","HM");
		addCountry("Honduras","HN");
		addCountry("Hongarije","HU");
		addCountry("Hongkong","HK");
		addCountry("Ierland","IE");
		addCountry("IJsland","IS");
		addCountry("India","IN");
		addCountry("Indonesi�","ID");
		addCountry("Irak","IQ");
		addCountry("Iran","IR");
		addCountry("Isra�l","IL");
		addCountry("Itali�","IT");
		addCountry("Ivoorkust","CI");
		addCountry("Jamaica","JM");
		addCountry("Japan","JP");
		addCountry("Jemen","YE");
		addCountry("Jersey","JE");
		addCountry("Jordani�","JO");
		addCountry("de Kaaimaneilanden","KY");
		addCountry("Kaaimaneilanden","KY");
		addCountry("Kaapverdi�","CV");
		addCountry("Kameroen","CM");
		addCountry("Kazachstan","KZ");
		addCountry("Kenia","KE");
		addCountry("Kirgizi�","KG");
		addCountry("Kiribati","KI");
		addCountry("Kleine afgelegen eilanden van de Verenigde Staten","UM");
		addCountry("Koeweit","KW");
		addCountry("Kosovo","XK");
		addCountry("Kroati�","HR");
		addCountry("Laos","LA");
		addCountry("Lesotho","LS");
		addCountry("Letland","LV");
		addCountry("Libanon","LB");
		addCountry("Liberia","LR");
		addCountry("Libi�","LY");
		addCountry("Liechtenstein","LI");
		addCountry("Litouwen","LT");
		addCountry("Luxemburg","LU");
		addCountry("Macau","MO");
		addCountry("Macedoni�","MK");
		addCountry("Madagaskar","MG");
		addCountry("Malawi","MW");
		addCountry("Maldiven","MV");
		addCountry("Maleisi�","MY");
		addCountry("Mali","ML");
		addCountry("Malta","MT");
		addCountry("Man","IM");
		addCountry("Marokko","MA");
		addCountry("de Marshalleilanden","MH");
		addCountry("Marshalleilanden","MH");
		addCountry("Martinique","MQ");
		addCountry("Mauritani�","MR");
		addCountry("Mauritius","MU");
		addCountry("Mayotte","YT");
		addCountry("Mexico","MX");
		addCountry("Micronesia","FM");
		addCountry("Moldavi�","MD");
		addCountry("Monaco","MC");
		addCountry("Mongoli�","MN");
		addCountry("Montenegro","ME");
		addCountry("Montserrat","MS");
		addCountry("Mozambique","MZ");
		addCountry("Myanmar","MM");
		addCountry("Namibi�","NA");
		addCountry("Nauru","NR");
		addCountry("Nederland","NL");
		addCountry("Nepal","NP");
		addCountry("Nicaragua","NI");
		addCountry("Nieuw-Caledoni�","NC");
		addCountry("Nieuw-Zeeland","NZ");
		addCountry("Niger","NE");
		addCountry("Nigeria","NG");
		addCountry("Niue","NU");
		addCountry("de Noordelijke Marianen","MP");
		addCountry("Noordelijke Marianen","MP");
		addCountry("Noord-Korea","KP");
		addCountry("Noorwegen","NO");
		addCountry("Norfolk","NF");
		addCountry("Oeganda","UG");
		addCountry("Oekra�ne","UA");
		addCountry("Oezbekistan","UZ");
		addCountry("Oman","OM");
		addCountry("Oostenrijk","AT");
		addCountry("Oost-Timor","TL");
		addCountry("Pakistan","PK");
		addCountry("Palau","PW");
		addCountry("Palestina","PS");
		addCountry("Panama","PA");
		addCountry("Papua-Nieuw-Guinea","PG");
		addCountry("Paraguay","PY");
		addCountry("Peru","PE");
		addCountry("de Pitcairneilanden","PN");
		addCountry("Pitcairneilanden","PN");
		addCountry("Polen","PL");
		addCountry("Portugal","PT");
		addCountry("Puerto Rico","PR");
		addCountry("Qatar","QA");
		addCountry("R�union","RE");
		addCountry("Roemeni�","RO");
		addCountry("Rusland","RU");
		addCountry("Rwanda","RW");
		addCountry("Saint-Barth�lemy","BL");
		addCountry("Saint Kitts en Nevis","KN");
		addCountry("Saint Lucia","LC");
		addCountry("Saint-Pierre en Miquelon","PM");
		addCountry("Saint Vincent en de Grenadines","VC");
		addCountry("de Salomonseilanden","SB");
		addCountry("Salomonseilanden","SB");
		addCountry("Samoa","WS");
		addCountry("San Marino","SM");
		addCountry("Saoedi-Arabi�","SA");
		addCountry("Sao Tom� en Principe","ST");
		addCountry("Senegal","SN");
		addCountry("Servi�","RS");
		addCountry("Seychellen","SC");
		addCountry("Sierra Leone","SL");
		addCountry("Singapore","SG");
		addCountry("Sint-Helena, Ascension en Tristan da Cunha","SH");
		addCountry("Sint-Helena","SH");
		addCountry("Sint-Maarten","MF");
		addCountry("Sloveni�","SI");
		addCountry("Slowakije","SK");
		addCountry("Soedan","SD");
		addCountry("Somali�","SO");
		addCountry("Spanje","ES");
		addCountry("Spitsbergen en Jan Mayen","SJ");
		addCountry("Sri Lanka","LK");
		addCountry("Suriname","SR");
		addCountry("Swaziland","SZ");
		addCountry("Syri�","SY");
		addCountry("Tadzjikistan","TJ");
		addCountry("Taiwan","TW");
		addCountry("Tanzania","TZ");
		addCountry("Thailand","TH");
		addCountry("Togo","TG");
		addCountry("Tokelau","TK");
		addCountry("Tonga","TO");
		addCountry("Trinidad en Tobago","TT");
		addCountry("Tsjaad","TD");
		addCountry("Tsjechi�","CZ");
		addCountry("Tunesi�","TN");
		addCountry("Turkije","TR");
		addCountry("Turkmenistan","TM");
		addCountry("Turks en Caicoseilanden","TC");
		addCountry("de Caicoseilanden","TC");
		addCountry("Caicoseilanden","TC");
		addCountry("Tuvalu","TV");
		addCountry("Uruguay","UY");
		addCountry("Vanuatu","VU");
		addCountry("Vaticaanstad","VA");
		addCountry("het Vaticaan","VA");
		addCountry("Venezuela","VE");
		addCountry("de Verenigde Arabische Emiraten","AE");
		addCountry("Verenigde Arabische Emiraten","AE");
		addCountry("de Verenigde Staten","US");
		addCountry("Verenigde Staten","US");
		addCountry("Amerika","US");
		addCountry("de VS","US");
		addCountry("VS","US");
		addCountry("het Verenigd Koninkrijk","GB");
		addCountry("Verenigd Koninkrijk","GB");
		addCountry("Engeland","GB");
		addCountry("Groot Brittannie","GB");
		addCountry("Vietnam","VN");
		addCountry("Wallis en Futuna","WF");
		addCountry("Westelijke Sahara","EH");
		addCountry("Wit-Rusland","BY");
		addCountry("Zambia","ZM");
		addCountry("Zimbabwe","ZW");
		addCountry("Zuid-Afrika","ZA");
		addCountry("Zuid-Georgia en de Zuidelijke Sandwicheilanden","GS");
		addCountry("Zuid-Korea","KR");
		addCountry("Zuid-Soedan","SS");
		addCountry("Zweden","SE");
		addCountry("Zwitserland","CH");
	}
	private void addCountry(String name,String code) {
		name = name.toLowerCase();
		addEntityValue(name,code,code);
		if (name.contains("�")) {
			name = name.replace("�","c");
			addEntityValue(name,code,code);
		}
		if (name.contains("�")) {
			name = name.replace("�","e");
			addEntityValue(name,code,code);
		}
		if (name.contains("�")) {
			name = name.replace("�","e");
			addEntityValue(name,code,code);
		}
		if (name.contains("�")) {
			name = name.replace("�","i");
			addEntityValue(name,code,code);
		}
		if (name.contains("�")) {
			name = name.replace("�","o");
			addEntityValue(name,code,code);
		}
		if (name.contains("-")) {
			name = name.replaceAll("-"," ");
			addEntityValue(name,code,code);
		}
	}
}
