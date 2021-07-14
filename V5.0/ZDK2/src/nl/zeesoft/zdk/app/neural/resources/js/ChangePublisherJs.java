package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class ChangePublisherJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderChangePublisherObject(r);
		setAndPublishValue(r);
		renderInit(r);
	}

	protected void renderChangePublisherObject(StringBuilder r) {
		append(r, "function ChangePublisher() {");
		append(r, "    var that = this;");
		append(r, "    this.listeners = [];");
		append(r, "    this.keyValues = {};");
		append(r, "    this.addListener = (callback) => {");
		append(r, "        that.listeners[that.listeners.length] = callback;");
		append(r, "    };");
		append(r, "    this.setValue = (key, value) => {");
		append(r, "        ChangePublisher.setAndPublishValue(that, key, value);");
		append(r, "    };");
		append(r, "};");
	}

	protected void setAndPublishValue(StringBuilder r) {
		append(r, "ChangePublisher.setAndPublishValue = (publisher, key, value) => {");
		append(r, "    var oldValue = null;");
		append(r, "    if (publisher.keyValues[key]) {");
		append(r, "       oldValue = publisher.keyValues[key];");
		append(r, "    }");
		append(r, "    publisher.keyValues[key] = value;");
		append(r, "    for (var i = 0; i < publisher.listeners.length; i++) {");
		append(r, "        try {");
		append(r, "            publisher.listeners[i](key, oldValue, value);");
		append(r, "        } catch(error) {");
		append(r, "            console.error(error)");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderInit(StringBuilder r) {
		append(r, "var changePublisher = changePublisher || new ChangePublisher();");
	}
}
