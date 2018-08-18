package nl.zeesoft.zsds.tester;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class TestCaseSet {
	private List<TestCase> testCases = new ArrayList<TestCase>();

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public TestCase getTestCase(String name) {
		TestCase r = null;
		for (TestCase tc: testCases) {
			if (tc.name.equals(name)) {
				r = tc;
				break;
			}
		}
		return r;
	}

	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem tcsElem = new JsElem("testCases",true);
		json.rootElement.children.add(tcsElem);
		for (TestCase testCase: testCases) {
			JsFile tcJs = testCase.toJson();
			JsElem tcElem = new JsElem();
			tcsElem.children.add(tcElem);
			tcElem.children = tcJs.rootElement.children;
		}
		return json;
	}

	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			JsElem tcsElem = json.rootElement.children.get(0);
			for (JsElem tcElem: tcsElem.children) {
				JsFile tcJs = new JsFile();
				tcJs.rootElement = tcElem;
				TestCase tc = new TestCase();
				tc.fromJson(tcJs);
				testCases.add(tc);
			}
		}
	}
}
