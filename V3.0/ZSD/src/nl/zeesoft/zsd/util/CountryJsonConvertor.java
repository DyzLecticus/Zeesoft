package nl.zeesoft.zsd.util;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class CountryJsonConvertor {
	public static void main(String[] args) {
		String fileName = "resources/DutchCountries.json";
		JsFile json = new JsFile();
		ZStringBuilder err = json.fromFile(fileName);
		if (err.length()>0) {
			System.err.println(err);
		} else {
			for (JsElem countryElem: json.rootElement.children.get(0).children) {
				String name = countryElem.getChildValueByName("name").toString();
				String code = countryElem.getChildValueByName("short_name").toString();
				//
				System.out.println("addCountry(\"" + name + "\",\"" + code + "\");");
			}
		}
	}
}
