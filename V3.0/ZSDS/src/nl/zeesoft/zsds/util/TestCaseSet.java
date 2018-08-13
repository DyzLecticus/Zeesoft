package nl.zeesoft.zsds.util;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

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
}
