package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class BindingsJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,"var bindings = bindings || {};");
		append(r,"bindings.onKeyDown = function(e) {");
		append(r,"    if (e.key == \"q\" && !e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        menu.quit();");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.key == \"1\" && !e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        menu.load();");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.key == \"2\" && !e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        menu.save();");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.key == \"3\" && !e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        menu.saveAs();");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.key == \"4\" && !e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        menu.delete();");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.key == \"5\" && !e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        menu.new();");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.key == \" \" && e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        sequencer.playSequence();");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.key == \" \" && e.shiftKey && !e.ctrlKey && !e.altKey) {");
		append(r,"        sequencer.playTheme();");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.key == \" \" && !e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        sequencer.stop();");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.code == \"Enter\" && !e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        var elem = document.getElementById(\"formOk\");");
		append(r,"        if (elem!=null) {");
		append(r,"            elem.onclick()");
		append(r,"        }");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (e.key == \"c\" && !e.shiftKey && e.ctrlKey && !e.altKey) {");
		append(r,"        var elem = document.getElementById(\"formCancel\");");
		append(r,"        if (elem!=null) {");
		append(r,"            elem.onclick()");
		append(r,"        }");
		append(r,"        e.stopPropagation();");
		append(r,"    } else if (!e.shiftKey && !e.ctrlKey && !e.altKey) {");
		//append(r,"        console.log(e);");
		append(r,"    }");
		append(r,"};");
		append(r,"bindings.onload = function() {");
		append(r,"    document.onkeydown = bindings.onKeyDown;");
		append(r,"};");
		return r;
	}
}
