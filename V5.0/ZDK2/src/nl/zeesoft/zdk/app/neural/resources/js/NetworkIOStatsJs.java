package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class NetworkIOStatsJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var networkIOStats = networkIOStats || {};");
		renderToHtmlTable(r);
		renderToHtmlTableHeader(r);
		renderToHtmlTableRow(r);
	}

	protected void renderToHtmlTable(StringBuilder r) {
		append(r, "networkIOStats.toHtmlTable = (json) => {");
		append(r, "    var checked = networkIOStatsLoader.autoRefresh ? \" CHECKED\" : \"\";");
		append(r, "    var html = \"<p class='mt-0'>Auto refresh <input type='checkbox' onclick='networkIOStatsLoader.toggleAutoRefresh(this);'\" + checked + \" /></p>\";");
		append(r, "    html += \"<p>Recorded requests: <b>\" + json.recorded + \"</b><br />\";");
		append(r, "    html += \"Average processing milliseconds per request: <b>\" + util.formatDecimal(json.totalNs / 1000000, 3) + \"</b> (last 100 requests)</p>\";");
		append(r, "    html += \"<table class='padded'>\";");
		append(r, "    html += networkIOStats.toHtmlTableHeader();");
		append(r, "    for (var i = 0; i < json.nsPerLayer.keyValues.length; i++) {");
		append(r, "        var kv = json.nsPerLayer.keyValues[i];");
		append(r, "        html += networkIOStats.toHtmlTableRow(kv.key.value, kv.value.value);");
		append(r, "    }");
		append(r, "    html += \"</table>\";");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToHtmlTableHeader(StringBuilder r) {
		append(r, "networkIOStats.toHtmlTableHeader = () => {");
		append(r, "    var html = \"<tr>\";");
		append(r, "    html += \"<th align='right'>Layer</th>\";");
		append(r, "    html += \"<th align='right'>Avg. ms</th>\";");
		append(r, "    html += \"<tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToHtmlTableRow(StringBuilder r) {
		append(r, "networkIOStats.toHtmlTableRow = (layer, ns) => {");
		append(r, "    var html = \"<tr>\";");
		append(r, "    html += \"<td align='right'>\" + layer + \"</td>\";");
		append(r, "    html += \"<td align='right'>\" + util.formatDecimal(ns / 1000000, 3) + \"</td>\";");
		append(r, "    html += \"<tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}
}
