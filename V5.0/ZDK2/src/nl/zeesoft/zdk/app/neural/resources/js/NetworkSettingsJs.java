package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettings;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettingsJsonHandler;
import nl.zeesoft.zdk.app.resource.Resource;

public class NetworkSettingsJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var networkSettings = networkSettings || {};");
		renderToHtmlTable(r);
		renderToHtmlTableRow(r);
		renderGetFormAsJson(r);
		renderSubmit(r);
	}
	
	protected void renderToHtmlTable(StringBuilder r) {
		append(r, "networkSettings.toHtmlTable = (json) => {");
		append(r, "    var processorLearning = util.objectFromMap(json.processorLearning);");
		append(r, "    var html = \"<table id='networkSettingsForm' class='padded'>\";");
		append(r, "    html += networkSettings.toHtmlTableRow(\"workers\", \"number\", \"Workers\", json.workers);");
		append(r, "    html += networkSettings.toHtmlTableRow(\"initTimeoutMs\", \"number\", \"Initialize timeout ms\", json.initTimeoutMs);");
		append(r, "    html += networkSettings.toHtmlTableRow(\"resetTimeoutMs\", \"number\", \"Reset timeout ms\", json.resetTimeoutMs);");
		append(r, "    html += \"<tr><td colspan='2'><b>Processor learning</b></td></tr>\";");
		append(r, "    for (name in processorLearning) {");
		append(r, "        html += networkSettings.toHtmlTableRow(\"learning\" + name, \"checkbox\", name, processorLearning[name]);");
		append(r, "    }");
		append(r, "    html += \"<tr><td></td><td><input type='button' value='Submit' onclick='networkSettings.submit();' /></td></tr>\";");
		append(r, "    html += \"</table>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderToHtmlTableRow(StringBuilder r) {
		append(r, "networkSettings.toHtmlTableRow = (id, type, label, value) => {");
		append(r, "    var checked = type == \"checkbox\" && value ? \" checked\" : '';");
		append(r, "    var html = \"<tr>\";");
		append(r, "    html += \"<td>\" + label + \"</td>\";");
		append(r, "    html += \"<td><input id='\" + id + \"' type='\" + type + \"' value='\" + value + \"'\" + checked + \" /></td>\";");
		append(r, "    html += \"</tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderSubmit(StringBuilder r) {
		append(r, "networkSettings.submit = () => {");
		append(r, "    request = new HttpRequest(\"POST\",\"" + NetworkSettingsJsonHandler.PATH + "\");");
		append(r, "    request.body = JSON.stringify(networkSettings.getFormAsJson());");
		append(r, "    request.contentType = \"application/json\";");
		append(r, "    request.execute(() => {");
		append(r, "        networkSettingsLoader.execute();");
		append(r, "    });");
		append(r, "};");
	}

	protected void renderGetFormAsJson(StringBuilder r) {
		append(r, "networkSettings.getFormAsJson = () => {");
		append(r, "    var json = { className: \"" + NetworkSettings.class.getName() + "\" };");
		append(r, "    var obj = {};");
		append(r, "    var form = document.getElementById(\"networkSettingsForm\");");
		append(r, "    var inputs = form.getElementsByTagName(\"INPUT\");");
		append(r, "    for (var i = 0; i < inputs.length; i++) {");
		append(r, "        if (inputs[i].id.indexOf(\"learning\")==0) {");
		append(r, "            var name = inputs[i].id.substring(8,inputs[i].id.length);");
		append(r, "            obj[name] = dom.getInputValue(inputs[i].id) === true;");
		append(r, "        } else if (inputs[i].type!=\"button\") {");
		append(r, "            json[inputs[i].id] = parseInt(dom.getInputValue(inputs[i].id));");
		append(r, "        }");
		append(r, "    }");
		append(r, "    json.processorLearning = util.objectToMap(obj,\"" + Boolean.class.getName() + "\");");
		append(r, "    return json;");
		append(r, "};");
	}
}
