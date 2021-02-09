package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class ChordsJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var chords = chords || {};");
		append(r,"chords.show = false;");
		append(r,"chords.refresh = function() {");
		append(r,"    if (chords.show) {");
		append(r,"        main.xhr.getText(\"/chordEditor.txt\",chords.refreshCallback,chords.errorCallback);");
		append(r,"    }");
		append(r,"};");
		append(r,"chords.refreshCallback = function(response) {");
		append(r,"    var elem = window.document.getElementById(\"chordEditor\");");
		append(r,"    if (elem!=null && chords.show) {");
		append(r,"        if (elem.innerHTML==null || elem.innerHTML == \"\") {");
		append(r,"            main.dom.startFadeIn(\"chordEditor\");");
		append(r,"        }");
		append(r,"        elem.innerHTML = response.responseText;");
		append(r,"    }");
		append(r,"};");
		append(r,"chords.errorCallback = function(response) {");
		append(r,"    if (response.status==503) {");
		append(r,"        setTimeout(function() { chords.refresh(); }, 500);");
		append(r,"    }");
		append(r,"};");
		append(r,"chords.alertErrorCallback = function(response) {");
		append(r,"    main.xhr.alertErrorCallback(response);");
		append(r,"    chords.refresh();");
		append(r,"};");
		append(r,"chords.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"chordEditor\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		append(r,"chords.changedStep = function(property) {");
		append(r,"    var elems = property.id.split(\"-\");");
		append(r,"    var chord = parseInt(elems[1],10);");
		append(r,"    var value = parseInt(property.value,10);");
		append(r,"    if (value<0) {");
		append(r,"        property.value = 0;");
		append(r,"        value = 0;");
		append(r,"    }");
		append(r,"    if (value!=chord) {");
		append(r,"        var body = \"SET_CHORD_STEP:\";");
		append(r,"        body += chord;");
		append(r,"        body += \":\";");
		append(r,"        body += value;");
		append(r,"        main.xhr.postText(\"/chordEditor.txt\",body,chords.refresh,chords.alertErrorCallback);");
		append(r,"    }");
		append(r,"};");
		append(r,"chords.changedBaseNote = function(property) {");
		append(r,"    var elems = property.id.split(\"-\");");
		append(r,"    var chord = elems[1];");
		append(r,"    var value = property.options[property.selectedIndex].value;");
		append(r,"    var body = \"SET_CHORD_BASE_NOTE:\";");
		append(r,"    body += chord;");
		append(r,"    body += \":\";");
		append(r,"    body += value;");
		append(r,"    main.xhr.postText(\"/chordEditor.txt\",body,function() {},chords.alertErrorCallback);");
		append(r,"};");
		append(r,"chords.changedInterval = function(property) {");
		append(r,"    var elems = property.id.split(\"-\");");
		append(r,"    var chord = elems[1];");
		append(r,"    var interval = elems[2];");
		append(r,"    var value = parseInt(property.value,10);");
		append(r,"    if (value<0) {");
		append(r,"        property.value = 0;");
		append(r,"        value = 0;");
		append(r,"    }");
		append(r,"    if (value>12) {");
		append(r,"        property.value = 12;");
		append(r,"        value = 12;");
		append(r,"    }");
		append(r,"    var body = \"SET_CHORD_INTERVAL:\";");
		append(r,"    body += chord;");
		append(r,"    body += \":\";");
		append(r,"    body += interval;");
		append(r,"    body += \":\";");
		append(r,"    body += value;");
		append(r,"    main.xhr.postText(\"/chordEditor.txt\",body,function() {},chords.alertErrorCallback);");
		append(r,"};");
		append(r,"chords.delete = function(chord) {");
		append(r,"    var d = confirm(\"Are you sure you want to delete the chord?\");");
		append(r,"    if (d==true) {");
		append(r,"        var body = \"DELETE:\";");
		append(r,"        body += chord;");
		append(r,"        main.xhr.postText(\"/chordEditor.txt\",body,chords.refresh,chords.alertErrorCallback);");
		append(r,"    }");
		append(r,"};");
		append(r,"chords.add = function() {");
		append(r,"    var body = \"ADD\";");
		append(r,"    main.xhr.postText(\"/chordEditor.txt\",body,chords.refresh,chords.alertErrorCallback);");
		append(r,"};");
		append(r,"chords.toggleShow = function() {");
		append(r,"    chords.show = !chords.show;");
		append(r,"    if (chords.show) {");
		append(r,"        chords.refresh();");
		append(r,"    } else {");
		append(r,"        chords.clear();");
		append(r,"    }");
		append(r,"    var elem = window.document.getElementById(\"showChordChanges\");");
		append(r,"    if (elem!=null) {");
		append(r,"        if (sequence.show) {");
		append(r,"            elem.value = \"-\";");
		append(r,"        } else {");
		append(r,"            elem.value = \"+\";");
		append(r,"        }");
		append(r,"    }");
		append(r,"};");
		return r;
	}
}
