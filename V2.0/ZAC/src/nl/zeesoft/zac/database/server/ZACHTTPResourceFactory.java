package nl.zeesoft.zac.database.server;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.file.HTMLFile;

public class ZACHTTPResourceFactory extends SvrHTTPResourceFactory {

	@Override
	protected String getIndexMenuTitle() {
		return "ZAC";
	}

	protected StringBuilder getZACMenuHtmlForPage(String page) {
		List<String> pages = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		
		pages.add("index");
		pages.add("home");

		titles.add("Home");
		titles.add("ZODB");
		
		StringBuilder html = new StringBuilder();
		int i = 0;
		for (String p: pages) {
			if (html.length()>0) {
				html.append("&nbsp;");
			}
			if (p.equals(page)) {
				html.append("<b>");
			} else {
				html.append("<a href=\"/" + p + ".html\">");
			}
			html.append(titles.get(i));
			if (p.equals(page)) {
				html.append("</b>");
			} else {
				html.append("</a>");
			}
			i++;
		}
		html.insert(0,"<div>");
		html.append("<br/><hr/></div>");
		
		return html;
	}
	
	@Override
	protected String getAuthorizerTitle() {
		return "ZAC - Authorizer";
	}
	
	@Override
	protected StringBuilder getAuthorizerMenuHtml() {
		return getZACMenuHtmlForPage("");
	}
	
	@Override
	protected HTMLFile get404Html() {
		HTMLFile file = new HTMLFile("ZAC - 404 Not Found");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append(getZACMenuHtmlForPage(""));
		body.append("<div>");
		body.append("No resource found for this URL. ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	@Override
	protected HTMLFile getInstalledHtml() {
		return getIndexHtml();
	}
	
	@Override
	protected HTMLFile getIndexHtml() {
		HTMLFile file = new HTMLFile("ZAC - Home");
		file.setBodyBgColor(BACKGROUND_COLOR);

		file.getBodyElements().add(getZACMenuHtmlForPage("index").toString());

		StringBuilder body = new StringBuilder();
		body.append("<div>");
		body.append("<b>Zeesoft Artificial Cognition</b> is a program that can produce artificial cognition. ");
		body.append("<br/>");
		body.append("<br/>");
		body.append("<b>Artificial cognition</b><br/>");
		body.append("It is thought that cognition is the result of neural networks evolving to have parts of their behavioral control structures turned inwardly, creating virtual thinking muscles. ");
		body.append("It is also thought that every mental object is represented in the brain by a certain group of neurons expressing excitation. ");
		body.append("Human language capabilities are thought to be an innate result of cognition so the process that assembles symbols into thoughts is reflected in linguistic expression. ");
		body.append("This makes words and punctuation the ideal candidates to function as symbols. ");
		body.append("Knowledge is gained by learning the links between symbols in a stochastic manner. ");
		body.append("Knowledge is applied by using modules to evaluate all relevant links between symbols simultaniously. ");
		body.append("<br/>");
		body.append("<br/>");
		body.append("<b>Inspiration</b><br/>");
		body.append("This software was inspired by ideas put forth in the following books; ");
		body.append("<ul> ");
		body.append("<li><b>Langauge and Mind</b> by Noam Chomsky</li>");
		body.append("<li><b>Godel Esher Bach</b> by Douglas R. Hofstadter</li>");
		body.append("<li><b>I Am a Strange Loop</b> by Douglas R. Hofstadter</li>");
		body.append("<li><b>The Mind's I</b> by Douglas R. Hofstadter and Daniel C. Dennet</li>");
		body.append("<li><b>Consciousness Explained</b> by Daniel C. Dennet</li>");
		body.append("<li><b>Confabulation Theory</b> by Robert Hecht-Nielsen</li>");
		body.append("</ul> ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
}
