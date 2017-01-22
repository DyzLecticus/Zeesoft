package nl.zeesoft.zodb.test;

import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.file.HTMLFile;

public class TestHTTPResourceFactory extends SvrHTTPResourceFactory {

	@Override
	protected void initializeHTTPResources() {
		super.initializeHTTPResources();
		addFile("test.js",getTestJavaScript());
		addFile("test.html",getTestHtml().toStringBuilder());
	}
		
	protected StringBuilder getTestJavaScript() {
		StringBuilder script = new StringBuilder();

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.test = {};\n");

		script.append("ZODB.test.testGetDebugCookieValueImpl = function() {\n");
		script.append("    ZODB.debug.log(ZODB.cookie.getCookieValue(\"debug\"));\n");
		script.append("};\n");

		script.append("ZODB.test.testGetTestURLParameterValueImpl = function() {\n");
		script.append("    ZODB.debug.log(ZODB.url.getURLParameterValue(\"test\"));\n");
		script.append("};\n");

		script.append("ZODB.test.testExecuteXMLHttpRequest = function(method,url) {\n");
		script.append("    ZODB.ajax.executeXMLHttPRequest(method,url,\n");
		script.append("        function(xhttp) {\n");
		script.append("            ZODB.debug.log(xhttp.responseText);\n");
		script.append("        }\n");
		script.append("    );\n");
		script.append("};\n");

		script.append("ZODB.test.testExecuteXMLHttpRequestImpl = function() {\n");
		script.append("    ZODB.test.testExecuteXMLHttpRequest(\"GET\",\"/data/" + MdlModel.PACKAGE_CLASS_FULL_NAME + ".xml\");\n");
		script.append("};\n");
		
		script.append("ZODB.test.testGetModelCallback = function(xhttp) {\n");
		script.append("    ZODB.model.getModelCallback(xhttp);\n");
		script.append("    for (packageName in ZODB.model.packages) {\n");
		script.append("        ZODB.debug.log(\"Package: \" + packageName);\n");
		script.append("        for (className in ZODB.model.packages[packageName].classes) {\n");
		script.append("            ZODB.debug.log(\"- Class: \" + className);\n");
		script.append("            for (propName in ZODB.model.packages[packageName].classes[className].properties) {\n");
		script.append("                var propType = ZODB.model.packages[packageName].classes[className].properties[propName].type;\n");
		script.append("                ZODB.debug.log(\"-- Property: \" + propName + \", type: \" + propType);\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZODB.test.testGetModelCallbackImpl = function() {\n");
		script.append("    ZODB.model.getModel(ZODB.test.testGetModelCallback);\n");
		script.append("};\n");

		script.append("ZODB.test.testGetDataForClassCallback = function(xhttp) {\n");
		script.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		script.append("    for (id in dataObjectSet.objects) {\n");
		script.append("        ZODB.debug.log(\"Package: \" + id + \", name: \" + dataObjectSet.objects[id].propertyValues[\"name\"]);\n");
		script.append("    }\n");
		script.append("};\n");
		
		script.append("ZODB.test.testGetDataForClassCallbackImpl = function() {\n");
		script.append("    ZODB.data.getDataForClass(\"" + MdlModel.PACKAGE_CLASS_FULL_NAME + "\",ZODB.test.testGetDataForClassCallback);\n");
		script.append("};\n");

		script.append("ZODB.test.testExecuteGetRequestCallbackImpl = function() {\n");
		script.append("    getRequest = new ZODB.data.GetRequest(\"" + MdlModel.PACKAGE_CLASS_FULL_NAME + "\");\n");
		script.append("    getRequest.start = 1;\n");
		script.append("    getRequest.orderBy = \"name\";\n");
		script.append("    getRequest.orderAscending = \"false\";\n");
		script.append("    ZODB.data.executeGetRequest(getRequest,ZODB.test.testGetDataForClassCallback);\n");
		script.append("    getRequest = new ZODB.data.GetRequest(\"" + MdlModel.PACKAGE_CLASS_FULL_NAME + "\");\n");
		script.append("    getRequest.filters[\"id\"] = 2;\n");
		script.append("    ZODB.data.executeGetRequest(getRequest,ZODB.test.testGetDataForClassCallback);\n");
		script.append("};\n");

		script.append("ZODB.test.testInitializeSelectImpl = function() {\n");
		script.append("    var sel = new ZODB.gui.Select(\"testDiv\",\"" + MdlModel.class.getPackage().getName() + "\",\"" + MdlModel.PACKAGE_CLASS_NAME + "\");\n");
		script.append("    sel.initialize();\n");
		script.append("};\n");

		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.debug = ZODB.debug || {};\n");
		script.append("ZODB.debug.console = true;\n");
		
		return script;
	}

	protected HTMLFile getTestHtml() {
		HTMLFile file = new HTMLFile("Test");
		
		file.getScriptFiles().add("ZODB.js");
		file.getStyleFiles().add("ZODB.css");
		file.getScriptFiles().add("test.js");

		StringBuilder body = new StringBuilder();
		body.append("<div>");
		body.append("\n");
		
		body.append("<input type=\"button\" value=\"Test getDebugCookieValue\" onclick=\"ZODB.test.testGetDebugCookieValueImpl();\"/>");
		body.append("<br/>\n");
		body.append("<input type=\"button\" value=\"Test getTestURLParameterValue\" onclick=\"ZODB.test.testGetTestURLParameterValueImpl();\"/>");
		body.append("<br/>\n");
		body.append("<input type=\"button\" value=\"Test executeXMLHttpRequest\" onclick=\"ZODB.test.testExecuteXMLHttpRequestImpl();\"/>");
		body.append("<br/>\n");
		body.append("<input type=\"button\" value=\"Test getModel\" onclick=\"ZODB.test.testGetModelCallbackImpl();\"/>");
		body.append("<br/>\n");
		body.append("<input type=\"button\" value=\"Test getDataForClass\" onclick=\"ZODB.test.testGetDataForClassCallbackImpl();\"/>");
		body.append("<br/>\n");
		body.append("<input type=\"button\" value=\"Test executeGetRequest\" onclick=\"ZODB.test.testExecuteGetRequestCallbackImpl();\"/>");
		body.append("<br/>\n");
		body.append("<input type=\"button\" value=\"Test initializeSelect\" onclick=\"ZODB.test.testInitializeSelectImpl();\"/>");
		body.append("<div id=\"testDiv\">");
		body.append("<br/>\n");

		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
}
