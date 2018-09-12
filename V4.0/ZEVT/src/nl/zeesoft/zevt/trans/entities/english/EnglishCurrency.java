package nl.zeesoft.zevt.trans.entities.english;

import nl.zeesoft.zevt.trans.EntityObject;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zodb.Languages;

public class EnglishCurrency extends EntityObject {
	public EnglishCurrency(Translator t) {
		super(t);
	}
	@Override
	public String getLanguage() {
		return Languages.ENG;
	}
	@Override
	public String getType() {
		return TYPE_CURRENCY;
	}
	@Override
	public int getMaximumSymbols() {
		return 4;
	}
	@Override
	public void initializeEntityValues() {
		addCurrency("US Dollar","USD");
		addCurrency("Dollar","USD");
		addCurrency("Canadian Dollar","CAD");
		addCurrency("Euro","EUR");
		addCurrency("United Arab Emirates Dirham","AED");
		addCurrency("Dirham","AED");
		addCurrency("Afghan Afghani","AFN");
		addCurrency("Afghani","AFN");
		addCurrency("Albanian Lek","ALL");
		addCurrency("Lek","ALL");
		addCurrency("Armenian Dram","AMD");
		addCurrency("Dram","AMD");
		addCurrency("Argentine Peso","ARS");
		addCurrency("Peso","ARS");
		addCurrency("Australian Dollar","AUD");
		addCurrency("Azerbaijani Manat","AZN");
		addCurrency("Manat","AZN");
		addCurrency("Bosnia-Herzegovina Convertible Mark","BAM");
		addCurrency("Mark","BAM");
		addCurrency("Bangladeshi Taka","BDT");
		addCurrency("Taka","BDT");
		addCurrency("Bulgarian Lev","BGN");
		addCurrency("Lev","BGN");
		addCurrency("Bahraini Dinar","BHD");
		addCurrency("Dinar","BHD");
		addCurrency("Burundian Franc","BIF");
		addCurrency("Franc","BIF");
		addCurrency("Brunei Dollar","BND");
		addCurrency("Bolivian Boliviano","BOB");
		addCurrency("Boliviano","BOB");
		addCurrency("Brazilian Real","BRL");
		addCurrency("Real","BRL");
		addCurrency("Botswanan Pula","BWP");
		addCurrency("Pula","BWP");
		addCurrency("Belarusian Ruble","BYR");
		addCurrency("Ruble","RUB");
		addCurrency("Belize Dollar","BZD");
		addCurrency("Congolese Franc","CDF");
		addCurrency("Swiss Franc","CHF");
		addCurrency("Chilean Peso","CLP");
		addCurrency("Chinese Yuan","CNY");
		addCurrency("Yuan","CNY");
		addCurrency("Colombian Peso","COP");
		addCurrency("Costa Rican Colon","CRC");
		addCurrency("Colon","CRC");
		addCurrency("Cape Verdean Escudo","CVE");
		addCurrency("Escudo","CVE");
		addCurrency("Czech Republic Koruna","CZK");
		addCurrency("Koruna","CZK");
		addCurrency("Djiboutian Franc","DJF");
		addCurrency("Danish Krone","DKK");
		addCurrency("Krone","DKK");
		addCurrency("Dominican Peso","DOP");
		addCurrency("Algerian Dinar","DZD");
		addCurrency("Estonian Kroon","EEK");
		addCurrency("Kroon","EEK");
		addCurrency("Egyptian Pound","EGP");
		addCurrency("Eritrean Nakfa","ERN");
		addCurrency("Nakfa","ERN");
		addCurrency("Ethiopian Birr","ETB");
		addCurrency("Birr","ETB");
		addCurrency("British Pound Sterling","GBP");
		addCurrency("British Pound","GBP");
		addCurrency("Pound Sterling","GBP");
		addCurrency("Pound","GBP");
		addCurrency("Georgian Lari","GEL");
		addCurrency("Lari","GEL");
		addCurrency("Ghanaian Cedi","GHS");
		addCurrency("Cedi","GHS");
		addCurrency("Guinean Franc","GNF");
		addCurrency("Guatemalan Quetzal","GTQ");
		addCurrency("Quetzal","GTQ");
		addCurrency("Hong Kong Dollar","HKD");
		addCurrency("Honduran Lempira","HNL");
		addCurrency("Lempira","HNL");
		addCurrency("Croatian Kuna","HRK");
		addCurrency("Kuna","HRK");
		addCurrency("Hungarian Forint","HUF");
		addCurrency("Forint","HUF");
		addCurrency("Indonesian Rupiah","IDR");
		addCurrency("Rupiah","IDR");
		addCurrency("Israeli New Sheqel","ILS");
		addCurrency("Sheqel","ILS");
		addCurrency("Indian Rupee","INR");
		addCurrency("Rupee","INR");
		addCurrency("Iraqi Dinar","IQD");
		addCurrency("Iranian Rial","IRR");
		addCurrency("Rial","IRR");
		addCurrency("Icelandic Krona","ISK");
		addCurrency("Krona","ISK");
		addCurrency("Jamaican Dollar","JMD");
		addCurrency("Jordanian Dinar","JOD");
		addCurrency("Japanese Yen","JPY");
		addCurrency("Yen","JPY");
		addCurrency("Kenyan Shilling","KES");
		addCurrency("Shilling","KES");
		addCurrency("Cambodian Riel","KHR");
		addCurrency("Riel","KHR");
		addCurrency("Comorian Franc","KMF");
		addCurrency("South Korean Won","KRW");
		addCurrency("Won","KRW");
		addCurrency("Kuwaiti Dinar","KWD");
		addCurrency("Kazakhstani Tenge","KZT");
		addCurrency("Tenge","KZT");
		addCurrency("Lebanese Pound","LBP");
		addCurrency("Sri Lankan Rupee","LKR");
		addCurrency("Lithuanian Litas","LTL");
		addCurrency("Litas","LTL");
		addCurrency("Latvian Lats","LVL");
		addCurrency("Lats","LVL");
		addCurrency("Libyan Dinar","LYD");
		addCurrency("Moroccan Dirham","MAD");
		addCurrency("Moldovan Leu","MDL");
		addCurrency("Leu","MDL");
		addCurrency("Malagasy Ariary","MGA");
		addCurrency("Ariary","MGA");
		addCurrency("Macedonian Denar","MKD");
		addCurrency("Denar","MKD");
		addCurrency("Myanma Kyat","MMK");
		addCurrency("Kyat","MMK");
		addCurrency("Macanese Pataca","MOP");
		addCurrency("Pataca","MOP");
		addCurrency("Mauritian Rupee","MUR");
		addCurrency("Mexican Peso","MXN");
		addCurrency("Malaysian Ringgit","MYR");
		addCurrency("Ringgit","MYR");
		addCurrency("Mozambican Metical","MZN");
		addCurrency("Metical","MZN");
		addCurrency("Namibian Dollar","NAD");
		addCurrency("Nigerian Naira","NGN");
		addCurrency("Naira","NGN");
		addCurrency("Nicaraguan Cordoba","NIO");
		addCurrency("Cordoba","NIO");
		addCurrency("Norwegian Krone","NOK");
		addCurrency("Nepalese Rupee","NPR");
		addCurrency("New Zealand Dollar","NZD");
		addCurrency("Omani Rial","OMR");
		addCurrency("Panamanian Balboa","PAB");
		addCurrency("Balboa","PAB");
		addCurrency("Peruvian Nuevo Sol","PEN");
		addCurrency("Sol","PEN");
		addCurrency("Philippine Peso","PHP");
		addCurrency("Pakistani Rupee","PKR");
		addCurrency("Polish Zloty","PLN");
		addCurrency("Zloty","PLN");
		addCurrency("Paraguayan Guarani","PYG");
		addCurrency("Guarani","PYG");
		addCurrency("Qatari Rial","QAR");
		addCurrency("Romanian Leu","RON");
		addCurrency("Serbian Dinar","RSD");
		addCurrency("Russian Ruble","RUB");
		addCurrency("Rwandan Franc","RWF");
		addCurrency("Saudi Riyal","SAR");
		addCurrency("Riyal","SAR");
		addCurrency("Sudanese Pound","SDG");
		addCurrency("Swedish Krona","SEK");
		addCurrency("Krona","SEK");
		addCurrency("Singapore Dollar","SGD");
		addCurrency("Somali Shilling","SOS");
		addCurrency("Syrian Pound","SYP");
		addCurrency("Thai Baht","THB");
		addCurrency("Baht","THB");
		addCurrency("Tunisian Dinar","TND");
		addCurrency("Tongan Pa'anga","TOP");
		addCurrency("Pa'anga","TOP");
		addCurrency("Tongan Paanga","TOP");
		addCurrency("Paanga","TOP");
		addCurrency("Turkish Lira","TRY");
		addCurrency("Lira","TRY");
		addCurrency("Trinidad and Tobago Dollar","TTD");
		addCurrency("New Taiwan Dollar","TWD");
		addCurrency("Tanzanian Shilling","TZS");
		addCurrency("Ukrainian Hryvnia","UAH");
		addCurrency("Hryvnia","UAH");
		addCurrency("Ugandan Shilling","UGX");
		addCurrency("Uruguayan Peso","UYU");
		addCurrency("Uzbekistan Som","UZS");
		addCurrency("Som","UZS");
		addCurrency("Venezuelan Bolivar","VEF");
		addCurrency("Bolivar","VEF");
		addCurrency("Vietnamese Dong","VND");
		addCurrency("Dong","VND");
		addCurrency("CFA Franc BEAC","XAF");
		addCurrency("BEAC","XAF");
		addCurrency("CFA Franc BCEAO","XOF");
		addCurrency("BCEAO","XOF");
		addCurrency("Yemeni Rial","YER");
		addCurrency("South African Rand","ZAR");
		addCurrency("Rand","ZAR");
		addCurrency("Zambian Kwacha","ZMK");
		addCurrency("Kwacha","ZMK");
	}
	private void addCurrency(String name,String code) {
		addEntityValue(name.toLowerCase(),code,code);
		if (name.contains("-")) {
			name = name.replaceAll("-"," ");
			addEntityValue(name.toLowerCase(),code,code);
		}
	}
}
