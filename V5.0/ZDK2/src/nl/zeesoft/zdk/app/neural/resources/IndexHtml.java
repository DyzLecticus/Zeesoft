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
import nl.zeesoft.zdk.app.neural.handlers.api.SdrPngHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;

public class IndexHtml extends HtmlResource {
	public IndexHtml() {
		this.title = "Zeesoft NeuralServer";
		onload = "loadApp(true);";
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
		renderNetworkIOStats(body);
		renderApiLinks(body);
		return body;
	}
	
	protected void renderNetworkConfig(StringBuilder body) {
		append(body, getAccordion("networkConfigAccordion","Configuration","<div id=\"networkConfig\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderNetworkSettings(StringBuilder body) {
		append(body, getAccordion("networkSettingsAccordion","Settings","<div id=\"networkSettings\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderNetworkStats(StringBuilder body) {
		append(body, getAccordion("networkStatsAccordion","Neural statistics","<div id=\"networkStats\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderNetworkIOStats(StringBuilder body) {
		append(body, getAccordion("networkIOStatsAccordion","Performance statistics","<div id=\"networkIOStats\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderApiLinks(StringBuilder body) {
		StringBuilder content = new StringBuilder("<ul class=\"x-scrollable\">");
		renderLinkListItems(content);
		append(content, "</ul>");
		append(body, getAccordion("apiLinksAccordion","API links",content));
	}

	protected void renderLinkListItems(StringBuilder body) {
		append(body, renderLinkListItem(AppStateTextHandler.PATH, "App state", "_blank"));
		append(body, renderLinkListItem(NetworkStateTextHandler.PATH, "Network state", "_blank"));
		append(body, renderLinkListItem(NetworkConfigJsonHandler.PATH, "Network configuration", "_blank"));
		append(body, renderLinkListItem(NetworkSettingsJsonHandler.PATH, "Network settings", "_blank"));
		append(body, renderLinkListItem(NetworkStatsJsonHandler.PATH, "Network neural statistics", "_blank"));
		append(body, renderLinkListItem(NetworkIOStatsJsonHandler.PATH, "Network performance statistics", "_blank"));
		append(body, renderLinkListItem(NetworkIOAccuracyJsonHandler.PATH, "Network accuracy", "_blank"));
		append(body, renderLinkListItem(NetworkIOJsonHandler.PATH, "Network I/O", "_blank"));
		append(body, renderLinkListItem(SdrPngHandler.PATH + "?100,7,8,9,10,11,12,58,59,60,61,62,63", "SDR image rendering", "_blank"));
	}
}
