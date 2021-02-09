package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class SoundpatchJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var soundpatch = soundpatch || {};");
		append(r,"soundpatch.showInstruments = false;");
		append(r,"soundpatch.refresh = function() {");
		append(r,"    if (soundpatch.showInstruments) {");
		append(r,"        main.xhr.getText(\"/soundpatch.txt\",soundpatch.refreshCallback,soundpatch.errorCallback);");
		append(r,"    }");
		append(r,"};");
		append(r,"soundpatch.refreshCallback = function(response) {");
		append(r,"    var elem = window.document.getElementById(\"instrumentList\");");
		append(r,"    if (elem!=null && soundpatch.showInstruments) {");
		append(r,"        elem.innerHTML = response.responseText;");
		append(r,"    }");
		append(r,"};");
		append(r,"soundpatch.errorCallback = function(response) {");
		append(r,"    if (response.status==503) {");
		append(r,"        setTimeout(function() { soundpatch.refresh(); }, 500);");
		append(r,"    }");
		append(r,"};");
		append(r,"soundpatch.toggleShowInstruments = function() {");
		append(r,"    var elem = window.document.querySelector('input[name=\"selectSoundPatch\"]:checked');");
		append(r,"    if (elem!=null && elem.value!=null && elem.value.length>0) {");
		append(r,"        main.xhr.postText(\"/soundpatch.txt\",\"LOAD:\" + elem.value,function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"        modal.hide();");
		append(r,"    } else {");
		append(r,"        alert(\"Select a sound patch to load\");");
		append(r,"    }");
		append(r,"};");
		append(r,"soundpatch.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"instrumentList\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		append(r,"soundpatch.toggleShowInstruments = function(property) {");
		append(r,"    soundpatch.showInstruments = !soundpatch.showInstruments;");
		append(r,"    if (soundpatch.showInstruments) {");
		append(r,"        main.dom.startFadeIn(\"instrumentList\");");
		append(r,"        soundpatch.refresh();");
		append(r,"        property.value = \"-\";");
		append(r,"    } else {");
		append(r,"        soundpatch.clear();");
		append(r,"        property.value = \"+\";");
		append(r,"    }");
		append(r,"};");
		append(r,"soundpatch.load = function() {");
		append(r,"    modal.load(\"SelectSoundPatch\");");
		append(r,"};");
		append(r,"soundpatch.loadSoundPatch = function() {");
		append(r,"    var elem = window.document.querySelector('input[name=\"selectSoundPatch\"]:checked');");
		append(r,"    if (elem!=null && elem.value!=null && elem.value.length>0) {");
		append(r,"        main.xhr.postText(\"/soundpatch.txt\",\"LOAD:\" + elem.value,function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"        modal.hide();");
		append(r,"    } else {");
		append(r,"        alert(\"Select a sound patch to load\");");
		append(r,"    }");
		append(r,"};");
		return r;
	}
}
