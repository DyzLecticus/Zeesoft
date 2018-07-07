package nl.zeesoft.zsd.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.entity.EntityObject;

public class LanguageCorrectorJsonGenerator {
	public static final String FILE_NAME_PREFIX = "LanguageCorrector";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageCorrectorJsonGenerator generator = new LanguageCorrectorJsonGenerator();
			EntityValueTranslator t = new EntityValueTranslator();
			t.initialize();
			DialogSet ds = new DialogSet();
			ds.initialize();
			String err = "";
			err = generator.generateEntityValueTranslator(t,ds,EntityObject.LANG_ENG,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
			err = generator.generateEntityValueTranslator(t,ds,EntityObject.LANG_NLD,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
		}
	}
	
	public String generateEntityValueTranslator(EntityValueTranslator t,DialogSet ds,String language,String directory,boolean readFormat) {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		DialogToJson dc = new DialogToJson();
		dc.addJsonForDialogs(json.rootElement,ds.getDialogs(),false,false);
		List<String> languages = new ArrayList<String>();
		languages.add(language);
		EntityToJson ec = new EntityToJson();
		ec.addJsonForEntities(json.rootElement,t.getEntities(languages,null),"",false);
		if (!directory.endsWith("/") && !directory.endsWith("\\")) {
			directory += "/";
		}
		String fileName = directory + FILE_NAME_PREFIX + language + ".json";
		return json.toFile(fileName,readFormat);
	}
}
