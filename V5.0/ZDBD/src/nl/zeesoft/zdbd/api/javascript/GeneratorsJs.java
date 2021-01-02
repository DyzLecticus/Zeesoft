package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class GeneratorsJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var generators = generators || {};");
		append(r,"generators.refresh = function() {");
		append(r,"    main.xhr.getText(\"/generators.txt\",generators.refreshCallback,generators.errorCallback);");
		append(r,"};");
		append(r,"generators.refreshCallback = function(response) {");
		//append(r,"    console.log(response);");
		append(r,"    var elem = window.document.getElementById(\"generators\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = response.responseText;");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.errorCallback = function(response) {");
		append(r,"    if (response.status==503) {");
		append(r,"        setTimeout(function() { generators.refresh(); }, 500);");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"generators\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.generateAll = function() {");
		append(r,"    var cb = function() { generators.refresh(); state.refresh(); };");
		append(r,"    main.xhr.postText(\"/generators.txt\",\"GENERATE_ALL\",cb,main.xhr.alertErrorCallback);");
		append(r,"};");
		append(r,"generators.generateSequence = function(name) {");
		append(r,"    var cb = function() { generators.refresh(); state.refresh(); };");
		append(r,"    main.xhr.postText(\"/generators.txt\",\"GENERATE:\" + name,cb,main.xhr.alertErrorCallback);");
		append(r,"};");
		return r;
	}
}
