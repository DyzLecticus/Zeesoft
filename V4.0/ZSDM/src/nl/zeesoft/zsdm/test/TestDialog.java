package nl.zeesoft.zsdm.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.zsdm.dialog.Dialog;

public class TestDialog extends TestObject {
	public TestDialog(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialog(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
		 *  TODO: Describe
		System.out.println("This test shows how to use the *Translator* to translate a sequence to and from internal values.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Translator");
		System.out.println("Translator translator = new Translator(new Config());");
		System.out.println("// Initialize the Translator (and wait or listen for initialization to finish)");
		System.out.println("translator.initialize();");
		System.out.println("// Use Translator to translate a sequence");
		System.out.println("ZStringSymbolParser translated = translator.translateToInternalValues(new ZStringSymbolParser(\"some sequence\"));");
		System.out.println("ZStringSymbolParser retranslated = translator.translateToExternalValues(translated);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockTranslator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDialog.class));
		System.out.println(" * " + getTester().getLinkForClass(MockTranslator.class));
		System.out.println(" * " + getTester().getLinkForClass(Translator.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it takes to initialize the translator  ");
		System.out.println(" * The translation results including the time it takes, for a set of input sequences  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		Dialog dialog = new Dialog();
		dialog.setlanguage(Languages.ENG);
		dialog.setMasterContext("MasterContext");
		dialog.setContext("Context");
		dialog.addExample("Test input 1.","Test output 1.");
		dialog.addExample("Test input 2.","Test output 2.");
		dialog.addVariable("testVariable1",Types.NUMERIC);
		dialog.addVariablePrompt("testVariable1","Test prompt 1?");
		dialog.addVariablePrompt("testVariable1","Test prompt 2?");
		dialog.addVariable("testVariable2",Types.CURRENCY);
		dialog.addVariablePrompt("testVariable2","Test prompt 1?");
		dialog.addVariablePrompt("testVariable2","Test prompt 2?");
		
		JsFile json = dialog.toJson();
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		dialog = new Dialog();
		dialog.fromJson(json);
		json = dialog.toJson();
		ZStringBuilder newStr = json.toStringBuilderReadFormat();
		if (!newStr.equals(oriStr)) {
			assertEqual(false,true,"Converted JSON does not match original");
			System.err.println(newStr);
		}
	}
}
