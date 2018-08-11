package nl.zeesoft.zsd.util;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.DialogSet;

public class LanguageContextJsonGenerator {
	public static final String FILE_NAME_PREFIX	= "LanguageContext";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageContextJsonGenerator generator = new LanguageContextJsonGenerator();
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
			err = generateLanguageDialogs(base,ds,language,directory,readFormat);
			if (err.length()>0) {
				break;
			}
		}
		return err;
	}
	
	private ZStringBuilder generateLanguageDialogs(BaseConfiguration base,DialogSet ds,String language,String directory,boolean readFormat) {
		ZStringBuilder err = new ZStringBuilder();
		if (!directory.endsWith("/") && !directory.endsWith("\\")) {
			directory += "/";
		}
		DialogToJson convertor = new DialogToJson();
		for (String masterContext: base.getSupportedMasterContexts().get(language)) {
			JsFile json = convertor.getJsonForDialogs(ds.getDialogs(language,masterContext),false,null);
			String fileName = directory + FILE_NAME_PREFIX + language + masterContext + ".json";
			err = json.toFile(fileName,readFormat);
			if (err.length()>0) {
				break;
			}
		}
		return err;
	}
}
