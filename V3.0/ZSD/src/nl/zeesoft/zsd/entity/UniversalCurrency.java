package nl.zeesoft.zsd.entity;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;

public class UniversalCurrency extends EntityObject {
	public static final String CURR_EUR		= "EUR";
	
	@Override
	public String getLanguage() {
		return BaseConfiguration.LANG_UNI;
	}
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_CURRENCY;
	}
	@Override
	public void initialize(EntityValueTranslator translator) {
		super.initialize(translator);
		addCurrency("USD");
		addCurrency("CAD");
		addCurrency("EUR");
		addCurrency("AED");
		addCurrency("AFN");
		addCurrency("ALL");
		addCurrency("AMD");
		addCurrency("ARS");
		addCurrency("AUD");
		addCurrency("AZN");
		addCurrency("BAM");
		addCurrency("BDT");
		addCurrency("BGN");
		addCurrency("BHD");
		addCurrency("BIF");
		addCurrency("BND");
		addCurrency("BOB");
		addCurrency("BRL");
		addCurrency("BWP");
		addCurrency("BYR");
		addCurrency("BZD");
		addCurrency("CDF");
		addCurrency("CHF");
		addCurrency("CLP");
		addCurrency("CNY");
		addCurrency("COP");
		addCurrency("CRC");
		addCurrency("CVE");
		addCurrency("CZK");
		addCurrency("DJF");
		addCurrency("DKK");
		addCurrency("DOP");
		addCurrency("DZD");
		addCurrency("EEK");
		addCurrency("EGP");
		addCurrency("ERN");
		addCurrency("ETB");
		addCurrency("GBP");
		addCurrency("GEL");
		addCurrency("GHS");
		addCurrency("GNF");
		addCurrency("GTQ");
		addCurrency("HKD");
		addCurrency("HNL");
		addCurrency("HRK");
		addCurrency("HUF");
		addCurrency("IDR");
		addCurrency("ILS");
		addCurrency("INR");
		addCurrency("IQD");
		addCurrency("IRR");
		addCurrency("ISK");
		addCurrency("JMD");
		addCurrency("JOD");
		addCurrency("JPY");
		addCurrency("KES");
		addCurrency("KHR");
		addCurrency("KMF");
		addCurrency("KRW");
		addCurrency("KWD");
		addCurrency("KZT");
		addCurrency("LBP");
		addCurrency("LKR");
		addCurrency("LTL");
		addCurrency("LVL");
		addCurrency("LYD");
		addCurrency("MAD");
		addCurrency("MDL");
		addCurrency("MGA");
		addCurrency("MKD");
		addCurrency("MMK");
		addCurrency("MOP");
		addCurrency("MUR");
		addCurrency("MXN");
		addCurrency("MYR");
		addCurrency("MZN");
		addCurrency("NAD");
		addCurrency("NGN");
		addCurrency("NIO");
		addCurrency("NOK");
		addCurrency("NPR");
		addCurrency("NZD");
		addCurrency("OMR");
		addCurrency("PAB");
		addCurrency("PEN");
		addCurrency("PHP");
		addCurrency("PKR");
		addCurrency("PLN");
		addCurrency("PYG");
		addCurrency("QAR");
		addCurrency("RON");
		addCurrency("RSD");
		addCurrency("RUB");
		addCurrency("RWF");
		addCurrency("SAR");
		addCurrency("SDG");
		addCurrency("SEK");
		addCurrency("SGD");
		addCurrency("SOS");
		addCurrency("SYP");
		addCurrency("THB");
		addCurrency("TND");
		addCurrency("TOP");
		addCurrency("TRY");
		addCurrency("TTD");
		addCurrency("TWD");
		addCurrency("TZS");
		addCurrency("UAH");
		addCurrency("UGX");
		addCurrency("UYU");
		addCurrency("UZS");
		addCurrency("VEF");
		addCurrency("VND");
		addCurrency("XAF");
		addCurrency("XOF");
		addCurrency("YER");
		addCurrency("ZAR");
		addCurrency("ZMK");
	}
	private void addCurrency(String code) {
		addEntityValue(code.toLowerCase(),code,code);
	}
}
