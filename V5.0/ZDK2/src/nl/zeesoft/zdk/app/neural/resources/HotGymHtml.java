package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.neural.handlers.HotGymJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;

public class HotGymHtml extends HtmlResource {
	public HotGymHtml() {
		this.title = "Zeesoft NeuralServer - HotGym";
		onload = "loadApp(); hotGym.loadData();";
		scriptFiles.add(IndexJsHandler.PATH);
		scriptFiles.add(HotGymJsHandler.PATH);
		styleFiles.add(IndexCssHandler.PATH);
	}
	
	@Override
	protected StringBuilder renderBody() {
		StringBuilder body = super.renderBody();
		append(body, "<h1>Zeesoft NeuralServer</h1>");
		append(body, "<p>This HTTP server exposes a configurable HTM network through a JSON API.</p>");
		append(body, "<p>Network state: <b><span id=\"networkStateText\" /></b></p>");
		renderNetworkStats(body);
		return body;
	}
	
	protected void renderNetworkStats(StringBuilder body) {
		append(body, getAccordion("networkStatsAccordion","Statistics","<div id=\"networkStats\" class=\"x-scrollable\"></div>"));
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
}
