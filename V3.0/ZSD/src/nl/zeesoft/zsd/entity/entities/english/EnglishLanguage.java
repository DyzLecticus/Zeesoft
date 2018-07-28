package nl.zeesoft.zsd.entity.entities.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.sequence.Analyzer;

public class EnglishLanguage extends EntityObject {
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_ENG;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_LANGUAGE;
	}
	@Override
	public int getMaximumSymbols() {
		return 2;
	}
	@Override
	public String getExternalValueForInternalValue(String str) {
		String v = super.getExternalValueForInternalValue(str);
		if (v!=null && v.length()>0) {
			v = Analyzer.upperCaseFirst(v);
		}
		return v;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		
		addLanguage("Abkhaz","AB");
		addLanguage("Afar","AA");
		addLanguage("Afrikaans","AF");
		addLanguage("Akan","AK");
		addLanguage("Albanian","SQ");
		addLanguage("Amharic","AM");
		addLanguage("Arabic","AR");
		addLanguage("Aragonese","AN");
		addLanguage("Armenian","HY");
		addLanguage("Assamese","AS");
		addLanguage("Avaric","AV");
		addLanguage("Avestan","AE");
		addLanguage("Aymara","AY");
		addLanguage("Azerbaijani","AZ");
		addLanguage("Bambara","BM");
		addLanguage("Bashkir","BA");
		addLanguage("Basque","EU");
		addLanguage("Belarusian","BE");
		addLanguage("Bengali","BN");
		addLanguage("Bihari","BH");
		addLanguage("Bislama","BI");
		addLanguage("Bosnian","BS");
		addLanguage("Breton","BR");
		addLanguage("Bulgarian","BG");
		addLanguage("Burmese","MY");
		addLanguage("Catalan","CA");
		addLanguage("Valencian","CA");
		addLanguage("Chamorro","CH");
		addLanguage("Chechen","CE");
		addLanguage("Chichewa","NY");
		addLanguage("Chewa","NY");
		addLanguage("Nyanja","NY");
		addLanguage("Chinese","ZH");
		addLanguage("Chuvash","CV");
		addLanguage("Cornish","KW");
		addLanguage("Corsican","CO");
		addLanguage("Cree","CR");
		addLanguage("Croatian","HR");
		addLanguage("Czech","CS");
		addLanguage("Danish","DA");
		addLanguage("Divehi","DV");
		addLanguage("Dhivehi","DV");
		addLanguage("Maldivian","DV");
		addLanguage("Dutch","NL");
		addLanguage("English","EN");
		addLanguage("Esperanto","EO");
		addLanguage("Estonian","ET");
		addLanguage("Ewe","EE");
		addLanguage("Faroese","FO");
		addLanguage("Fijian","FJ");
		addLanguage("Finnish","FI");
		addLanguage("French","FR");
		addLanguage("Fula","FF");
		addLanguage("Fulah","FF");
		addLanguage("Pulaar","FF");
		addLanguage("Pular","FF");
		addLanguage("Galician","GL");
		addLanguage("Georgian","KA");
		addLanguage("German","DE");
		addLanguage("Greek","EL");
		addLanguage("Guarani","GN");
		addLanguage("Gujarati","GU");
		addLanguage("Haitian","HT");
		addLanguage("Haitian Creole","HT");
		addLanguage("Hausa","HA");
		addLanguage("Hebrew","HE");
		addLanguage("Herero","HZ");
		addLanguage("Hindi","HI");
		addLanguage("Hiri Motu","HO");
		addLanguage("Hungarian","HU");
		addLanguage("Interlingua","IA");
		addLanguage("Indonesian","ID");
		addLanguage("Interlingue","IE");
		addLanguage("Irish","GA");
		addLanguage("Igbo","IG");
		addLanguage("Inupiaq","IK");
		addLanguage("Ido","IO");
		addLanguage("Icelandic","IS");
		addLanguage("Italian","IT");
		addLanguage("Inuktitut","IU");
		addLanguage("Japanese","JA");
		addLanguage("Javanese","JV");
		addLanguage("Kalaallisut","KL");
		addLanguage("Greenlandic","KL");
		addLanguage("Kannada","KN");
		addLanguage("Kanuri","KR");
		addLanguage("Kashmiri","KS");
		addLanguage("Kazakh","KK");
		addLanguage("Khmer","KM");
		addLanguage("Kikuyu","KI");
		addLanguage("Gikuyu","KI");
		addLanguage("Kinyarwanda","RW");
		addLanguage("Kirghiz","KY");
		addLanguage("Kyrgyz","KY");
		addLanguage("Komi","KV");
		addLanguage("Kongo","KG");
		addLanguage("Korean","KO");
		addLanguage("Kurdish","KU");
		addLanguage("Kwanyama","KJ");
		addLanguage("Kuanyama","KJ");
		addLanguage("Latin","LA");
		addLanguage("Luxembourgish","LB");
		addLanguage("Letzeburgesch","LB");
		addLanguage("Luganda","LG");
		addLanguage("Limburgish","LI");
		addLanguage("Limburgan","LI");
		addLanguage("Limburger","LI");
		addLanguage("Lingala","LN");
		addLanguage("Lao","LO");
		addLanguage("Lithuanian","LT");
		addLanguage("Luba-Katanga","LU");
		addLanguage("Latvian","LV");
		addLanguage("Manx","GV");
		addLanguage("Macedonian","MK");
		addLanguage("Malagasy","MG");
		addLanguage("Malay","MS");
		addLanguage("Malayalam","ML");
		addLanguage("Maltese","MT");
		addLanguage("Maori","MI");
		addLanguage("Marathi","MR");
		addLanguage("Marshallese","MH");
		addLanguage("Mongolian","MN");
		addLanguage("Nauru","NA");
		addLanguage("Navajo","NV");
		addLanguage("Navaho","NV");
		addLanguage("Norwegian Bokmal","NB");
		addLanguage("North Ndebele","ND");
		addLanguage("Nepali","NE");
		addLanguage("Ndonga","NG");
		addLanguage("Norwegian Nynorsk","NN");
		addLanguage("Norwegian","NO");
		addLanguage("Nuosu","II");
		addLanguage("South Ndebele","NR");
		addLanguage("Occitan","OC");
		addLanguage("Ojibwe","OJ");
		addLanguage("Ojibwa","OJ");
		addLanguage("Old Church Slavonic","CU");
		addLanguage("Church Slavic","CU");
		addLanguage("Church Slavonic","CU");
		addLanguage("Old Bulgarian","CU");
		addLanguage("Old Slavonic","CU");
		addLanguage("Oromo","OM");
		addLanguage("Oriya","OR");
		addLanguage("Ossetian","OS");
		addLanguage("Ossetic","OS");
		addLanguage("Panjabi","PA");
		addLanguage("Punjabi","PA");
		addLanguage("Pali","PI");
		addLanguage("Persian","FA");
		addLanguage("Polish","PL");
		addLanguage("Pashto","PS");
		addLanguage("Pushto","PS");
		addLanguage("Portuguese","PT");
		addLanguage("Quechua","QU");
		addLanguage("Romansh","RM");
		addLanguage("Kirundi","RN");
		addLanguage("Romanian","RO");
		addLanguage("Moldavian","RO");
		addLanguage("Moldovan","RO");
		addLanguage("Russian","RU");
		addLanguage("Sanskrit","SA");
		addLanguage("Sardinian","SC");
		addLanguage("Sindhi","SD");
		addLanguage("Northern Sami","SE");
		addLanguage("Samoan","SM");
		addLanguage("Sango","SG");
		addLanguage("Serbian","SR");
		addLanguage("Scottish Gaelic","GD");
		addLanguage("Gaelic","GD");
		addLanguage("Shona","SN");
		addLanguage("Sinhala","SI");
		addLanguage("Sinhalese","SI");
		addLanguage("Slovak","SK");
		addLanguage("Slovene","SL");
		addLanguage("Somali","SO");
		addLanguage("Southern Sotho","ST");
		addLanguage("Spanish","ES");
		addLanguage("Castilian","ES");
		addLanguage("Sundanese","SU");
		addLanguage("Swahili","SW");
		addLanguage("Swati","SS");
		addLanguage("Swedish","SV");
		addLanguage("Tamil","TA");
		addLanguage("Telugu","TE");
		addLanguage("Tajik","TG");
		addLanguage("Thai","TH");
		addLanguage("Tigrinya","TI");
		addLanguage("Tibetan Standard","BO");
		addLanguage("Tibetan","BO");
		addLanguage("Central","BO");
		addLanguage("Turkmen","TK");
		addLanguage("Tagalog","TL");
		addLanguage("Tswana","TN");
		addLanguage("Tonga","TO");
		addLanguage("Turkish","TR");
		addLanguage("Tsonga","TS");
		addLanguage("Tatar","TT");
		addLanguage("Twi","TW");
		addLanguage("Tahitian","TY");
		addLanguage("Uighur","UG");
		addLanguage("Uyghur","UG");
		addLanguage("Ukrainian","UK");
		addLanguage("Urdu","UR");
		addLanguage("Uzbek","UZ");
		addLanguage("Venda","VE");
		addLanguage("Vietnamese","VI");
		addLanguage("Volapuk","VO");
		addLanguage("Walloon","WA");
		addLanguage("Welsh","CY");
		addLanguage("Wolof","WO");
		addLanguage("Western Frisian","FY");
		addLanguage("Xhosa","XH");
		addLanguage("Yiddish","YI");
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
