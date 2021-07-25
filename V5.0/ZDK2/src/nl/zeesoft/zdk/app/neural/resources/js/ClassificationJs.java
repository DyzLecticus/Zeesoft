package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class ClassificationJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderClassificationObject(r);
		renderGetMostCountedValues(r);
	}

	protected void renderClassificationObject(StringBuilder r) {
		append(r, "function Classification(json) {");
		append(r, "    var that = this;");
		append(r, "    this.json = json;");
		append(r, "    this.getPredictions = () => {");
		append(r, "        return Classification.getMostCountedValues(that);");
		append(r, "    };");
		append(r, "};");
	}

	protected void renderGetMostCountedValues(StringBuilder r) {
		append(r, "Classification.getMostCountedValues = (classification) => {");
		append(r, "    var r = [];");
		append(r, "    var max = 0.0;");
		append(r, "    for (var i = 0; i < classification.json.valueCounts.keyValues.length; i++) {");
		append(r, "        var valueCount = classification.json.valueCounts.keyValues[i];");
		append(r, "        var count = parseInt(valueCount.value.value, 10);");
		append(r, "        var value = parseFloat(valueCount.key.value);");
		append(r, "        if (count>max) {");
		append(r, "            max = count;");
		append(r, "            r = [value] ");
		append(r, "        } else if (count==max) {");
		append(r, "            r[r.length] = value;");
		append(r, "        }");
		append(r, "    }");
		append(r, "    return r;");
		append(r, "};");
	}
}
