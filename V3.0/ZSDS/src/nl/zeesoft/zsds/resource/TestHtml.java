package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class TestHtml extends HtmlResource {
	public TestHtml() {
		setTitle("ZSDS - Test");
		
		getScriptFiles().add("zsds.js");
		getScriptFiles().add("apiClient.js");
		getScriptFiles().add("test.js");
		getStyleFiles().add("zsds.css");
		
		ZStringBuilder script = new ZStringBuilder();
		script.append("<script type=\"text/javascript\">\n");
		script.append("ZSDS.test.onload = function() {\n");
		script.append("    ZSDS.test.getRequest();\n");
		script.append("    ZSDS.dom.bindEnterFunctionToElementId(\"input\",ZSDS.test.sendRequest);\n");
		script.append("    ZSDS.test.focusInput();\n");
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
		html.append("Output");
		html.append("</td>\n");
		html.append("<td>");
		html.append("<input type=\"text\" id=\"output\" style=\"width: 100%;\" DISABLED/>");
		html.append("</td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>");
		html.append("Prompt");
		html.append("</td>\n");
		html.append("<td>");
		html.append("<input type=\"text\" id=\"prompt\" onchange=\"ZSDS.test.refreshRequest();\" style=\"width: 100%;\"/>");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("Input");
		html.append("</td>\n");
		html.append("<td>");
		html.append("<input type=\"text\" id=\"input\" onchange=\"ZSDS.test.refreshRequest();\" style=\"width: 400px;\"/>");
		html.append("<input type=\"button\" value=\"send\" onclick=\"ZSDS.test.sendRequest();\" />\n");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("</tbody>\n");
		html.append("</table>\n");
		html.append("</div>\n");
		
		html.append("<br />\n");
		html.append("<div>\n");
		html.append("Request<br />\n");
		html.append("<textarea id=\"request\"></textarea>\n");
		html.append("</div>\n");

		html.append("<br />\n");
		html.append("<div>\n");
		html.append("Response<br />\n");
		html.append("<textarea id=\"response\"></textarea>\n");
		html.append("</div>\n");

		html.append("<br />\n");
		html.append("<div>\n");
		html.append("Test case<br />\n");
		html.append("<textarea id=\"testCase\"></textarea>\n");
		html.append("</div>\n");

		html.append("</div>\n");

		setOnload("ZSDS.test.onload();");

		getBodyElements().add(html);
	}
}
