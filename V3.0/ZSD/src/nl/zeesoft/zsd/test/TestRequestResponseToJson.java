package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;
import nl.zeesoft.zsd.dialog.dialogs.Generic;
import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class TestRequestResponseToJson extends TestObject {
	public TestRequestResponseToJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestRequestResponseToJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert a *DialogRequest* and *DialogResponse* objects from and to JSON respectively.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the dialog request");
		System.out.println("DialogRequest req = new DialogRequest();");
		System.out.println("// Create the JSON");
		System.out.println("JsFile json = new JsFile();");
		System.out.println("// Convert the dialog request from JSON");
		System.out.println("req.fromJson(json);");
		System.out.println("// Create the dialog response");
		System.out.println("DialogResponse res = new DialogResponse();");
		System.out.println("// Convert the dialog response to JSON");
		System.out.println("json = res.toJson();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestRequestResponseToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(DialogResponse.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		System.out.println("Dialog request JSON;");
		DialogRequest req = new DialogRequest();
		req.setAllActions(true);
		req.prompt.append("test prompt?");
		req.input.append("test input.");
		req.language = BaseConfiguration.LANG_ENG;
		req.masterContext = Generic.MASTER_CONTEXT_GENERIC;
		req.context = GenericHandshake.CONTEXT_GENERIC_HANDSHAKE;
		req.classifyMasterContextThreshold = 0.8;
		req.classifyContextThreshold = 0.8;
		req.translateEntityTypes.add(BaseConfiguration.TYPE_NAME);
		req.matchThreshold = 0.8;
		req.randomizeOutput = false;
		DialogVariableValue dvv = new DialogVariableValue();
		dvv.name = "testVariable";
		dvv.externalValue = "testExtVal";
		dvv.internalValue = "testIntVal";
		req.dialogVariableValues.put(dvv.name,dvv);
		JsFile json = req.toJson();
		ZStringBuilder txtOri = json.toStringBuilderReadFormat();
		System.out.println(txtOri);
		assertEqual(json.rootElement.children.size(),18,"The number of children does not match expectation");
		req = new DialogRequest();
		req.fromJson(json);
		json = req.toJson();
		ZStringBuilder txtNew = json.toStringBuilderReadFormat();
		assertEqual(txtOri.length(),txtNew.length(),"The request does not match the original");
		if (!txtOri.equals(txtNew)) {
			System.err.println(txtNew);
		}
		System.out.println();
		System.out.println("Dialog response JSON;");
		DialogResponse res = new DialogResponse();
		SequenceClassifierResult scr = new SequenceClassifierResult();
		scr.symbol = BaseConfiguration.LANG_ENG;
		scr.prob = 0.4D;
		scr.probNormalized = 1.0D;
		res.responseLanguages.add(scr);
		scr = new SequenceClassifierResult();
		scr.symbol = BaseConfiguration.LANG_NLD;
		scr.prob = 0.2D;
		scr.probNormalized = 0.5D;
		res.responseLanguages.add(scr);
		res.correctedInput = new ZStringSymbolParser("Test corrected input.");
		res.entityValueTranslation = new ZStringSymbolParser("Test entity value translation.");
		res.entityValueTranslationCorrected = new ZStringSymbolParser("Test corrected entity value translation.");
		DialogResponseOutput output = new DialogResponseOutput();
		output.context = "testContext";
		output.output = new ZStringSymbolParser("Test output!");
		output.prompt = new ZStringSymbolParser("Test prompt!");
		output.promptVariable = "testPromptVar";
		res.contextOutputs.add(output);
		dvv = new DialogVariableValue();
		dvv.name = "testVariable";
		dvv.externalValue = "testExtVal";
		dvv.internalValue = "testIntVal";
		output.values.put(dvv.name,dvv);
		json = res.toJson();
		txtOri = json.toStringBuilderReadFormat();
		System.out.println(txtOri);
		assertEqual(json.rootElement.children.size(),8,"The number of children does not match expectation");
		res = new DialogResponse();
		res.fromJson(json);
		json = res.toJson();
		txtNew = json.toStringBuilderReadFormat();
		assertEqual(txtOri.length(),txtNew.length(),"The response does not match the original");
		if (!txtOri.equals(txtNew)) {
			System.err.println(txtNew);
		}
	}
}
