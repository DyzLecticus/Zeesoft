package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class BaseJavaScript {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		script.append("var ZSDS = ZSDS || {};\n");
		
		script.append("\n");
		script.append("ZSDS.xhr = {};\n");
		script.append("ZSDS.xhr.postJSON = function(url,json,successCallback,errorCallback) {\n");
		script.append("    var xhr = new XMLHttpRequest();\n");
		script.append("    xhr.onreadystatechange = function() {\n");
		script.append("        if (this.readyState == 4) {\n");
		script.append("            if (this.status == 200) {\n");
		script.append("                successCallback(this);\n");
		script.append("            } else if (errorCallback!=null) {\n");
		script.append("                errorCallback(this);\n");
		script.append("            } else {\n");
		script.append("                ZSDS.xhr.defaultErrorCallback(this);\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    };\n");
		script.append("    xhr.open(\"POST\",url,true);\n");
		script.append("    xhr.setRequestHeader(\"Content-type\",\"application/json\");\n");
		script.append("    xhr.send(JSON.stringify(json));\n");
		script.append("    return xhr;\n");
		script.append("};\n");
		script.append("ZSDS.xhr.defaultErrorCallback = function(xhr) {\n");
		script.append("    console.error(\"Post request response error: \" + xhr.status + \" \" + xhr.responseText);\n");
		script.append("};\n");
		script.append("ZSDS.xhr.parseResponseJSON = function(text) {\n");
		script.append("    var json = JSON.parse(text);\n");
		script.append("    json = ZSDS.xhr.correctJSONValues(json);\n");
		script.append("    return json;\n");
		script.append("};\n");
		script.append("ZSDS.xhr.correctJSONValues = function(value) {\n");
		script.append("    if (typeof(value)==\"object\") {\n");
		script.append("        var copy = {};\n");
		script.append("        for (var id in value) {\n");
		script.append("            copy[id] = ZSDS.xhr.correctJSONValues(value[id]);\n");
		script.append("        }\n");
		script.append("        value = copy;\n");
		script.append("    } else if (typeof(value)==\"string\") {\n");
		script.append("        value = value.replace(/<NEWLINE>/g,\"\\n\");\n");
		script.append("        value = value.replace(/<QUOTE>/g,\"\\\"\");\n");
		script.append("    }\n");
		script.append("    return value;\n");
		script.append("};\n");
		
		script.append("\n");
		script.append("ZSDS.dom = {};\n");
		script.append("ZSDS.dom.enterFunctions = {};\n");
		script.append("ZSDS.dom.cursorUpFunctions = {};\n");
		script.append("ZSDS.dom.cursorDownFunctions = {};\n");
		script.append("ZSDS.dom.bindEnterFunctionToElementId = function(id,bindFunction) {\n");
		script.append("    ZSDS.dom.enterFunctions[id] = bindFunction;\n");
		script.append("};\n");
		script.append("ZSDS.dom.bindCursorUpFunctionToElementId = function(id,bindFunction) {\n");
		script.append("    ZSDS.dom.cursorUpFunctions[id] = bindFunction;\n");
		script.append("};\n");
		script.append("ZSDS.dom.bindCursorDownFunctionToElementId = function(id,bindFunction) {\n");
		script.append("    ZSDS.dom.cursorDownFunctions[id] = bindFunction;\n");
		script.append("};\n");
		script.append("ZSDS.dom.handleKeyPress = function(event) {\n");
		script.append("    var elem = event.target || event.srcElement;\n");
		script.append("    if (elem!=null && elem.id!=null) {\n");
		script.append("        if (event.keyCode==13 && ZSDS.dom.enterFunctions[elem.id]!=null) {\n");
		script.append("            ZSDS.dom.enterFunctions[elem.id]();\n");
		script.append("        } else if (event.keyCode==38 && ZSDS.dom.cursorUpFunctions[elem.id]!=null) {\n");
		script.append("            ZSDS.dom.cursorUpFunctions[elem.id]();\n");
		script.append("        } else if (event.keyCode==40 && ZSDS.dom.cursorDownFunctions[elem.id]!=null) {\n");
		script.append("            ZSDS.dom.cursorDownFunctions[elem.id]();\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("window.document.onkeypress = ZSDS.dom.handleKeyPress;\n");
		
		return script;
	}
}
