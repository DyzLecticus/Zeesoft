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
		append(r, "function HttpRequest(method, url) {");
		append(r, "    var that = this;");
		append(r, "    this.method = method;");
		append(r, "    this.url = url;");
		append(r, "    this.contentType = \"application/text\";");
		append(r, "    this.retryDelayMs = 250;");
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
		append(r, "            successCallback(xhr);");
		append(r, "        } else if (xhr.status == 503) {");
		append(r, "            setTimeout(() => { xhr.request.execute(successCallback); }, xhr.request.retryDelayMs);");
		append(r, "        } else {");
		append(r, "            xhr.request.errorCallback(xhr);");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");
	}

	protected void renderExecuteHttpRequest(StringBuilder r) {
		append(r, "HttpRequest.executeHttpRequest = (request, successCallback ) => {");
		append(r, "    var xhr = new XMLHttpRequest();");
		append(r, "    xhr.request = request;");
		append(r, "    xhr.onreadystatechange = () => {");
		append(r, "        HttpRequest.onReadyStateChange(xhr, successCallback);");
		append(r, "    };");
		append(r, "    xhr.open(request.method,request.url,true);");
		append(r, "    xhr.setRequestHeader(\"Content-Type\",request.contentType);");
		append(r, "    xhr.send();");
		append(r, "    return xhr;");
		append(r, "};");
	}
}
