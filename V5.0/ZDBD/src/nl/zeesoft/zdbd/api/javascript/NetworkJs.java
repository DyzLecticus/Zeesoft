package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class NetworkJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var network = network || {};");
		append(r,"network.refresh = function() {");
		append(r,"    main.xhr.getText(\"/network.txt\",network.refreshCallback,network.errorCallback);");
		append(r,"    main.xhr.getText(\"/networkStatistics.txt\",network.refreshStatisticsCallback,network.errorCallback);");
		append(r,"};");
		append(r,"network.refreshCallback = function(response) {");
		//append(r,"    console.log(response);");
		append(r,"    var obj = main.xhr.parseResponseText(response.responseText);");
		//append(r,"    console.log(obj);");
		append(r,"    var elem = window.document.getElementById(\"trainNetwork\");");
		append(r,"    if (elem!=null) {");
		append(r,"        if (obj.isTraining || !obj.needsTraining) {");
		append(r,"            elem.disabled = true;");
		append(r,"        } else {");
		append(r,"            elem.disabled = false;");
		append(r,"        }");
		append(r,"        if (obj.needsTraining) {");
		append(r,"            elem.value = \"Train*\";");
		append(r,"        } else {");
		append(r,"            elem.value = \"Train\";");
		append(r,"        }");
		append(r,"    }");
		append(r,"    if (obj.isTraining==\"true\") {");
		append(r,"        setTimeout(function() { network.refresh(); }, 1000);");
		append(r,"    }");
		append(r,"};");
		append(r,"network.errorCallback = function(response) {");
		append(r,"    if (response.status==503) {");
		append(r,"        setTimeout(function() { network.refresh(); }, 500);");
		append(r,"    }");
		append(r,"};");
		append(r,"network.refreshStatisticsCallback = function(response) {");
		//append(r,"    console.log(response);");
		append(r,"    var elem = window.document.getElementById(\"statistics\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = response.responseText;");
		append(r,"    }");
		append(r,"};");
		append(r,"network.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"statistics\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		append(r,"network.train = function() {");
		append(r,"    var cb = function() { network.refresh(); state.refresh(); };");
		append(r,"    main.xhr.postText(\"/network.txt\",\"TRAIN\",cb,main.xhr.alertErrorCallback);");
		append(r,"};");
		return r;
	}
}
