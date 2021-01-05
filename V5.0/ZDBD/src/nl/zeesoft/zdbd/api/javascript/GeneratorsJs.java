package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class GeneratorsJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var generators = generators || {};");
		append(r,"generators.editName = \"\";");
		append(r,"generators.showList = false;");
		append(r,"generators.refresh = function() {");
		append(r,"    if (generators.showList) {");
		append(r,"        main.xhr.getText(\"/generators.txt\",generators.refreshCallback,generators.errorCallback);");
		append(r,"    }");
		append(r,"};");
		append(r,"generators.refreshCallback = function(response) {");
		//append(r,"    console.log(response);");
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
		append(r,"    main.xhr.postText(\"/generators.txt\",direction + \":\" + name,generators.refresh,main.xhr.alertErrorCallback);");
		append(r,"};");
		append(r,"generators.delete = function(name) {");
		append(r,"    var d = confirm(\"Are you sure you want to delete the generator?\");");
		append(r,"    if (d==true) {");
		append(r,"        main.xhr.postText(\"/generators.txt\",\"DELETE:\" + name,generators.refresh,main.xhr.alertErrorCallback);");
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
		append(r,"    modal.hide();");
		append(r,"};");
		append(r,"generators.propertyChange = function(property) {");
		append(r,"    var body = \"SET_PROPERTY:\";");
		append(r,"    body += generators.editName;");
		append(r,"    body += \":\";");
		append(r,"    body += property.id;");
		append(r,"    body += \":\";");
		append(r,"    if (property.nodeName==\"SELECT\") {");
		append(r,"        body += property.options[property.selectedIndex].value;");
		append(r,"    } else if (property.type==\"text\" || property.type==\"number\" || property.type==\"any\") {");
		append(r,"        body += property.value;");
		append(r,"    } else if (property.type==\"checkbox\") {");
		append(r,"        body += property.checked;");
		append(r,"    }");
		append(r,"    var cb = null;");
		append(r,"    if (property.id==\"name\") {");
		append(r,"        cb = function() { generators.editName = property.value; };");
		append(r,"    }");
		append(r,"    main.xhr.postText(\"/generator.txt\",body,cb);");
		append(r,"}");
		return r;
	}
}
