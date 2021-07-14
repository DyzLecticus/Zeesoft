package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class ObjectLoaderJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderObjectLoaderObject(r);
		renderExecuteAndPublish(r);
	}

	protected void renderObjectLoaderObject(StringBuilder r) {
		append(r, "function ObjectLoader(url, key) {");
		append(r, "    var that = this;");
		append(r, "    this.request = new HttpRequest(\"GET\",url);");
		append(r, "    this.key = key;");
		append(r, "    this.execute = () => {");
		append(r, "        ObjectLoader.executeAndPublish(that);");
		append(r, "    };");
		append(r, "};");
	}

	protected void renderExecuteAndPublish(StringBuilder r) {
		append(r, "ObjectLoader.executeAndPublish = (loader) => {");
		append(r, "    loader.request.execute((xhr) => {");
		append(r, "        let value = xhr.response;");
		append(r, "        if (xhr.getResponseHeader(\"Content-Type\")===\"application/json\") {");
		append(r, "            value = JSON.parse(xhr.response);");
		append(r, "        }");
		append(r, "        changePublisher.setValue(loader.key, value);");
		append(r, "    });");
		append(r, "};");
	}
}
