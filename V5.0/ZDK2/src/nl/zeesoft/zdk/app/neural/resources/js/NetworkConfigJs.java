package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class NetworkConfigJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var networkConfig = networkConfig || {};");
		renderProcessorsByLayer(r);
		renderProcessorsByLayerHeight(r);
		renderGetWidthHeight(r);
		renderToInputsString(r);
		renderToHtmlTable(r);
		renderToHtmlTableRow(r);
		renderProcessorToHtmlTable(r);
		renderProcessorToHtmlTableRow(r);
		renderGetPropertyValue(r);
		renderProcessorLinksToHtmlTableRow(r);
	}

	protected void renderProcessorsByLayer(StringBuilder r) {
		append(r, "networkConfig.getProcessorsPerLayer = (json) => {");
		append(r, "    var height = 0;");
		append(r, "    for (var i = 0; i < json.processorConfigs.length; i++) {");
		append(r, "        var pConfig = json.processorConfigs[i];");
		append(r, "        if ((pConfig.layer + 1) > height) {");
		append(r, "            height = (pConfig.layer + 1);");
		append(r, "        }");
		append(r, "    }");
		append(r, "    return networkConfig.getProcessorsPerLayerHeight(json, height);");
		append(r, "};");
	}

	protected void renderProcessorsByLayerHeight(StringBuilder r) {
		append(r, "networkConfig.getProcessorsPerLayerHeight = (json, height) => {");
		append(r, "    var r = [];");
		append(r, "    for (var i = 0; i < height; i++) {");
		append(r, "        r[i] = [];");
		append(r, "    }");
		append(r, "    for (var i = 0; i < json.processorConfigs.length; i++) {");
		append(r, "        var pConfig = json.processorConfigs[i];");
		append(r, "        r[pConfig.layer][r[pConfig.layer].length] = pConfig;");
		append(r, "    }");
		append(r, "    return r;");
		append(r, "};");
	}

	protected void renderGetWidthHeight(StringBuilder r) {
		append(r, "networkConfig.getWidthHeight = (processorsByLayer) => {");
		append(r, "    var height = processorsByLayer.length;");
		append(r, "    var width = 0;");
		append(r, "    for (var i = 0; i < height; i++) {");
		append(r, "        if (processorsByLayer[i].length > width) {");
		append(r, "            width = processorsByLayer[i].length;");
		append(r, "        }");
		append(r, "    }");
		append(r, "    return { width, height };");
		append(r, "};");
	}
	
	protected void renderToInputsString(StringBuilder r) {
		append(r, "networkConfig.toInputsString = (json) => {");
		append(r, "    var html = \"<p class='mt-0'>Inputs: \";");
		append(r, "    for (var i = 0; i < json.inputNames.length; i++) {");
		append(r, "        if (i > 0) {");
		append(r, "            html += \", \";");
		append(r, "        }");
		append(r, "        html += \"<b>\" + json.inputNames[i].value + \"</b>\";");
		append(r, "    }");
		append(r, "    html += \"</p>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderToHtmlTable(StringBuilder r) {
		append(r, "networkConfig.toHtmlTable = (json) => {");
		append(r, "    var processorsByLayer = networkConfig.getProcessorsPerLayer(json);");
		append(r, "    var { width, height } = networkConfig.getWidthHeight(processorsByLayer);");
		append(r, "    var html = networkConfig.toInputsString(json);");
		append(r, "    html += \"<table class='padded'>\";");
		append(r, "    for (var i = 0; i < height; i++) {");
		append(r, "        html += networkConfig.toHtmlTableRow(processorsByLayer,i);");
		append(r, "    }");
		append(r, "    html += \"</table>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderToHtmlTableRow(StringBuilder r) {
		append(r, "networkConfig.toHtmlTableRow = (processorsByLayer, layer) => {");
		append(r, "    var html = \"<tr>\";");
		append(r, "    for (var j = 0; j < processorsByLayer[layer].length; j++) {");
		append(r, "        html += \"<td>\";");
		append(r, "        html += \"<b>\" + processorsByLayer[layer][j].name + \"</b>\";");
		append(r, "        html += networkConfig.processorToHtmlTable(processorsByLayer[layer][j]);");
		append(r, "        html += \"</td>\";");
		append(r, "    }");
		append(r, "    html += \"</tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderProcessorToHtmlTable(StringBuilder r) {
		append(r, "networkConfig.processorToHtmlTable = (processor) => {");
		append(r, "    var props = processor.config || processor.encoder;");
		append(r, "    var html = \"<table>\";");
		append(r, "    for (var name in props) {");
		append(r, "        if (name!==\"className\") {");
		append(r, "            html += networkConfig.processorToHtmlTableRow(name, props[name]);");
		append(r, "        }");
		append(r, "    }");
		append(r, "    html += networkConfig.processorLinksToHtmlTableRow(processor.inputLinks);");
		append(r, "    html += \"</table>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderProcessorToHtmlTableRow(StringBuilder r) {
		append(r, "networkConfig.processorToHtmlTableRow = (name, value) => {");
		append(r, "    var html = \"<tr>\";");
		append(r, "    html += \"<td>\" + name + \"</td>\";");
		append(r, "    html += \"<td>\" + networkConfig.getPropertyValue(value) + \"</td>\";");
		append(r, "    html += \"</tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderGetPropertyValue(StringBuilder r) {
		append(r, "networkConfig.getPropertyValue = (value) => {");
		append(r, "    if (value.className) {");
		append(r, "        value = value.x + \"*\" + value.y + \"*\" + value.z;");
		append(r, "    }");
		append(r, "    return value;");
		append(r, "};");
	}

	protected void renderProcessorLinksToHtmlTableRow(StringBuilder r) {
		append(r, "networkConfig.processorLinksToHtmlTableRow = (links) => {");
		append(r, "    var ordered = [];");
		append(r, "    for (var i = 0; i < links.length; i++) {");
		append(r, "        ordered[links[i].toInput] = links[i].fromName + \" / \" + links[i].fromOutput;");
		append(r, "    }");
		append(r, "    var html = \"<tr><td colspan='2'><b>Input links</b><ul>\";");
		append(r, "    for (var i = 0; i < ordered.length; i++) {");
		append(r, "        html += \"<li>\" + ordered[i] + \"</li>\";");
		append(r, "    }");
		append(r, "    html += \"</ul></td></tr>\";");
		append(r, "    return html;");
		append(r, "};");
	}
}
