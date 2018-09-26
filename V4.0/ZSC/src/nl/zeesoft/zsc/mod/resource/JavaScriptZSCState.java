package nl.zeesoft.zsc.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsc.mod.ModZSC;
import nl.zeesoft.zsc.mod.handler.JsonZSCStateHandler;

public class JavaScriptZSCState {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		String path = "../" + ModZSC.NAME + JsonZSCStateHandler.PATH;
		
		script.append("var ZSC = ZSC || {};\n");
		script.append("ZSC.state = ZSC.state || {};\n");
		script.append("ZSC.state.obj = null;\n");
		script.append("ZSC.state.refresh = function() {\n");
		script.append("    ZODB.xhr.getJSON(\"" + path + "\",ZSC.state.refreshCallback,ZSC.state.refreshCallback);\n");
		script.append("};\n");
		script.append("ZSC.state.refreshCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (ZSC.state.obj==null) {\n");
		script.append("        ZSC.state.obj = JSON.parse(response);\n");
		script.append("        var sel = window.document.getElementById(\"selector\");\n");
		script.append("        if (sel!=null) {\n");
		script.append("            for (var num in ZSC.state.obj.confabulators) {\n");
		script.append("                var opt = document.createElement(\"option\");\n");
		script.append("                opt.value = ZSC.state.obj.confabulators[num].name;\n");	
		script.append("                opt.innerHTML = ZSC.state.obj.confabulators[num].name;\n");
		script.append("                sel.appendChild(opt);\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    var elem = window.document.getElementById(\"state\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.innerHTML = JSON.stringify(object,null,2);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSC.state.retrain = function() {\n");
		script.append("    var elem = window.document.getElementById(\"retrainCheck\");\n");
		script.append("    if (elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        var sel = window.document.getElementById(\"selector\");\n");
		script.append("        var request = {};\n");
		script.append("        request.action = \"retrain\";\n");
		script.append("        request.name = sel.value;\n");
		script.append("        ZODB.xhr.postJSON(\"" + path + "\",request,ZSC.state.defaultCallback,ZSC.state.defaultCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSC.state.reinitialize = function() {\n");
		script.append("    var elem = window.document.getElementById(\"reinitializeCheck\");\n");
		script.append("    if (elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        var request = {};\n");
		script.append("        request.action = \"reinitialize\";\n");
		script.append("        ZODB.xhr.postJSON(\"" + path + "\",request,ZSC.state.defaultCallback,ZSC.state.defaultCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSC.state.defaultCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else {\n");
		script.append("        ZSC.state.refresh();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSC.state.onload = function() {\n");
		script.append("    ZSC.state.refresh();\n");
		script.append("};\n");
		
		return script;
	}
}
