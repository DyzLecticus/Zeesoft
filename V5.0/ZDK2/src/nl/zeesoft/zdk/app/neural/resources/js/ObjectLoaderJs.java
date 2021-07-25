package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class ObjectLoaderJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderObjectLoaderObject(r);
		renderExecuteAndPublish(r);
		renderParseResponse(r);
		renderToggleRefresh(r);
	}

	protected void renderObjectLoaderObject(StringBuilder r) {
		append(r, "function ObjectLoader(url, key) {");
		append(r, "    var that = this;");
		append(r, "    this.request = new HttpRequest(\"GET\",url);");
		append(r, "    this.key = key;");
		append(r, "    this.autoRefresh = false;");
		append(r, "    this.autoRefreshing = false;");
		append(r, "    this.autoRefreshMs = 10000;");
		append(r, "    this.execute = () => {");
		append(r, "        ObjectLoader.executeAndPublish(that);");
		append(r, "    };");
		append(r, "    this.toggleAutoRefresh = (input) => {");
		append(r, "        ObjectLoader.toggleRefresh(that, input);");
		append(r, "    };");
		append(r, "};");
	}

	protected void renderExecuteAndPublish(StringBuilder r) {
		append(r, "ObjectLoader.executeAndPublish = (loader) => {");
		append(r, "    loader.request.execute((xhr) => {");
		append(r, "        changePublisher.setValue(loader.key, ObjectLoader.parseResponse(xhr));");
		append(r, "        if (loader.autoRefresh) {");
		append(r, "            setTimeout(() => { if (loader.autoRefresh) loader.execute(); }, loader.autoRefreshMs);");
		append(r, "        } else {");
		append(r, "            loader.autoRefreshing = false;");
		append(r, "        }");
		append(r, "    });");
		append(r, "};");
	}

	protected void renderParseResponse(StringBuilder r) {
		append(r, "ObjectLoader.parseResponse = (xhr) => {");
		append(r, "    var value = xhr.response;");
		append(r, "    if (xhr.getResponseHeader(\"Content-Type\")===\"application/json\") {");
		append(r, "        value = JSON.parse(xhr.response);");
		append(r, "    }");
		append(r, "    return value");
		append(r, "};");
	}
	
	protected void renderToggleRefresh(StringBuilder r) {
		append(r, "ObjectLoader.toggleRefresh = (loader, input) => {");
		append(r, "    var newValue = input === true || input.checked === true;");
		append(r, "    if (newValue!=loader.autoRefresh) {");
		append(r, "        loader.autoRefresh = newValue;");
		append(r, "        if (loader.autoRefresh && !loader.autoRefreshing) {");
		append(r, "            loader.autoRefreshing = true;");
		append(r, "            loader.execute();");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");
	}
}
