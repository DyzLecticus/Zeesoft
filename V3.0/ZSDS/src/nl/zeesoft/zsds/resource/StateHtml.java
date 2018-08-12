package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class StateHtml extends HtmlResource {
	public StateHtml() {
		setTitle("ZSDS - State manager");
		
		getScriptFiles().add("zsds.js");
		getStyleFiles().add("zsds.css");
		
		ZStringBuilder script = new ZStringBuilder();
		script.append("<script type=\"text/javascript\">\n");
		script.append("ZSDS.state = {};\n");
		script.append("ZSDS.state.auto = true;\n");
		script.append("ZSDS.state.refresh = function() {\n");
		script.append("    if (!ZSDS.state.auto) {\n");
		script.append("        elem = window.document.getElementById(\"summary\");\n");
		script.append("        elem.value = \"Sending request ...\";\n");
		script.append("    }\n");
		script.append("    ZSDS.xhr.getJSON(\"state.json\",ZSDS.state.refreshCallback,ZSDS.state.refreshCallback);\n");
		script.append("};\n");
		script.append("ZSDS.state.refreshCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZSDS.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        ZSDS.state.auto = true;\n");
		script.append("    } else {\n");
		script.append("        ZSDS.state.auto = false;\n");
		script.append("    }\n");
		script.append("    elem = window.document.getElementById(\"summary\");\n");
		script.append("    elem.value = JSON.stringify(object,null,2);\n");
		script.append("};\n");
		script.append("ZSDS.state.autoRefresh = function() {\n");
		script.append("    if (ZSDS.state.auto) {\n");
		script.append("        ZSDS.state.refresh();\n");
		script.append("    }\n");
		script.append("    setTimeout(function(){ ZSDS.state.autoRefresh(); }, 1000);\n");
		script.append("};\n");
		script.append("ZSDS.state.defaultCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZSDS.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    }\n");
		script.append("    setTimeout(function(){ ZSDS.state.refresh(); }, 200);\n");
		script.append("};\n");
		script.append("ZSDS.state.reload = function() {\n");
		script.append("    elem = window.document.getElementById(\"reloadCheck\");\n");
		script.append("    if (elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        elem = window.document.getElementById(\"summary\");\n");
		script.append("        elem.value = \"Sending request ...\";\n");
		script.append("        ZSDS.xhr.postJSON(\"reload.json\",\"\",ZSDS.state.defaultCallback,ZSDS.state.defaultCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.state.rebase = function() {\n");
		script.append("    elem = window.document.getElementById(\"rebaseCheck\");\n");
		script.append("    if (elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        elem = window.document.getElementById(\"summary\");\n");
		script.append("        elem.value = \"Sending request ...\";\n");
		script.append("        ZSDS.xhr.postJSON(\"rebase.json\",\"\",ZSDS.state.defaultCallback,ZSDS.state.defaultCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.state.generate = function() {\n");
		script.append("    elem = window.document.getElementById(\"generateCheck\");\n");
		script.append("    if (elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        elem = window.document.getElementById(\"summary\");\n");
		script.append("        elem.value = \"Sending request ...\";\n");
		script.append("        ZSDS.xhr.postJSON(\"generate.json\",\"\",ZSDS.state.defaultCallback,ZSDS.state.defaultCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.state.onload = function() {\n");
		script.append("    ZSDS.state.autoRefresh();\n");
		script.append("};\n");
		script.append("</script>\n");
		getHeadElements().add(script);
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"index.html\">Index</a>");
		html.append("<hr />");
		html.append("<div>\n");
		
		html.append("<div>\n");
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("<input type=\"button\" value=\"Refresh\" onclick=\"ZSDS.state.refresh();\"/>");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("<input type=\"button\" value=\"Generate\" onclick=\"ZSDS.state.generate();\"/>");
		html.append("<input type=\"checkbox\" id=\"generateCheck\"/>");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("<input type=\"button\" value=\"Reload\" onclick=\"ZSDS.state.reload();\"/>");
		html.append("<input type=\"checkbox\" id=\"reloadCheck\"/>");
		html.append("</td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>");
		html.append("<input type=\"button\" value=\"Rebase\" onclick=\"ZSDS.state.rebase();\"/>");
		html.append("<input type=\"checkbox\" id=\"rebaseCheck\"/>");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		html.append("<br />\n");
		html.append("<div>\n");
		html.append("State<br />\n");
		html.append("<textarea id=\"summary\"></textarea>\n");
		html.append("</div>\n");
		
		html.append("</div>\n");

		setOnload("ZSDS.state.onload();");

		getBodyElements().add(html);
	}
}
