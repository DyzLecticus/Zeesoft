package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class DomJs extends Resource {

	@Override
	protected void render(StringBuilder r) {
		append(r, "var dom = dom || {};");
		append(r, "dom.setInnerHTML = function(id, html) {");
		append(r, "    var elem = window.document.getElementById(id);");
		append(r, "    if (elem!=null) {");
		append(r, "        elem.innerHTML = html;");
		append(r, "    }");
		append(r, "};");
		append(r, "dom.toggleVisible = function(id, html) {");
		append(r, "    var elem = window.document.getElementById(id);");
		append(r, "    if (elem!=null) {");
		append(r, "        elem.classList.toggle(\"hidden\");");
		append(r, "    }");
		append(r, "};");
		append(r, "dom.startFadeIn = function(id) {");
		append(r, "    var elem = window.document.getElementById(id);");
		append(r, "    if (elem!=null) {");
		append(r, "        elem.style.opacity = 0;");
		append(r, "        elem.opacity = 0;");
		append(r, "        dom.fadeIn(id);");
		append(r, "    }");
		append(r, "};");
		append(r, "dom.fadeIn = function(id) {");
		append(r, "    var elem = window.document.getElementById(id);");
		append(r, "    if (elem!=null) {");
		append(r, "        if (elem.opacity<1) {");
		append(r, "            elem.opacity += 0.1;");
		append(r, "            elem.style.opacity = elem.opacity;");
		append(r, "        }");
		append(r, "        if (elem.opacity<1) {");
		append(r, "            setTimeout(function() { dom.fadeIn(id) }, 50);");
		append(r, "        } else {");
		append(r, "            elem.opacity = 1;");
		append(r, "            elem.style.opacity = 1;");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");
		append(r, "dom.startFadeOut = function(id) {");
		append(r, "    var elem = window.document.getElementById(id);");
		append(r, "    if (elem!=null) {");
		append(r, "        elem.style.opacity = 1;");
		append(r, "        elem.opacity = 1;");
		append(r, "        dom.fadeOut(id);");
		append(r, "    }");
		append(r, "};");
		append(r, "dom.fadeOut = function(id) {");
		append(r, "    var elem = window.document.getElementById(id);");
		append(r, "    if (elem!=null) {");
		append(r, "        if (elem.opacity>0) {");
		append(r, "            elem.opacity -= 0.1;");
		append(r, "            elem.style.opacity = elem.opacity;");
		append(r, "        }");
		append(r, "        if (elem.opacity>0) {");
		append(r, "            setTimeout(function() { dom.fadeOut(id) }, 50);");
		append(r, "        } else {");
		append(r, "            elem.opacity = 0;");
		append(r, "            elem.style.opacity = 0;");
		append(r, "        }");
		append(r, "    }");
		append(r, "};");

	}

}
