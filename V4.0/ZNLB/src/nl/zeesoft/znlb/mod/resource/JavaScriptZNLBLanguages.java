package nl.zeesoft.znlb.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.znlb.mod.handler.JsonZNLBLanguagesHandler;

public class JavaScriptZNLBLanguages {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		String path = "../" + ModZNLB.NAME + JsonZNLBLanguagesHandler.PATH;
		
		script.append("var ZNLB = ZNLB || {};\n");
		script.append("ZNLB.lang = ZNLB.lang || {};\n");
		script.append("ZNLB.lang.languages = {};\n");
		script.append("ZNLB.lang.get = function() {\n");
		script.append("    ZODB.xhr.getJSON(\"" + path + "\",ZNLB.lang.getCallback,ZNLB.lang.getCallback);\n");
		script.append("};\n");
		script.append("ZNLB.lang.getCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = JSON.parse(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else if (object.errors) {\n");
		script.append("        alert(object.errors[0]);\n");
		script.append("    } else {\n");
		script.append("        ZNLB.lang.languages = object;\n");
		script.append("        ZNLB.lang.gotLanguages();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZNLB.lang.gotLanguages = function() {\n");
		script.append("};\n");
		script.append("ZNLB.lang.get();\n");
		
		return script;
	}
}
