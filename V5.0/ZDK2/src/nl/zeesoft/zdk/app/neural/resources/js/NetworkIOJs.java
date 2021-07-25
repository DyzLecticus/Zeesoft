package nl.zeesoft.zdk.app.neural.resources.js;

import java.util.TreeMap;

import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.resource.Resource;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class NetworkIOJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var networkIO = networkIO || {};");
		append(r, "networkIO.renderedSdrTables = false;");
		append(r, "networkIO.initializedSdrTables = false;");
		renderNetworkIOObject(r);
		
		renderNetworkConfigChangeListener(r);
		renderToHtmlTable(r);
		renderToHtmlTableRow(r);
		renderProcessedNetworkIO(r);
		renderInitializeSdrTables(r);
		renderUpdateSdrTables(r);
	}
	
	protected void renderNetworkIOObject(StringBuilder r) {
		renderNetworkIOConstructor(r);
		renderExecuteNetworkIO(r);
		renderGetNewNetworkIO(r);
		renderGetClassificationByName(r);
	}

	protected void renderNetworkIOConstructor(StringBuilder r) {
		append(r, "function NetworkIO(json) {");
		append(r, "    var that = this;");
		append(r, "    this.json = json || NetworkIO.getNewNetworkIO();");
		append(r, "    this.execute = () => {");
		append(r, "        NetworkIO.executeNetworkIO(that);");
		append(r, "    };");
		append(r, "    this.getClassification = (classifierName) => {");
		append(r, "        return NetworkIO.getClassificationByName(that, classifierName);");
		append(r, "    };");
		append(r, "};");
	}

	protected void renderExecuteNetworkIO(StringBuilder r) {
		append(r, "NetworkIO.executeNetworkIO = (networkIo) => {");
		append(r, "    var request = new HttpRequest(\"POST\",\"" + NetworkIOJsonHandler.PATH + "\");");
		append(r, "    request.body = JSON.stringify(networkIo.json);");
		append(r, "    var callback = (xhr) => {");
		append(r, "        networkIo.json = JSON.parse(xhr.response);");
		append(r, "        changePublisher.setValue(\"networkIO\", networkIo);");
		append(r, "    };");
		append(r, "    request.execute(callback);");
		append(r, "};");
	}
	
	protected void renderGetNewNetworkIO(StringBuilder r) {
		append(r, "NetworkIO.getNewNetworkIO = () => {");
		append(r, "    return {");
		append(r, "        className: \"" + NetworkIO.class.getName() + "\",");
		append(r, "        inputs: {");
		append(r, "            className: \"" + TreeMap.class.getName() + "\",");
		append(r, "            keyValues: [");
		append(r, "                {");
		append(r, "                    key: {className:\"" + String.class.getName() + "\", value: \"DateTime\"},");
		append(r, "                    value: {className:\"" + Long.class.getName() + "\", value: 0}");
		append(r, "                },");
		append(r, "                {");
		append(r, "                    key: {className:\"" + String.class.getName() + "\", value: \"Value\"},");
		append(r, "                    value: {className:\"" + Float.class.getName() + "\", value: 0.0}");
		append(r, "                }");
		append(r, "            ]");
		append(r, "        }");
		append(r, "    };");
		append(r, "};");
	}
	
	protected void renderGetClassificationByName(StringBuilder r) {
		append(r, "NetworkIO.getClassificationByName = (networkIo, classifierName) => {");
		append(r, "    var r = null;");
		append(r, "    for (var i = 0; i < networkIo.json.processorIO.keyValues.length; i++) {");
		append(r, "        var kv = networkIo.json.processorIO.keyValues[i];");
		append(r, "        if (kv.key.value == classifierName && kv.value.outputValue.className) {");
		append(r, "            r = new Classification(kv.value.outputValue);");
		append(r, "        }");
		append(r, "    }");
		append(r, "    return r;");
		append(r, "};");
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
		append(r, "    networkIO.renderedSdrTables = true;");
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
		append(r, "networkIO.processedNetworkIO = (networkIo) => {");
		append(r, "    if (networkIO.renderedSdrTables) {");
		append(r, "        if (!networkIO.initializedSdrTables) {");
		append(r, "            networkIO.initializeSdrTables(networkIo.json);");
		append(r, "        }");
		append(r, "        networkIO.updateSdrTables(networkIo.json);");
		append(r, "    };");
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
	
	protected void renderUpdateSdrTables(StringBuilder r) {
		append(r, "networkIO.updateSdrTables = (json) => {");
		append(r, "    for (var i = 0; i < json.processorIO.keyValues.length; i++) {");
		append(r, "        var kv = json.processorIO.keyValues[i];");
		append(r, "        if (kv.value.outputs[0]) {");
		append(r, "            var sdr = Sdr.fromStr(kv.value.outputs[0].SdrStringConvertor);");
		append(r, "            var sdrTableId = kv.key.value + \"OutputSdr0\";");
		append(r, "            changePublisher.setValue(sdrTableId, sdr);");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");
	}
}
