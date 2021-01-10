package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class ModalJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var modal = modal || {};");
		append(r,"modal.show = function(html) {");
		append(r,"    var elem = window.document.getElementById(\"modal\");");
		append(r,"    if (elem!=null) {");
		append(r,"        main.dom.startFadeIn(\"modal\");");
		append(r,"        elem.innerHTML = html;");
		append(r,"    }");
		append(r,"    var elem = window.document.getElementById(\"app\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.classList.add(\"hidden\");");
		append(r,"    }");
		append(r,"};");
		append(r,"modal.hide = function(html) {");
		append(r,"    var elem = window.document.getElementById(\"modal\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.innerHTML = \"\";");
		append(r,"    }");
		append(r,"    var elem = window.document.getElementById(\"app\");");
		append(r,"    if (elem!=null) {");
		append(r,"        elem.classList.remove(\"hidden\");");
		append(r,"    }");
		append(r,"};");
		append(r,"modal.load = function(name) {");
		append(r,"    main.xhr.postText(\"/modal.txt\",name,modal.loadCallback);");
		append(r,"};");
		append(r,"modal.loadCallback = function(response) {");
		//append(r,"    console.log(response);");
		append(r,"    modal.show(response.responseText);");
		append(r,"};");
		return r;
	}
}
