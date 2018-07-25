package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class TestHtml extends HtmlResource {
	public TestHtml() {
		setTitle("ZSDS - Test");
		
		getScriptFiles().add("zsds.js");
		getStyleFiles().add("zsds.css");
		
		ZStringBuilder script = new ZStringBuilder();
		script.append("<script type=\"text/javascript\">\n");
		script.append("ZSDS.test = {};\n");
		script.append("ZSDS.test.response = \"\";\n");
		script.append("ZSDS.test.sendRequest = function() {\n");
		script.append("    var elem = window.document.getElementById(\"request\");\n");
		script.append("    if (elem!=null && elem.value.length>0) {\n");
		script.append("        ZSDS.xhr.postJSON(\"handleDialogRequest.json\",elem.value,ZSDS.test.sendRequestCallback,ZSDS.test.sendRequestCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.test.sendRequestCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZSDS.xhr.parseResponseJSON(response);\n");
		script.append("    ZSDS.test.showResponse(object);\n");
		script.append("};\n");
		script.append("ZSDS.test.focusInput = function() {\n");
		script.append("    var elem = window.document.getElementById(\"input\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.blur();\n");
		script.append("        elem.focus();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.test.showResponse = function(object) {\n");
		script.append("    var elem = window.document.getElementById(\"response\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        var response = JSON.stringify(object,null,2);\n");
		script.append("        elem.value = response;\n");
		script.append("        elem.scrollTop = elem.scrollHeight;\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.test.onload = function() {\n");
		script.append("    ZSDS.dom.bindEnterFunctionToElementId(\"input\",ZSDS.test.sendRequest);\n");
		script.append("    ZSDS.test.focusInput();\n");
		script.append("};\n");
		script.append("</script>\n");
		getHeadElements().add(script);
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<div>\n");
		html.append("<a href=\"index.html\">Index</a>");
		html.append("<br />\n");
		html.append("<input type=\"text\" id=\"prompt\" style=\"width: 300px;\"/>\n");
		html.append("<br />\n");
		html.append("<input type=\"text\" id=\"input\" style=\"width: 300px;\"/>\n");
		html.append("<br />\n");
		html.append("<textarea id=\"request\"></textarea>\n");
		html.append("<br />\n");
		html.append("<input type=\"button\" value=\"send\" onclick=\"ZSDS.test.sendRequest();\" />\n");
		html.append("<br />\n");
		html.append("<textarea id=\"response\"></textarea>\n");
		html.append("</div>\n");

		setOnload("ZSDS.test.onload();");

		getBodyElements().add(html);
	}
}
