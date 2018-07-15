package nl.zeesoft.zsd.util;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.DialogSet;

public class LanguageDialogSetJsonGenerator {
	public static final String FILE_NAME_PREFIX	= "DialogSet";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageDialogSetJsonGenerator generator = new LanguageDialogSetJsonGenerator();
			EntityValueTranslator t = new EntityValueTranslator();
			t.initialize();
			DialogSet ds = new DialogSet();
			ds.initialize(t);
			BaseConfiguration base = new BaseConfiguration();
			ZStringBuilder err = generator.generateLanguageDialogSet(base,ds,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
		}
	}
	
	public ZStringBuilder generateLanguageDialogSet(BaseConfiguration base,DialogSet ds,String directory,boolean readFormat) {
		ZStringBuilder err = new ZStringBuilder();
		if (!directory.endsWith("/") && !directory.endsWith("\\")) {
			directory += "/";
		}
		for (String language: base.getSupportedLanguages()) {
			DialogSetToJson convertor = new DialogSetToJson();
			JsFile json = convertor.toJson(ds, language);
			String fileName = directory + FILE_NAME_PREFIX + language + ".json";
			err = json.toFile(fileName,readFormat);
			if (err.length()>0) {
				break;
			}
		}
		return err;
	}
}
