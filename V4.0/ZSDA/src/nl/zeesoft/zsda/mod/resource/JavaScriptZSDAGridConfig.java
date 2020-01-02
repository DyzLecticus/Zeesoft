package nl.zeesoft.zsda.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsda.mod.ModZSDA;
import nl.zeesoft.zsda.mod.handler.JsonZSDAGridConfigHandler;

public class JavaScriptZSDAGridConfig {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		String path = "../" + ModZSDA.NAME + JsonZSDAGridConfigHandler.PATH;
		
		script.append("var ZSDA = ZSDA || {};\n");
		script.append("ZSDA.config = ZSDA.config || {};\n");
		script.append("ZSDA.config.grid = null;\n");
		script.append("ZSDA.config.get = function() {\n");
		script.append("    var request = {};\n");
		script.append("    ZODB.xhr.postJSON(\"" + path + "\",request,ZSDA.config.getCallback,ZSDA.config.getCallback);\n");
		script.append("};\n");
		script.append("ZSDA.config.getCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.code==\"503\") {\n");
		script.append("        setTimeout(function() { ZSDA.config.get(); },1000);\n");
		script.append("    } else if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else if (object.errors) {\n");
		script.append("        alert(object.errors[0]);\n");
		script.append("    } else {\n");
		script.append("        ZSDA.config.grid = object;\n");
		script.append("        var elem = window.document.getElementById(\"numRows\");\n");
		script.append("        if (elem!=null) {\n");
		script.append("            elem.value = ZSDA.config.grid.rows;\n");
		script.append("        }\n");
		script.append("        elem = window.document.getElementById(\"numColumns\");\n");
		script.append("        if (elem!=null) {\n");
		script.append("            elem.value = ZSDA.config.grid.columns;\n");
		script.append("        }\n");
		script.append("        elem = window.document.getElementById(\"configurations\");\n");
		script.append("        if (elem!=null) {\n");
		script.append("            elem.innerHTML = ZSDA.config.getConfigurationsTable();\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDA.config.getConfigurationByColumnId = function(grid,columnId) {\n");
		script.append("    var config = null;\n");
		script.append("    for (var name in grid.configurations) {\n");
		script.append("        if (grid.configurations[name].columnId==columnId) {\n");
		script.append("            config = grid.configurations[name];\n");
		script.append("            break;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    return config;\n");
		script.append("};\n");
		script.append("ZSDA.config.getConfigurationsTable = function() {\n");
		script.append("    var html = \"\";\n");
		script.append("    html += '<table width=\"100%\">';\n");
		script.append("    html += '<tbody>';\n");
		script.append("    for (var r = 0; r < ZSDA.config.grid.rows; r++) {\n");
		script.append("        html += '<tr>';\n");
		script.append("        for (var c = 0; c < ZSDA.config.grid.columns; c++) {\n");
		script.append("            var id = ZSDA.config.getColumnId(r,c);\n");
		script.append("            var config = ZSDA.config.getConfigurationByColumnId(ZSDA.config.grid,id);\n");
		script.append("            html += '<td valign=\"top\">';\n");
		script.append("            if (config!=null) {\n");
		script.append("                html += ZSDA.config.getConfigurationTable(config);\n");
		script.append("            }\n");
		script.append("            html += '</td>';\n");
		script.append("        }\n");
		script.append("        html += '</tr>';\n");
		script.append("    }\n");
		script.append("    html += '</tbody>';\n");
		script.append("    html += '</table>';\n");
		script.append("    return html;\n");
		script.append("};\n");
		script.append("ZSDA.config.getConfigurationTable = function(configuration) {\n");
		script.append("    var html = \"\";\n");
		script.append("    html += '<table width=\"100%\">';\n");
		script.append("    html += '<tbody>';\n");
		script.append("    html += '<td colspan=\"2\">';\n");
		script.append("    html += '<b>';\n");
		script.append("    html += configuration[\"columnId\"];\n");
		script.append("    html += '&nbsp;';\n");
		script.append("    html += configuration[\"className\"];\n");
		script.append("    html += '</b>';\n");
		script.append("    html += '</td>';\n");
		script.append("    for (var name in configuration) {\n");
		script.append("        if (name==\"contexts\") {\n");
		script.append("            html += '<tr>';\n");
		script.append("            html += '<td colspan=\"2\">';\n");
		script.append("            html += ZSDA.config.getConfigurationContextsTable(configuration[name]);\n");
		script.append("            html += '</td>';\n");
		script.append("            html += '</tr>';\n");
		script.append("            break;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    for (var name in configuration) {\n");
		script.append("        html += '<tr>';\n");
		script.append("        if (name!=\"columnId\" && name!=\"className\" && name!=\"contexts\") {\n");
		script.append("            html += '<td width=\"50%\">';\n");
		script.append("            html += name;\n");
		script.append("            html += '</td>';\n");
		script.append("            html += '<td>';\n");
		script.append("            html += '<input type=\"text\" value=\"' + configuration[name] + '\">';\n");
		script.append("            html += '</td>';\n");
		script.append("        }\n");
		script.append("        html += '</tr>';\n");
		script.append("    }\n");
		script.append("    html += '</tbody>';\n");
		script.append("    html += '</table>';\n");
		script.append("    return html;\n");
		script.append("};\n");
		script.append("ZSDA.config.getConfigurationContextsTable = function(contexts) {\n");
		script.append("    var html = \"\";\n");
		script.append("    html += '<table width=\"100%\">';\n");
		script.append("    html += '<tbody>';\n");
		script.append("    for (var name in contexts) {\n");
		script.append("        var context = contexts[name];\n");
		script.append("        var id = ZSDA.config.getColumnId(context.sourceRow,context.sourceColumn);\n");
		script.append("        var config = ZSDA.config.getConfigurationByColumnId(ZSDA.config.grid,id);\n");
		script.append("        html += '<tr>';\n");
		script.append("        html += '<td>';\n");
		script.append("        html += '<b>';\n");
		script.append("        html += '&lt;&nbsp;';\n");
		script.append("        html += id + '/' + context.sourceIndex;\n");
		script.append("        html += '&nbsp;';\n");
		script.append("        html += '</b>';\n");
		script.append("        html += config.className;\n");
		script.append("        html += '</td>';\n");
		script.append("        html += '</tr>';\n");
		script.append("    }\n");
		script.append("    html += '</tbody>';\n");
		script.append("    html += '</table>';\n");
		script.append("    return html;\n");
		script.append("};\n");
		script.append("ZSDA.config.getColumnId = function(rowIndex,columnIndex) {\n");
		script.append("    return (\"0\" + rowIndex).slice(-2) + \"-\" + (\"0\" + columnIndex).slice(-2);\n");
		script.append("};\n");
		script.append("ZSDA.config.onload = function() {\n");
		script.append("    ZSDA.config.get();\n");
		script.append("};\n");
		
		return script;
	}
}
