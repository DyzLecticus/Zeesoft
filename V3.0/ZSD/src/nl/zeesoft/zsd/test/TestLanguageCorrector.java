package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SymbolCorrector;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;

public class TestLanguageCorrector extends TestObject {
	public TestLanguageCorrector(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestLanguageCorrector(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test uses two large JSON datasets to test language specific *SymbolCorrector* instances.");
	}
	
	@Override
	protected void test(String[] args) {
		InterpreterConfiguration config = TestInterpreterConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			SymbolCorrector scEng = config.getLanguageCorrectors().get(EntityObject.LANG_ENG);
			SymbolCorrector scNld = config.getLanguageCorrectors().get(EntityObject.LANG_NLD);

			assertEqual(scEng.getLinkContextCounts().get(""),204712,"The total number of english links does not match expectation");
			assertEqual(scNld.getLinkContextCounts().get(""),197779,"The total number of dutch links does not match expectation");
			
			System.out.println();
			
			ZStringSymbolParser sequence = new ZStringSymbolParser("Wat is your name?");
			ZStringSymbolParser corrected = new ZStringSymbolParser(sequence); 
			scEng.correct(corrected);
			System.out.println("'" + sequence + "' (ENG) => '" + corrected + "'");
			assertEqual(corrected.toString(),"What is your name?","The correction does not match expectation");

			corrected = new ZStringSymbolParser(sequence); 
			scNld.correct(corrected);
			System.out.println("'" + sequence + "' (NLD) => '" + corrected + "'");
			assertEqual(corrected.toString(),"Wat is jouw naam?","The correction does not match expectation");
		}
	}
}
