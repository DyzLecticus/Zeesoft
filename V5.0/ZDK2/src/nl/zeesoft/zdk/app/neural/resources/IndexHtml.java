package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.AppStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettingsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStatsJsonHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;

public class IndexHtml extends HtmlResource {
	public IndexHtml() {
		this.title = "ZHS";
		onload = "loadState((state) => { console.log(state) });";
		scriptFiles.add(IndexJsHandler.PATH);
		styleFiles.add(IndexCssHandler.PATH);
	}
	
	@Override
	protected StringBuilder renderBody() {
		StringBuilder body = super.renderBody();
		append(body, "<h1>Zeesoft HTM Server</h1>");
		append(body, "<p>This HTTP server exposes a configurable HTM network through a JSON API.</p>");
		renderLinks(body);
		return body;
	}
	
	protected void renderLinks(StringBuilder body) {
		append(body, "API links;");
		append(body, "<ul>");
		append(body, renderLinkListItem(AppStateTextHandler.PATH, "App state"));
		append(body, renderLinkListItem(NetworkStateTextHandler.PATH, "Network state"));
		append(body, renderLinkListItem(NetworkConfigJsonHandler.PATH, "Network configuration"));
		append(body, renderLinkListItem(NetworkSettingsJsonHandler.PATH, "Network settings"));
		append(body, renderLinkListItem(NetworkStatsJsonHandler.PATH, "Network statistics"));
		append(body, renderLinkListItem(NetworkIOJsonHandler.PATH, "Network I/O (POST only)"));
		append(body, "</ul>");
	}
}
