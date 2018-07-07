package nl.zeesoft.zsd.util;

public class LanguageJsonGenerator {
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguageClassifierJsonGenerator.main(args);
			LanguageCorrectorJsonGenerator.main(args);
			LanguageMasterContextJsonGenerator.main(args);
			LanguageContextJsonGenerator.main(args);
		}
	}
}
