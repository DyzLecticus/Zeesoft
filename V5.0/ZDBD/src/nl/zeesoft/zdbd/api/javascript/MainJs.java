package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class MainJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var main = main || {};");
		append(r,"main.xhr = {};");
		append(r,"main.xhr.onReadyStateChange = function(xhr,successCallback,errorCallback) {");
		append(r,"    if (xhr.readyState == 4) {");
		append(r,"        if (xhr.status == 200) {");
		append(r,"            successCallback(xhr);");
		append(r,"        } else if (errorCallback!=null) {");
		append(r,"            errorCallback(xhr);");
		append(r,"        } else {");
		append(r,"            main.xhr.defaultErrorCallback(xhr);");
		append(r,"        }");
		append(r,"    }");
		append(r,"};");
		append(r,"main.xhr.getText = function(url,successCallback,errorCallback) {");
		append(r,"    var xhr = new XMLHttpRequest();");
		append(r,"    xhr.onreadystatechange = function() {");
		append(r,"        main.xhr.onReadyStateChange(this,successCallback,errorCallback);");
		append(r,"    }");
		append(r,"    xhr.open(\"GET\",url,true);");
		append(r,"    xhr.setRequestHeader(\"Content-type\",\"application/text\");");
		append(r,"    xhr.send();");
		append(r,"    return xhr;");
		append(r,"};");
		append(r,"main.xhr.postText = function(url,text,successCallback,errorCallback) {");
		append(r,"    var xhr = new XMLHttpRequest();");
		append(r,"    xhr.onreadystatechange = function() {");
		append(r,"        main.xhr.onReadyStateChange(this,successCallback,errorCallback);");
		append(r,"    }");
		append(r,"    xhr.open(\"POST\",url,true);");
		append(r,"    xhr.setRequestHeader(\"Content-type\",\"application/text\");");
		append(r,"    xhr.send(text);");
		append(r,"    return xhr;");
		append(r,"};");
		append(r,"main.xhr.defaultErrorCallback = function(xhr) {");
		append(r,"    console.error(\"Post request response error: \" + xhr.status + \" \" + xhr.responseText);");
		append(r,"};");
		append(r,"main.xhr.alertErrorCallback = function(xhr) {");
		append(r,"    alert(xhr.responseText);");
		append(r,"};");
		append(r,"main.xhr.parseResponseText = function(text) {");
		append(r,"    var obj = {};");
		append(r,"    try {");
		append(r,"        var lines = text.split(\"\\n\");");
		append(r,"        for (var i = 0; i < lines.length; i++) {");
		append(r,"            var keyValue = lines[i].split(\":\");");
		append(r,"            obj[keyValue[0]] = keyValue[1];");
		append(r,"        }");
		append(r,"    } catch (err) {");
		append(r,"        obj = {};");
		append(r,"        obj.error = \"Failed to parse text\";");
		append(r,"    }");
		append(r,"    return obj;");
		append(r,"};");
		return r;
	}
}
