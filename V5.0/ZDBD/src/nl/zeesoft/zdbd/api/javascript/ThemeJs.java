package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class ThemeJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var theme = theme || {};");
		append(r,"theme.refresh = function() {");
		append(r,"    main.xhr.getText(\"/theme.txt\",theme.refreshCallback,theme.errorCallback);");
		append(r,"};");
		append(r,"theme.refreshCallback = function(response) {");
		//append(r,"    console.log(response);");
		append(r,"    var obj = main.xhr.parseResponseText(response.responseText);");
		//append(r,"    console.log(obj);");
		append(r,"    var elem = window.document.getElementById(\"theme\");");
		append(r,"    if (elem!=null) {");
		append(r,"        var html = '<div class=\"column-left column-padding column-label\">Name</div>';");
		append(r,"        html += '<div class=\"column-left column-padding\">';");
		append(r,"        html += \"<b>\";");
		append(r,"        html += obj.name;");
		append(r,"        html += \"</b>&nbsp;(\";");
		append(r,"        html += Math.round(obj.beatsPerMinute);");
		append(r,"        html += \"&nbsp;BPM,&nbsp;\";");
		append(r,"        html += obj.beatsPerPattern;");
		append(r,"        html += \"/\";");
		append(r,"        html += obj.stepsPerBeat;");
		append(r,"        html += \")\";");
		append(r,"        html += \"</div>\";");
		append(r,"        elem.innerHTML = html;");
		append(r,"    }");
		append(r,"};");
		append(r,"theme.errorCallback = function(response) {");
		append(r,"    if (response.status==503) {");
		append(r,"        setTimeout(function() { theme.refresh(); }, 500);");
		append(r,"    }");
		append(r,"};");
		append(r,"theme.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"theme\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		return r;
	}
}
