package nl.zeesoft.zdk.app.neural.resources.js;

import java.util.TreeMap;

import nl.zeesoft.zdk.app.resource.Resource;

public class UtilJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "var util = util || {};");
		renderObjectFromMap(r);
		renderObjectToMap(r);
	}

	protected void renderObjectFromMap(StringBuilder r) {
		append(r, "util.objectFromMap = function(map) {");
		append(r, "    var r = {};");
		append(r, "    for (var i = 0; i < map.keyValues.length; i++) {");
		append(r, "        var kv = map.keyValues[i];");
		append(r, "        r[kv.key.value] = kv.value.value;");
		append(r, "    }");
		append(r, "    return r;");
		append(r, "};");
	}

	protected void renderObjectToMap(StringBuilder r) {
		append(r, "util.objectToMap = function(obj, valCls, keyCls, mapCls) {");
		append(r, "    keyCls = keyCls || \"" + String.class.getName() + "\";");
		append(r, "    valCls = valCls || keyCls;");
		append(r, "    mapCls = mapCls || \"" + TreeMap.class.getName() + "\";");
		append(r, "    keyValues = [];");
		append(r, "    for (var key in obj) {");
		append(r, "        var ko = { className: keyCls, value: key };");
		append(r, "        var vo = { className: valCls, value: obj[key] };");
		append(r, "        var kv = { key: ko, value: vo };");
		append(r, "        keyValues[keyValues.length] = kv;");
		append(r, "    }");
		append(r, "    return { className: mapCls, keyValues };");
		append(r, "};");
	}
}
