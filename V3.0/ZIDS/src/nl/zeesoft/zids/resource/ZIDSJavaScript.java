package nl.zeesoft.zids.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class ZIDSJavaScript {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		script.append("var ZIDS = ZIDS || {};\n");
		
		script.append("\n");
		script.append("ZIDS.xhr = {};\n");
		script.append("ZIDS.xhr.postJSON = function(url,json,successCallback,errorCallback) {\n");
		script.append("    var xhr = new XMLHttpRequest();\n");
		script.append("    xhr.onreadystatechange = function() {\n");
		script.append("        if (this.readyState == 4) {\n");
		script.append("            if (this.status == 200) {\n");
		script.append("                successCallback(this);\n");
		script.append("            } else if (errorCallback!=null) {\n");
		script.append("                errorCallback(this);\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    xhr.open(\"POST\",url,true);\n");
		script.append("    xhr.setRequestHeader(\"Content-type\",\"application/json\");\n");
		script.append("    xhr.send(JSON.stringify(json));\n");
		script.append("    return xhr;\n");
		script.append("};\n");
		script.append("ZIDS.xhr.parseResponseJSON = function(text) {\n");
		script.append("    var json = JSON.parse(text);\n");
		script.append("    json = ZIDS.xhr.correctJSONValues(json);\n");
		script.append("    return json;\n");
		script.append("};\n");
		script.append("ZIDS.xhr.correctJSONValues = function(value) {\n");
		script.append("    if (typeof(value)==\"object\") {\n");
		script.append("        var copy = {};\n");
		script.append("        for (var id in value) {\n");
		script.append("            copy[id] = ZIDS.xhr.correctJSONValues(value[id]);\n");
		script.append("        }\n");
		script.append("        value = copy;\n");
		script.append("    } else if (typeof(value)==\"string\") {\n");
		script.append("        value = value.replace(/<NEWLINE>/g,\"\\n\");\n");
		script.append("        value = value.replace(/<QUOTE>/g,\"\\\"\");\n");
		script.append("    }\n");
		script.append("    return value;\n");
		script.append("};\n");
		
		script.append("\n");
		script.append("ZIDS.dom = {};\n");
		script.append("ZIDS.dom.enterFunctions = {};\n");
		script.append("ZIDS.dom.cursorUpFunctions = {};\n");
		script.append("ZIDS.dom.cursorDownFunctions = {};\n");
		script.append("ZIDS.dom.bindEnterFunctionToElementId = function(id,bindFunction) {\n");
		script.append("    ZIDS.dom.enterFunctions[id] = bindFunction;\n");
		script.append("};\n");
		script.append("ZIDS.dom.bindCursorUpFunctionToElementId = function(id,bindFunction) {\n");
		script.append("    ZIDS.dom.cursorUpFunctions[id] = bindFunction;\n");
		script.append("};\n");
		script.append("ZIDS.dom.bindCursorDownFunctionToElementId = function(id,bindFunction) {\n");
		script.append("    ZIDS.dom.cursorDownFunctions[id] = bindFunction;\n");
		script.append("};\n");
		script.append("ZIDS.dom.handleKeyPress = function(event) {\n");
		script.append("    var elem = event.target || event.srcElement;\n");
		script.append("    if (elem!=null && elem.id!=null) {\n");
		script.append("        if (event.keyCode==13 && ZIDS.dom.enterFunctions[elem.id]!=null) {\n");
		script.append("            ZIDS.dom.enterFunctions[elem.id]();\n");
		script.append("        } else if (event.keyCode==38 && ZIDS.dom.cursorUpFunctions[elem.id]!=null) {\n");
		script.append("            ZIDS.dom.cursorUpFunctions[elem.id]();\n");
		script.append("        } else if (event.keyCode==40 && ZIDS.dom.cursorDownFunctions[elem.id]!=null) {\n");
		script.append("            ZIDS.dom.cursorDownFunctions[elem.id]();\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("window.document.onkeypress = ZIDS.dom.handleKeyPress;\n");

		script.append("\n");
		script.append("ZIDS.session = {};\n");
		script.append("ZIDS.session.url = \"sessions.json\";\n");
		script.append("ZIDS.session.id = null;\n");
		script.append("ZIDS.session.open = function() {\n");
		script.append("    var request = {};\n");
		script.append("    request.action = \"open\";\n");
		script.append("    ZIDS.xhr.postJSON(ZIDS.session.url,request,ZIDS.session.openCallback);\n");
		script.append("};\n");
		script.append("ZIDS.session.openCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZIDS.xhr.parseResponseJSON(response);\n");
		script.append("    ZIDS.session.id = object.externalId;\n");
		script.append("};\n");
		script.append("ZIDS.session.send = function(msg,callback) {\n");
		script.append("    var request = {};\n");
		script.append("    request.action = \"input\";\n");
		script.append("    request.externalId = ZIDS.session.id;\n");
		script.append("    request.input = msg;\n");
		script.append("    ZIDS.xhr.postJSON(ZIDS.session.url,request,callback);\n");
		script.append("};\n");
		
		return script;
	}
}
