package nl.zeesoft.zodb.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.mod.ModZODB;
import nl.zeesoft.zodb.mod.handler.JsonZODBLanguagesHandler;

public class JavaScriptZODBLanguages {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		String path = "../" + ModZODB.NAME + JsonZODBLanguagesHandler.PATH;
		
		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.lang = ZODB.lang || {};\n");
		script.append("ZODB.lang.languages = {};\n");
		script.append("ZODB.lang.get = function() {\n");
		script.append("    ZODB.xhr.getJSON(\"" + path + "\",ZODB.lang.getCallback,ZODB.lang.getCallback);\n");
		script.append("};\n");
		script.append("ZODB.lang.getCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = JSON.parse(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else if (object.errors) {\n");
		script.append("        alert(object.errors[0]);\n");
		script.append("    } else {\n");
		script.append("        ZODB.lang.languages = object;\n");
		script.append("        ZODB.lang.gotLanguages();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.lang.gotLanguages = function() {\n");
		script.append("};\n");
		script.append("ZODB.lang.get();\n");
		
		return script;
	}
}
