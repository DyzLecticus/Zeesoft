package nl.zeesoft.zevt.app.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.app.AppZEVT;
import nl.zeesoft.zevt.app.handler.JsonZEVTRequestHandler;

public class JavaScriptZEVTEntityTranslator {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		String path = "../" + AppZEVT.NAME + JsonZEVTRequestHandler.PATH;
		
		script.append("var ZEVT = ZEVT || {};\n");
		script.append("ZEVT.et = ZEVT.et || {};\n");
		script.append("ZEVT.et.translate = function() {\n");
		script.append("    elem = window.document.getElementById(\"sequence\");\n");
		script.append("    if (elem!=null && elem.value.length>0) {\n");
		script.append("        var request = {};\n");
		script.append("        request.sequence = elem.value;\n");
		script.append("        ZEVT.et.handleRequest(request);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZEVT.et.retranslate = function() {\n");
		script.append("    elem = window.document.getElementById(\"translation\");\n");
		script.append("    if (elem!=null && elem.value.length>0) {\n");
		script.append("        var request = {};\n");
		script.append("        request.entityValueTranslation = elem.value;\n");
		script.append("        ZEVT.et.handleRequest(request);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZEVT.et.handleRequest = function(request) {\n");
		script.append("    ZODB.xhr.postJSON(\"" + path + "\",request,ZEVT.et.defaultCallback,ZEVT.et.defaultCallback);\n");
		script.append("};\n");
		script.append("ZEVT.et.defaultCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else {\n");
		script.append("        if (object.entityValueTranslation) {\n");
		script.append("            elem = window.document.getElementById(\"translation\");\n");
		script.append("            if (elem!=null) {\n");
		script.append("                elem.value = object.entityValueTranslation;\n");
		script.append("            }\n");
		script.append("        } else if (object.sequence) {\n");
		script.append("            elem = window.document.getElementById(\"sequence\");\n");
		script.append("            if (elem!=null) {\n");
		script.append("                elem.value = object.sequence;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZEVT.et.onload = function() {\n");
		script.append("    var elem = window.document.getElementById(\"sequence\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.blur();\n");
		script.append("        elem.focus();\n");
		script.append("    }\n");
		script.append("    ZODB.dom.bindEnterFunctionToElementId(\"sequence\",ZEVT.et.translate);\n");
		script.append("    ZODB.dom.bindEnterFunctionToElementId(\"translation\",ZEVT.et.retranslate);\n");
		script.append("};\n");
		
		return script;
	}
}
