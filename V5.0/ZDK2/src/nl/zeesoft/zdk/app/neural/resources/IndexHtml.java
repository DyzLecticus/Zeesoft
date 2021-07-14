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
		this.title = "ZNS";
		onload = "loadApp();";
		scriptFiles.add(IndexJsHandler.PATH);
		styleFiles.add(IndexCssHandler.PATH);
	}
	
	@Override
	protected StringBuilder renderBody() {
		StringBuilder body = super.renderBody();
		append(body, "<h1>Zeesoft Neural Server</h1>");
		append(body, "<p>This HTTP server exposes a configurable HTM network through a JSON API.</p>");
		append(body, "<p>Network state: <b><span id=\"networkStateText\" /></b></p>");
		renderNetworkConfig(body);
		renderNetworkStats(body);
		renderApiLinks(body);
		return body;
	}
	
	protected void renderNetworkConfig(StringBuilder body) {
		append(body, "<hr />");
		append(body, "<h2 onclick=\"dom.toggleVisible('networkConfig');\" class=\"clickable\">Network configuration</h2>");
		append(body, "<div id=\"networkConfig\" class=\"x-scrollable hidden\"></div>");
	}
	
	protected void renderNetworkStats(StringBuilder body) {
		append(body, "<hr />");
		append(body, "<h2 onclick=\"dom.toggleVisible('networkStats');\" class=\"clickable\">Network statistics</h2>");
		append(body, "<div id=\"networkStats\" class=\"x-scrollable hidden\"></div>");
	}
	
	protected void renderApiLinks(StringBuilder body) {
		append(body, "<hr />");
		append(body, "<h2 onclick=\"dom.toggleVisible('apiLinks');\" class=\"clickable\">API links</h2>");
		append(body, "<ul id=\"apiLinks\" class=\"x-scrollable hidden\">");
		renderLinkListItems(body);
		append(body, "</ul>");
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
