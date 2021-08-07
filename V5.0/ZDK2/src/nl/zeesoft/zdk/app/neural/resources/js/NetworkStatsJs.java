package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class NetworkStatsJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var networkStats = networkStats || {};");
		renderToHtmlTable(r);
		renderToHtmlTableHeader(r);
		renderToHtmlTableRow(r);
	}

	protected void renderToHtmlTable(StringBuilder r) {
		append(r, "networkStats.toHtmlTable = (json) => {");
		append(r, "    var html = \"Cells: <b>\" + json.cells + \"</b><br />\";");
		append(r, "    html += \"<table class='padded'>\";");
		append(r, "    html += networkStats.toHtmlTableHeader();");
		append(r, "    html += networkStats.toHtmlTableRow(\"Proximal\", json.proximalStats);");
		append(r, "    html += networkStats.toHtmlTableRow(\"Distal\", json.distalStats);");
		append(r, "    html += networkStats.toHtmlTableRow(\"Apical\", json.apicalStats);");
		append(r, "    html += \"</table>\";");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToHtmlTableHeader(StringBuilder r) {
		append(r, "networkStats.toHtmlTableHeader = () => {");
		append(r, "    var html = \"<tr>\";");
		append(r, "    html += \"<th></th>\";");
		append(r, "    html += \"<th align='right'>Segments</th>\";");
		append(r, "    html += \"<th align='right'>Synapses</th>\";");
		append(r, "    html += \"<th align='right'>Active</th>\";");
		append(r, "    html += \"<tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToHtmlTableRow(StringBuilder r) {
		append(r, "networkStats.toHtmlTableRow = (type, segmentStats) => {");
		append(r, "    var html = \"<tr>\";");
		append(r, "    html += \"<td>\" + type + \"</td>\";");
		append(r, "    html += \"<td align='right'>\" + segmentStats.segments + \"</td>\";");
		append(r, "    html += \"<td align='right'>\" + segmentStats.synapses + \"</td>\";");
		append(r, "    html += \"<td align='right'>\" + segmentStats.activeSynapses + \"</td>\";");
		append(r, "    html += \"<tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}
}
