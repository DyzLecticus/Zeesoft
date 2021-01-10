package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdbd.pattern.instruments.Bass;
import nl.zeesoft.zdbd.pattern.instruments.Hihat;
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
		append(r,"    if (elem!=null && sequence.show) {");
		append(r,"        main.dom.startFadeIn(\"sequenceEditor\");");
		append(r,"        elem.innerHTML = response.responseText;");
		append(r,"    }");
		append(r,"};");
		append(r,"sequence.errorCallback = function(response) {");
		append(r,"    if (response.status==503) {");
		append(r,"        setTimeout(function() { sequence.refresh(); }, 500);");
		append(r,"    }");
		append(r,"};");
		append(r,"sequence.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"sequenceEditor\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		append(r,"sequence.changedSequencePatternSelect = function(property) {");
		append(r,"    var index = property.id.split(\"-\")[1];");
		append(r,"    var value = property.options[property.selectedIndex].value;");
		append(r,"    var body = \"SET_SEQUENCE_PATTERN:\";");
		append(r,"    body += index;");
		append(r,"    body += \":\";");
		append(r,"    body += value;");
		append(r,"    main.xhr.postText(\"/sequenceEditor.txt\",body,network.refreshHeader,sequence.errorCallback);");
		append(r,"}");
		append(r,"sequence.changedPatternSelect = function(property) {");
		append(r,"    var select = property.options[property.selectedIndex].value;");
		append(r,"    if (select!=sequence.selectedPattern) {");
		append(r,"        elem = window.document.getElementById(\"pattern\" + sequence.selectedPattern);");
		append(r,"        if (elem!=null) {");
		append(r,"            elem.classList.add(\"hidden\");");
		append(r,"        }");
		append(r,"        sequence.selectedPattern = select;");
		append(r,"        elem = window.document.getElementById(\"pattern\" + sequence.selectedPattern);");
		append(r,"        if (elem!=null) {");
		append(r,"            elem.classList.remove(\"hidden\");");
		append(r,"        }");
		append(r,"    }");
		append(r,"}");
		append(r,"sequence.clickedStepValue = function(property) {");
		//append(r,"    console.log(property.id + \"=\" + property.value)");
		append(r,"    var elems = property.id.split(\"-\");");
		append(r,"    var name = elems[0];");
		append(r,"    var step = parseInt(elems[1],10);");
		append(r,"    var value = parseInt(property.value,10);");
		append(r,"    var newValue = value + 1;");
		append(r,"    var maxValue = 2;");
		append(r,"    if (name==\"" + Hihat.NAME + "\") {");
		append(r,"        maxValue = 4;");
		append(r,"    }");
		append(r,"    if (name==\"" + Bass.NAME + "\") {");
		append(r,"        maxValue = 16;");
		append(r,"    }");
		append(r,"    if (newValue>maxValue) {");
		append(r,"        newValue = 0;");
		append(r,"    }");
		append(r,"    var oldClass=\"grey\";");
		append(r,"    var newClass=\"grey\";");
		append(r,"    if (value!=0) {");
		append(r,"        if (value % 2 == 0) {");
		append(r,"            oldClass = \"yellow\";");
		append(r,"        } else {");
		append(r,"            oldClass = \"orange\";");
		append(r,"        }");
		append(r,"    }");
		append(r,"    if (newValue!=0) {");
		append(r,"        if (newValue % 2 == 0) {");
		append(r,"            newClass = \"yellow\";");
		append(r,"        } else {");
		append(r,"            newClass = \"orange\";");
		append(r,"        }");
		append(r,"    }");
		append(r,"    if (oldClass.length>0) {");
		append(r,"        property.classList.remove(oldClass);");
		append(r,"    }");
		append(r,"    if (newClass.length>0) {");
		append(r,"        property.classList.add(newClass);");
		append(r,"    }");
		append(r,"    property.value = newValue;");
		append(r,"    var body = \"SET_PATTERN_STEP_VALUE:\";");
		append(r,"    body += sequence.selectedPattern;");
		append(r,"    body += \":\";");
		append(r,"    body += name;");
		append(r,"    body += \":\";");
		append(r,"    body += step;");
		append(r,"    body += \":\";");
		append(r,"    body += newValue;");
		append(r,"    main.xhr.postText(\"/sequenceEditor.txt\",body,network.refreshHeader,sequence.errorCallback);");
		append(r,"}");
		append(r,"sequence.changedStepValue = function(property) {");
		append(r,"    var elems = property.id.split(\"-\");");
		append(r,"    var name = elems[0];");
		append(r,"    var step = parseInt(elems[1],10);");
		append(r,"    var newValue = property.options[property.selectedIndex].value;");
		append(r,"    var body = \"SET_PATTERN_STEP_VALUE:\";");
		append(r,"    body += sequence.selectedPattern;");
		append(r,"    body += \":\";");
		append(r,"    body += name;");
		append(r,"    body += \":\";");
		append(r,"    body += step;");
		append(r,"    body += \":\";");
		append(r,"    body += newValue;");
		append(r,"    main.xhr.postText(\"/sequenceEditor.txt\",body,network.refreshHeader,sequence.errorCallback);");
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
