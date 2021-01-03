package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class SequenceJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var sequence = sequence || {};");
		append(r,"sequence.show = false;");
		append(r,"sequence.selectedPattern = 0;");
		append(r,"sequence.refresh = function() {");
		append(r,"    main.xhr.getText(\"/sequenceEditor.txt\",sequence.refreshCallback,sequence.errorCallback);");
		append(r,"};");
		append(r,"sequence.refreshCallback = function(response) {");
		//append(r,"    console.log(response);");
		append(r,"    var elem = window.document.getElementById(\"sequenceEditor\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = response.responseText;");
		append(r,"    }");
		append(r,"};");
		append(r,"sequence.errorCallback = function(response) {");
		append(r,"    if (response.status==503) {");
		append(r,"        setTimeout(function() { sequencer.refresh(); }, 500);");
		append(r,"    }");
		append(r,"};");
		append(r,"sequence.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"sequenceEditor\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		append(r,"sequence.changedPatternSelect = function() {");
		append(r,"    sequence.refresh();");
		append(r,"}");
		append(r,"sequence.toggleShow = function() {");
		append(r,"    sequence.show = !sequence.show;");
		append(r,"    if (sequence.show) {");
		append(r,"        sequence.refresh();");
		append(r,"    } else {");
		append(r,"        sequence.clear();");
		append(r,"    }");
		append(r,"    var elem = window.document.getElementById(\"showTrainingSequence\");");
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
