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
		append(body, "<p>Network state: <b><span id=\"networkStateText\"></span></b></p>");
		renderNetworkConfig(body);
		renderNetworkSettings(body);
		renderNetworkStatistics(body);
		renderApiLinks(body);
		return body;
	}
	
	protected void renderNetworkConfig(StringBuilder body) {
		append(body, getAccordion("networkConfigAccordion","Configuration","<div id=\"networkConfig\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderNetworkSettings(StringBuilder body) {
		append(body, getAccordion("networkSettingsAccordion","Settings","<div id=\"networkSettings\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderNetworkStatistics(StringBuilder body) {
		StringBuilder content = new StringBuilder();
		content.append("<table id=\"networkStatistics\" class=\"x-scrollable padded\">");
		content.append(getStatisticsRow("Neural","networkStats",false));
		content.append("<tr><td>&nbsp;</td></tr>");
		content.append(getStatisticsRow("Performance","networkIOStats",true));
		content.append("<tr><td>&nbsp;</td></tr>");
		content.append(getStatisticsRow("Accuracy","networkIOAccuracy",true));
		content.append("</table>");
		append(body, getAccordion("networkStatisticsAccordion","Statistics",content.toString()));
	}
	
	protected StringBuilder getStatisticsRow(String title, String id, boolean autoRefreshChecked) {
		StringBuilder r = new StringBuilder(); 
		r.append("<tr><td><h3 class='mb-0'>");
		r.append(title);
		r.append("</h3><input type='button' value='Refresh' onclick='");
		r.append(id);
		r.append("Loader.refresh(this);' /><input type='checkbox' onclick='");
		r.append(id);
		r.append("Loader.toggleAutoRefresh(this);' ");
		if (autoRefreshChecked) {
			r.append("CHECKED ");
		}
		r.append("/><div id='");
		r.append(id);
		r.append("'></div></td></tr>");
		return r;
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
		append(body, renderLinkListItem(NetworkIOAccuracyJsonHandler.PATH + "?max=1000", "Network accuracy", "_blank"));
		append(body, renderLinkListItem(NetworkIOJsonHandler.PATH, "Network I/O", "_blank"));
		append(body, renderLinkListItem(SdrPngHandler.PATH + "?sdr=100,7,8,9,10,11,12,58,59,60,61,62,63", "SDR image rendering", "_blank"));
	}
}
