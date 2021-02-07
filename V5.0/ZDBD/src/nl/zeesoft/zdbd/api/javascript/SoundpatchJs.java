package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class SoundpatchJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var soundpatch = soundpatch || {};");
		append(r,"soundpatch.select = function() {");
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
