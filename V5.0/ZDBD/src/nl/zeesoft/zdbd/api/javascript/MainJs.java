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
		append(r,"            if (successCallback) {");
		append(r,"                successCallback(xhr);");
		append(r,"            }");
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
		append(r,"            if (obj[keyValue[0]]==\"true\") {");
		append(r,"                obj[keyValue[0]] = true;");
		append(r,"            } else if (obj[keyValue[0]]==\"false\") {");
		append(r,"                obj[keyValue[0]] = false;");
		append(r,"            }");
		append(r,"        }");
		append(r,"    } catch (err) {");
		append(r,"        obj = {};");
		append(r,"        obj.error = \"Failed to parse text\";");
		append(r,"    }");
		append(r,"    return obj;");
		append(r,"};");
		append(r,"main.dom = {};");
		append(r,"main.dom.getElementValue = function(id) {");
		append(r,"    var r = null;");
		append(r,"    var elem = window.document.getElementById(id);");
		append(r,"    if (elem!=null) {");
		append(r,"        if (elem.nodeName==\"SELECT\") {");
		append(r,"            r = elem.options[elem.selectedIndex].value;");
		append(r,"        } else if (elem.type==\"text\" || elem.type==\"number\" || elem.type==\"any\") {");
		append(r,"            r = elem.value;");
		append(r,"        } else if (elem.type==\"checkbox\") {");
		append(r,"            r = elem.checked;");
		append(r,"        }");
		append(r,"    }");
		append(r,"    return r;");
		append(r,"};");
		append(r,"main.dom.buildBodyText = function(ids) {");
		append(r,"    var r = \"\";");
		append(r,"    for (var i = 0; i < ids.length; i++) {");
		append(r,"        r += ids[i] + \":\" + main.dom.getElementValue(ids[i]) + \"\\n\";");
		append(r,"    }");
		append(r,"    return r;");
		append(r,"};");
		append(r,"main.dom.startFadeIn = function(id) {");
		append(r,"    var elem = window.document.getElementById(id);");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.style.opacity = 0;");
		append(r,"        elem.opacity = 0;");
		append(r,"        main.dom.fadeIn(id);");
		append(r,"    }");
		append(r,"};");
		append(r,"main.dom.fadeIn = function(id) {");
		append(r,"    var elem = window.document.getElementById(id);");
		append(r,"    if (elem!=null) {");
		append(r,"        if (elem.opacity<1) {");
		append(r,"            elem.opacity += 0.1;");
		append(r,"            elem.style.opacity = elem.opacity;");
		append(r,"        }");
		append(r,"        console.log(elem.style.opacity);");
		append(r,"        if (elem.opacity<1) {");
		append(r,"            setTimeout(function() { main.dom.fadeIn(id) }, 50);");
		append(r,"        } else {");
		append(r,"            elem.opacity = 1;");
		append(r,"        }");
		append(r,"    }");
		append(r,"};");
		append(r,"main.dom.startFadeOut = function(id) {");
		append(r,"    var elem = window.document.getElementById(id);");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.style.opacity = 1;");
		append(r,"        elem.opacity = 1;");
		append(r,"        main.dom.fadeOut(id);");
		append(r,"    }");
		append(r,"};");
		append(r,"main.dom.fadeOut = function(id) {");
		append(r,"    var elem = window.document.getElementById(id);");
		append(r,"    if (elem!=null) {");
		append(r,"        if (elem.opacity>0) {");
		append(r,"            elem.opacity -= 0.1;");
		append(r,"            elem.style.opacity = elem.opacity;");
		append(r,"        }");
		append(r,"        console.log(elem.style.opacity);");
		append(r,"        if (elem.opacity>0) {");
		append(r,"            setTimeout(function() { main.dom.fadeOut(id) }, 50);");
		append(r,"        } else {");
		append(r,"            elem.opacity = 0;");
		append(r,"        }");
		append(r,"    }");
		append(r,"};");
		return r;
	}
}
