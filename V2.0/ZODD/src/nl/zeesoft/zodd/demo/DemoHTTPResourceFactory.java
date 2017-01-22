package nl.zeesoft.zodd.demo;

import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.file.HTMLFile;

public class DemoHTTPResourceFactory extends SvrHTTPResourceFactory {

	@Override
	protected String getIndexMenuTitle() {
		return "ZODD";
	}

	@Override
	protected HTMLFile getIndexHtml() {
		HTMLFile file = new HTMLFile("ZODD - Welcome");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append("<div>");
		body.append("\n");
		body.append("Welcome to the <a href=\"/home.html\">ZODB</a> demo application. ");
		body.append("This application generates 250000 objects upon installation in order to demonstrate the ZODB functionality and performance. ");
		body.append("Check the <a href=\"/" + DemoModel.DEMO_PARENT_FULL_NAME + ".html\">DemoParent</a> and <a href=\"/" + DemoModel.DEMO_CHILD_FULL_NAME + ".html\">DemoChild</a> managers to see the results. ");
		body.append("\n");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
}
