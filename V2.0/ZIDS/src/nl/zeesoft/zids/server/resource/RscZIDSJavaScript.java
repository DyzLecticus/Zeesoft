package nl.zeesoft.zids.server.resource;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlProperty;



public class RscZIDSJavaScript extends RscJavaScriptObject {
	@Override
	public StringBuilder toStringBuilder() {
		StringBuilder script = new StringBuilder();
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
		script.append("    xhr.send(json);\n");
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
		script.append("ZIDS.xhr.GetRequestFilter = function(property,operator,value,invert) {\n");
		script.append("    this.property = property;\n");
		script.append("    this.operator = operator;\n");
		script.append("    this.value = value;\n");
		script.append("    if (invert) {\n");
		script.append("        this.invert = invert;\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZIDS.xhr.GetRequest = function(className,id) {\n");
		script.append("    this.type = \"get\";\n");
		script.append("    this.className = className;\n");
		script.append("    this.start = 0;\n");
		script.append("    this.limit = 0;\n");
		script.append("    this.orderBy = \"\";\n");
		script.append("    this.orderAscending = true;\n");
		script.append("    this.properties = [\"*\"];\n");
		script.append("    this.filters = [];\n");
		script.append("    if (id) {\n");
		script.append("        var filter = new ZIDS.xhr.GetRequestFilter(\"id\",\"equals\",id);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZIDS.xhr.AddRequest = function(className) {\n");
		script.append("    this.type = \"add\";\n");
		script.append("    this.className = className;\n");
		script.append("    this.objects = [];\n");
		script.append("};\n");
		script.append("ZIDS.xhr.UpdateRequest = function(className,id) {\n");
		script.append("    this.type = \"update\";\n");
		script.append("    this.className = className;\n");
		script.append("    this.getRequest = new ZIDS.xhr.GetRequest(className,id);\n");
		script.append("    this.objects = [];\n");
		script.append("};\n");
		script.append("ZIDS.xhr.RemoveRequest = function(className,id) {\n");
		script.append("    this.type = \"remove\";\n");
		script.append("    this.className = className;\n");
		script.append("    this.getRequest = new ZIDS.xhr.GetRequest(className,id);\n");
		script.append("};\n");
		script.append("ZIDS.xhr.RequestObject = function() {\n");
		script.append("    this.object = {};\n");
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
		script.append("ZIDS.data = {};\n");
		script.append("ZIDS.data.model = {\n");
		boolean firstClass = true;
		for (MdlClass cls: DbConfig.getInstance().getModel().getClasses()) {
			if (!cls.isAbstr()) {
				if (!firstClass) {
					script.append(",\n");
				}
				firstClass = false;
				script.append("    \"");
				script.append(cls.getFullName());
				script.append("\": {\"properties\": [");
				boolean firstProp = true;
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					if (!firstProp) {
						script.append(",");
					}
					firstProp = false;
					script.append("\"");
					script.append(prop.getName());
					script.append("\"");
				}
				script.append("]}");
			}
		}
		script.append("\n};\n");
		script.append("ZIDS.data.getNewDbDataObjectForClass = function(className) {\n");
		script.append("    var obj = {};\n");
		script.append("    if (ZIDS.data.model[className]) {\n");
		script.append("        for (var i = 0; i<ZIDS.data.model[className].properties.length; i++) {\n");
		script.append("            if (ZIDS.data.model[className].properties[i]!=\"id\") {\n");
		script.append("                obj[ZIDS.data.model[className].properties[i]] = null;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    return obj;\n");
		script.append("};\n");
		script.append("ZIDS.data.getNewRequestObjectForClass = function(className) {\n");
		script.append("    var obj = new ZIDS.xhr.RequestObject();\n");
		script.append("    if (ZIDS.data.model[className]) {\n");
		script.append("        obj.object = ZIDS.data.getNewDbDataObjectForClass(className);\n");
		script.append("    }\n");
		script.append("    return obj;\n");
		script.append("};\n");
		
		script.append("\n");
		script.append("ZIDS.dialog = {};\n");
		script.append("ZIDS.dialog.Request = function(sessionId,type,input,context) {\n");
		script.append("    this.sessionId = \"\" + sessionId;\n");
		script.append("    this.type = type;\n");
		script.append("    if (context) {\n");
		script.append("        this.context = context;\n");
		script.append("    }\n");
		script.append("    if (input) {\n");
		script.append("        this.input = input;\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZIDS.dialog.getNewSessionProcessInputRequest = function(sessionId,input) {\n");
		script.append("    return new ZIDS.dialog.Request(sessionId,\"SessionProcessInputRequest\",input);\n");
		script.append("};\n");
		script.append("ZIDS.dialog.getNewSessionEndedRequest = function(sessionId) {\n");
		script.append("    return new ZIDS.dialog.Request(sessionId,\"SessionEndedRequest\");\n");
		script.append("};\n");

		return script;
	}
}
