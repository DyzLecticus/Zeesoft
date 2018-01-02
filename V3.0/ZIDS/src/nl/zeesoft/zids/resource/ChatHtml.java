package nl.zeesoft.zids.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class ChatHtml extends HtmlResource {
	public ChatHtml() {
		setTitle("Chat");

		getScriptFiles().add("ZIDS.js");
		getStyleFiles().add("ZIDS.css");
		
		ZStringBuilder script = new ZStringBuilder();
		script.append("<script type=\"text/javascript\">\n");
		script.append("ZIDS.chat = {};\n");
		script.append("ZIDS.chat.log = \"\";\n");
		script.append("ZIDS.chat.sendMsg = function() {\n");
		script.append("    var elem = window.document.getElementById(\"msg\");\n");
		script.append("    if (elem!=null && elem.value.length>0) {\n");
		script.append("        ZIDS.session.send(elem.value,ZIDS.chat.sendMsgCallback);\n");
		script.append("        elem.value=\"\";\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZIDS.chat.sendMsgCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZIDS.xhr.parseResponseJSON(response);\n");
		script.append("    ZIDS.chat.log = object.log;\n");
		script.append("    ZIDS.chat.refreshLog();\n");
		script.append("};\n");
		script.append("ZIDS.chat.focusInput = function() {\n");
		script.append("    var elem = window.document.getElementById(\"msg\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.blur();\n");
		script.append("        elem.focus();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZIDS.chat.refreshLog = function() {\n");
		script.append("    var elem = window.document.getElementById(\"showThoughts\");\n");
		script.append("    if (elem!=null && elem.checked) {\n");
		script.append("        ZIDS.chat.showLog(ZIDS.chat.log);\n");
		script.append("    } else {\n");
		script.append("        var log = \"\";\n");
		script.append("        var lines = ZIDS.chat.log.split(\"\\n\");\n");
		script.append("    	   for (var i = 0; i < lines.length; i++) {\n");
		script.append("            if (lines[i].indexOf(\"<<<\")>=0 || lines[i].indexOf(\">>>\")>=0) {\n");
		script.append("                log = log + lines[i] + \"\\n\";\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        ZIDS.chat.showLog(log);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZIDS.chat.showLog = function(log) {\n");
		script.append("    var elem = window.document.getElementById(\"log\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.value = log;\n");
		script.append("        elem.scrollTop = elem.scrollHeight;\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZIDS.chat.onload = function() {\n");
		script.append("    ZIDS.dom.bindEnterFunctionToElementId(\"msg\",ZIDS.chat.sendMsg);\n");
		script.append("    ZIDS.session.open();\n");
		script.append("    ZIDS.chat.focusInput();\n");
		script.append("};\n");
		script.append("</script>\n");
		getHeadElements().add(script);
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<div>\n");
		html.append("<textarea id=\"log\"></textarea>\n");
		html.append("<br />\n");
		html.append("<input type=\"text\" id=\"msg\" style=\"width: 300px;\"/>\n");
		html.append("<input type=\"button\" value=\"send\" onclick=\"ZIDS.chat.sendMsg();\" />\n");
		html.append("<input type=\"checkbox\" id=\"showThoughts\" onchange=\"ZIDS.chat.refreshLog();\" /> Show thoughts\n");
		html.append("</div>\n");

		setOnload("ZIDS.chat.onload();");
		
		getBodyElements().add(html);
	}

}
