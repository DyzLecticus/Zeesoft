package nl.zeesoft.zsd.test;

import java.util.Date;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SymbolCorrector;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.util.LanguageCorrectorJsonGenerator;

public class TestLanguageCorrector extends TestObject {
	public static final String LANGUAGE_FILE_NAME_ENG = "resources/" + LanguageCorrectorJsonGenerator.FILE_NAME_PREFIX + EntityObject.LANG_ENG + ".json";
	public static final String LANGUAGE_FILE_NAME_NLD = "resources/" + LanguageCorrectorJsonGenerator.FILE_NAME_PREFIX + EntityObject.LANG_NLD + ".json";
	
	public TestLanguageCorrector(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestLanguageCorrector(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test uses two large JSON datasets to test language specific *SymbolCorrector* instances intended to be used as language correctors.");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		SymbolCorrector scEng = new SymbolCorrector();
		SymbolCorrector scNld = new SymbolCorrector();
		String err = "";
		err = scEng.initialize(LANGUAGE_FILE_NAME_ENG);
		assertEqual(err.length(),0,"Reading " + LANGUAGE_FILE_NAME_ENG + " produced an unexpected error");
		if (err.length()==0) {
			err = scNld.initialize(LANGUAGE_FILE_NAME_NLD);
			assertEqual(err.length(),0,"Reading " + LANGUAGE_FILE_NAME_NLD + " produced an unexpected error");
		}
		if (err.length()==0) {
			System.out.println("Sequentially initializing the language SymbolCorrector instances took: " + ((new Date()).getTime() - started.getTime()) + " ms");

			assertEqual(scEng.getLinkContextCounts().get(""),204556,"The total number of english links does not match expectation");
			assertEqual(scNld.getLinkContextCounts().get(""),197595,"The total number of dutch links does not match expectation");
			
			ZStringSymbolParser sequence = new ZStringSymbolParser("Wat is your name?");
			ZStringSymbolParser corrected = new ZStringSymbolParser(sequence); 
			scEng.correct(corrected);
			System.out.println("'" + sequence + "' (ENG) => '" + corrected + "'");

			corrected = new ZStringSymbolParser(sequence); 
			scNld.correct(corrected);
			System.out.println("'" + sequence + "' (NLD) => '" + corrected + "'");
		}
	}
}
