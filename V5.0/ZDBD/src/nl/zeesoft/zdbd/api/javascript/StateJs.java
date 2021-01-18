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
		append(r,"    var obj = main.xhr.parseResponseText(response.responseText);");
		append(r,"    var elem = window.document.getElementById(\"progressBar\");");
		append(r,"    if (elem!=null) {");
		append(r,"        var perc = Math.round(obj.donePercentage * 100) + \"%\";");
		append(r,"        elem.style.width = perc;");
		append(r,"        elem.innerHTML = perc;");
		append(r,"    }");
		append(r,"    var elem = window.document.getElementById(\"footer\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.style.opacity = 1;");
		append(r,"    }");
		append(r,"    if (obj.donePercentage<1) {");
		append(r,"        setTimeout(function() { state.refresh(); }, 200);");
		append(r,"    } else if (obj.donePercentage==1) {");
		append(r,"        state.refreshApp();");
		append(r,"        setTimeout(function() { main.dom.startFadeOut(\"footer\");  }, 250);");
		append(r,"        setTimeout(function() { state.clear();  }, 1000);");
		append(r,"    }");
		append(r,"};");
		append(r,"state.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"state\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"    var elem = window.document.getElementById(\"progressBar\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.style.width = \"0%\";");
		append(r,"    }");
		append(r,"};");
		append(r,"state.refreshApp = function() {");
		append(r,"    theme.refresh();");
		append(r,"    sequencer.refresh();");
		append(r,"    sequence.refresh();");
		append(r,"    network.refresh();");
		append(r,"    generators.refresh();");
		append(r,"};");
		append(r,"state.onload = function() {");
		append(r,"    state.refresh();");
		append(r,"    if (bindings) {");
		append(r,"        bindings.onload();");
		append(r,"    }");
		append(r,"    state.refreshApp();");
		append(r,"};");
		return r;
	}
}
