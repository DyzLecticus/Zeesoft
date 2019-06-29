package nl.zeesoft.zodb.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.mod.ModZODB;
import nl.zeesoft.zodb.mod.handler.JsonZODBStateHandler;

public class JavaScriptZODBStateManager {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		String path = "../" + ModZODB.NAME + JsonZODBStateHandler.PATH;
		
		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.sm = ZODB.sm || {};\n");
		script.append("ZODB.sm.indexes = {};\n");
		script.append("ZODB.sm.selectedId = 0;\n");
		script.append("ZODB.sm.get = function() {\n");
		script.append("    var request = {};\n");
		script.append("    ZODB.xhr.postJSON(\"" + path + "\",request,ZODB.sm.getCallback,ZODB.sm.getCallback);\n");
		script.append("};\n");
		script.append("ZODB.sm.getCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.code==\"503\") {\n");
		script.append("        setTimeout(function() { ZODB.sm.get(); },1000);\n");
		script.append("    } else if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else if (object.errors) {\n");
		script.append("        alert(object.errors[0]);\n");
		script.append("    } else if (object.state) {\n");
		script.append("        var elem1 = window.document.getElementById(\"restart\");\n");
		script.append("        var elem2 = window.document.getElementById(\"restartCheck\");\n");
		script.append("        if (object.state==\"" + Database.STAT_OPEN + "\") {\n");
		script.append("            if (elem1!=null) {\n");
		script.append("                elem1.disabled = false;\n");
		script.append("            }\n");
		script.append("            if (elem2!=null) {\n");
		script.append("                elem2.disabled = false;\n");
		script.append("            }\n");
		script.append("        } else {\n");
		script.append("            if (elem1!=null) {\n");
		script.append("                elem1.disabled = true;\n");
		script.append("            }\n");
		script.append("            if (elem2!=null) {\n");
		script.append("                elem2.disabled = true;\n");
		script.append("            }\n");
		script.append("            setTimeout(function() { ZODB.sm.get(); },1000);\n");
		script.append("        }\n");
		script.append("        var elem = window.document.getElementById(\"state\");\n");
		script.append("        if (elem!=null) {\n");
		script.append("            elem.innerHTML = object.state;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.sm.restart = function(xhr) {\n");
		script.append("    var elem = window.document.getElementById(\"restartCheck\");\n");
		script.append("    if (elem!=null && elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        var request = {};\n");
		script.append("        request.restart = true;\n");
		script.append("        ZODB.xhr.postJSON(\"" + path + "\",request,ZODB.sm.getCallback,ZODB.sm.getCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.sm.onload = function() {\n");
		script.append("    ZODB.sm.get();\n");
		script.append("};\n");
		
		return script;
	}
}
