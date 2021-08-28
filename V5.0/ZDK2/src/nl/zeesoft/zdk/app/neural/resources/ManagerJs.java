package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.app.resource.Resource;

public class ManagerJs extends Resource {
	public static String	CONFIRM_RESET_MESSAGE	= "This action will reset the network. Are you sure?";
	
	@Override
	protected void render(StringBuilder r) {
		append(r, "var manager = manager || {};");
		append(r, "manager.editorId = \"configEditor\";");
		renderLoadApp(r);
		renderReset(r);
		renderLoadedConfig(r);
		renderPost(r);
		renderPostRequest(r);
		renderParseConfig(r);
		renderSetCaret(r);
	}
	
	protected void renderLoadApp(StringBuilder r) {
		append(r, "manager.loadApp = () => {");
		append(r, "    loadApp();");
		append(r, "};");
	}
	
	protected void renderReset(StringBuilder r) {
		append(r, "manager.reset = () => {");
		append(r, "    if (confirm(\"" + CONFIRM_RESET_MESSAGE + "\")) {");
		append(r, "        var request = new HttpRequest(\"POST\",\"" + NetworkStateTextHandler.PATH + "\");");
		append(r, "        request.body = \"RESET\";");
		append(r, "        request.execute((xhr) => {");
		append(r, "            networkStateLoader.refresh({});");
		append(r, "        });");
		append(r, "    }");
		append(r, "};");
	}

	protected void renderLoadedConfig(StringBuilder r) {
		append(r, "changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "    if (key == \"networkConfig\") {");
		append(r, "        manager.loadedConfig(newValue);");
		append(r, "    }");
		append(r, "});");
		append(r, "manager.loadedConfig = (json) => {");
		append(r, "    var elem = window.document.getElementById(manager.editorId);");
		append(r, "    if (elem) {");
		append(r, "        elem.value = JSON.stringify(json, null, 2);");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderPost(StringBuilder r) {
		append(r, "manager.post = () => {");
		append(r, "    var elem = window.document.getElementById(manager.editorId);");
		append(r, "    if (elem) {");
		append(r, "        var body = manager.parseConfig();");
		append(r, "        if (body && confirm(\"" + CONFIRM_RESET_MESSAGE + "\")) {");
		append(r, "            manager.postRequest(body);");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderParseConfig(StringBuilder r) {
		append(r, "manager.parseConfig = () => {");
		append(r, "    var body = null;");
		append(r, "    var elem = window.document.getElementById(manager.editorId);");
		append(r, "    if (elem) {");
		append(r, "        try {");
		append(r, "            body = JSON.parse(elem.value);");
		append(r, "            body = JSON.stringify(body);");
		append(r, "        } catch(error) {");
		append(r, "            alert(error);");
		append(r, "            var split = error.toString().split(\"position \");");
		append(r, "            if (split && split.length==2) {");
		append(r, "                console.log(split[1]);");
		append(r, "                console.log(split[1].length);");
		append(r, "                manager.setCaret(parseInt(split[1],10));");
		append(r, "            }");
		append(r, "        }");
		append(r, "    }");
		append(r, "    return body;");
		append(r, "};");
	}
	
	protected void renderPostRequest(StringBuilder r) {
		append(r, "manager.postRequest = (body) => {");
		append(r, "    var request = new HttpRequest(\"POST\",\"" + NetworkConfigJsonHandler.PATH + "\");");
		append(r, "    request.body = body;");
		append(r, "    request.errorCallback = (xhr) => {");
		append(r, "        alert(xhr.response);");
		append(r, "    };");
		append(r, "    request.execute((xhr) => {");
		append(r, "        networkStateLoader.refresh({});");
		append(r, "        networkConfigLoader.refresh({});");
		append(r, "    });");
		append(r, "};");
	}
	
	protected void renderSetCaret(StringBuilder r) {
		append(r, "manager.setCaret = (pos) => {");
		append(r, "    var elem = window.document.getElementById(manager.editorId);");
		append(r, "    if (elem && elem.setSelectionRange) {");
		append(r, "        elem.focus();");
		append(r, "        elem.setSelectionRange(pos, pos);");
		append(r, "    }");
		append(r, "};");
	}
}
