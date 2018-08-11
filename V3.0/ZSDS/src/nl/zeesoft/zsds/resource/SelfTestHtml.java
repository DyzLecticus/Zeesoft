package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class SelfTestHtml extends HtmlResource {
	public SelfTestHtml() {
		setTitle("ZSDS - Self test");
		
		getScriptFiles().add("zsds.js");
		getStyleFiles().add("zsds.css");
		
		ZStringBuilder script = new ZStringBuilder();
		script.append("<script type=\"text/javascript\">\n");
		script.append("ZSDS.selfTest = {};\n");
		script.append("ZSDS.selfTest.auto = true;\n");
		script.append("ZSDS.selfTest.refresh = function() {\n");
		script.append("    if (!ZSDS.selfTest.auto) {\n");
		script.append("        elem = window.document.getElementById(\"summary\");\n");
		script.append("        elem.value = \"Sending request ...\";\n");
		script.append("    }\n");
		script.append("    ZSDS.xhr.getJSON(\"selfTest.json\",ZSDS.selfTest.refreshCallback,ZSDS.selfTest.refreshCallback);\n");
		script.append("};\n");
		script.append("ZSDS.selfTest.refreshCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZSDS.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        ZSDS.selfTest.auto = true;\n");
		script.append("    } else {\n");
		script.append("        ZSDS.selfTest.auto = false;\n");
		script.append("    }\n");
		script.append("    elem = window.document.getElementById(\"summary\");\n");
		script.append("    elem.value = JSON.stringify(object,null,2);\n");
		script.append("};\n");
		script.append("ZSDS.selfTest.autoRefresh = function() {\n");
		script.append("    if (ZSDS.selfTest.auto) {\n");
		script.append("        ZSDS.selfTest.refresh();\n");
		script.append("    }\n");
		script.append("    setTimeout(function(){ ZSDS.selfTest.autoRefresh(); }, 1000);\n");
		script.append("};\n");
		script.append("ZSDS.selfTest.reload = function() {\n");
		script.append("    elem = window.document.getElementById(\"reloadCheck\");\n");
		script.append("    if (elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        elem = window.document.getElementById(\"summary\");\n");
		script.append("        elem.value = \"Sending request ...\";\n");
		script.append("        ZSDS.xhr.postJSON(\"reload.json\",\"\",ZSDS.selfTest.reloadCallback,ZSDS.selfTest.reloadCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.selfTest.reloadCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZSDS.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    }\n");
		script.append("    ZSDS.selfTest.refresh();\n");
		script.append("};\n");
		script.append("ZSDS.selfTest.rebase = function() {\n");
		script.append("    elem = window.document.getElementById(\"rebaseCheck\");\n");
		script.append("    if (elem.checked) {\n");
		script.append("        elem.checked = false;\n");
		script.append("        elem = window.document.getElementById(\"summary\");\n");
		script.append("        elem.value = \"Sending request ...\";\n");
		script.append("        ZSDS.xhr.postJSON(\"rebase.json\",\"\",ZSDS.selfTest.rebaseCallback,ZSDS.selfTest.rebaseCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.selfTest.rebaseCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZSDS.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    }\n");
		script.append("    ZSDS.selfTest.refresh();\n");
		script.append("};\n");
		script.append("ZSDS.selfTest.onload = function() {\n");
		script.append("    ZSDS.selfTest.autoRefresh();\n");
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
		html.append("<input type=\"button\" value=\"Refresh\" onclick=\"ZSDS.selfTest.refresh();\"/>");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("<input type=\"button\" value=\"Reload\" onclick=\"ZSDS.selfTest.reload();\"/>");
		html.append("<input type=\"checkbox\" id=\"reloadCheck\"/>");
		html.append("</td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>");
		html.append("<input type=\"button\" value=\"Rebase\" onclick=\"ZSDS.selfTest.rebase();\"/>");
		html.append("<input type=\"checkbox\" id=\"rebaseCheck\"/>");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		html.append("<br />\n");
		html.append("<div>\n");
		html.append("Summary<br />\n");
		html.append("<textarea id=\"summary\"></textarea>\n");
		html.append("</div>\n");
		
		html.append("</div>\n");

		setOnload("ZSDS.selfTest.onload();");

		getBodyElements().add(html);
	}
}
