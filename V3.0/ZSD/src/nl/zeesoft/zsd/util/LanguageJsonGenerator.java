package nl.zeesoft.zsd.util;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.dialog.DialogSet;

public class LanguageJsonGenerator {
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			BaseConfiguration base = new BaseConfiguration();
			EntityValueTranslator t = new EntityValueTranslator();
			t.initialize();
			DialogSet ds = new DialogSet();
			ds.initialize(t);
			
			LanguageJsonGenerator generator = new LanguageJsonGenerator();
			generator.generate(base,t,ds,args[0]);
		}
	}
	
	public void generate(BaseConfiguration base,EntityValueTranslator t, DialogSet ds,String dir) {
		LanguageClassifierJsonGenerator lang = new LanguageClassifierJsonGenerator();
		LanguageMasterContextJsonGenerator master = new LanguageMasterContextJsonGenerator();
		LanguageContextJsonGenerator context = new LanguageContextJsonGenerator();
		LanguageDialogSetJsonGenerator dialog = new LanguageDialogSetJsonGenerator();
		
		lang.generate(t,ds,dir,base.isGenerateReadFormat());
		master.generate(base,ds,dir,base.isGenerateReadFormat());
		context.generate(base,ds,dir,base.isGenerateReadFormat());
		dialog.generate(base,ds,dir,base.isGenerateReadFormat());
	}
}
