package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.ZSDSServlet;

public class IndexHtml extends HtmlResource {
	public IndexHtml(boolean selfTest) {
		setTitle("ZSDS - Index");
		
		ZStringBuilder html = new ZStringBuilder();
		
		html.append("<h1>Zeesoft Smart Dialog Server</h1>");
		html.append(ZSDSServlet.DESCRIPTION);

		html.append("<h2>Main application</h2>");
		
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
		
		html.append("<tr>\n");
		html.append("<td width=\"20%\">");
		html.append("<a href=\"chat.html\">Chat</a>");
		html.append("</td>\n");
		html.append("<td>");
		html.append("A simple chat implementation demo.");
		html.append("</td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>");
		html.append("<a href=\"test.html\">Test</a>");
		html.append("</td>\n");
		html.append("<td>");
		html.append("Test dialog request/response handling and record test cases.");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("<a href=\"state.html\">State manager</a>");
		html.append("</td>\n");
		html.append("<td>");
		if (selfTest) {
			html.append("Manage the state of the application and review the internal self test summary.");
		} else {
			html.append("Manage the state of the application.");
		}
		html.append("</td>\n");
		html.append("</tr>\n");
		
		if (selfTest) {
			html.append("<tr>\n");
			html.append("<td>");
			html.append("<a href=\"selfTestSummary.json\">Self test summary JSON</a>");
			html.append("</td>\n");
			html.append("<td>");
			html.append("Returns the internal self test summary as a JSON file.");
			html.append("</td>\n");
			html.append("</tr>\n");
		}
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("<a href=\"dialogs.json\">Dialogs JSON</a>");
		html.append("</td>\n");
		html.append("<td>");
		html.append("Returns the dialog set as a JSON file.");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("<tr>\n");
		html.append("<td>");
		html.append("<a href=\"config.json\">Configuration JSON</a>");
		html.append("</td>\n");
		html.append("<td>");
		html.append("Returns the application configuration as a JSON file.");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("</tbody>\n");
		html.append("</table>\n");
		
		html.append("<h2>Test application</h2>");
		
		html.append("<table style=\"width: 100%;\">\n");
		html.append("<tbody>\n");
		
		html.append("<tr>\n");
		html.append("<td width=\"20%\">");
		html.append("<a href=\"environment.html\">Test environments</a>");
		html.append("</td>\n");
		html.append("<td>");
		html.append("Test environments with an environment specific set of test cases.");
		html.append("</td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>");
		html.append("<a href=\"testConfig.json\">Test configuration JSON</a>");
		html.append("</td>\n");
		html.append("<td>");
		html.append("Returns the application configuration as a JSON file.");
		html.append("</td>\n");
		html.append("</tr>\n");
		
		html.append("</tbody>\n");
		html.append("</table>\n");
		
		getBodyElements().add(html);
	}
}
