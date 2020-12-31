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
		append(r,"        location.replace(\"/bye.html\");");
		append(r,"    }");
		append(r,"};");
		append(r,"menu.load = function() {");
		append(r,"    console.log(\"Load request!\");");
		append(r,"    modal.load(\"LoadTheme\");");
		append(r,"};");
		append(r,"menu.loadTheme = function() {");
		append(r,"    console.log(\"Load theme request!\");");
		append(r,"    var elem = window.document.querySelector('input[name=\"selectTheme\"]:checked');");
		append(r,"    if (elem!=null && elem.value!=null && elem.value.length>0) {");
		append(r,"        main.xhr.postText(\"/state.txt\",\"LOAD:\" + elem.value,function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"        modal.hide();");
		append(r,"    }");
		append(r,"};");
		append(r,"menu.save = function() {");
		append(r,"    console.log(\"Save request!\");");
		append(r,"    main.xhr.postText(\"/state.txt\",\"SAVE\",function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"};");
		append(r,"menu.saveAs = function() {");
		append(r,"    console.log(\"Save as request!\");");
		append(r,"    modal.load(\"SaveThemeAs\");");
		append(r,"};");
		append(r,"menu.saveThemeAs = function() {");
		append(r,"    console.log(\"Save theme as request!\");");
		append(r,"    var elem = window.document.getElementById(\"saveThemeAs\");");
		append(r,"    if (elem!=null && elem.value.length>0) {");
		append(r,"        main.xhr.postText(\"/state.txt\",\"SAVE_AS:\" + elem.value,function() { state.refresh(); },main.xhr.alertErrorCallback);");
		append(r,"        modal.hide();");
		append(r,"    }");
		append(r,"};");
		append(r,"menu.delete = function() {");
		append(r,"    console.log(\"Delete request!\");");
		append(r,"    modal.load(\"DeleteTheme\");");
		append(r,"};");
		append(r,"menu.new = function() {");
		append(r,"    console.log(\"New request!\");");
		append(r,"};");
		return r;
	}
}
