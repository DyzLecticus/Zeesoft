package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdk.Str;

public class GeneratorsJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var generators = generators || {};");
		append(r,"generators.editName = \"\";");
		append(r,"generators.showList = false;");
		append(r,"generators.refresh = function() {");
		append(r,"    generators.refreshStatus();");
		append(r,"    if (generators.showList) {");
		append(r,"        main.xhr.getText(\"/generators.txt\",generators.refreshCallback,generators.errorCallback);");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.refreshStatus = function() {");
		append(r,"    main.xhr.getText(\"/generatorStatus.txt\",generators.refreshStatusCallback,generators.errorCallback);");
		append(r,"};");
		append(r,"generators.refreshStatusCallback = function(response) {");
		append(r,"    var obj = main.xhr.parseResponseText(response.responseText);");
		append(r,"    var elem = window.document.getElementById(\"generateSequences\");");
		append(r,"    if (elem!=null) {");
		append(r,"        if (obj.isGenerating || !obj.canGenerate) {");
		append(r,"            elem.disabled = true;");
		append(r,"        } else {");
		append(r,"            elem.disabled = false;");
		append(r,"        }");
		append(r,"        if (obj.isGenerating) {");
		append(r,"            setTimeout(function() { generators.refreshStatus(); }, 1000);");
		append(r,"        }");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.refreshCallback = function(response) {");
		append(r,"    var elem = window.document.getElementById(\"generatorList\");");
		append(r,"    if (elem!=null && generators.showList) {");
		append(r,"        elem.innerHTML = response.responseText;");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.errorCallback = function(response) {");
		append(r,"    if (response.status==503) {");
		append(r,"        setTimeout(function() { generators.refresh(); }, 500);");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.clear = function() {");
		append(r,"    var elem = window.document.getElementById(\"generatorList\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.toggleShowList = function(property) {");
		append(r,"    generators.showList = !generators.showList;");
		append(r,"    if (generators.showList) {");
		append(r,"        main.dom.startFadeIn(\"generatorList\");");
		append(r,"        generators.refresh();");
		append(r,"        property.value = \"-\";");
		append(r,"    } else {");
		append(r,"        generators.clear();");
		append(r,"        property.value = \"+\";");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.generateAll = function() {");
		append(r,"    var cb = function() { generators.refresh(); state.refresh(); };");
		append(r,"    main.xhr.postText(\"/generators.txt\",\"GENERATE_ALL\",cb,main.xhr.alertErrorCallback);");
		append(r,"};");
		append(r,"generators.generateSequence = function(name) {");
		append(r,"    var cb = function() { generators.refresh(); state.refresh(); };");
		append(r,"    main.xhr.postText(\"/generators.txt\",\"GENERATE:\" + name,cb,main.xhr.alertErrorCallback);");
		append(r,"};");
		append(r,"generators.moveUp = function(name) {");
		append(r,"    generators.move(\"MOVE_UP\",name);");
		append(r,"};");
		append(r,"generators.moveDown = function(name) {");
		append(r,"    generators.move(\"MOVE_DOWN\",name);");
		append(r,"};");
		append(r,"generators.move = function(direction,name) {");
		append(r,"    var cb = function() { generators.refresh(); sequencer.refresh(); };");
		append(r,"    main.xhr.postText(\"/generators.txt\",direction + \":\" + name,cb,main.xhr.alertErrorCallback);");
		append(r,"};");
		append(r,"generators.delete = function(name) {");
		append(r,"    var d = confirm(\"Are you sure you want to delete the generator?\");");
		append(r,"    if (d==true) {");
		append(r,"        var cb = function() { generators.refresh(); sequencer.refresh(); };");
		append(r,"        main.xhr.postText(\"/generators.txt\",\"DELETE:\" + name,cb,main.xhr.alertErrorCallback);");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.edit = function(name) {");
		append(r,"    generators.editName = name;");
		append(r,"    main.xhr.postText(\"/generator.txt\",\"EDIT:\" + name,generators.editCallback,main.xhr.alertErrorCallback);");
		append(r,"};");
		append(r,"generators.editCallback = function(response) {");
		append(r,"    modal.loadCallback(response);");
		append(r,"};");
		append(r,"generators.editDone = function() {");
		append(r,"    generators.refresh();");
		append(r,"    sequencer.refresh();");
		append(r,"    modal.hide();");
		append(r,"};");
		append(r,"generators.propertyChange = function(property) {");
		append(r,"    if (property.id==\"name\" && property.value.length==0) {");
		append(r,"        alert(\"Generator name is mandatory\");");
		append(r,"        return;");
		append(r,"    }");
		append(r,"    var body = \"SET_PROPERTY:\";");
		append(r,"    body += generators.editName;");
		append(r,"    body += \":\";");
		append(r,"    body += property.id;");
		append(r,"    body += \":\";");
		append(r,"    body += main.dom.getElementValue(property.id);");
		append(r,"    var cb = null;");
		append(r,"    if (property.id==\"name\") {");
		append(r,"        cb = function() { generators.editName = property.value; generators.refresh(); };");
		append(r,"    }");
		append(r,"    main.xhr.postText(\"/generator.txt\",body,cb,main.xhr.alertErrorCallback);");
		append(r,"}");
		append(r,"generators.add = function() {");
		append(r,"    generators.editName = \"\";");
		append(r,"    main.xhr.postText(\"/generator.txt\",\"ADD\",generators.editCallback,main.xhr.alertErrorCallback);");
		append(r,"};");
		append(r,"generators.addCancel = function() {");
		append(r,"    modal.hide();");
		append(r,"};");
		append(r,"generators.addDone = function() {");
		append(r,"    var name = main.dom.getElementValue(\"name\");");
		append(r,"    if (name==null || name.length==0) {");
		append(r,"        alert(\"Generator name is mandatory\");");
		append(r,"        return;");
		append(r,"    }");
		append(r,"    var ids = [\"name\",\"group1Distortion\",\"group2Distortion\",\"group3Distortion\",\"randomChunkOffset\",\"mixStart\",\"mixEnd\",\"maintainBeat\",\"maintainFeedback\"];");
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			append(r,"    ids[ids.length] = \"maintain-");
			r.sb().append(inst.name());
			r.sb().append("\";");
		}
		append(r,"    var obj = main.dom.buildBodyText(ids);");
		append(r,"    var cb = function() { modal.hide(); generators.refresh(); sequencer.refresh(); };");
		append(r,"    main.xhr.postText(\"/generator.txt\",\"SAVE\\n\" + obj,cb,main.xhr.alertErrorCallback);");
		append(r,"};");
		return r;
	}
}
