package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class NetworkIOHistJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var networkIOHist = networkIOHist || {};");
		append(r, "networkIOHist.io = [];");
		append(r, "networkIOHist.classifierName = \"Classifier\";");
		append(r, "networkIOHist.accuracyRange = 0.0;");
		append(r, "networkIOHist.maxValue = 99.0;");
		append(r, "networkIOHist.graphWidth = 50;");
		append(r, "networkIOHist.maxTableRows = 30;");
		
		renderProcessedNetworkIO(r);
		renderToHtmlTable(r);
		renderToHtmlTableRow(r);
		renderToHtmlTableColumns(r);
		renderToHtmlTableGraphColumns(r);
		renderToGraphValue(r);
		renderAppendHtmlTableRow(r);
	}

	protected void renderProcessedNetworkIO(StringBuilder r) {
		append(r, "changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "    if (key == \"networkIO\") {");
		append(r, "        networkIOHist.processedNetworkIO(newValue);");
		append(r, "    }");
		append(r, "});");
		append(r, "networkIOHist.processedNetworkIO = (networkIo) => {");
		append(r, "    networkIOHist.io[networkIOHist.io.length] = networkIo;");
		append(r, "    networkIOHist.appendHtmlTableRow(networkIo, networkIOHist.io.length);");
		append(r, "};");
	}
	
	protected void renderToHtmlTable(StringBuilder r) {
		append(r, "networkIOHist.toHtmlTable = () => {");
		append(r, "    var html = \"<table id='networkIOHistTable'>\";");
		append(r, "    html += \"<tr>\";");
		append(r, "    html += \"<th align='left'>DateTime</th>\";");
		append(r, "    html += \"<th align='right'>Value</th>\";");
		append(r, "    html += \"<th align='right'>Predicted</th>\";");
		append(r, "    html += \"<th colspan='\" + networkIOHist.graphWidth + \"' align='left'>Graph</th>\";");
		append(r, "    html += \"</tr>\";");
		append(r, "    var start = 0;");
		append(r, "    if (networkIOHist.maxTableRows && networkIOHist.io.length > networkIOHist.maxTableRows) {");
		append(r, "        start = networkIOHist.io.length - networkIOHist.maxTableRows;");
		append(r, "    }");
		append(r, "    for (var i = start; i < networkIOHist.io.length; i++) {");
		append(r, "        html += networkIOHist.toHtmlTableRow(networkIOHist.io[i], i);");
		append(r, "    }");
		append(r, "    html += \"</table>\";");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToHtmlTableRow(StringBuilder r) {
		append(r, "networkIOHist.toHtmlTableRow = (networkIo, index) => {");
		append(r, "    var html = \"<tr id='networkIOHistTable-row\" + index + \"'>\";");
		append(r, "    html += networkIOHist.toHtmlTableColumns(networkIo);");
		append(r, "    html += \"</tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToHtmlTableColumns(StringBuilder r) {
		append(r, "networkIOHist.toHtmlTableColumns = (networkIo) => {");
		append(r, "    var actualValue = networkIo.json.inputs.keyValues[1].value.value;");
		append(r, "    var predictedValue = '';");
		append(r, "    if (networkIOHist.io.length > 1) {");
		append(r, "        var prevClassification = networkIOHist.io[networkIOHist.io.length - 2].getClassification(networkIOHist.classifierName);");
		append(r, "        var predictions = prevClassification ? prevClassification.getPredictions() : [];");
		append(r, "        if (predictions.length==1) {");
		append(r, "            predictedValue = predictions[0];");
		append(r, "        }");
		append(r, "    }");
		append(r, "    var dateTime = new Date();");
		append(r, "    dateTime.setTime(networkIo.json.inputs.keyValues[0].value.value);");
		append(r, "    var html = \"<td align='right'>\" + dateTime.toISOString() + \"</td>\";");
		append(r, "    html += \"<td align='right'>\" + actualValue + \"</td>\";");
		append(r, "    html += \"<td align='right'>\" + predictedValue + \"</td>\";");
		append(r, "    html += networkIOHist.toHtmlTableGraphColumns(networkIo, actualValue, predictedValue);");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToHtmlTableGraphColumns(StringBuilder r) {
		append(r, "networkIOHist.toHtmlTableGraphColumns = (networkIo, actualValue, predictedValue) => {");
		append(r, "    var html = \"\";");
		append(r, "    for (var i = 0; i < networkIOHist.graphWidth; i++) {");
		append(r, "        var cls = \"bg-w\";");
		append(r, "        if (predictedValue && i == networkIOHist.toGraphValue(predictedValue)) {");
		append(r, "            cls = \"bg-r\";");
		append(r, "            var diff = actualValue - predictedValue;");
		append(r, "            diff = diff < 0 ? diff * -1.0 : diff;");
		append(r, "            if (diff <= networkIOHist.accuracyRange) {");
		append(r, "               cls = \"bg-g\";");
		append(r, "            }");
		append(r, "        } else if (i == networkIOHist.toGraphValue(actualValue)) {");
		append(r, "            cls = \"bg-b\";");
		append(r, "        }");
		append(r, "        html += \"<td class='\" + cls + \"'>&nbsp;</td>\";");
		append(r, "    }");
		append(r, "    return html;");
		append(r, "};");
	}
	
	protected void renderToGraphValue(StringBuilder r) {
		append(r, "networkIOHist.toGraphValue = (value) => {");
		append(r, "    return Math.round((value / networkIOHist.maxValue) * networkIOHist.graphWidth);");
		append(r, "};");
	}
	
	protected void renderAppendHtmlTableRow(StringBuilder r) {
		append(r, "networkIOHist.appendHtmlTableRow = (networkIo, index) => {");
		append(r, "    var table = window.document.getElementById(\"networkIOHistTable\");");
		append(r, "    if (table) {");
		append(r, "        var tr = window.document.createElement(\"TR\");");
		append(r, "        tr.id = \"networkIOHistTable-row\" + index;");
		append(r, "        tr.innerHTML = networkIOHist.toHtmlTableColumns(networkIo);");
		append(r, "        table.appendChild(tr);");
		append(r, "        if (networkIOHist.maxTableRows && networkIOHist.io.length > networkIOHist.maxTableRows) {");
		append(r, "            var remove = networkIOHist.io.length - networkIOHist.maxTableRows;");
		append(r, "            var remTr = window.document.getElementById(\"networkIOHistTable-row\" + remove);");
		append(r, "            if (remTr) {");
		append(r, "                table.removeChild(remTr);");
		append(r, "            }");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");
	}
}
