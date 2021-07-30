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
}
