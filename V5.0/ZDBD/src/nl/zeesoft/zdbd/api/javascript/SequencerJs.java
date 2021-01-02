package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class SequencerJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var sequencer = sequencer || {};");
		append(r,"sequencer.refresh = function() {");
		append(r,"    main.xhr.getText(\"/sequencer.txt\",sequencer.refreshCallback,sequencer.errorCallback);");
		append(r,"};");
		append(r,"sequencer.refreshCallback = function(response) {");
		//append(r,"    console.log(response);");
		append(r,"    if (response.status==200) {");
		append(r,"        var elem = window.document.getElementById(\"sequencer\");");
		append(r,"        if (elem!=null) {");
		append(r,"            elem.innerHTML = response.responseText;");
		append(r,"        }");
		append(r,"    }");
		append(r,"};");
		append(r,"sequencer.errorCallback = function(response) {");
		append(r,"    if (response.status==503) {");
		append(r,"        setTimeout(function() { sequencer.refresh(); }, 500);");
		append(r,"    }");
		append(r,"};");
		append(r,"sequencer.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"sequencer\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		append(r,"sequencer.propertyChange = function(property) {");
		append(r,"    var body = \"SET_PROPERTY:\";");
		append(r,"    body += property.id;");
		append(r,"    body += \":\";");
		append(r,"    if (property.nodeName==\"SELECT\") {");
		append(r,"        body += property.options[property.selectedIndex].value;");
		append(r,"    } else if (property.type==\"text\" || property.type==\"number\") {");
		append(r,"        body += property.value;");
		append(r,"    } else if (property.type==\"checkbox\") {");
		append(r,"        body += property.checked;");
		append(r,"    }");
		append(r,"    var cb = null;");
		append(r,"    if (property.id==\"beatsPerMinute\") {");
		append(r,"        cb = function() { theme.refresh(); };");
		append(r,"    } else if (property.id.indexOf(\"Sequence\")>0) {");
		append(r,"        cb = function() { sequencer.refresh(); };");
		append(r,"    }");
		append(r,"    main.xhr.postText(\"/sequencer.txt\",body,cb);");
		append(r,"}");
		append(r,"sequencer.playSequence = function() {");
		append(r,"    main.xhr.postText(\"/sequencer.txt\",\"PLAY_SEQUENCE\",sequencer.refresh,main.xhr.alertErrorCallback);");
		append(r,"}");
		append(r,"sequencer.playTheme = function() {");
		append(r,"    main.xhr.postText(\"/sequencer.txt\",\"PLAY_THEME\",sequencer.refresh,main.xhr.alertErrorCallback);");
		append(r,"}");
		append(r,"sequencer.stop = function() {");
		append(r,"    main.xhr.postText(\"/sequencer.txt\",\"STOP\",sequencer.refresh,main.xhr.alertErrorCallback);");
		append(r,"}");
		return r;
	}
}
