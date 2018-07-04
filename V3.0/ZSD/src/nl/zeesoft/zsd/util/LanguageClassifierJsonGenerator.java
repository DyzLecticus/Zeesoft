package nl.zeesoft.zsd.util;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.EntityValueTranslator;

public class LanguageClassifierJsonGenerator {
	public static final String FILE_NAME = "LanguageClassifier.json";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageClassifierJsonGenerator generator = new LanguageClassifierJsonGenerator();
			EntityValueTranslator t = new EntityValueTranslator();
			t.initialize();
			String err = generator.generateEntityValueTranslator(t,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
		}
	}
	
	public String generateEntityValueTranslator(EntityValueTranslator t,String directory,boolean readFormat) {
		EntityToJson convertor = new EntityToJson();
		JsFile json = convertor.getJsonForEntities(t.getEntities(),"",true);
		if (!directory.endsWith("/") && !directory.endsWith("\\")) {
			directory += "/";
		}
		String fileName = directory + FILE_NAME;
		return json.toFile(fileName,readFormat);
	}
}
