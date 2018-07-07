package nl.zeesoft.zsd.util;

import java.util.List;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.entity.EntityObject;

public class LanguageContextJsonGenerator {
	public static final String FILE_NAME_PREFIX	= "LanguageContext";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageContextJsonGenerator generator = new LanguageContextJsonGenerator();
			DialogSet ds = new DialogSet();
			ds.initialize();
			String err = "";
			err = generator.generateLanguageDialogs(ds,EntityObject.LANG_ENG,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
			err = generator.generateLanguageDialogs(ds,EntityObject.LANG_NLD,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
		}
	}
	
	public String generateLanguageDialogs(DialogSet ds,String language,String directory,boolean readFormat) {
		String err = "";
		if (!directory.endsWith("/") && !directory.endsWith("\\")) {
			directory += "/";
		}
		DialogToJson convertor = new DialogToJson();
		List<String> masterContexts = ds.getLanguageMasterContexts().get(language);
		for (String masterContext: masterContexts) {
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
