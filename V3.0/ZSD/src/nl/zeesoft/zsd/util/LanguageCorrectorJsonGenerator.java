package nl.zeesoft.zsd.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.entity.EntityObject;

public class LanguageCorrectorJsonGenerator {
	public static final String FILE_NAME_PREFIX = "LanguageCorrector";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageCorrectorJsonGenerator generator = new LanguageCorrectorJsonGenerator();
			EntityValueTranslator t = new EntityValueTranslator();
			t.initialize();
			String err = "";
			err = generator.generateEntityValueTranslator(t,EntityObject.LANG_ENG,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
			err = generator.generateEntityValueTranslator(t,EntityObject.LANG_NLD,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
		}
	}
	
	public String generateEntityValueTranslator(EntityValueTranslator t,String language,String directory,boolean readFormat) {
		EntityToJson convertor = new EntityToJson();
		List<String> languages = new ArrayList<String>();
		languages.add(language);
		JsFile json = convertor.getJsonForEntities(t.getEntities(languages,null),"");
		String fileName = directory + FILE_NAME_PREFIX + language + ".json";
		return json.toFile(fileName,readFormat);
	}
}
