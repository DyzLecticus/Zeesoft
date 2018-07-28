package nl.zeesoft.zsd.util;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class LanguageJsonConvertor {
	public static void main(String[] args) {
		String fileName = "resources/EnglishLanguages.json";
		JsFile json = new JsFile();
		ZStringBuilder err = json.fromFile(fileName);
		if (err.length()>0) {
			System.err.println(err);
		} else {
			for (JsElem langElem: json.rootElement.children.get(0).children) {
				String code = langElem.getChildValueByName("code").toString();
				String name = langElem.getChildValueByName("name").toString();
				String[] split = name.split("; ");
				for (int i = 0; i < split.length; i++) {
					String[] split2 = split[i].split(", ");
					for (int i2 = 0; i2 < split2.length; i2++) {
						System.out.println("addLanguage(\"" + split2[i2] + "\",\"" + code.toUpperCase() + "\");");
					}
				}
			}
		}
	}
}
