package nl.zeesoft.zsd.util;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.SequencePreprocessor;

public class LanguagePreprocessorJsonGenerator {
	public static final String FILE_NAME = "LanguagePreprocessor.json";
	
	public static void main(String[] args) {
		if (args!=null && args.length>0 && args[0].length()>0) {
			LanguagePreprocessorJsonGenerator generator = new LanguagePreprocessorJsonGenerator();
			SequencePreprocessor sp = new SequencePreprocessor();
			sp.initialize();
			ZStringBuilder err = generator.generate(sp,args[0],true);
			if (err.length()>0) {
				System.err.println(err);
			}
		}
	}
	
	public ZStringBuilder generate(SequencePreprocessor sp,String directory,boolean readFormat) {
		if (!directory.endsWith("/") && !directory.endsWith("\\")) {
			directory += "/";
		}
		String fileName = directory + FILE_NAME;
		return sp.toJson().toFile(fileName,readFormat);
	}
}
