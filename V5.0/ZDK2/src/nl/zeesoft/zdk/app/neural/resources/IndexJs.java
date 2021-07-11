package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.resource.Resource;

public class IndexJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderHttpRequest(r);
		append(r, "var request = new HttpRequest(\"GET\",\"/app/state.txt\");");
		append(r, "request.execute((xhr) => { console.log(xhr.response); });");
	}
	
	protected void renderHttpRequest(StringBuilder r) {
		renderOnReadyStateChange(r);
		renderExecuteHttpRequest(r);
		renderHttpRequestObject(r);
	}

	protected void renderHttpRequestObject(StringBuilder r) {
		append(r, "function HttpRequest(method, url) {");
		append(r, "    var that = this;");
		append(r, "    this.method = method;");
		append(r, "    this.url = url;");
		append(r, "    this.contentType = \"application/text\";");
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
		append(r, "    xhr.setRequestHeader(\"Content-type\",this.contentType);");
		append(r, "    xhr.send();");
		append(r, "    return xhr;");
		append(r, "};");
	}
	/*
	protected void renderHttpRequestOnReadyStateChange(StringBuilder r) {
		append(r,"HttpRequest.prototype.onReadyStateChange = (xhr,request,successCallback) => {");
		append(r,"    if (xhr.readyState == 4) {");
		append(r,"        if (xhr.status == 200) {");
		append(r,"            successCallback(xhr);");
		append(r,"        } else {");
		append(r,"            request.errorCallback(xhr);");
		append(r,"        }");
		append(r,"    }");
		append(r,"};");
	}
	
	protected void renderHttpRequestErrorCallback(StringBuilder r) {
		append(r,"HttpRequest.prototype.errorCallback = (xhr) => {");
		append(r,"    console.error(\"Post request response error: \" + xhr.status + \" \" + xhr.responseText);");
		append(r,"};");
	}
	
	protected void renderHttpRequestExecute(StringBuilder r) {
		append(r, "HttpRequest.prototype.execute = (successCallback) => {");
		append(r,"    var that = this;");
		append(r,"    var xhr = new XMLHttpRequest();");
		append(r,"    xhr.onreadystatechange = () => {");
		append(r,"        HttpRequest.prototype.onReadyStateChange(this,that,successCallback);");
		append(r,"    }");
		append(r,"    xhr.open(this.method,this.url,true);");
		append(r,"    xhr.setRequestHeader(\"Content-type\",this.contentType);");
		append(r,"    xhr.send();");
		append(r,"    return xhr;");
		append(r, "};");
	}
	*/
}
