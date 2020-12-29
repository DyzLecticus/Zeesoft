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
		append(r,"};");
		append(r,"menu.save = function() {");
		append(r,"    console.log(\"Save request!\");");
		append(r,"    main.xhr.postText(\"/state.txt\",\"SAVE\",function() { state.refresh(); });");
		append(r,"};");
		append(r,"menu.saveAs = function() {");
		append(r,"    console.log(\"Save as request!\");");
		append(r,"    var name = prompt(\"Save theme as\",\"\");");
		append(r,"    if (name.length>0) {");
		append(r,"        main.xhr.postText(\"/state.txt\",\"SAVE_AS:\" + name,function() { state.refresh(); });");
		append(r,"    }");
		append(r,"};");
		append(r,"menu.delete = function() {");
		append(r,"    console.log(\"Delete request!\");");
		append(r,"};");
		append(r,"menu.new = function() {");
		append(r,"    console.log(\"New request!\");");
		append(r,"};");
		return r;
	}
}
