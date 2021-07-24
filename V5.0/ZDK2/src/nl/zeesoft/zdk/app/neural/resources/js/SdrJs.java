package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class SdrJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var dom = dom || {};");
		renderSdrObject(r);
		renderFromStr(r);
		renderBitIsOn(r);
		renderGetWidthHeight(r);
		renderAddChangeListener(r);
		renderChangedSdr(r);
		renderActivateTableColumn(r);
		renderDeactivateTableColumn(r);
		renderToHtmlTable(r);
		renderToHtmlTableColumn(r);
	}

	protected void renderSdrObject(StringBuilder r) {
		append(r, "function Sdr(length) {");
		append(r, "    var that = this;");
		append(r, "    this.length = length || 1;");
		append(r, "    this.onBits = [];");
		append(r, "    this.fromString = (str) => {");
		append(r, "        return Sdr.fromStr(str, that);");
		append(r, "    };");
		append(r, "    this.isOn = (bit) => {");
		append(r, "        return Sdr.bitIsOn(that, bit);");
		append(r, "    };");
		append(r, "    this.toHtml = (id) => {");
		append(r, "        return Sdr.toHtmlTable(that, id);");
		append(r, "    };");
		append(r, "};");
	}

	protected void renderFromStr(StringBuilder r) {
		append(r, "Sdr.fromStr = (str, sdr) => {");
		append(r, "    if (!sdr) {;");
		append(r, "        sdr = new Sdr();");
		append(r, "    };");
		append(r, "    sdr.onBits = [];");
		append(r, "    var elems = str.split(\",\");");
		append(r, "    for (var i = 0; i < elems.length; i++) {");
		append(r, "        if (i == 0) {");
		append(r, "            sdr.length = parseInt(elems[i], 10);");
		append(r, "        } else {");
		append(r, "            sdr.onBits[sdr.onBits.length] = parseInt(elems[i], 10);");
		append(r, "        }");
		append(r, "    }");
		append(r, "    return sdr;");
		append(r, "};");
	}

	protected void renderBitIsOn(StringBuilder r) {
		append(r, "Sdr.bitIsOn = (sdr, bit) => {");
		append(r, "    var r = false;");
		append(r, "    for (var i = 0; i < sdr.onBits.length; i++) {");
		append(r, "        if (sdr.onBits[i] == bit) {");
		append(r, "            r = true;");
		append(r, "            break;");
		append(r, "        }");
		append(r, "    }");
		append(r, "    return r;");
		append(r, "};");
	}

	protected void renderGetWidthHeight(StringBuilder r) {
		append(r, "Sdr.getWidthHeight = (length) => {");
		append(r, "    var width = Math.sqrt(length);");
		append(r, "    if (width > 100) {");
		append(r, "        width = 100;");
		append(r, "    }");
		append(r, "    var height = length / width;");
		append(r, "    if (width * height < length) {");
		append(r, "        width++;");
		append(r, "    }");
		append(r, "    if (width * height < length) {");
		append(r, "        height++;");
		append(r, "    }");
		append(r, "    return { width, height }");
		append(r, "};");
	}

	protected void renderAddChangeListener(StringBuilder r) {
		append(r, "Sdr.addChangeListener = (tableId) => {");
		append(r, "    changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "        if (key == tableId) {");
		append(r, "            Sdr.changedSdr(tableId, oldValue, newValue);");
		append(r, "        }");
		append(r, "    });");
		append(r, "};");
	}

	protected void renderChangedSdr(StringBuilder r) {
		append(r, "Sdr.changedSdr = (tableId, oldValue, newValue) => {");
		append(r, "    activateBits = [];");
		append(r, "    deactivateBits = [];");
		append(r, "    if (oldValue) {");
		append(r, "        for (var i = 0; i < oldValue.onBits.length; i++) {");
		append(r, "            if (!newValue.isOn(oldValue.onBits[i])) {");
		append(r, "                Sdr.deactivateTableColumn(tableId, oldValue.onBits[i]);");
		append(r, "            }");
		append(r, "        }");
		append(r, "    }");
		append(r, "    for (var i = 0; i < newValue.onBits.length; i++) {");
		append(r, "        if (!oldValue || !oldValue.isOn(newValue.onBits[i])) {");
		append(r, "            Sdr.activateTableColumn(tableId, newValue.onBits[i]);");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");
	}

	protected void renderActivateTableColumn(StringBuilder r) {
		append(r, "Sdr.activateTableColumn = (tableId, bit) => {");
		append(r, "    elem = window.document.getElementById(tableId + \"-\" + bit);");
		append(r, "    if (elem) {");
		append(r, "        elem.classList.add(\"bg-b\");");
		append(r, "        elem.classList.remove(\"bg-w\");");
		append(r, "    };");
		append(r, "};");
	}

	protected void renderDeactivateTableColumn(StringBuilder r) {
		append(r, "Sdr.deactivateTableColumn = (tableId, bit) => {");
		append(r, "    elem = window.document.getElementById(tableId + \"-\" + bit);");
		append(r, "    if (elem) {");
		append(r, "        elem.classList.add(\"bg-w\");");
		append(r, "        elem.classList.remove(\"bg-b\");");
		append(r, "    };");
		append(r, "};");
	}

	protected void renderToHtmlTable(StringBuilder r) {
		append(r, "Sdr.toHtmlTable = (sdr, id) => {");
		append(r, "    var size = Sdr.getWidthHeight(sdr.length);");
		append(r, "    var bit = 0;");
		append(r, "    var html = \"<table id='\" + id + \"' class='sdr'>\";");
		append(r, "    for (var i = 0; i < size.height; i++) {");
		append(r, "        html += \"<tr>\";");
		append(r, "        for (var j = 0; j < size.width; j++) {");
		append(r, "            html += Sdr.toHtmlTableColumn(id, bit);");
		append(r, "            bit++;");
		append(r, "        }");
		append(r, "        html += \"</tr>\";");
		append(r, "    }");
		append(r, "    html += \"</table>\";");
		append(r, "    return html;");
		append(r, "};");
	}

	protected void renderToHtmlTableColumn(StringBuilder r) {
		append(r, "Sdr.toHtmlTableColumn = (idPrefix, bit) => {");
		append(r, "    var cls = \"bg-w\";");
		append(r, "    var html = \"<td id='\" + idPrefix + \"-\" + bit + \"' class='bg-w'>\";");
		append(r, "    html += \"&nbsp;\";");
		append(r, "    html += \"</td>\";");
		append(r, "    return html;");
		append(r, "};");
	}
}
