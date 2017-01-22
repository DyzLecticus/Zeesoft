package nl.zeesoft.zids.server.resource;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.file.HTMLFile;

public class RscDataPosterHtml extends RscHtmlObject {
	public RscDataPosterHtml(String title, String backgroundColor) {
		super(title, backgroundColor);
	}

	@Override
	public StringBuilder toStringBuilder() {
		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");
		header.append("var ZIDS = ZIDS || {};\n");
		header.append("ZIDS.poster = {};\n");
		header.append("ZIDS.poster.updateRequest = function() {\n");
		header.append("    var type = document.getElementById(\"selectType\").value;\n");
		header.append("    var className = document.getElementById(\"selectClass\").value;\n");
		header.append("    var active = document.getElementById(\"filterActive\").checked;\n");
		header.append("    var property = document.getElementById(\"filterProperty\").value;\n");
		header.append("    var invert = document.getElementById(\"filterInvert\").value;\n");
		header.append("    var operator = document.getElementById(\"filterOperator\").value;\n");
		header.append("    var value = document.getElementById(\"filterValue\").value;\n");
		header.append("    var filter = null;\n");
		header.append("    if (active!=null && active && property!=null && property!=\"\" && value!=null && value!=\"\") {\n");
		header.append("        if (invert!=null && invert==\"true\") {\n");
		header.append("             filter = new ZIDS.xhr.GetRequestFilter(property,operator,value,true);\n");
		header.append("        } else {\n");
		header.append("             filter = new ZIDS.xhr.GetRequestFilter(property,operator,value);\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    var newRequest = \"\";\n");
		header.append("    if (type==\"get\") {\n");
		header.append("        var request = new ZIDS.xhr.GetRequest(className);\n");
		header.append("        if (filter!=null) {\n");
		header.append("            request.filters[0]=filter;\n");
		header.append("        }\n");
		header.append("        newRequest = JSON.stringify(request,null,2);\n");
		header.append("    } else if (type==\"add\") {\n");
		header.append("        var request = new ZIDS.xhr.AddRequest(className);\n");
		header.append("        request.objects[0] = ZIDS.data.getNewRequestObjectForClass(className);\n");
		header.append("        newRequest = JSON.stringify(request,null,2);\n");
		header.append("    } else if (type==\"update\") {\n");
		header.append("        var request = new ZIDS.xhr.UpdateRequest(className);\n");
		header.append("        if (filter!=null) {\n");
		header.append("            request.getRequest.filters[0]=filter;\n");
		header.append("        }\n");
		header.append("        request.objects[0] = ZIDS.data.getNewRequestObjectForClass(className);\n");
		header.append("        newRequest = JSON.stringify(request,null,2);\n");
		header.append("    } else if (type==\"remove\") {\n");
		header.append("        var request = new ZIDS.xhr.RemoveRequest(className);\n");
		header.append("        if (filter!=null) {\n");
		header.append("            request.getRequest.filters[0]=filter;\n");
		header.append("        }\n");
		header.append("        newRequest = JSON.stringify(request,null,2);\n");
		header.append("    }\n");
		header.append("    document.getElementById(\"request\").value = newRequest;\n");
		header.append("};\n");
		header.append("ZIDS.poster.postRequestCallback = function(xhr) {\n");
		header.append("    var response = xhr.responseText;\n");
		header.append("    document.getElementById(\"response\").value = ZIDS.xhr.correctJSONValues(response);\n");
		header.append("};\n");
		header.append("ZIDS.poster.postRequest = function() {\n");
		header.append("    var json = document.getElementById(\"request\").value;\n");
		header.append("    ZIDS.xhr.postJSON(\"../data/\",json,ZIDS.poster.postRequestCallback);\n");
		header.append("};\n");
		header.append("</script>\n");

		StringBuilder body = new StringBuilder();
		body.append("<div>\n");
		body.append("<table>\n");
		
		body.append("<tr>\n");
		body.append("<td>\n");
		body.append("Type&nbsp;");
		body.append("</td>\n");
		body.append("<td>\n");
		body.append("<select id=\"selectType\" onchange=\"ZIDS.poster.updateRequest();\">\n");
		body.append("<option value=\"get\" SELECTED >Get</option>\n");
		body.append("<option value=\"add\">Add</option>\n");
		body.append("<option value=\"update\">Update</option>\n");
		body.append("<option value=\"remove\">Remove</option>\n");
		body.append("</select><br />\n");
		body.append("</td>\n");
		body.append("</tr>\n");
		
		body.append("<tr>\n");
		body.append("<td>\n");
		body.append("Class&nbsp;");
		body.append("</td>\n");
		body.append("<td>\n");
		body.append("<select id=\"selectClass\" onchange=\"ZIDS.poster.updateRequest();\">\n");
		String selected = " SELECTED";
		for (MdlClass cls: DbConfig.getInstance().getModel().getClasses()) {
			if (!cls.isAbstr()) {
				body.append("<option value=\"" + cls.getFullName() + "\"" + selected + ">" + cls.getFullName() + "</option>\n");
				selected = "";
			}
		}
		body.append("</select><br />\n");
		body.append("</td>\n");
		body.append("</tr>\n");

		body.append("<tr>\n");
		body.append("<td>\n");
		body.append("Filter&nbsp;");
		body.append("</td>\n");
		body.append("<td>\n");
		body.append("<input type=\"checkbox\" value=\"true\" id=\"filterActive\" onchange=\"ZIDS.poster.updateRequest();\" CHECKED/>&nbsp;\n");
		body.append("<input id=\"filterProperty\" onchange=\"ZIDS.poster.updateRequest();\" />&nbsp;\n");
		body.append("<select id=\"filterInvert\" onchange=\"ZIDS.poster.updateRequest();\">\n");
		body.append("<option value=\"false\" SELECTED ></option>\n");
		body.append("<option value=\"true\">not</option>\n");
		body.append("</select>&nbsp;\n");
		body.append("<select id=\"filterOperator\" onchange=\"ZIDS.poster.updateRequest();\">\n");
		body.append("<option value=\"equals\" SELECTED >equals</option>\n");
		body.append("<option value=\"contains\">contains</option>\n");
		body.append("<option value=\"greater\">greater</option>\n");
		body.append("<option value=\"greaterOrEquals\">greater or equals</option>\n");
		body.append("<option value=\"less\">less</option>\n");
		body.append("<option value=\"lessOrEquals\">less or equals</option>\n");
		body.append("</select>&nbsp;\n");
		body.append("<input id=\"filterValue\" onchange=\"ZIDS.poster.updateRequest();\" />\n");
		body.append("</td>\n");
		body.append("</tr>\n");

		body.append("</table>\n");
		body.append("</div>\n");

		body.append("<div>\n");
		body.append("<br />\n");
		body.append("Request<br />");
		body.append("<textarea id=\"request\" width=\"100%\" height=\"200px\">\n");
		body.append("</textarea>\n");
		body.append("</div>\n");
		
		body.append("<div>\n");
		body.append("<input type=\"button\" value=\"Post\" onclick=\"ZIDS.poster.postRequest();\"/>\n");
		body.append("</div>\n");
		
		body.append("<div>\n");
		body.append("<br />\n");
		body.append("Response<br />");
		body.append("<textarea id=\"response\" width=\"100%\" height=\"200px\">\n");
		body.append("</textarea>\n");
		body.append("</div>\n");

		HTMLFile f = getNewHTMLFile();
		f.getScriptFiles().add("../ZIDS.js");
		f.getStyleFiles().add("../ZIDS.css");
		f.getHeadElements().add(header.toString());
		f.getBodyElements().add(getMenuForPage("../","","").toString());
		f.getBodyElements().add(body.toString());
		f.setOnload("ZIDS.poster.updateRequest();");
		return f.toStringBuilder();
	}
}
