package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class HttpRequestJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderHttpRequestObject(r);
		renderOnReadyStateChange(r);
		renderExecuteHttpRequest(r);
	}

	protected void renderHttpRequestObject(StringBuilder r) {
		append(r, "function HttpRequest(method, url, body) {");
		append(r, "    var that = this;");
		append(r, "    this.method = method;");
		append(r, "    this.url = url;");
		append(r, "    this.body = body;");
		append(r, "    this.headers = {\"Content-Type\": \"application/text\"};");
		append(r, "    this.retryDelayMs = 250;");
		append(r, "    this.maxRetries = 40;");
		append(r, "    this.retries = 0;");
		append(r, "    this.errorCallback = (xhr) => {};");
		append(r, "    this.execute = (successCallback) => {");
		append(r, "        HttpRequest.executeHttpRequest(that,successCallback);");
		append(r, "    };");
		append(r, "};");
	}

	protected void renderOnReadyStateChange(StringBuilder r) {
		append(r, "HttpRequest.onReadyStateChange = (xhr, successCallback) => {");
		append(r, "    if (xhr.readyState == 4) {");
		append(r, "        if (xhr.status == 200) {");
		append(r, "            xhr.request.retries = 0;");
		append(r, "            successCallback(xhr);");
		append(r, "        } else if (xhr.status == 503 && xhr.request.retries < xhr.request.maxRetries) {");
		append(r, "            xhr.request.retries++;");
		append(r, "            setTimeout(() => { xhr.request.execute(successCallback); }, xhr.request.retryDelayMs);");
		append(r, "        } else {");
		append(r, "            xhr.request.errorCallback(xhr);");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");
	}

	protected void renderExecuteHttpRequest(StringBuilder r) {
		append(r, "HttpRequest.executeHttpRequest = (request, successCallback) => {");
		append(r, "    var xhr = new XMLHttpRequest();");
		append(r, "    xhr.request = request;");
		append(r, "    xhr.onreadystatechange = () => {");
		append(r, "        HttpRequest.onReadyStateChange(xhr, successCallback);");
		append(r, "    };");
		append(r, "    xhr.open(request.method,request.url,true);");
		append(r, "    for (name in request.headers) {");
		append(r, "        xhr.setRequestHeader(name,request.headers[name]);");
		append(r, "    }");
		append(r, "    xhr.send(request.body);");
		append(r, "    return xhr;");
		append(r, "};");
	}
}
