package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class QuitJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var quit = quit || {};");
		append(r,"quit.onload = function() {");
		append(r,"    main.xhr.postText(\"/state.txt\",\"QUIT\");");
		append(r,"    setTimeout(function() { window.close(); }, 3000);");
		append(r,"};");
		return r;
	}
}
