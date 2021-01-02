package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class StateJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var state = state || {};");
		append(r,"state.refresh = function() {");
		append(r,"    main.xhr.getText(\"/state.txt\",state.refreshCallback);");
		append(r,"};");
		append(r,"state.refreshCallback = function(response) {");
		//append(r,"    console.log(response);");
		append(r,"    if (response.status==200) {");
		append(r,"        var obj = main.xhr.parseResponseText(response.responseText);");
		//append(r,"        console.log(obj);");
		append(r,"        var elem = window.document.getElementById(\"state\");");
		append(r,"        if (elem!=null) {");
		append(r,"            var html = obj.text;");
		append(r,"            html += \":&nbsp;\";");
		append(r,"            html += Math.round(obj.donePercentage * 100);");
		append(r,"            html += \"%\";");
		append(r,"            elem.innerHTML = html;");
		append(r,"        }");
		append(r,"        if (obj.donePercentage<1) {");
		append(r,"            setTimeout(function() { state.refresh(); }, 200);");
		append(r,"        } else if (obj.donePercentage==1) {");
		append(r,"            state.refreshApp();");
		append(r,"            setTimeout(function() { state.clear(); }, 1000);");
		append(r,"        }");
		append(r,"    }");
		append(r,"};");
		append(r,"state.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"state\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		append(r,"state.refreshApp = function() {");
		append(r,"    theme.refresh();");
		append(r,"    sequencer.refresh();");
		append(r,"};");
		append(r,"state.onload = function() {");
		append(r,"    state.refresh();");
		append(r,"};");
		return r;
	}
}
