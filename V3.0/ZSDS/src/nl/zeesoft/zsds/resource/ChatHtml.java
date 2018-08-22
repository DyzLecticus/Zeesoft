package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class ChatHtml extends HtmlResource {
	public ChatHtml() {
		setTitle("ZSDS - Chat");
		
		getScriptFiles().add("zsds.js");
		getScriptFiles().add("apiClient.js");
		getStyleFiles().add("zsds.css");
		
		ZStringBuilder script = new ZStringBuilder();
		script.append("<script type=\"text/javascript\">\n");
		script.append("ZSDS.chat = {};\n");
		script.append("ZSDS.chat.log = \"\";\n");
		script.append("ZSDS.chat.client = new ZSDS.api.Client();\n");
		script.append("ZSDS.chat.sendRequest = function() {\n");
		script.append("    var elemInput = window.document.getElementById(\"input\");\n");
		script.append("    if (ZSDS.chat.client.workingRequest!=null) {\n");
		script.append("        ZSDS.chat.log += \"<<< \" + elemInput.value + \"\\n\";\n");
		script.append("        var elem = window.document.getElementById(\"log\");\n");
		script.append("        elem.value = ZSDS.chat.log;\n");
		script.append("        elem.scrollTop = elem.scrollHeight;\n");
		script.append("        ZSDS.chat.client.workingRequest.input = elemInput.value;\n");
		script.append("        ZSDS.xhr.postJSON(\"dialogRequestHandler.json\",ZSDS.chat.client.workingRequest,ZSDS.chat.sendRequestCallback,ZSDS.chat.sendRequestCallback);\n");
		script.append("        elemInput.value = \"\";\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.chat.sendRequestCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var obj = JSON.parse(response);\n");
		script.append("    if (!obj.code) {\n");
		script.append("        ZSDS.chat.client.processRequestResponse(obj);\n");
		script.append("        if (ZSDS.chat.client.response.output.length>0) {\n");
		script.append("            ZSDS.chat.log += \">>> \" + ZSDS.chat.client.response.output + \"\\n\";\n");
		script.append("        }\n");
		script.append("        if (ZSDS.chat.client.response.prompt.length>0) {\n");
		script.append("            ZSDS.chat.log += \">>> \" + ZSDS.chat.client.response.prompt + \"\\n\";\n");
		script.append("        }\n");
		script.append("    } else {\n");
		script.append("        ZSDS.chat.log += \">>> \" + obj.error + \"\\n\";\n");
		script.append("    }\n");
		script.append("    var elem = window.document.getElementById(\"log\");\n");
		script.append("    elem.value = ZSDS.chat.log;\n");
		script.append("    elem.scrollTop = elem.scrollHeight;\n");
		script.append("};\n");
		script.append("ZSDS.chat.focusInput = function() {\n");
		script.append("    var elem = window.document.getElementById(\"input\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.blur();\n");
		script.append("        elem.focus();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.chat.onload = function() {\n");
		script.append("    ZSDS.dom.bindEnterFunctionToElementId(\"input\",ZSDS.chat.sendRequest);\n");
		script.append("    ZSDS.chat.focusInput();\n");
		script.append("};\n");
		script.append("</script>\n");
		getHeadElements().add(script);
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"index.html\">Index</a>");
		html.append("<hr />");
		html.append("<div>\n");

		html.append("<br />\n");
		html.append("<div>\n");
		html.append("Log<br />\n");
		html.append("<textarea id=\"log\"></textarea>\n");
		html.append("</div>\n");

		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("Input");
		html.append("</td>\n");
		html.append("<td>");
		html.append("<input type=\"text\" id=\"input\" style=\"width: 400px;\"/>");
		html.append("<input type=\"button\" value=\"send\" onclick=\"ZSDS.chat.sendRequest();\" />\n");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		html.append("</div>\n");

		setOnload("ZSDS.chat.onload();");

		getBodyElements().add(html);
	}
}
