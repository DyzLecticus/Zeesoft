package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.AppStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.DemoTrainerHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.ManagerHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOAccuracyJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOStatsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettingsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStatsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.RegularDateTimeValuesCsvHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.SdrPngHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;

public class IndexHtml extends HtmlResource {
	protected int	port	= 80;
	
	public IndexHtml(int port) {
		this.port = port;
		this.title = "NeuralServer";
		onload = "loadApp(false);";
		scriptFiles.add(IndexJsHandler.PATH);
		styleFiles.add(IndexCssHandler.PATH);
	}
	
	@Override
	protected StringBuilder renderBody() {
		StringBuilder body = super.renderBody();
		append(body, "<h1>Zeesoft NeuralServer</h1>");
		append(body, "<p>This HTTP server exposes a configurable HTM network through a JSON API.</p>");
		append(body, "<p>Network state: <b><span id=\"networkStateText\"></span></b></p>");
		renderNetworkStatistics(body);
		renderNetworkSettings(body);
		renderNetworkConfig(body);
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
		content.append(getStatisticsRow("Performance","networkIOStats",true));
		content.append(getStatisticsRow("Accuracy","networkIOAccuracy",true));
		content.append("</table>");
		append(body, getAccordion("networkStatisticsAccordion","Statistics",content.toString()));
	}
	
	protected StringBuilder getStatisticsRow(String title, String id, boolean autoRefreshChecked) {
		StringBuilder r = new StringBuilder();
		r.append("<tr><td>");
		r.append(getStatisticsHeader(title, id, autoRefreshChecked));
		r.append("&nbsp;");
		r.append(getAutoRefreshInputs(title, id, autoRefreshChecked));
		r.append("</td></tr>");
		r.append("<tr><td><div id='");
		r.append(id);
		r.append("'></div></td></tr>");
		return r;
	}
	
	protected StringBuilder getStatisticsHeader(String title, String id, boolean autoRefreshChecked) {
		StringBuilder r = new StringBuilder();
		r.append("<h3 class='mb-0");
		if (!autoRefreshChecked) {
			r.append(" mt-0");
		}
		r.append("' style='display:inline-block;' >");
		r.append(title);
		r.append("</h3>");
		return r;
	}
	
	protected StringBuilder getAutoRefreshInputs(String title, String id, boolean autoRefreshChecked) {
		StringBuilder r = new StringBuilder();
		r.append("<input type='button' value='Refresh' onclick='");
		r.append(id);
		r.append("Loader.refresh(this);' class='text-small' /><input type='checkbox' onclick='");
		r.append(id);
		r.append("Loader.toggleAutoRefresh(this);' ");
		if (autoRefreshChecked) {
			r.append("CHECKED ");
		}
		r.append("/>");
		return r;
	}
	
	protected void renderApiLinks(StringBuilder body) {
		StringBuilder content = new StringBuilder("<ul class=\"x-scrollable\">");
		renderLinkListItems(content);
		append(content, "</ul>");
		append(body, getAccordion("apiLinksAccordion","API links",content));
	}

	protected void renderLinkListItems(StringBuilder body) {
		append(body, renderLinkListItem(ManagerHtmlHandler.PATH, "Manager", "_blank"));
		append(body, renderLinkListItem(AppStateTextHandler.PATH, "App state", "_blank"));
		append(body, renderLinkListItem(NetworkStateTextHandler.PATH, "Network state", "_blank"));
		append(body, renderLinkListItem(NetworkConfigJsonHandler.PATH, "Network configuration", "_blank"));
		append(body, renderLinkListItem(NetworkSettingsJsonHandler.PATH, "Network settings", "_blank"));
		append(body, renderLinkListItem(NetworkStatsJsonHandler.PATH, "Network neural statistics", "_blank"));
		append(body, renderLinkListItem(NetworkIOStatsJsonHandler.PATH, "Network performance statistics", "_blank"));
		append(body, renderLinkListItem(NetworkIOAccuracyJsonHandler.PATH + "?max=1000", "Network accuracy", "_blank"));
		append(body, renderLinkListItem(NetworkIOJsonHandler.PATH, "Network I/O", "_blank"));
		append(body, renderLinkListItem(SdrPngHandler.PATH + "?sdr=100,11,12,13,14,15,16,17,18,27,36,45,54,63,72,81,82,83,84,85,86,87,88", "SDR image rendering", "_blank"));
		if (port!=80) {
			append(body, renderLinkListItem(DemoTrainerHtmlHandler.PATH, "Demo trainer", "_blank"));
			append(body, renderLinkListItem(RegularDateTimeValuesCsvHandler.PATH, "Regular DateTime/Value training data", "_blank"));
		}
	}
}
