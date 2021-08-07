package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class NetworkIOAccuracyJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var networkIOAccuracy = networkIOAccuracy || {};");
		renderToHtmlTable(r);
		renderToHtmlTableHeader(r);
		renderToHtmlTableRow(r);
	}

	protected void renderToHtmlTable(StringBuilder r) {
		append(r, "networkIOAccuracy.toHtmlTable = (json) => {");
		append(r, "    var html = \"Network accuracy over the last 100 requests<br />\";");
		append(r, "    html += \"<table class='padded'>\";");
		append(r, "    for (var i = 0; i < json.accuracies.length; i++) {");
		append(r, "        if (i==0) {");
		append(r, "            html += networkIOAccuracy.toHtmlTableHeader();");
		append(r, "        }");
		append(r, "        html += networkIOAccuracy.toHtmlTableRow(json.accuracies[i]);");
		append(r, "        if (json.accuracies.length==2) {");
		append(r, "            break;");
		append(r, "        }");
		append(r, "    }");
		append(r, "    html += \"</table>\";");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToHtmlTableHeader(StringBuilder r) {
		append(r, "networkIOAccuracy.toHtmlTableHeader = () => {");
		append(r, "    var html = \"<tr>\";");
		append(r, "    html += \"<th></th>\";");
		append(r, "    html += \"<th align='right'>Accuracy</th>\";");
		append(r, "    html += \"<th align='right'>RMSE</th>\";");
		append(r, "    html += \"<th align='right'>MAPE</th>\";");
		append(r, "    html += \"<tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToHtmlTableRow(StringBuilder r) {
		append(r, "networkIOAccuracy.toHtmlTableRow = (ioAccuracy) => {");
		append(r, "    var html = \"<tr>\";");
		append(r, "    html += \"<td>\" + ioAccuracy.name + \"</td>\";");
		append(r, "    html += \"<td align='right'>\" + util.formatDecimal(ioAccuracy.accuracy, 2) + \"</td>\";");
		append(r, "    html += \"<td align='right'>\" + util.formatDecimal(ioAccuracy.rootMeanSquaredError, 3) + \"</td>\";");
		append(r, "    html += \"<td align='right'>\" + util.formatDecimal(ioAccuracy.meanAveragePercentageError, 2) + \"</td>\";");
		append(r, "    html += \"<tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}
}
