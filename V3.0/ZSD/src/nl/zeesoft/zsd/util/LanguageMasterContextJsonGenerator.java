package nl.zeesoft.zsd.util;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.DialogSet;

public class LanguageMasterContextJsonGenerator {
	public static final String FILE_NAME_PREFIX	= "LanguageMasterContext";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageMasterContextJsonGenerator generator = new LanguageMasterContextJsonGenerator();
			EntityValueTranslator t = new EntityValueTranslator();
			t.initialize();
			DialogSet ds = new DialogSet();
			ds.initialize(t);
			BaseConfiguration base = new BaseConfiguration();
			ZStringBuilder err = generator.generate(base,ds,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
		}
	}
	
	public ZStringBuilder generate(BaseConfiguration base,DialogSet ds,String directory,boolean readFormat) {
		ZStringBuilder err = new ZStringBuilder();
		for (String language: base.getSupportedLanguages()) {
			err = generateLanguageDialogs(ds,language,directory,readFormat);
			if (err.length()>0) {
				break;
			}
		}
		return err;
	}
	
	private ZStringBuilder generateLanguageDialogs(DialogSet ds,String language,String directory,boolean readFormat) {
		DialogToJson convertor = new DialogToJson();
		JsFile json = convertor.getJsonForDialogs(ds.getDialogs(language),false,true);
		if (!directory.endsWith("/") && !directory.endsWith("\\")) {
			directory += "/";
		}
		String fileName = directory + FILE_NAME_PREFIX + language + ".json";
		return json.toFile(fileName,readFormat);
	}
}
