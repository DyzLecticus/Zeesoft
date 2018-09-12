package nl.zeesoft.zevt.trans.entities.dutch;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zodb.Languages;

public class DutchLanguage extends EntityObject {
	public DutchLanguage(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.NLD;
	}
	@Override
	public String getType() {
		return TYPE_LANGUAGE;
	}
	@Override
	public int getMaximumSymbols() {
		return 2;
	}
	@Override
	public String getExternalValueForInternalValue(String str) {
		String v = super.getExternalValueForInternalValue(str);
		if (v!=null && v.length()>0) {
			v = 	upperCaseFirst(v);
		}
		return v;
	}
	@Override
	public void initializeEntityValues() {
		addLanguage("Abchazisch","AB");
		addLanguage("Afar","AA");
		addLanguage("Afrikaans","AF");
		addLanguage("Akan","AK");
		addLanguage("Albanees","SQ");
		addLanguage("Amharisch","AM");
		addLanguage("Arabisch","AR");
		addLanguage("Aragonees","AN");
		addLanguage("Armeens","HY");
		addLanguage("Assamees","AS");
		addLanguage("Avarisch","AV");
		addLanguage("Avestisch","AE");
		addLanguage("Aymara","AY");
		addLanguage("Azerbeidzjaans","AZ");
		addLanguage("Bambara","BM");
		addLanguage("Basjkiers","BA");
		addLanguage("Baskisch","EU");
		addLanguage("Wit Russisch","BE");
		addLanguage("Bengalees","BN");
		addLanguage("Bihari","BH");
		addLanguage("Bislama","BI");
		addLanguage("Bosnisch","BS");
		addLanguage("Bretons","BR");
		addLanguage("Bulgarisch","BG");
		addLanguage("Burmees","MY");
		addLanguage("Catalaans","CA");
		addLanguage("Chamorro","CH");
		addLanguage("Tsjetsjeens","CE");
		addLanguage("Chichewa","NY");
		addLanguage("Nyanja","NY");
		addLanguage("Chinees","ZH");
		addLanguage("Tsjoevasjisch","CV");
		addLanguage("Cornish","KW");
		addLanguage("Corsicaans","CO");
		addLanguage("Cree","CR");
		addLanguage("Kroatisch","HR");
		addLanguage("Tsjechisch","CS");
		addLanguage("Deens","DA");
		addLanguage("Divehi","DV");
		addLanguage("Maldivisch","DV");
		addLanguage("Nederlands","NL");
		addLanguage("Engels","EN");
		addLanguage("Esperanto","EO");
		addLanguage("Estisch","ET");
		addLanguage("Ewe","EE");
		addLanguage("Faeröers","FO");
		addLanguage("Fijisch","FJ");
		addLanguage("Fins","FI");
		addLanguage("Frans","FR");
		addLanguage("Fula","FF");
		addLanguage("Fulah","FF");
		addLanguage("Pulaar","FF");
		addLanguage("Pular","FF");
		addLanguage("Galicisch","GL");
		addLanguage("Georgisch","KA");
		addLanguage("Duits","DE");
		addLanguage("Grieks","EL");
		addLanguage("Guarani","GN");
		addLanguage("Gujarati","GU");
		addLanguage("Haitisch","HT");
		addLanguage("Haitisch Creools","HT");
		addLanguage("Hausa","HA");
		addLanguage("Hebreeuws","HE");
		addLanguage("Herero","HZ");
		addLanguage("Hindi","HI");
		addLanguage("Hiri Motu","HO");
		addLanguage("Hongaars","HU");
		addLanguage("Interlingua","IA");
		addLanguage("Indonesisch","ID");
		addLanguage("Interlingue","IE");
		addLanguage("Iers","GA");
		addLanguage("Igbo","IG");
		addLanguage("Inupiaq","IK");
		addLanguage("Ido","IO");
		addLanguage("Ijslands","IS");
		addLanguage("Italiaans","IT");
		addLanguage("Inuktitut","IU");
		addLanguage("Japans","JA");
		addLanguage("Javaans","JV");
		addLanguage("Groenlands","KL");
		addLanguage("Kannada","KN");
		addLanguage("Kanuri","KR");
		addLanguage("Kasjmiri","KS");
		addLanguage("Kazachs","KK");
		addLanguage("Khmer","KM");
		addLanguage("Gikuyu","KI");
		addLanguage("Kinyarwanda","RW");
		addLanguage("Kirgizisch","KY");
		addLanguage("Zurjeens","KV");
		addLanguage("Komi","KV");
		addLanguage("Kongo","KG");
		addLanguage("Koreaans","KO");
		addLanguage("Koerdish","KU");
		addLanguage("Kwanyama","KJ");
		addLanguage("Kuanyama","KJ");
		addLanguage("Latijn","LA");
		addLanguage("Luxemburgs","LB");
		addLanguage("Luganda","LG");
		addLanguage("Limburgs","LI");
		addLanguage("Lingala","LN");
		addLanguage("Laotiaans","LO");
		addLanguage("Litouws","LT");
		addLanguage("Luba-Katanga","LU");
		addLanguage("Lets","LV");
		addLanguage("Manx","GV");
		addLanguage("Macedonisch","MK");
		addLanguage("Malagasy","MG");
		addLanguage("Malay","MS");
		addLanguage("Malayalam","ML");
		addLanguage("Maltees","MT");
		addLanguage("Maori","MI");
		addLanguage("Marathi","MR");
		addLanguage("Marshallees","MH");
		addLanguage("Mongools","MN");
		addLanguage("Nauru","NA");
		addLanguage("Navajo","NV");
		addLanguage("Bokmal Noors","NB");
		addLanguage("Noord Ndebele","ND");
		addLanguage("Nepalees","NE");
		addLanguage("Ndonga","NG");
		addLanguage("Nynorsk","NN");
		addLanguage("Noors","NO");
		addLanguage("Yi","II");
		addLanguage("Zuid Ndebele","NR");
		addLanguage("Occitaans","OC");
		addLanguage("Ojibweg","OJ");
		addLanguage("Kerkslavisch","CU");
		addLanguage("Oromo","OM");
		addLanguage("Oriya","OR");
		addLanguage("Ossetisch","OS");
		addLanguage("Ossetisch","OS");
		addLanguage("Punjabi","PA");
		addLanguage("Pali","PI");
		addLanguage("Perzisch","FA");
		addLanguage("Pools","PL");
		addLanguage("Pasjtoe","PS");
		addLanguage("Portuguees","PT");
		addLanguage("Quechua","QU");
		addLanguage("Romaans","RM");
		addLanguage("Kirundi","RN");
		addLanguage("Roemeens","RO");
		addLanguage("Moldavisch","RO");
		addLanguage("Moldovan","RO");
		addLanguage("Russisch","RU");
		addLanguage("Sanskriet","SA");
		addLanguage("Sardinisch","SC");
		addLanguage("Sindhi","SD");
		addLanguage("Samisch","SE");
		addLanguage("Samoaans","SM");
		addLanguage("Sango","SG");
		addLanguage("Servisch","SR");
		addLanguage("Schots Gaelisch","GD");
		addLanguage("Gaelisch","GD");
		addLanguage("Shona","SN");
		addLanguage("Sinhala","SI");
		addLanguage("Sinhalees","SI");
		addLanguage("Slovaaks","SK");
		addLanguage("Sloveens","SL");
		addLanguage("Somalisch","SO");
		addLanguage("Sotho","ST");
		addLanguage("Spaans","ES");
		addLanguage("Castilisch","ES");
		addLanguage("Sundanees","SU");
		addLanguage("Swahili","SW");
		addLanguage("Swazi","SS");
		addLanguage("Zweeds","SV");
		addLanguage("Tamil","TA");
		addLanguage("Telugu","TE");
		addLanguage("Tadzjieks","TG");
		addLanguage("Thai","TH");
		addLanguage("Tigrinya","TI");
		addLanguage("Tibetaans","BO");
		addLanguage("Central","BO");
		addLanguage("Turkmeens","TK");
		addLanguage("Tagalog","TL");
		addLanguage("Tswana","TN");
		addLanguage("Tonga","TO");
		addLanguage("Turks","TR");
		addLanguage("Tsonga","TS");
		addLanguage("Tataars","TT");
		addLanguage("Twi","TW");
		addLanguage("Tahitiaans","TY");
		addLanguage("Oeigoers","UG");
		addLanguage("Oekraïens","UK");
		addLanguage("Urdu","UR");
		addLanguage("Oezbeeks","UZ");
		addLanguage("Venda","VE");
		addLanguage("Vietnamees","VI");
		addLanguage("Volapük","VO");
		addLanguage("Waals","WA");
		addLanguage("Welsh","CY");
		addLanguage("Wolof","WO");
		addLanguage("West Fries","FY");
		addLanguage("Fries","FY");
		addLanguage("Xhosa","XH");
		addLanguage("Jiddish","YI");
		addLanguage("Yoruba","YO");
		addLanguage("Zhuang","ZA");
		addLanguage("Chuang","ZA");
	}
	private void addLanguage(String name,String code) {
		addEntityValue(name.toLowerCase(),code,code);
		if (name.contains("-")) {
			name = name.replaceAll("-"," ");
			addEntityValue(name.toLowerCase(),code,code);
		}
	}
}
