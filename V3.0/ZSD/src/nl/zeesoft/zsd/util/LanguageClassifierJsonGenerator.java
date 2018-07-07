package nl.zeesoft.zsd.util;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.DialogSet;

public class LanguageClassifierJsonGenerator {
	public static final String FILE_NAME = "LanguageClassifier.json";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageClassifierJsonGenerator generator = new LanguageClassifierJsonGenerator();
			EntityValueTranslator t = new EntityValueTranslator();
			t.initialize();
			DialogSet ds = new DialogSet();
			ds.initialize();
			String err = generator.generateEntityValueTranslator(t,ds,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
		}
	}
	
	public String generateEntityValueTranslator(EntityValueTranslator t,DialogSet ds,String directory,boolean readFormat) {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		DialogToJson dc = new DialogToJson();
		dc.addJsonForDialogs(json.rootElement,ds.getDialogs(),true,false);
		EntityToJson ec = new EntityToJson();
		ec.addJsonForEntities(json.rootElement,t.getEntities(),"",true);
		if (!directory.endsWith("/") && !directory.endsWith("\\")) {
			directory += "/";
		}
		String fileName = directory + FILE_NAME;
		return json.toFile(fileName,readFormat);
	}
}
