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
		script.append("ZSDS.test.request = null;\n");
		script.append("ZSDS.test.getRequest = function() {\n");
		script.append("    ZSDS.xhr.getJSON(\"testDialogRequest.json\",ZSDS.test.getRequestCallback,ZSDS.test.getRequestCallback);\n");
		script.append("};\n");
		script.append("ZSDS.test.getRequestCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZSDS.xhr.parseResponseJSON(response);\n");
		script.append("    ZSDS.test.request = object;\n");
		script.append("    ZSDS.test.refreshRequest();\n");
		script.append("};\n");
		script.append("ZSDS.test.sendRequest = function() {\n");
		script.append("    ZSDS.test.refreshRequest();\n");
		script.append("    var elem = window.document.getElementById(\"request\");\n");
		script.append("    if (elem!=null && elem.value.length>0) {\n");
		script.append("        elemResponse = window.document.getElementById(\"response\");\n");
		script.append("        if (elemResponse!=null) {\n");
		script.append("            elemResponse.value = \"Sending request ...\";\n");
		script.append("        }\n");
		script.append("        ZSDS.test.request = JSON.parse(elem.value);\n");
		script.append("        ZSDS.xhr.postJSON(\"dialogRequestHandler.json\",ZSDS.test.request,ZSDS.test.sendRequestCallback,ZSDS.test.sendRequestCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.test.sendRequestCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    console.log(xhr.responseText);\n");
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
		script.append("ZSDS.test.refreshRequest = function() {\n");
		script.append("    var elem = window.document.getElementById(\"request\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        var elemPrompt = window.document.getElementById(\"prompt\");\n");
		script.append("        var elemInput = window.document.getElementById(\"input\");\n");
		script.append("        if (ZSDS.test.request!=null) {\n");
		script.append("            ZSDS.test.request.prompt = elemPrompt.value;\n");
		script.append("            ZSDS.test.request.input = elemInput.value;\n");
		script.append("            elem.value = JSON.stringify(ZSDS.test.request,null,2);\n");
		script.append("         } else {\n");
		script.append("             elem.value = \"\";\n");
		script.append("         }\n");
		script.append("    }\n");
		script.append("}\n");
		script.append("ZSDS.test.showResponse = function(object) {\n");
		script.append("    var elem = window.document.getElementById(\"response\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        if (typeof(object.contextOutputs)!==\"undefined\" && typeof(object.contextOutputs[0])!==\"undefined\") {\n");
		script.append("            var elemOutput = window.document.getElementById(\"output\");\n");
		script.append("            var elemPrompt = window.document.getElementById(\"prompt\");\n");
		script.append("            var elemInput = window.document.getElementById(\"input\");\n");
		script.append("            if (elemOutput!=null && elemPrompt!=null && elemInput!=null) {\n");
		script.append("                elemOutput.value = object.contextOutputs[0].output;\n");
		script.append("                elemPrompt.value = object.contextOutputs[0].prompt;\n");
		script.append("                elemInput.value = \"\";\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        var oriMasterContext = ZSDS.test.request.masterContext;\n");
		script.append("        var oriContext = ZSDS.test.request.context;\n");
		script.append("        if (typeof(object.responseLanguages)!==\"undefined\" && typeof(object.responseLanguages[0])!==\"undefined\") {\n");
		script.append("            ZSDS.test.request.language = object.responseLanguages[0].symbol;\n");
		script.append("        }\n");
		script.append("        if (typeof(object.responseMasterContexts)!==\"undefined\" && typeof(object.responseMasterContexts[0])!==\"undefined\") {\n");
		script.append("            ZSDS.test.request.masterContext = object.responseMasterContexts[0].symbol;\n");
		script.append("        }\n");
		script.append("        if (typeof(object.responseContexts)!==\"undefined\" && typeof(object.responseContexts[0])!==\"undefined\") {\n");
		script.append("            ZSDS.test.request.context = object.responseContexts[0].symbol;\n");
		script.append("        }\n");
		script.append("        if (oriMasterContext!=ZSDS.test.request.masterContext || oriContext!=ZSDS.test.request.context) {\n");
		script.append("            ZSDS.test.request.dialogVariableValues = [];\n");
		script.append("        } else if (\n");
		script.append("            typeof(object.contextOutputs)!==\"undefined\" &&\n");
		script.append("            typeof(object.contextOutputs[0])!==\"undefined\" &&\n");
		script.append("            typeof(object.contextOutputs[0].dialogVariableValues)!==\"undefined\" &&\n");
		script.append("            typeof(object.contextOutputs[0].dialogVariableValues[0])!==\"undefined\"\n");
		script.append("            ) {\n");
		script.append("            ZSDS.test.request.dialogVariableValues = object.contextOutputs[0].dialogVariableValues;\n");
		script.append("        }\n");
		script.append("        if (\n");
		script.append("            typeof(object.contextOutputs)!==\"undefined\" &&\n");
		script.append("            typeof(object.contextOutputs[0])!==\"undefined\" &&\n");
		script.append("            object.contextOutputs[0].promptVariable==\"nextDialog\"\n");
		script.append("            ) {\n");
		script.append("            ZSDS.test.request.dialogVariableValues = [];\n");
		script.append("        }\n");
		script.append("        ZSDS.test.refreshRequest();\n");
		script.append("        var response = JSON.stringify(object,null,2);\n");
		script.append("        response = response.replace(/\\\\n/g,\"\\n\");\n");
		script.append("        elem.value = response;\n");
		script.append("        elem.scrollTop = elem.scrollHeight;\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.test.onload = function() {\n");
		script.append("    ZSDS.test.getRequest();\n");
		script.append("    ZSDS.dom.bindEnterFunctionToElementId(\"input\",ZSDS.test.sendRequest);\n");
		script.append("    ZSDS.test.focusInput();\n");
		script.append("};\n");
		script.append("</script>\n");
		getHeadElements().add(script);
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<div>\n");
		html.append("<a href=\"index.html\">Index</a>");
		
		html.append("<div>\n");
		html.append("<table>\n");
		html.append("<tbody>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("Output");
		html.append("</td>\n");
		html.append("<td>");
		html.append("<input type=\"text\" id=\"output\" style=\"width: 300px;\" DISABLED/>");
		html.append("</td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>");
		html.append("Prompt");
		html.append("</td>\n");
		html.append("<td>");
		html.append("<input type=\"text\" id=\"prompt\" onchange=\"ZSDS.test.refreshRequest();\" style=\"width: 300px;\"/>");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("Input");
		html.append("</td>\n");
		html.append("<td>");
		html.append("<input type=\"text\" id=\"input\" value=\"hello\" onchange=\"ZSDS.test.refreshRequest();\" style=\"width: 300px;\"/>");
		html.append("<input type=\"button\" value=\"send\" onclick=\"ZSDS.test.sendRequest();\" />\n");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		html.append("<div>\n");
		html.append("Request<br />\n");
		html.append("<textarea id=\"request\"></textarea>\n");
		html.append("Response<br />\n");
		html.append("<textarea id=\"response\"></textarea>\n");
		html.append("</div>\n");
		
		html.append("</div>\n");

		setOnload("ZSDS.test.onload();");

		getBodyElements().add(html);
	}
}
