package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class ObjectLoaderJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderObjectLoaderObject(r);
		renderExecuteAndPublish(r);
		renderParseResponse(r);
		renderToggleRefresh(r);
		renderExecuteRefresh(r);
	}

	protected void renderObjectLoaderObject(StringBuilder r) {
		append(r, "function ObjectLoader(url, key) {");
		append(r, "    var that = this;");
		append(r, "    this.request = new HttpRequest(\"GET\",url);");
		append(r, "    this.key = key;");
		append(r, "    this.autoRefresh = false;");
		append(r, "    this.autoRefreshing = false;");
		append(r, "    this.autoRefreshMs = 10000;");
		append(r, "    this.execute = (callback) => {");
		append(r, "        ObjectLoader.executeAndPublish(that, callback);");
		append(r, "    };");
		append(r, "    this.toggleAutoRefresh = (input) => {");
		append(r, "        ObjectLoader.toggleRefresh(that, input);");
		append(r, "    };");
		append(r, "    this.refresh = (input) => {");
		append(r, "        ObjectLoader.executeRefresh(that, input);");
		append(r, "    };");
		append(r, "};");
	}

	protected void renderExecuteAndPublish(StringBuilder r) {
		append(r, "ObjectLoader.executeAndPublish = (loader, callback) => {");
		append(r, "    loader.request.execute((xhr) => {");
		append(r, "        changePublisher.setValue(loader.key, ObjectLoader.parseResponse(xhr));");
		append(r, "        if (loader.autoRefresh && !loader.autoRefreshing) {");
		append(r, "            setTimeout(() => {");
		append(r, "                if (loader.autoRefresh) {");
		append(r, "                    loader.execute();");
		append(r, "                } else {");
		append(r, "                    loader.autoRefreshing = false;");
		append(r, "                }");
		append(r, "            }, loader.autoRefreshMs);");
		append(r, "        }");
		append(r, "        if (callback) {");
		append(r, "            callback(xhr);");
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
	
	protected void renderExecuteRefresh(StringBuilder r) {
		append(r, "ObjectLoader.executeRefresh = (loader, input) => {");
		append(r, "    if (!input.disabled) {");
		append(r, "        input.disabled = true;");
		append(r, "        loader.execute((xhr) => { input.disabled = false });");
		append(r, "    }");
		append(r, "};");
	}
}
