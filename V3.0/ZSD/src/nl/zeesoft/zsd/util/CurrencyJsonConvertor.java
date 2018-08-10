package nl.zeesoft.zsd.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class CurrencyJsonConvertor {
	public static void main(String[] args) {
		String fileName = "resources/Currency.json";
		JsFile json = new JsFile();
		ZStringBuilder err = json.fromFile(fileName);
		List<String> added = new ArrayList<String>();
		if (err.length()>0) {
			System.err.println(err);
		} else {
			for (JsElem countryElem: json.rootElement.children) {
				String name = countryElem.getChildValueByName("name").toString();
				String code = countryElem.getChildValueByName("code").toString();
				//
				System.out.println("addCurrency(\"" + name + "\",\"" + code + "\");");
				
				String[] split = name.split(" ");
				if (split.length>1) {
					String nam = split[split.length - 1];
					if (!added.contains(nam)) {
						added.add(nam);
						System.out.println("addCurrency(\"" + nam + "\",\"" + code + "\");");
					}
				}
			}
			System.out.println();
			for (JsElem countryElem: json.rootElement.children) {
				String code = countryElem.getChildValueByName("code").toString();
				//
				System.out.println("addCurrency(\"" + code + "\");");
			}
		}
	}
}
