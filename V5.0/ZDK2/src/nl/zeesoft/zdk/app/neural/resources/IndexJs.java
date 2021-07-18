package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.neural.resources.js.ApiJs;
import nl.zeesoft.zdk.app.resource.Resource;

public class IndexJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, (new ApiJs()).render());
		renderLoadApp(r);
		renderChangeListener(r);
		renderOnNetworkStateChanged(r);
		renderOnNetworkConfigChanged(r);
		renderOnNetworkSettingsChanged(r);
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
		append(r, "    } else if (key===\"networkConfig\") {");
		append(r, "        onNetworkConfigChanged(newValue);");
		append(r, "    } else if (key===\"networkSettings\") {");
		append(r, "        onNetworkSettingsChanged(newValue);");
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
	
	protected void renderOnNetworkConfigChanged(StringBuilder r) {
		append(r, "var onNetworkConfigChanged = (newValue) => {");
		append(r, "    dom.setInnerHTML(\"networkConfig\", networkConfig.toHtmlTable(newValue));");
		append(r, "};");
	}
	
	protected void renderOnNetworkSettingsChanged(StringBuilder r) {
		append(r, "var onNetworkSettingsChanged = (newValue) => {");
		append(r, "    dom.setInnerHTML(\"networkSettings\", networkSettings.toHtmlTable(newValue));");
		append(r, "};");
	}
	
	protected void renderOnNetworkStatsChanged(StringBuilder r) {
		append(r, "var onNetworkStatsChanged = (newValue) => {");
		append(r, "    dom.setInnerHTML(\"networkStats\", networkStats.toHtmlTable(newValue));");
		append(r, "};");
	}
}
