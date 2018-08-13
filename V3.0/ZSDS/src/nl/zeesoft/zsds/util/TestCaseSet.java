package nl.zeesoft.zsds.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;

public class TestCaseSet {
	private List<TestCase> testCases = new ArrayList<TestCase>();

	public List<TestCase> getTestCases() {
		return testCases;
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem tcsElem = new JsElem("testCases",true);
		json.rootElement.children.add(tcsElem);
		for (TestCase testCase: testCases) {
			JsElem tcElem = new JsElem();
			tcsElem.children.add(tcElem);
			JsElem iosElem = new JsElem("io",true);
			tcElem.children.add(iosElem);
			for (TestCaseIO io: testCase.io) {
				JsElem ioElem = new JsElem();
				iosElem.children.add(ioElem);
				
				JsFile request = io.request.toJson();
				JsElem reqElem = new JsElem("request");
				reqElem.children = request.rootElement.children;
				ioElem.children.add(reqElem);
				
				JsFile response = io.expectedResponse.toJson();
				JsElem resElem = new JsElem("expectedResponse");
				resElem.children = response.rootElement.children;
				ioElem.children.add(resElem);
			}
		}
		return json;
	}

	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			JsElem tcsElem = json.rootElement.children.get(0);
			for (JsElem tcElem: tcsElem.children) {
				TestCase tc = new TestCase();
				testCases.add(tc);
				JsElem iosElem = tcElem.getChildByName("io");
				if (iosElem!=null) {
					for (JsElem ioElem: iosElem.children) {
						DialogRequest request = null;
						DialogResponse response = null;

						JsElem reqElem = ioElem.getChildByName("request");
						if (reqElem!=null) {
							JsFile reqJson = new JsFile();
							reqJson.rootElement = new JsElem();
							reqJson.rootElement.children = reqElem.children;
							request = new DialogRequest();
							request.fromJson(reqJson);
						}
						
						JsElem resElem = ioElem.getChildByName("expectedResponse");
						if (resElem!=null) {
							JsFile resJson = new JsFile();
							resJson.rootElement = new JsElem();
							resJson.rootElement.children = resElem.children;
							response = new DialogResponse();
							response.fromJson(resJson);
						}

						if (request!=null && response!=null) {
							TestCaseIO tcIO = new TestCaseIO();
							tcIO.request = request;
							tcIO.expectedResponse = response;
							tc.io.add(tcIO);
						}
					}
				}
			}
		}
	}
}
