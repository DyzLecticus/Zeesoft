package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class NetworkIOJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var networkIO = networkIO || {};");
		append(r, "networkIO.initializedSdrTables = false;");
		renderNetworkConfigChangeListener(r);
		renderToHtmlTable(r);
		renderToHtmlTableRow(r);
		renderProcessedNetworkIO(r);
		renderInitializeSdrTables(r);
	}

	protected void renderNetworkConfigChangeListener(StringBuilder r) {
		append(r, "changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "    if (key == \"networkConfig\") {");
		append(r, "        networkIO.configChanged(newValue);");
		append(r, "    }");
		append(r, "});");
		append(r, "networkIO.configChanged = (json) => {");
		append(r, "    var html = networkIO.toHtmlTable(json);");
		append(r, "    dom.setInnerHTML(\"networkIO\", html);");
		append(r, "};");
	}

	protected void renderToHtmlTable(StringBuilder r) {
		append(r, "networkIO.toHtmlTable = (json) => {");
		append(r, "    var processorsByLayer = networkConfig.getProcessorsPerLayer(json);");
		append(r, "    var { width, height } = networkConfig.getWidthHeight(processorsByLayer);");
		append(r, "    var html = \"<table class='padded'>\";");
		append(r, "    for (var i = 0; i < height; i++) {");
		append(r, "        html += networkIO.toHtmlTableRow(processorsByLayer,i);");
		append(r, "    }");
		append(r, "    html += \"</table>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderToHtmlTableRow(StringBuilder r) {
		append(r, "networkIO.toHtmlTableRow = (processorsByLayer, layer) => {");
		append(r, "    var html = \"<tr>\";");
		append(r, "    for (var j = 0; j < processorsByLayer[layer].length; j++) {");
		append(r, "        html += \"<td>\";");
		append(r, "        html += \"<b>\" + processorsByLayer[layer][j].name + \"</b>\";");
		append(r, "        html += \"<div id='\" + processorsByLayer[layer][j].name + \"OutputSdrs'></div>\";");
		append(r, "        html += \"</td>\";");
		append(r, "    }");
		append(r, "    html += \"</tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderProcessedNetworkIO(StringBuilder r) {
		append(r, "changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "    if (key == \"networkIO\") {");
		append(r, "        networkIO.processedNetworkIO(newValue);");
		append(r, "    }");
		append(r, "});");
		append(r, "networkIO.processedNetworkIO = (json) => {");
		append(r, "    if (!networkIO.initializedSdrTables) {");
		append(r, "        networkIO.initializeSdrTables(json);");
		append(r, "    }");
		append(r, "    for (var i = 0; i < json.processorIO.keyValues.length; i++) {");
		append(r, "        var kv = json.processorIO.keyValues[i];");
		append(r, "        var sdr = Sdr.fromStr(kv.value.outputs[0].SdrStringConvertor);");
		append(r, "        var sdrTableId = kv.key.value + \"OutputSdr0\";");
		append(r, "        changePublisher.setValue(sdrTableId, sdr);");
		append(r, "    }");
		append(r, "};");
	}

	protected void renderInitializeSdrTables(StringBuilder r) {
		append(r, "networkIO.initializeSdrTables = (json) => {");
		append(r, "    for (var i = 0; i < json.processorIO.keyValues.length; i++) {");
		append(r, "        var kv = json.processorIO.keyValues[i];");
		append(r, "        var id = kv.key.value + \"OutputSdrs\";");
		append(r, "        var sdr = Sdr.fromStr(kv.value.outputs[0].SdrStringConvertor);");
		append(r, "        var sdrTableId = kv.key.value + \"OutputSdr0\";");
		append(r, "        dom.setInnerHTML(id, sdr.toHtml(sdrTableId));");
		append(r, "        Sdr.addChangeListener(sdrTableId);");
		append(r, "    }");
		append(r, "    networkIO.initializedSdrTables = true;");
		append(r, "};");
	}
}
