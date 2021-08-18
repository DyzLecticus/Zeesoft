package nl.zeesoft.zdk.app.neural.resources.js;

import java.util.TreeMap;

import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.SdrPngHandler;
import nl.zeesoft.zdk.app.resource.Resource;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class NetworkIOJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var networkIO = networkIO || {};");
		append(r, "networkIO.selectedProcessor = '';");
		renderNetworkIOObject(r);
		
		renderNetworkConfigChangeListener(r);
		renderToHtmlSelect(r);
		renderChangedSelectedProcessor(r);
		renderProcessedNetworkIO(r);
		renderUpdateSdrImage(r);
		renderClearSdrImage(r);
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
		append(r, "            r = kv.value.outputValue;");
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
		append(r, "    var html = networkIO.toHtmlSelect(json);");
		append(r, "    dom.setInnerHTML(\"networkIO\", html);");
		append(r, "};");
	}

	protected void renderToHtmlSelect(StringBuilder r) {
		append(r, "networkIO.toHtmlSelect = (json) => {");
		append(r, "    var processorsByLayer = networkConfig.getProcessorsPerLayer(json);");
		append(r, "    var { width, height } = networkConfig.getWidthHeight(processorsByLayer);");
		append(r, "    var html = \"Network processor <select class='pb-3' onchange='networkIO.changedSelectedProcessor(this);'>\";");
		append(r, "    html += \"<option value='' SELECTED></option>\";");
		append(r, "    for (var i = 0; i < height; i++) {");
		append(r, "        for (var j = 0; j < processorsByLayer[i].length; j++) {");
		append(r, "            html += \"<option value='\" + processorsByLayer[i][j].name + \"'>\" +  processorsByLayer[i][j].name + \"</option>\";");
		append(r, "        }");
		append(r, "    }");
		append(r, "    html += \"</select><br /><br />\";");
		append(r, "    html += \"<img id='sdrImage' />\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderChangedSelectedProcessor(StringBuilder r) {
		append(r, "networkIO.changedSelectedProcessor = (select) => {");
		append(r, "    if (select.value!=networkIO.selectedProcessor) {");
		append(r, "        networkIO.selectedProcessor = select.value;");
		append(r, "    }");
		append(r, "};");
	}

	protected void renderProcessedNetworkIO(StringBuilder r) {
		append(r, "changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "    if (key == \"networkIO\") {");
		append(r, "        networkIO.processedNetworkIO(newValue);");
		append(r, "    } else if (key == \"networkIOAccordion:visible\") {");
		append(r, "        networkIO.clearSdrImage();");
		append(r, "    }");
		append(r, "});");
		append(r, "networkIO.processedNetworkIO = (networkIo) => {");
		append(r, "    if (changePublisher.keyValues[\"networkIOAccordion:visible\"]) {");
		append(r, "        networkIO.updateSdrImage(networkIo.json);");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderUpdateSdrImage(StringBuilder r) {
		append(r, "networkIO.updateSdrImage = (json) => {");
		append(r, "    var elem = window.document.getElementById(\"sdrImage\");");
		append(r, "    if (elem && networkIO.selectedProcessor) {");
		append(r, "        for (var i = 0; i < json.processorIO.keyValues.length; i++) {");
		append(r, "            var kv = json.processorIO.keyValues[i];");
		append(r, "            if (kv.key.value==networkIO.selectedProcessor && kv.value.outputs[0]) {");
		append(r, "                elem.src = \"" + SdrPngHandler.PATH + "?\" + kv.value.outputs[0].SdrStringConvertor;");
		append(r, "            }");
		append(r, "        }");
		append(r, "    } else {");
		append(r, "        networkIO.clearSdrImage();");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderClearSdrImage(StringBuilder r) {
		append(r, "networkIO.clearSdrImage = () => {");
		append(r, "    var elem = window.document.getElementById(\"sdrImage\");");
		append(r, "    if (elem) {");
		append(r, "        elem.src = \"\";");
		append(r, "    }");
		append(r, "};");
	}
}
