package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.AppStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOAccuracyJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOStatsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettingsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStatsJsonHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;

public class IndexHtml extends HtmlResource {
	public IndexHtml() {
		this.title = "Zeesoft NeuralServer";
		onload = "loadApp();";
		scriptFiles.add(IndexJsHandler.PATH);
		styleFiles.add(IndexCssHandler.PATH);
	}
	
	@Override
	protected StringBuilder renderBody() {
		StringBuilder body = super.renderBody();
		append(body, "<h1>Zeesoft NeuralServer</h1>");
		append(body, "<p>This HTTP server exposes a configurable HTM network through a JSON API.</p>");
		append(body, "<p>Network state: <b><span id=\"networkStateText\" /></b></p>");
		renderNetworkConfig(body);
		renderNetworkSettings(body);
		renderNetworkStats(body);
		renderApiLinks(body);
		return body;
	}
	
	protected void renderNetworkConfig(StringBuilder body) {
		append(body, getAccordion("networkConfigAccordion","Network configuration","<div id=\"networkConfig\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderNetworkSettings(StringBuilder body) {
		append(body, getAccordion("networkSettingsAccordion","Network settings","<div id=\"networkSettings\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderNetworkStats(StringBuilder body) {
		append(body, getAccordion("networkStatsAccordion","Network statistics","<div id=\"networkStats\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderApiLinks(StringBuilder body) {
		StringBuilder content = new StringBuilder("<ul class=\"x-scrollable\">");
		append(content, "<ul class=\"x-scrollable\">");
		renderLinkListItems(content);
		append(content, "</ul>");
		append(body, getAccordion("apiLinksAccordion","API links",content));
	}

	protected StringBuilder getAccordion(String id, String title, String content) {
		return getAccordion(id, title, new StringBuilder(content));
	}
	
	protected StringBuilder getAccordion(String id, String title, StringBuilder content) {
		StringBuilder r = new StringBuilder();
		append(r, "<hr />");
		append(r, getAccordionStart(id,title));
		append(r, content);
		append(r, getAccordionEnd());
		return r;
	}

	protected void renderLinkListItems(StringBuilder body) {
		append(body, renderLinkListItem(AppStateTextHandler.PATH, "App state"));
		append(body, renderLinkListItem(NetworkStateTextHandler.PATH, "Network state"));
		append(body, renderLinkListItem(NetworkConfigJsonHandler.PATH, "Network configuration"));
		append(body, renderLinkListItem(NetworkSettingsJsonHandler.PATH, "Network settings"));
		
		append(body, renderLinkListItem(NetworkStatsJsonHandler.PATH, "Network statistics"));
		append(body, renderLinkListItem(NetworkIOStatsJsonHandler.PATH, "Network I/O statistics"));
		append(body, renderLinkListItem(NetworkIOAccuracyJsonHandler.PATH, "Network I/O accuracy"));
		
		append(body, renderLinkListItem(NetworkIOJsonHandler.PATH, "Network I/O (POST only)"));
	}
}
