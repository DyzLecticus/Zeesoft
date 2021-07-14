package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class IndexJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, (new ApiJs()).render());
		renderLoadApp(r);
		renderChangeListener(r);
		renderOnNetworkStateChanged(r);
		renderOnNetworkStatsChanged(r);
	}
	
	protected void renderLoadApp(StringBuilder r) {
		append(r, "var loadApp = () => {");
		append(r, "    changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "        console.log(key + \" was \" + oldValue + \" is \" + newValue);");
		append(r, "    });");
		append(r, "    loadApiObjects();");
		append(r, "};");
	}
	
	protected void renderChangeListener(StringBuilder r) {
		append(r, "changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "    if (key===\"networkState\") {");
		append(r, "        onNetworkStateChanged(newValue);");
		append(r, "    } else if (key===\"networkStats\") {");
		append(r, "        onNetworkStatsChanged(newValue);");
		append(r, "    }");
		append(r, "});");
	}
	
	protected void renderOnNetworkStateChanged(StringBuilder r) {
		append(r, "var onNetworkStateChanged = (newValue) => {");
		append(r, "    var elem = document.getElementById(\"networkStateText\");");
		append(r, "    if (elem!=null) {");
		append(r, "        elem.innerHTML = newValue");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderOnNetworkStatsChanged(StringBuilder r) {
		append(r, "var onNetworkStatsChanged = (newValue) => {");
		append(r, "    var elem = document.getElementById(\"networkStats\");");
		append(r, "    if (elem!=null) {");
		append(r, "        elem.innerHTML = networkStats.toHtmlTable(newValue);");
		append(r, "    }");
		append(r, "};");
	}
}
