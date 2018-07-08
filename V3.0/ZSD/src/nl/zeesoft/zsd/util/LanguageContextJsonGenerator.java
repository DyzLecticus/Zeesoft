package nl.zeesoft.zsd.util;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogSet;

public class LanguageContextJsonGenerator {
	public static final String FILE_NAME_PREFIX	= "LanguageContext";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageContextJsonGenerator generator = new LanguageContextJsonGenerator();
			DialogSet ds = new DialogSet();
			ds.initialize();
			BaseConfiguration base = new BaseConfiguration();
			String err = generator.generateLanguageDialogs(base,ds,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
		}
	}
	
	public String generateLanguageDialogs(BaseConfiguration base,DialogSet ds,String directory,boolean readFormat) {
		String err = "";
		for (String language: base.getSupportedLanguages()) {
			err = generateLanguageDialogs(base,ds,language,directory,readFormat);
			if (err.length()>0) {
				break;
			}
		}
		return err;
	}
	
	private String generateLanguageDialogs(BaseConfiguration base,DialogSet ds,String language,String directory,boolean readFormat) {
		String err = "";
		if (!directory.endsWith("/") && !directory.endsWith("\\")) {
			directory += "/";
		}
		DialogToJson convertor = new DialogToJson();
		for (String masterContext: base.getSupportedMasterContexts().get(language)) {
			JsFile json = convertor.getJsonForDialogs(ds.getDialogs(language,masterContext),false,false);
			String fileName = directory + FILE_NAME_PREFIX + language + masterContext + ".json";
			err = json.toFile(fileName,readFormat);
			if (err.length()>0) {
				break;
			}
		}
		return err;
	}
}
