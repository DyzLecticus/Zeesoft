package nl.zeesoft.zsds.tester;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;

public class TestCase {
	public String			name	= "";
	public List<TestCaseIO> io		= new ArrayList<TestCaseIO>();

	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		JsElem iosElem = new JsElem("io",true);
		json.rootElement.children.add(iosElem);
		for (TestCaseIO tcIO: io) {
			JsElem ioElem = new JsElem();
			iosElem.children.add(ioElem);
			
			JsFile request = tcIO.request.toJson();
			JsElem reqElem = new JsElem("request");
			reqElem.children = request.rootElement.children;
			ioElem.children.add(reqElem);
			
			JsFile response = tcIO.expectedResponse.toJson();
			JsElem resElem = new JsElem("expectedResponse");
			resElem.children = response.rootElement.children;
			ioElem.children.add(resElem);
		}
		return json;
	}

	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			name = json.rootElement.getChildString("name");
			JsElem iosElem = json.rootElement.getChildByName("io");
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
						io.add(tcIO);
					}
				}
			}
		}
	}
}
