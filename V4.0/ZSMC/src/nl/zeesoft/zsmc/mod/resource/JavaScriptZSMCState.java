package nl.zeesoft.zsmc.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsmc.mod.ModZSMC;
import nl.zeesoft.zsmc.mod.handler.JsonZSMCStateHandler;

public class JavaScriptZSMCState {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		String path = "../" + ModZSMC.NAME + JsonZSMCStateHandler.PATH;
		
		script.append("var ZSMC = ZSMC || {};\n");
		script.append("ZSMC.state = ZSMC.state || {};\n");
		script.append("ZSMC.state.obj = null;\n");
		script.append("ZSMC.state.refresh = function() {\n");
		script.append("    ZODB.xhr.getJSON(\"" + path + "\",ZSMC.state.refreshCallback,ZSMC.state.refreshCallback);\n");
		script.append("};\n");
		script.append("ZSMC.state.refreshCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (ZSMC.state.obj==null) {\n");
		script.append("        ZSMC.state.obj = JSON.parse(response);\n");
		script.append("        var sel = window.document.getElementById(\"selector\");\n");
		script.append("        if (sel!=null) {\n");
		script.append("            for (var num in ZSMC.state.obj.confabulators) {\n");
		script.append("                var opt = document.createElement(\"option\");\n");
		script.append("                opt.value = ZSMC.state.obj.confabulators[num].name;\n");	
		script.append("                opt.innerHTML = ZSMC.state.obj.confabulators[num].name;\n");
		script.append("                sel.appendChild(opt);\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    var elem = window.document.getElementById(\"state\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.innerHTML = JSON.stringify(object,null,2);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSMC.state.retrain = function() {\n");
		script.append("    var elem = window.document.getElementById(\"retrainCheck\");\n");
		script.append("    if (elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        var sel = window.document.getElementById(\"selector\");\n");
		script.append("        var request = {};\n");
		script.append("        request.action = \"retrain\";\n");
		script.append("        request.name = sel.value;\n");
		script.append("        ZODB.xhr.postJSON(\"" + path + "\",request,ZSMC.state.defaultCallback,ZSMC.state.defaultCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSMC.state.reinitialize = function() {\n");
		script.append("    var elem = window.document.getElementById(\"reinitializeCheck\");\n");
		script.append("    if (elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        var request = {};\n");
		script.append("        request.action = \"reinitialize\";\n");
		script.append("        ZODB.xhr.postJSON(\"" + path + "\",request,ZSMC.state.defaultCallback,ZSMC.state.defaultCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSMC.state.defaultCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else {\n");
		script.append("        ZSMC.state.refresh();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSMC.state.onload = function() {\n");
		script.append("    ZSMC.state.refresh();\n");
		script.append("};\n");
		
		return script;
	}
}
