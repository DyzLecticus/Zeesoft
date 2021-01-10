package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class MenuJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var menu = menu || {};");
		append(r,"menu.quit = function() {");
		append(r,"    var q = confirm(\"Are you sure you want to quit?\");");
		append(r,"    if (q==true) {");
		append(r,"        location.assign(\"/bye.html\");");
		append(r,"    }");
		append(r,"};");
		append(r,"menu.load = function() {");
		append(r,"    modal.load(\"LoadTheme\");");
		append(r,"};");
		append(r,"menu.loadTheme = function() {");
		append(r,"    var elem = window.document.querySelector('input[name=\"selectTheme\"]:checked');");
		append(r,"    if (elem!=null && elem.value!=null && elem.value.length>0) {");
		append(r,"        main.xhr.postText(\"/state.txt\",\"LOAD:\" + elem.value,function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"        modal.hide();");
		append(r,"    } else {");
		append(r,"        alert(\"Select a theme to load\");");
		append(r,"    }");
		append(r,"};");
		append(r,"menu.save = function() {");
		append(r,"    main.xhr.postText(\"/state.txt\",\"SAVE\",function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"};");
		append(r,"menu.saveAs = function() {");
		append(r,"    modal.load(\"SaveThemeAs\");");
		append(r,"};");
		append(r,"menu.saveThemeAs = function() {");
		append(r,"    var elem = window.document.getElementById(\"saveThemeAs\");");
		append(r,"    if (elem!=null && elem.value.length>0) {");
		append(r,"        main.xhr.postText(\"/state.txt\",\"SAVE_AS:\" + elem.value,function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"        modal.hide();");
		append(r,"        state.refreshApp();");
		append(r,"    }");
		append(r,"};");
		append(r,"menu.delete = function() {");
		append(r,"    modal.load(\"DeleteTheme\");");
		append(r,"};");
		append(r,"menu.deleteTheme = function() {");
		append(r,"    var elem = window.document.querySelector('input[name=\"selectTheme\"]:checked');");
		append(r,"    if (elem!=null && elem.value!=null && elem.value.length>0) {");
		append(r,"        var body = \"DELETE:\" + elem.value;");
		append(r,"        main.xhr.postText(\"/state.txt\",body,function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"        modal.hide();");
		append(r,"        state.refreshApp();");
		append(r,"    } else {");
		append(r,"        alert(\"Select a theme to delete\");");
		append(r,"    }");
		append(r,"};");
		append(r,"menu.new = function() {");
		append(r,"    modal.load(\"NewTheme\");");
		append(r,"};");
		append(r,"menu.newTheme = function() {");
		append(r,"    var elem = window.document.getElementById(\"themeName\");");
		append(r,"    var bpmElem = window.document.getElementById(\"themeBPM\");");
		append(r,"    if (elem!=null && elem.value.length>0) {");
		append(r,"        var bpm = 120;");
		append(r,"        if (bpmElem!=null && bpmElem.value>0) {");
		append(r,"            bpm = bpmElem.value;");
		append(r,"        }");
		append(r,"        var body = \"NEW:\" + elem.value + \":\" + bpm;");
		append(r,"        main.xhr.postText(\"/state.txt\",body,function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"        modal.hide();");
		append(r,"        state.refreshApp();");
		append(r,"    }");
		append(r,"};");
		return r;
	}
}
