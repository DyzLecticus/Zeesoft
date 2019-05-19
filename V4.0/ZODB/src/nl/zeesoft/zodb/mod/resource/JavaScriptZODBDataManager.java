package nl.zeesoft.zodb.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.mod.ModZODB;
import nl.zeesoft.zodb.mod.handler.JsonZODBRequestHandler;

public class JavaScriptZODBDataManager {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		String path = "../" + ModZODB.NAME + JsonZODBRequestHandler.PATH;
		
		script.append("var ZODB = ZODB || {};\n");
		script.append("ZODB.dm = ZODB.dm || {};\n");
		script.append("ZODB.dm.selectedId = 0;\n");
		script.append("ZODB.dm.next = function() {\n");
		script.append("    elemS = window.document.getElementById(\"start\");\n");
		script.append("    elemM = window.document.getElementById(\"max\");\n");
		script.append("    if (elemS!=null && elemM!=null) {\n");
		script.append("        elemS.value = parseInt(elemS.value,10) + parseInt(elemM.value,10);\n");
		script.append("        ZODB.dm.list();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.prev = function() {\n");
		script.append("    elemS = window.document.getElementById(\"start\");\n");
		script.append("    elemM = window.document.getElementById(\"max\");\n");
		script.append("    if (elemS!=null && elemM!=null && parseInt(elemS.value,10)>0) {\n");
		script.append("        var start = parseInt(elemS.value,10) - parseInt(elemM.value,10);\n");
		script.append("        if (start<0) {\n");
		script.append("            start=0;\n");
		script.append("        }\n");
		script.append("        elemS.value = start;\n");
		script.append("        ZODB.dm.list();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.list = function() {\n");
		script.append("    var request = {};\n");
		script.append("    request.type = \"" + DatabaseRequest.TYPE_LIST + "\";\n");
		script.append("    request.max = 10;\n");
		script.append("    var elem = window.document.getElementById(\"name\");\n");
		script.append("    if (elem!=null && elem.value.length>0) {\n");
		script.append("        request.contains = elem.value;\n");
		script.append("    }\n");
		script.append("    elem = window.document.getElementById(\"start\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        request.start = parseInt(elem.value,10);\n");
		script.append("    }\n");
		script.append("    elem = window.document.getElementById(\"max\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        request.max = parseInt(elem.value,10);\n");
		script.append("    }\n");
		script.append("    var elem = window.document.getElementById(\"fetched\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.innerHTML = \"Fetching&nbsp;...\";\n");
		script.append("    }\n");
		script.append("    ZODB.xhr.postJSON(\"" + path + "\",request,ZODB.dm.listCallback,ZODB.dm.listCallback);\n");
		script.append("};\n");
		script.append("ZODB.dm.listCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else if (object.errors) {\n");
		script.append("        alert(object.errors[0]);\n");
		script.append("    }\n");
		script.append("    if (object.results) {\n");
		script.append("        ZODB.dm.showList(object);\n");
		script.append("    } else {\n");
		script.append("        var size = 0;\n");
		script.append("        if (object.size) {\n");
		script.append("            size = object.size;\n");
		script.append("        }\n");
		script.append("        var elem = window.document.getElementById(\"fetched\");\n");
		script.append("        if (elem!=null) {\n");
		script.append("            elem.innerHTML = \"Fetched&nbsp;0&nbsp;/&nbsp;\" + size;\n");
		script.append("        }\n");
		script.append("        ZODB.dm.clearList();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.clearList = function() {\n");
		script.append("    var elem = window.document.getElementById(\"list\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.innerHTML = \"\";\n");
		script.append("    }\n");
		script.append("    ZODB.dm.clearObject();\n");
		script.append("};\n");
		script.append("ZODB.dm.showList = function(listObject) {\n");
		script.append("    ZODB.dm.clearObject();\n");
		script.append("    var html=\"\";\n");
		script.append("    html+='<table style=\"width: 100%;\">';\n");
		script.append("        html+=\"<tr>\";\n");
		script.append("        html+='<th width=\"1%\">';\n");
		script.append("        html+=\"</th>\";\n");
		script.append("        html+='<th align=\"right\" width=\"9%\">';\n");
		script.append("        html+= \"ID\";\n");
		script.append("        html+=\"</th>\";\n");
		script.append("        html+='<th align=\"left\" width=\"60%\">';\n");
		script.append("        html+= \"Name\";\n");
		script.append("        html+=\"</th>\";\n");
		script.append("        html+='<th align=\"left\">';\n");
		script.append("        html+= \"Modified\";\n");
		script.append("        html+=\"</th>\";\n");
		script.append("        html+=\"</tr>\";\n");
		script.append("    html+=\"<tbody>\";\n");
		script.append("    var fetched = 0;\n");
		script.append("    var selId = 0;\n");
		script.append("    for (var num in listObject.results) {\n");
		script.append("        var sel = \"\";\n");
		script.append("        if (listObject.results[num].id==ZODB.dm.selectedId) {\n");
		script.append("            sel = ' checked=\"checked\"';\n");
		script.append("            selId = listObject.results[num].id;\n");
		script.append("        }\n");
		script.append("        fetched++;\n");
		script.append("        html+=\"<tr>\";\n");
		script.append("        html+='<td>';\n");
		script.append("        html+= '<input type=\"radio\" name=\"selector\" value=\"' + listObject.results[num].id + '\" onchange=\"ZODB.dm.select();\"' + sel + '/>';\n");
		script.append("        html+=\"</td>\";\n");
		script.append("        html+='<td align=\"right\">';\n");
		script.append("        html+= listObject.results[num].id;\n");
		script.append("        html+=\"</td>\";\n");
		script.append("        html+='<td>';\n");
		script.append("        html+= listObject.results[num].name;\n");
		script.append("        html+=\"</td>\";\n");
		script.append("        html+='<td>';\n");
		script.append("        var dat = new Date();\n");
		script.append("        dat.setTime(listObject.results[num].modified);\n");
		script.append("        html+= dat.toISOString().replace(\"T\",\"&nbsp;\").replace(\"Z\",\"\");\n");
		script.append("        html+=\"</td>\";\n");
		script.append("        html+=\"</tr>\";\n");
		script.append("    }\n");
		script.append("    html+=\"</tbody>\";\n");
		script.append("    html+=\"</table>\";\n");
		script.append("    var elem = window.document.getElementById(\"list\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.innerHTML = html;\n");
		script.append("    }\n");
		script.append("    var elem = window.document.getElementById(\"fetched\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        html=\"Fetched&nbsp;\";\n");
		script.append("        html+=fetched;\n");
		script.append("        html+=\"&nbsp;/&nbsp;\";\n");
		script.append("        html+=listObject.size\n");
		script.append("        elem.innerHTML = html;\n");
		script.append("    }\n");
		script.append("    if (selId>0) {\n");
		script.append("        ZODB.dm.selectById(selId);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.select = function() {\n");
		script.append("    var id = ZODB.dom.getSelectedRadioByElementName(\"selector\");\n");
		script.append("    ZODB.dm.selectedId = id;\n");
		script.append("    ZODB.dm.selectById(id);\n");
		script.append("};\n");
		script.append("ZODB.dm.selectById = function(id) {\n");
		script.append("    var request = {};\n");
		script.append("    request.type = \"" + DatabaseRequest.TYPE_GET + "\";\n");
		script.append("    request.id = id;\n");
		script.append("    request.encoding = \"" + DatabaseRequest.ENC_ASCII + "\";\n");
		script.append("    ZODB.xhr.postJSON(\"" + path + "\",request,ZODB.dm.selectCallback,ZODB.dm.selectCallback);\n");
		script.append("};\n");
		script.append("ZODB.dm.selectCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else if (object.errors) {\n");
		script.append("        alert(object.errors[0]);\n");
		script.append("    }\n");
		script.append("    if (object.results) {\n");
		script.append("        for (var num in object.results) {\n");
		script.append("            object.results[num].object = JSON.parse(ZODB.encode.decodeAscii(object.results[num].encoded));\n");
		script.append("            ZODB.dm.showObject(object.results[num].object,object.results[num].name);\n");
		script.append("            break;\n");
		script.append("        }\n");
		script.append("    } else {\n");
		script.append("        ZODB.dm.clearObject();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.clearObject = function() {\n");
		script.append("    var elem = window.document.getElementById(\"object\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.value = \"\";\n");
		script.append("    }\n");
		script.append("    elem = window.document.getElementById(\"saveName\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.value = \"\";\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.showObject = function(obj,name) {\n");
		script.append("    var elem = window.document.getElementById(\"object\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.value = JSON.stringify(obj,null,2);\n");
		script.append("    }\n");
		script.append("    elem = window.document.getElementById(\"saveName\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.value = name;\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.new = function() {\n");
		script.append("    ZODB.dom.selectRadioByElementName(\"selector\",0);\n");
		script.append("    ZODB.dm.clearObject();\n");
		script.append("    var elem = window.document.getElementById(\"saveName\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.value = \"\";\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.save = function() {\n");
		script.append("    var obj = null;\n");
		script.append("    var name = \"\";\n");
		script.append("    var elem = window.document.getElementById(\"object\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        try {\n");
		script.append("            obj = JSON.parse(elem.value);\n");
		script.append("        } catch(err) {\n");
		script.append("            obj = null;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    elem = window.document.getElementById(\"saveName\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        name = elem.value;\n");
		script.append("    }\n");
		script.append("    if (obj && name.length>0) {\n");
		script.append("        var id = ZODB.dom.getSelectedRadioByElementName(\"selector\");\n");
		script.append("        var request = {};\n");
		script.append("        if (id && id>0) {\n");
		script.append("            request.type = \"" + DatabaseRequest.TYPE_SET + "\";\n");
		script.append("            request.id = id;\n");
		script.append("        } else {\n");
		script.append("            request.type = \"" + DatabaseRequest.TYPE_ADD + "\";\n");
		script.append("        }\n");
		script.append("        request.name = name;\n");
		script.append("        request.encoding = \"" + DatabaseRequest.ENC_ASCII + "\";\n");
		script.append("        request.encoded = ZODB.encode.encodeAscii(JSON.stringify(obj));\n");
		script.append("        ZODB.xhr.postJSON(\"" + path + "\",request,ZODB.dm.defaultCallback,ZODB.dm.defaultCallback);\n");
		script.append("    } else {\n");
		script.append("        if (name.length==0) {\n");
		script.append("            alert(\"Name is mandatory\");\n");
		script.append("        } else {\n");
		script.append("            alert(\"Invalid JSON\");\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.remove = function() {\n");
		script.append("    var id = ZODB.dom.getSelectedRadioByElementName(\"selector\");\n");
		script.append("    if (id && id>0) {\n");
		script.append("        var elem = window.document.getElementById(\"removeCheck\");\n");
		script.append("        if (elem!=null && elem.checked) {\n");
		script.append("            elem.checked = false;\n");
		script.append("            var request = {};\n");
		script.append("            request.type = \"" + DatabaseRequest.TYPE_REMOVE + "\";\n");
		script.append("            request.id = id;\n");
		script.append("            ZODB.xhr.postJSON(\"" + path + "\",request,ZODB.dm.defaultCallback,ZODB.dm.defaultCallback);\n");
		script.append("        }\n");
		script.append("    } else {\n");
		script.append("        alert(\"No object selected\");\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.removeAll = function() {\n");
		script.append("    var contains = \"\";\n");
		script.append("    var elemN = window.document.getElementById(\"name\");\n");
		script.append("    if (elemN!=null) {\n");
		script.append("        contains = elemN.value;\n");
		script.append("    }\n");
		script.append("    if (contains) {\n");
		script.append("        var elem = window.document.getElementById(\"removeAllCheck\");\n");
		script.append("        if (elem!=null && elem.checked) {\n");
		script.append("            elem.checked = false;\n");
		script.append("            var request = {};\n");
		script.append("            request.type = \"" + DatabaseRequest.TYPE_REMOVE + "\";\n");
		script.append("            request.contains = contains;\n");
		script.append("            ZODB.xhr.postJSON(\"" + path + "\",request,ZODB.dm.defaultCallback,ZODB.dm.defaultCallback);\n");
		script.append("        }\n");
		script.append("    } else {\n");
		script.append("        alert(\"Name contains filter is required\");\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.defaultCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else if (object.errors) {\n");
		script.append("        alert(object.errors[0]);\n");
		script.append("    } else {\n");
		script.append("        ZODB.dm.list();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZODB.dm.onload = function() {\n");
		script.append("    var elem = window.document.getElementById(\"name\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.blur();\n");
		script.append("        elem.focus();\n");
		script.append("    }\n");
		script.append("    ZODB.dm.list();\n");
		script.append("    ZODB.dom.bindEnterFunctionToElementId(\"name\",ZODB.dm.list);\n");
		script.append("    ZODB.dom.bindEnterFunctionToElementId(\"start\",ZODB.dm.list);\n");
		script.append("    ZODB.dom.bindEnterFunctionToElementId(\"max\",ZODB.dm.list);\n");
		script.append("};\n");
		
		return script;
	}
}
