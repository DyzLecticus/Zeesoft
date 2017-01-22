package nl.zeesoft.zdsm.database.server;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdsm.database.model.ZDSMModel;
import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.file.HTMLFile;

public class ZDSMHTTPResourceFactory extends SvrHTTPResourceFactory {

	@Override
	protected void initializeHTTPResources() {
		super.initializeHTTPResources();

		StringBuilder js = new StringBuilder();
		js.append(getDomainJavaScript());
		js.append(getServiceJavaScript());
		js.append(getProcessJavaScript());
		addFile("ZDSM.js",js);

		addFile("monitor.html",getMonitorHtml().toStringBuilder());
		addFile("processLog.html",getProcessLogHtml().toStringBuilder());
		addFile("response.html",getResponseHtml().toStringBuilder());
		addFile("analyzer.html",getAnalyzerHtml().toStringBuilder());
		addFile("exportImport.html",getServiceExportImportHtml().toStringBuilder());
		addFile("manager.html",getManagerHtml().toStringBuilder());
		addFile("processManager.html",getProcessManagerHtml().toStringBuilder());
		addFile("serviceManager.html",getServiceManagerHtml().toStringBuilder());
		addFile("domainManager.html",getDomainManagerHtml().toStringBuilder());
	}

	@Override
	protected String getIndexMenuTitle() {
		return "ZDSM";
	}

	protected StringBuilder getZDSMMenuHtmlForPage(String page) {
		List<String> pages = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		
		pages.add("index");
		pages.add("monitor");
		pages.add("analyzer");
		pages.add("manager");
		pages.add("home");

		titles.add("Home");
		titles.add("Monitor");
		titles.add("Analyzer");
		titles.add("Manager");
		titles.add("ZODB");
		
		StringBuilder html = new StringBuilder();
		int i = 0;
		for (String p: pages) {
			if (html.length()>0) {
				html.append("&nbsp;");
			}
			if (p.equals(page)) {
				html.append("<b>");
			} else {
				html.append("<a href=\"/" + p + ".html\">");
			}
			html.append(titles.get(i));
			if (p.equals(page)) {
				html.append("</b>");
			} else {
				html.append("</a>");
			}
			i++;
		}
		html.insert(0,"<div>");
		html.append("<br/><hr/></div>");
		
		return html;
	}
	
	@Override
	protected String getAuthorizerTitle() {
		return "ZDSM - Authorizer";
	}
	
	@Override
	protected StringBuilder getAuthorizerMenuHtml() {
		return getZDSMMenuHtmlForPage("");
	}
	
	@Override
	protected HTMLFile get404Html() {
		HTMLFile file = new HTMLFile("ZDSM - 404 Not Found");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append(getZDSMMenuHtmlForPage(""));
		body.append("<div>");
		body.append("No resource found for this URL. ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	@Override
	protected HTMLFile getInstalledHtml() {
		return getIndexHtml();
	}
	
	@Override
	protected HTMLFile getIndexHtml() {
		HTMLFile file = new HTMLFile("ZDSM - Home");
		file.setBodyBgColor(BACKGROUND_COLOR);

		StringBuilder body = new StringBuilder();
		body.append(getZDSMMenuHtmlForPage("index"));
		body.append("<div>");
		body.append("Welcome to the <b>Zeesoft Domain Service Monitor</b>. ");
		body.append("You can use this interface to <a href=\"/monitor.html\">monitor</a> and <a href=\"/analyzer.html\">analyze</a> trends for a set of <a href=\"/processManager.html\">processes</a> and <a href=\"/serviceManager.html\">services</a> across several <a href=\"/domainManager.html\">domains</a>. ");
		body.append("Processes are preprogrammed combinations of service requests that require complex data manipulation (like authentication and authorization mechanisms). ");
		body.append("The Zeesoft Domain Service Monitor extends the <a href=\"/about.html\">Zeesoft Object Database</a>. ");
		body.append("As a result, this program itself can be extended programmatically to include a custom collection of preprogrammed processes (including a custom data model and default data set). ");
		body.append("The actual monitoring program runs in the background on the server. ");
		body.append("Every second it determines which domains require checking and then it starts all processes and fires all service requests that are applicable to those domains. ");
		body.append("Processes and service requests are handled by separate threads to prevent long response durations from clogging up the pipes. ");
		body.append("Process logs and service request responses are stored in the database including a boolean to indicate success. ");
		body.append("Configuring domains and services can be a time consuming task. ");
		body.append("In order to help backup and share configured domains and services, this interface also provides a domain and service <a href=\"/exportImport.html\">export and import</a> program. ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getMonitorHtml() {
		HTMLFile file = new HTMLFile("ZDSM - Monitor");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZDSM.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZDSM = ZDSM || {};\n");
		header.append("ZDSM.monitor = {};\n");
		
		header.append("ZDSM.monitor.getDomainObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.domain.getObjectsCallback(xhttp);\n");
		header.append("    ZDSM.process.getObjects(ZDSM.monitor.getProcessObjectsCallback);\n");
		header.append("};\n");

		header.append("ZDSM.monitor.getProcessObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.process.getObjectsCallback(xhttp);\n");
		header.append("    ZDSM.service.getObjects(ZDSM.monitor.getServiceObjectsCallback);\n");
		header.append("};\n");

		header.append("ZDSM.monitor.GetProcessLog = function(request) {\n");
		header.append("    var that = this;\n");
		header.append("    this.getRequest = request;\n");
		header.append("    this.get = function() {\n");
		header.append("        ZODB.data.executePostRequest(this.getRequest,this.callback,ZODB.gui.genericResponseCallback);\n");	
		header.append("    }\n");
		header.append("    this.callback = function(xhttp) {\n");
		header.append("        var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("        var pdId = that.getRequest.filters[\"process\"] + \"P-\" + that.getRequest.filters[\"domain\"];\n");
		header.append("        var elem = window.document.getElementById(pdId);\n");
		header.append("        if (elem && dataObjectSet.objectArray.length>0) {\n");
		header.append("            var success = dataObjectSet.objectArray[0].propertyValues[\"success\"];\n");
 		header.append("            if (success==\"true\") {\n");
 		header.append("                elem.className = \"green\";\n");
 		header.append("            } else {\n");
 		header.append("                elem.className = \"red\";\n");
		header.append("            }\n");
		header.append("            var dateTime = dataObjectSet.objectArray[0].propertyValues[\"dateTime\"];\n");
		header.append("            var dateStr = ZODB.date.toString(new Date(parseInt(dateTime,10)));\n");
		header.append("            dateStr = dateStr.split(\" \")[1];\n");
		header.append("            var anchor = \"&nbsp;<a href='/processLog.html?processLogId=\" + dataObjectSet.objectArray[0].getId() + \"'>\" + dateStr + \"</a>\";\n");
		header.append("            elem.innerHTML=anchor;\n");
 		header.append("        } else if (elem) {\n");
		header.append("            elem.innerHTML=\"?\";\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZDSM.monitor.GetResponse = function(request) {\n");
		header.append("    var that = this;\n");
		header.append("    this.getRequest = request;\n");
		header.append("    this.get = function() {\n");
		header.append("        ZODB.data.executePostRequest(this.getRequest,this.callback,ZODB.gui.genericResponseCallback);\n");	
		header.append("    }\n");
		header.append("    this.callback = function(xhttp) {\n");
		header.append("        var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("        var sdId = that.getRequest.filters[\"service\"] + \"S-\" + that.getRequest.filters[\"domain\"];\n");
		header.append("        var elem = window.document.getElementById(sdId);\n");
		header.append("        if (elem && dataObjectSet.objectArray.length>0) {\n");
		header.append("            var success = dataObjectSet.objectArray[0].propertyValues[\"success\"];\n");
		header.append("            var code = dataObjectSet.objectArray[0].propertyValues[\"code\"];\n");
		header.append("            var dateTime = dataObjectSet.objectArray[0].propertyValues[\"dateTime\"];\n");
		header.append("            var dateStr = ZODB.date.toString(new Date(parseInt(dateTime,10)));\n");
		header.append("            dateStr = dateStr.split(\" \")[1];\n");
 		header.append("            if (success==\"true\") {\n");
 		header.append("                elem.className = \"green\";\n");
 		header.append("            } else {\n");
 		header.append("                elem.className = \"red\";\n");
		header.append("                dateStr += \" (\" + code + \")\";\n");
		header.append("            }\n");
		header.append("            var anchor = \"&nbsp;<a href='/response.html?responseId=\" + dataObjectSet.objectArray[0].getId() + \"'>\" + dateStr + \"</a>\";\n");
		header.append("            elem.innerHTML=anchor;\n");
 		header.append("        } else if (elem) {\n");
		header.append("            elem.innerHTML=\"?\";\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.monitor.getServiceObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.service.getObjectsCallback(xhttp);\n");
		header.append("    var now=new Date();\n");
		header.append("    var html=\"\";\n");
		header.append("    html+=\"Date/time: \";\n");
		header.append("    html+=ZODB.date.toString(now);\n");
		header.append("    if (ZDSM.process.dataObjectSet.objectArray.length>0) {\n");
		header.append("        html+=\", <b>processes:&nbsp;\";\n");
		header.append("        html+=ZDSM.process.dataObjectSet.objectArray.length;\n");
		header.append("    }\n");
		header.append("    html+=\"</b>, services:&nbsp;\";\n");
		header.append("    html+=ZDSM.service.dataObjectSet.objectArray.length;\n");
		header.append("    html+=\", domains:&nbsp;\";\n");
		header.append("    html+=ZDSM.domain.dataObjectSet.objectArray.length;\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"total\",html);\n");
		header.append("    html=\"\";\n");
		header.append("    html+=\"<table>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<th>&nbsp;</th>\";\n");
		header.append("    for (var i=0; i < ZDSM.domain.dataObjectSet.objectArray.length; i++) {\n");
		header.append("        var domainId = ZDSM.domain.dataObjectSet.objectArray[i].getId();\n");
		header.append("        var domain = ZDSM.domain.dataObjectSet.objects[domainId];\n");
		header.append("        html+=\"<th>\";\n");
		header.append("        html+=domain.propertyValues[\"name\"];\n");
		header.append("        html+=\"</th>\";\n");
		header.append("    }\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    var getters = [];\n");
		header.append("    for (var i=0; i < ZDSM.process.dataObjectSet.objectArray.length; i++) {\n");
		header.append("        var processId = ZDSM.process.dataObjectSet.objectArray[i].getId();\n");
		header.append("        var process = ZDSM.process.dataObjectSet.objects[processId];\n");
		header.append("        html+=\"<tr>\";\n");
		header.append("        html+=\"<td><b>\";\n");
		header.append("        html+=process.propertyValues[\"name\"];\n");
		header.append("        html+=\"</b></td>\";\n");
		header.append("        for (var i2=0; i2 < ZDSM.domain.dataObjectSet.objectArray.length; i2++) {\n");
		header.append("            var domainId = ZDSM.domain.dataObjectSet.objectArray[i2].getId();\n");
		header.append("            var domain = ZDSM.domain.dataObjectSet.objects[domainId];\n");
		header.append("            var pdId = process.getId() + \"P-\" + domain.getId();\n");
		header.append("            html+=\"<td id='\" + pdId + \"'>&nbsp;</td>\";\n");
		header.append("            if (process.propertyValues[\"domainNameContains\"].length==0 || domain.propertyValues[\"name\"].indexOf(process.propertyValues[\"domainNameContains\"])>=0) {\n");
		header.append("                var from=new Date(now.getTime() - (parseInt(domain.propertyValues[\"checkProcessSeconds\"]) * 1500));\n");
		header.append("                var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.PROCESS_LOG_CLASS_FULL_NAME + "\");\n");
		header.append("                getRequest.filters[\"dateTime\"] = from.getTime();\n");
		header.append("                getRequest.filterOperators[\"dateTime\"] = \"greaterOrEquals\";\n");
		header.append("                getRequest.filters[\"process\"] = processId;\n");
		header.append("                getRequest.filters[\"domain\"] = domainId;\n");
		header.append("                getRequest.orderBy = \"dateTime\";\n");
		header.append("                getRequest.orderAscending = false;\n");
		header.append("                getRequest.limit = 1;\n");
		header.append("                getRequest.properties[0] = \"id\";\n");
		header.append("                getRequest.properties[1] = \"success\";\n");
		header.append("                getRequest.properties[2] = \"dateTime\";\n");
		header.append("                var getter = new ZDSM.monitor.GetProcessLog(getRequest);\n");
		header.append("                getters[getters.length]=getter;\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("        html+=\"</tr>\";\n");
		header.append("        ZODB.dom.showElementById(\"showServicesToggle\");\n");
		header.append("    }\n");
		header.append("    if (ZDSM.process.dataObjectSet.objectArray.length==0) {\n");
		header.append("        ZODB.dom.hideElementById(\"showServicesToggle\");\n");
		header.append("    }\n");
		header.append("    var showServices = ZODB.dom.getInputCheckedByElementId(\"showServices\");\n");
		header.append("    if (ZDSM.process.dataObjectSet.objectArray.length==0 || showServices) {\n");
		header.append("        for (var i=0; i < ZDSM.service.dataObjectSet.objectArray.length; i++) {\n");
		header.append("            var serviceId = ZDSM.service.dataObjectSet.objectArray[i].getId();\n");
		header.append("            var service = ZDSM.service.dataObjectSet.objects[serviceId];\n");
		header.append("            html+=\"<tr>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=service.propertyValues[\"name\"];\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            for (var i2=0; i2 < ZDSM.domain.dataObjectSet.objectArray.length; i2++) {\n");
		header.append("                var domainId = ZDSM.domain.dataObjectSet.objectArray[i2].getId();\n");
		header.append("                var domain = ZDSM.domain.dataObjectSet.objects[domainId];\n");
		header.append("                var sdId = service.getId() + \"S-\" + domain.getId();\n");
		header.append("                html+=\"<td id='\" + sdId + \"'>&nbsp;</td>\";\n");
		header.append("                if (service.propertyValues[\"domainNameContains\"].length==0 || domain.propertyValues[\"name\"].indexOf(service.propertyValues[\"domainNameContains\"])>=0) {\n");
		header.append("                    var from=new Date(now.getTime() - (parseInt(domain.propertyValues[\"checkServiceSeconds\"]) * 1500));\n");
		header.append("                    var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.RESPONSE_CLASS_FULL_NAME + "\");\n");
		header.append("                    getRequest.filters[\"dateTime\"] = from.getTime();\n");
		header.append("                    getRequest.filterOperators[\"dateTime\"] = \"greaterOrEquals\";\n");
		header.append("                    getRequest.filters[\"service\"] = serviceId;\n");
		header.append("                    getRequest.filters[\"domain\"] = domainId;\n");
		header.append("                    getRequest.orderBy = \"dateTime\";\n");
		header.append("                    getRequest.orderAscending = false;\n");
		header.append("                    getRequest.limit = 1;\n");
		header.append("                    getRequest.properties[0] = \"id\";\n");
		header.append("                    getRequest.properties[1] = \"success\";\n");
		header.append("                    getRequest.properties[2] = \"dateTime\";\n");
		header.append("                    getRequest.properties[3] = \"code\";\n");
		header.append("                    var getter = new ZDSM.monitor.GetResponse(getRequest);\n");
		header.append("                    getters[getters.length]=getter;\n");
		header.append("                }\n");
		header.append("            }\n");
		header.append("            html+=\"</tr>\";\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    html+=\"</table>\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"table\",html);\n");
		header.append("    for (var i = 0; i < getters.length; i++) {\n");
		header.append("        getters[i].get();\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZDSM.monitor.refresh = function() {\n");
		header.append("    var refresh = ZODB.dom.getInputCheckedByElementId(\"autoRefresh\");\n");
		header.append("    if (refresh) {\n");
		header.append("        ZDSM.domain.getObjects(ZDSM.monitor.getDomainObjectsCallback);\n");
		header.append("    }\n");
		header.append("    setTimeout(ZDSM.monitor.refresh,30000);\n");
		header.append("};\n");

		header.append("ZDSM.monitor.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZDSM.monitor.refresh();\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZDSM.monitor.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZDSMMenuHtmlForPage("monitor"));
		body.append("<div>");
		body.append("\n");

		body.append("<div>");
		body.append("\n");
		body.append("<input type=\"button\" value=\"Refresh\" onclick=\"ZDSM.domain.getObjects(ZDSM.monitor.getDomainObjectsCallback);\"> ");
		body.append("<input id=\"autoRefresh\" type=\"checkbox\" onclick=\"ZDSM.domain.getObjects(ZDSM.monitor.getDomainObjectsCallback);\" CHECKED/>&nbsp;");
		body.append("Auto&nbsp;refresh ");
		body.append("<div id=\"showServicesToggle\" style=\"visibility: hidden; display: none\">\n");
		body.append("<input id=\"showServices\" type=\"checkbox\" onclick=\"ZDSM.domain.getObjects(ZDSM.monitor.getDomainObjectsCallback);\" CHECKED/>&nbsp;");
		body.append("Show&nbsp;services");
		body.append("</div>");
		body.append("</div>");
		body.append("\n");

		body.append("<div id=\"total\">Initializing ...</div>\n");

		body.append("<div id=\"table\"></div>\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getProcessLogHtml() {
		HTMLFile file = new HTMLFile("ZDSM - Process log");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZDSM.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZDSM = ZDSM || {};\n");
		header.append("ZDSM.process = {};\n");
		header.append("ZDSM.process.processLog = null;\n");
		header.append("ZDSM.process.process = null;\n");
		header.append("ZDSM.process.domain = null;\n");
		
		header.append("ZDSM.process.getProcessLogObjectCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    if (dataObjectSet.objectArray.length>0) {\n");
		header.append("        ZDSM.process.processLog = dataObjectSet.objectArray[0];\n");
		header.append("        var domainId = ZDSM.process.processLog.linkValues[\"domain\"];\n");
		header.append("        ZDSM.process.getDomainObject(domainId);\n");
		header.append("    } else {\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"processLog\",\"No process log was found for the provided processLogId.\");\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.process.getProcessLogObject = function(id) {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.PROCESS_LOG_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.filters[\"id\"] = id;\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZDSM.process.getProcessLogObjectCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZDSM.process.getDomainObjectCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    ZDSM.process.domain = dataObjectSet.objectArray[0];\n");
		header.append("    var processId = ZDSM.process.processLog.linkValues[\"process\"];\n");
		header.append("    ZDSM.process.getProcessObject(processId);\n");
		header.append("};\n");

		header.append("ZDSM.process.getDomainObject = function(id) {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.DOMAIN_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.filters[\"id\"] = id;\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZDSM.process.getDomainObjectCallback,ZODB.gui.genericDomainCallback);\n");	
		header.append("};\n");

		header.append("ZDSM.process.getProcessObjectCallback = function(xhttp) {\n");
		header.append("    ZDSM.process.process = ZODB.data.getDataForClassCallback(xhttp).objectArray[0];\n");
		header.append("    var dateTime = ZDSM.process.processLog.propertyValues[\"dateTime\"];\n");
		header.append("    var dateTimeStr = ZODB.date.toString(new Date(parseInt(dateTime,10)));\n");
		header.append("    var html=\"\";\n");
		header.append("    html+=\"<b>Process log</b><br />\";\n");
		header.append("    html+=\"<table>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Date time\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    if (ZDSM.process.processLog.propertyValues[\"success\"]==\"true\") {\n");
		header.append("        html+=\"<td class='green'>\";\n");
		header.append("    } else {\n");
		header.append("        html+=\"<td class='red'>\";\n");
		header.append("    }\n");
		header.append("    html+=dateTimeStr;\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Duration\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.process.processLog.propertyValues[\"duration\"];\n");
		header.append("    html+=\" ms</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Domain\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.process.domain.propertyValues[\"name\"];\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Process\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td><b>\";\n");
		header.append("    html+=ZDSM.process.process.propertyValues[\"name\"];\n");
		header.append("    html+=\"</b> (\";\n");
		header.append("    html+=ZDSM.process.process.propertyValues[\"className\"];\n");
		header.append("    html+=\")\";\n");
		header.append("    html+=\"<br />\";\n");
		header.append("    html+=ZDSM.process.process.propertyValues[\"description\"];\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    if (ZDSM.process.processLog.propertyValues[\"log\"]) {\n");
		header.append("        html+=\"<tr>\";\n");
		header.append("        html+=\"<td>\";\n");
		header.append("        html+=\"Log\";\n");
		header.append("        html+=\"</td>\";\n");
		header.append("        html+=\"<td>\";\n");
		header.append("        html+=ZDSM.process.processLog.propertyValues[\"log\"].replace(/\\n/g,\"<br />\");\n");
		header.append("        html+=\"</td>\";\n");
		header.append("        html+=\"</tr>\";\n");
		header.append("        html+=\"<tr>\";\n");
		header.append("    }\n");
		header.append("    html+=\"</table>\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"processLog\",html);\n");
		header.append("};\n");

		header.append("ZDSM.process.getProcessObject = function(id) {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.PROCESS_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.filters[\"id\"] = id;\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZDSM.process.getProcessObjectCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZDSM.process.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    var processLogId = ZODB.url.getURLParameterValue(\"processLogId\");\n");
		header.append("    if (processLogId) {\n");
		header.append("        ZDSM.process.getProcessLogObject(processLogId);\n");
		header.append("    } else {\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"processLog\",\"Please provide 'processLogId' URL parameter with a valid value.\");\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZDSM.process.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZDSMMenuHtmlForPage(""));
		body.append("<div>");
		body.append("\n");
		
		body.append("<div id=\"processLog\"></div>\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
	
	protected HTMLFile getResponseHtml() {
		HTMLFile file = new HTMLFile("ZDSM - Response");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZDSM.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZDSM = ZDSM || {};\n");
		header.append("ZDSM.response = {};\n");
		header.append("ZDSM.response.response = null;\n");
		header.append("ZDSM.response.service = null;\n");
		header.append("ZDSM.response.domain = null;\n");
		
		header.append("ZDSM.response.getResponseObjectCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    if (dataObjectSet.objectArray.length>0) {\n");
		header.append("        ZDSM.response.response = dataObjectSet.objectArray[0];\n");
		header.append("        var domainId = ZDSM.response.response.linkValues[\"domain\"];\n");
		header.append("        ZDSM.response.getDomainObject(domainId);\n");
		header.append("    } else {\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"response\",\"No response was found for the provided responseId.\");\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.response.getResponseObject = function(id) {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.RESPONSE_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.filters[\"id\"] = id;\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZDSM.response.getResponseObjectCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZDSM.response.getDomainObjectCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    ZDSM.response.domain = dataObjectSet.objectArray[0];\n");
		header.append("    var serviceId = ZDSM.response.response.linkValues[\"service\"];\n");
		header.append("    ZDSM.response.getServiceObject(serviceId);\n");
		header.append("};\n");

		header.append("ZDSM.response.getDomainObject = function(id) {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.DOMAIN_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.filters[\"id\"] = id;\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZDSM.response.getDomainObjectCallback,ZODB.gui.genericDomainCallback);\n");	
		header.append("};\n");

		header.append("ZDSM.response.getServiceObjectCallback = function(xhttp) {\n");
		header.append("    ZDSM.response.service = ZODB.data.getDataForClassCallback(xhttp).objectArray[0];\n");
		header.append("    var dateTime = ZDSM.response.response.propertyValues[\"dateTime\"];\n");
		header.append("    var dateTimeStr = ZODB.date.toString(new Date(parseInt(dateTime,10)));\n");
		header.append("    var html=\"\";\n");
		header.append("    html+=\"<b>Request</b><br />\";\n");
		header.append("    html+=\"<table>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Date time\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    if (ZDSM.response.response.propertyValues[\"success\"]==\"true\") {\n");
		header.append("        html+=\"<td class='green'>\";\n");
		header.append("    } else {\n");
		header.append("        html+=\"<td class='red'>\";\n");
		header.append("    }\n");
		header.append("    html+=dateTimeStr;\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Duration\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.response.response.propertyValues[\"duration\"];\n");
		header.append("    html+=\" ms</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Domain\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.response.domain.propertyValues[\"name\"];\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Service\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.response.service.propertyValues[\"name\"];\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Call\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.response.response.propertyValues[\"method\"];\n");
		header.append("    html+=\"&nbsp;\";\n");
		header.append("    html+=ZDSM.response.response.propertyValues[\"fullUrl\"];\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    if (ZDSM.response.response.propertyValues[\"requestHeader\"]) {\n");
		header.append("        html+=\"<tr>\";\n");
		header.append("        html+=\"<td>\";\n");
		header.append("        html+=\"Header\";\n");
		header.append("        html+=\"</td>\";\n");
		header.append("        html+=\"<td>\";\n");
		header.append("        html+=ZDSM.response.response.propertyValues[\"requestHeader\"].replace(/\\n/g,\"<br />\");\n");
		header.append("        html+=\"</td>\";\n");
		header.append("        html+=\"</tr>\";\n");
		header.append("        html+=\"<tr>\";\n");
		header.append("    }\n");
		header.append("    if (ZDSM.response.response.propertyValues[\"requestBody\"]) {\n");
		header.append("        html+=\"<tr>\";\n");
		header.append("        html+=\"<td>\";\n");
		header.append("        html+=\"Body\";\n");
		header.append("        html+=\"</td>\";\n");
		header.append("        html+=\"<td>\";\n");
		header.append("        html+=ZDSM.response.response.propertyValues[\"requestBody\"].replace(/\\n/g,\"<br />\");\n");
		header.append("        html+=\"</td>\";\n");
		header.append("        html+=\"</tr>\";\n");
		header.append("        html+=\"<tr>\";\n");
		header.append("    }\n");
		header.append("    html+=\"</table>\";\n");
		header.append("    html+=\"<b>Response</b><br />\";\n");
		header.append("    html+=\"<table>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td></td>\";\n");
		header.append("    html+=\"<td width='49%'>\";\n");
		header.append("    html+=\"<b>Value</b>\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td width='49%'>\";\n");
		header.append("    html+=\"Expected\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Code\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.response.response.propertyValues[\"code\"];\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.response.service.propertyValues[\"expectedCode\"];\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Header\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.response.response.propertyValues[\"header\"].replace(/\\n/g,\"<br />\");\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    if (ZDSM.response.service.propertyValues[\"expectedHeader\"]) {\n");
		header.append("        html+=ZDSM.response.service.propertyValues[\"expectedHeader\"].replace(/\\n/g,\"<br />\");\n");
		header.append("    }\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=\"Body\";\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    html+=ZDSM.response.response.propertyValues[\"body\"].replace(/\\n/g,\"<br />\");\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"<td>\";\n");
		header.append("    if (ZDSM.response.service.propertyValues[\"expectedBody\"]) {\n");
		header.append("        html+=ZDSM.response.service.propertyValues[\"expectedBody\"].replace(/\\n/g,\"<br />\");\n");
		header.append("    }\n");
		header.append("    html+=\"</td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"</table>\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"response\",html);\n");
		header.append("};\n");

		header.append("ZDSM.response.getServiceObject = function(id) {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.SERVICE_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.filters[\"id\"] = id;\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZDSM.response.getServiceObjectCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZDSM.response.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    var responseId = ZODB.url.getURLParameterValue(\"responseId\");\n");
		header.append("    if (responseId) {\n");
		header.append("        ZDSM.response.getResponseObject(responseId);\n");
		header.append("    } else {\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"response\",\"Please provide 'responseId' URL parameter with a valid value.\");\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZDSM.response.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZDSMMenuHtmlForPage(""));
		body.append("<div>");
		body.append("\n");
		
		body.append("<div id=\"response\"></div>\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getAnalyzerHtml() {
		HTMLFile file = new HTMLFile("ZDSM - Analyzer");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZDSM.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZDSM = ZDSM || {};\n");
		header.append("ZDSM.analyze = {};\n");
		header.append("ZDSM.analyze.domain = null;\n");
		header.append("ZDSM.analyze.dateTimeStart = 0;\n");
		header.append("ZDSM.analyze.total = [];\n");
		header.append("ZDSM.analyze.totalSuccess = [];\n");
		header.append("ZDSM.analyze.totalDuration = [];\n");
		header.append("ZDSM.analyze.totalDeviation = [];\n");
		
		header.append("ZDSM.analyze.getDomainObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.domain.getObjectsCallback(xhttp);\n");
		header.append("    ZODB.dom.clearSelectOptionsByElementId(\"domainSelect\");\n");
		header.append("    for (var i=0; i < ZDSM.domain.dataObjectSet.objectArray.length; i++) {\n");
		header.append("        var obj = ZDSM.domain.dataObjectSet.objectArray[i];\n");
		header.append("        ZODB.dom.addSelectOptionByElementId(\"domainSelect\",obj.getId(),obj.propertyValues[\"name\"]);\n");
		header.append("    }\n");
		header.append("    ZDSM.process.getObjects(ZDSM.analyze.getProcessObjectsCallbackTest);\n");
		header.append("};\n");

		header.append("ZDSM.analyze.updateTotalAvailability = function(type) {\n");
		header.append("    var elem = window.document.getElementById(type + \"Availability\");\n");
		header.append("    if (elem) {\n");
		header.append("        var perc = Math.round((ZDSM.analyze.totalSuccess[type] / ZDSM.analyze.total[type]) * 10000) / 100;\n");
		header.append("        elem.innerHTML = \"<b>\" + perc + \"% (\" + ZDSM.analyze.totalSuccess[type] + \"/\" + ZDSM.analyze.total[type] + \")<b>\";\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.analyze.updateTotalDuration = function(type) {\n");
		header.append("    var elem = window.document.getElementById(type + \"Duration\");\n");
		header.append("    if (elem) {\n");
		header.append("        var avg = Math.round(ZDSM.analyze.totalDuration[type] / ZDSM.analyze.totalSuccess[type]);\n");
		header.append("        var dev = 0;\n");
		header.append("        if (ZDSM.analyze.totalSuccess[type] > 1) {\n");
		header.append("            dev = Math.round(Math.sqrt(ZDSM.analyze.totalDeviation[type] / (ZDSM.analyze.totalSuccess[type] - 1)));\n");
		header.append("        }\n");
		header.append("        elem.innerHTML = \"<b>\" + avg + \" ms (+- \" + dev + \" ms)<b>\";\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZDSM.analyze.GetLog = function(request,type) {\n");
		header.append("    var that = this;\n");
		header.append("    this.getRequest = request;\n");
		header.append("    this.totalDataObjectSet = null;\n");
		header.append("    this.get = function() {\n");
		header.append("        ZODB.data.executePostRequest(this.getRequest,this.callback,ZODB.gui.genericResponseCallback);\n");	
		header.append("    }\n");
		header.append("    this.callback = function(xhttp) {\n");
		header.append("        var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("        if (that.totalDataObjectSet==null) {\n");
		header.append("            that.totalDataObjectSet = dataObjectSet;\n");
		header.append("        } else {\n");
		header.append("            for (var i = 0; i<dataObjectSet.objectArray.length; i++) {\n");
		header.append("                that.totalDataObjectSet.objects[dataObjectSet.objectArray[i].getId()] = dataObjectSet.objectArray[i];\n");
		header.append("                that.totalDataObjectSet.objectArray[that.totalDataObjectSet.objectArray.length] = dataObjectSet.objectArray[i];\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("        var updateTotals = true;\n");
		header.append("        if (dataObjectSet.objectArray.length>=100) {\n");
		header.append("            var updateTotals = false;\n");
		header.append("            that.getRequest.start+=100;\n");
		header.append("            that.get();\n");
		header.append("        }\n");
		header.append("        that.refresh(that.totalDataObjectSet,updateTotals);\n");
		header.append("    }\n");
		header.append("    this.refresh = function(dataObjectSet,updateTotals) {\n");
		header.append("        var elemA = window.document.getElementById(that.getRequest.filters[type] + \"-A\" + type);\n");
		header.append("        var elemT = window.document.getElementById(that.getRequest.filters[type] + \"-T\" + type);\n");
		header.append("        var elemD = window.document.getElementById(that.getRequest.filters[type] + \"-D\" + type);\n");
		header.append("        var elemTD = window.document.getElementById(that.getRequest.filters[type] + \"-TD\" + type);\n");
		header.append("        if (dataObjectSet.objectArray.length>0) {\n");
		header.append("            var total = dataObjectSet.objectArray.length;\n");
		header.append("            var duration = 0;\n");
		header.append("            var success = 0;\n");
		header.append("            var totalHalf1 = 0;\n");
		header.append("            var totalHalf2 = 0;\n");
		header.append("            var successHalf1 = 0;\n");
		header.append("            var successHalf2 = 0;\n");
		header.append("            var durationHalf1 = 0;\n");
		header.append("            var durationHalf2 = 0;\n");
		header.append("            for (var i = 0; i<total; i++) {\n");
		header.append("                if (i < (total/2)) {\n");
		header.append("                    totalHalf1++;\n");
		header.append("                } else {\n");
		header.append("                    totalHalf2++;\n");
		header.append("                }\n");
		header.append("                var log = dataObjectSet.objectArray[i];\n");
		header.append("                if (log.propertyValues[\"success\"]==\"true\") {\n");
		header.append("                    success++;\n");
		header.append("                    var dur = parseInt(log.propertyValues[\"duration\"],10);\n");
		header.append("                    duration += dur;\n");
		header.append("                    if (i < (total/2)) {\n");
		header.append("                        successHalf1++;\n");
		header.append("                        durationHalf1+=dur;\n");
		header.append("                    } else {\n");
		header.append("                        successHalf2++;\n");
		header.append("                        durationHalf2+=dur;\n");
		header.append("                    }\n");
		header.append("                }\n");
		header.append("            }\n");
		header.append("            var percTotal = Math.round((success / total) * 10000) / 100;\n");
		header.append("            var percHalf1 = Math.round((successHalf1 / totalHalf1) * 10000) / 100;\n");
		header.append("            var percHalf2 = Math.round((successHalf2 / totalHalf2) * 10000) / 100;\n");
		header.append("            var avgHalf1 = 0;\n");
		header.append("            var avgHalf2 = 0;\n");
		header.append("            if (successHalf1>0) {\n");
		header.append("                avgHalf1 = Math.round(durationHalf1 / successHalf1);\n");
		header.append("            }\n");
		header.append("            if (successHalf2>0) {\n");
		header.append("                avgHalf2 = Math.round(durationHalf2 / successHalf2);\n");
		header.append("            }\n");
		header.append("            elemA.innerHTML=percTotal + \"% (\" + success + \"/\" + total + \")\";\n");
		header.append("            if (total<=1) {\n");
		header.append("                elemT.innerHTML=\"?\";\n");
		header.append("                elemT.className=\"\";\n");
		header.append("            } else {\n");
		header.append("                elemT.innerHTML=percHalf1 + \"% - \" + percHalf2 + \"%\";\n");
		header.append("                if (percHalf1==percHalf2) {\n");
		header.append("                    elemT.className=\"\";\n");
		header.append("                } else if (percHalf1>percHalf2) {\n");
		header.append("                    elemT.className=\"red\";\n");
		header.append("                } else if (percHalf1<percHalf2) {\n");
		header.append("                    elemT.className=\"green\";\n");
		header.append("                }\n");
		header.append("            }\n");
		header.append("            var avgDuration = Math.round(duration / success);\n");
		header.append("            var deviation = 0;\n");
		header.append("            for (var i = 0; i<total; i++) {\n");
		header.append("                var log = dataObjectSet.objectArray[i];\n");
		header.append("                if (log.propertyValues[\"success\"]==\"true\") {\n");
		header.append("                    var dur = parseInt(log.propertyValues[\"duration\"],10);\n");
		header.append("                    if (dur >= avgDuration) {\n");
		header.append("                        deviation = deviation + ((dur - avgDuration) * (dur - avgDuration));\n");
		header.append("                    } else {\n");
		header.append("                        deviation = deviation + ((avgDuration - dur) * (avgDuration - dur));\n");
		header.append("                    }\n");
		header.append("                }\n");
		header.append("            }\n");
		header.append("            var devDuration = 0;\n");
		header.append("            if (success>1) {\n");
		header.append("                devDuration = Math.round(Math.sqrt(deviation / (success - 1)));\n");
		header.append("            }\n");
		header.append("            elemD.innerHTML=avgDuration + \" ms (+- \" + devDuration + \" ms)\";\n");
		header.append("            if (total<=1) {\n");
		header.append("                elemTD.innerHTML=\"?\";\n");
		header.append("                elemTD.className=\"\";\n");
		header.append("            } else {\n");
		header.append("                elemTD.innerHTML=avgHalf1 + \" ms - \" + avgHalf2 + \" ms\";\n");
		header.append("                if (avgHalf1==avgHalf2) {\n");
		header.append("                    elemTD.className=\"\";\n");
		header.append("                } else if (avgHalf1>avgHalf2) {\n");
		header.append("                    elemTD.className=\"green\";\n");
		header.append("                } else if (avgHalf1<avgHalf2) {\n");
		header.append("                    elemTD.className=\"red\";\n");
		header.append("                }\n");
		header.append("            }\n");
		header.append("            if (updateTotals) {\n");
		header.append("                ZDSM.analyze.total[type]+=total;\n");
		header.append("                ZDSM.analyze.totalDuration[type]+=duration;\n");
		header.append("                ZDSM.analyze.totalDeviation[type]+=deviation;\n");
		header.append("                ZDSM.analyze.totalSuccess[type]+=success;\n");
		header.append("                ZDSM.analyze.updateTotalAvailability(type);\n");
		header.append("                ZDSM.analyze.updateTotalDuration(type);\n");
		header.append("            }\n");
 		header.append("        } else {\n");
		header.append("            elemA.innerHTML=\"?\";\n");
		header.append("            elemT.innerHTML=\"?\";\n");
		header.append("            elemT.className=\"\";\n");
		header.append("            elemD.innerHTML=\"?\";\n");
		header.append("            elemTD.innerHTML=\"?\";\n");
		header.append("            elemTD.className=\"\";\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.analyze.getProcessObjectsCallbackTest = function(xhttp) {\n");
		header.append("    ZDSM.process.getObjectsCallback(xhttp);\n");
		header.append("    if (ZDSM.process.dataObjectSet.objectArray.length>0) {\n");
		header.append("        ZODB.dom.showElementById(\"showServicesToggle\");\n");
		header.append("    } else {\n");
		header.append("        ZODB.dom.hideElementById(\"showServicesToggle\");\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZDSM.analyze.getProcessObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.process.getObjectsCallback(xhttp);\n");
		header.append("    ZDSM.analyze.renderProcesses();\n");
		header.append("    var showServices = ZODB.dom.getInputCheckedByElementId(\"showServices\");\n");
		header.append("    if (ZDSM.process.dataObjectSet.objectArray.length==0 || showServices) {\n");
		header.append("        ZDSM.service.getObjects(ZDSM.analyze.getServiceObjectsCallback);\n");
		header.append("    } else {\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"services\",\"\");\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.analyze.renderProcesses = function() {\n");
		header.append("    if (ZDSM.process.dataObjectSet.objectArray.length>0) {\n");
		header.append("        var html=\"\";\n");
		header.append("        html+=\"<b>Processes</b>\";\n");
		header.append("        html+=\"<table>\";\n");
		header.append("        html+=\"<tr>\";\n");
		header.append("        html+=\"<th>\";\n");
		header.append("        html+=\"Process\";\n");
		header.append("        html+=\"</th>\";\n");
		header.append("        html+=\"<th>\";\n");
		header.append("        html+=\"Availability\";\n");
		header.append("        html+=\"</th>\";\n");
		header.append("        html+=\"<th>\";\n");
		header.append("        html+=\"Trend\";\n");
		header.append("        html+=\"</th>\";\n");
		header.append("        html+=\"<th>\";\n");
		header.append("        html+=\"Avg. duration\";\n");
		header.append("        html+=\"</th>\";\n");
		header.append("        html+=\"<th>\";\n");
		header.append("        html+=\"Trend\";\n");
		header.append("        html+=\"</th>\";\n");
		header.append("        html+=\"</tr>\";\n");
		header.append("        var getters=[];\n");
		header.append("        for (var i=0; i < ZDSM.process.dataObjectSet.objectArray.length; i++) {\n");
		header.append("            var processId = ZDSM.process.dataObjectSet.objectArray[i].getId();\n");
		header.append("            var process = ZDSM.process.dataObjectSet.objects[processId];\n");
		header.append("            var domain = ZDSM.analyze.domain;\n");
		header.append("            if (process.propertyValues[\"domainNameContains\"].length==0 || domain.propertyValues[\"name\"].indexOf(process.propertyValues[\"domainNameContains\"])>=0) {\n");
		header.append("                html+=\"<tr>\";\n");
		header.append("                html+=\"<td>\";\n");
		header.append("                html+=process.propertyValues[\"name\"];\n");
		header.append("                html+=\"</td>\";\n");
		header.append("                html+=\"<td id='\" + process.getId() + \"-Aprocess'>&nbsp;</td>\";\n");
		header.append("                html+=\"<td id='\" + process.getId() + \"-Tprocess'>&nbsp;</td>\";\n");
		header.append("                html+=\"<td id='\" + process.getId() + \"-Dprocess'>&nbsp;</td>\";\n");
		header.append("                html+=\"<td id='\" + process.getId() + \"-TDprocess'>&nbsp;</td>\";\n");
		header.append("                html+=\"</tr>\";\n");
		header.append("                var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.PROCESS_LOG_CLASS_FULL_NAME + "\");\n");
		header.append("                getRequest.filters[\"process\"] = processId;\n");
		header.append("                getRequest.filters[\"domain\"] = domain.getId();\n");
		header.append("                getRequest.filters[\"dateTime\"] = ZDSM.analyze.dateTimeStart;\n");
		header.append("                getRequest.filterOperators[\"dateTime\"] = \"greaterOrEquals\";\n");
		header.append("                getRequest.orderBy = \"dateTime\";\n");
		header.append("                getRequest.orderAscending = true;\n");
		header.append("                getRequest.properties[0] = \"dateTime\";\n");
		header.append("                getRequest.properties[1] = \"duration\";\n");
		header.append("                getRequest.properties[2] = \"success\";\n");
		header.append("                getRequest.start = 0;\n");
		header.append("                getRequest.limit = 100;\n");
		header.append("                var getter = new ZDSM.analyze.GetLog(getRequest,\"process\");\n");
		header.append("                getters[getters.length]=getter;\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("        html+=\"<tr>\";\n");
		header.append("        html+=\"<td></td>\";\n");
		header.append("        html+=\"<td id='processAvailability'>&nbsp;</td>\";\n");
		header.append("        html+=\"<td></td>\";\n");
		header.append("        html+=\"<td id='processDuration'>&nbsp;</td>\";\n");
		header.append("        html+=\"<td></td>\";\n");
		header.append("        html+=\"</tr>\";\n");
		header.append("        html+=\"</table>\";\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"processes\",html);\n");
		header.append("        ZODB.dom.showElementById(\"showServicesToggle\");\n");
		header.append("        for (var i = 0; i < getters.length; i++) {\n");
		header.append("            getters[i].get();\n");
		header.append("        }\n");
		header.append("    } else {\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"processes\",\"\");\n");
		header.append("        ZODB.dom.hideElementById(\"showServicesToggle\");\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZDSM.analyze.getServiceObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.service.getObjectsCallback(xhttp);\n");
		header.append("    var html=\"\";\n");
		header.append("    html+=\"<b>Services</b>\";\n");
		header.append("    html+=\"<table>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Service\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Availability\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Trend\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Avg. duration\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Trend\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    var getters=[];\n");
		header.append("    for (var i=0; i < ZDSM.service.dataObjectSet.objectArray.length; i++) {\n");
		header.append("        var serviceId = ZDSM.service.dataObjectSet.objectArray[i].getId();\n");
		header.append("        var service = ZDSM.service.dataObjectSet.objects[serviceId];\n");
		header.append("        var domain = ZDSM.analyze.domain;\n");
		header.append("        if (service.propertyValues[\"domainNameContains\"].length==0 || domain.propertyValues[\"name\"].indexOf(service.propertyValues[\"domainNameContains\"])>=0) {\n");
		header.append("            html+=\"<tr>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=service.propertyValues[\"name\"];\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td id='\" + service.getId() + \"-Aservice'>&nbsp;</td>\";\n");
		header.append("            html+=\"<td id='\" + service.getId() + \"-Tservice'>&nbsp;</td>\";\n");
		header.append("            html+=\"<td id='\" + service.getId() + \"-Dservice'>&nbsp;</td>\";\n");
		header.append("            html+=\"<td id='\" + service.getId() + \"-TDservice'>&nbsp;</td>\";\n");
		header.append("            html+=\"</tr>\";\n");
		header.append("            var getRequest = new ZODB.data.GetRequest(\"" + ZDSMModel.RESPONSE_CLASS_FULL_NAME + "\");\n");
		header.append("            getRequest.filters[\"service\"] = serviceId;\n");
		header.append("            getRequest.filters[\"domain\"] = domain.getId();\n");
		header.append("            getRequest.filters[\"dateTime\"] = ZDSM.analyze.dateTimeStart;\n");
		header.append("            getRequest.filterOperators[\"dateTime\"] = \"greaterOrEquals\";\n");
		header.append("            getRequest.orderBy = \"dateTime\";\n");
		header.append("            getRequest.orderAscending = true;\n");
		header.append("            getRequest.properties[0] = \"dateTime\";\n");
		header.append("            getRequest.properties[1] = \"duration\";\n");
		header.append("            getRequest.properties[2] = \"success\";\n");
		header.append("            getRequest.start = 0;\n");
		header.append("            getRequest.limit = 100;\n");
		header.append("            var getter = new ZDSM.analyze.GetLog(getRequest,\"service\");\n");
		header.append("            getters[getters.length]=getter;\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<td></td>\";\n");
		header.append("    html+=\"<td id='serviceAvailability'>&nbsp;</td>\";\n");
		header.append("    html+=\"<td></td>\";\n");
		header.append("    html+=\"<td id='serviceDuration'>&nbsp;</td>\";\n");
		header.append("    html+=\"<td></td>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    html+=\"</table>\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"services\",html);\n");
		header.append("    for (var i = 0; i < getters.length; i++) {\n");
		header.append("        getters[i].get();\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.analyze.analyze = function() {\n");
		header.append("    ZDSM.analyze.total[\"process\"] = 0;\n");
		header.append("    ZDSM.analyze.totalDuration[\"process\"] = 0;\n");
		header.append("    ZDSM.analyze.totalDeviation[\"process\"] = 0;\n");
		header.append("    ZDSM.analyze.totalSuccess[\"process\"] = 0;\n");
		header.append("    ZDSM.analyze.total[\"service\"] = 0;\n");
		header.append("    ZDSM.analyze.totalDuration[\"service\"] = 0;\n");
		header.append("    ZDSM.analyze.totalDeviation[\"service\"] = 0;\n");
		header.append("    ZDSM.analyze.totalSuccess[\"service\"] = 0;\n");
		header.append("    var period = ZODB.dom.getSelectValueByElementId(\"periodSelect\");\n");
		header.append("    var date = new Date();\n");
		header.append("    date.setHours(0);\n");
		header.append("    date.setMinutes(0);\n");
		header.append("    date.setSeconds(0);\n");
		header.append("    date.setMilliseconds(0);\n");
		header.append("    if (period==\"7days\") {\n");
		header.append("        date.setTime(date.getTime() - (7 * 86400000));\n");
		header.append("    } else if (period==\"14days\") {\n");
		header.append("        date.setTime(date.getTime() - (14 * 86400000));\n");
		header.append("    } else if (period==\"30days\") {\n");
		header.append("        date.setTime(date.getTime() - (30 * 86400000));\n");
		header.append("    } else if (period==\"90days\") {\n");
		header.append("        date.setTime(date.getTime() - (90 * 86400000));\n");
		header.append("    }\n");
		header.append("    ZDSM.analyze.dateTimeStart = date.getTime();\n");
		header.append("    var domainId = ZODB.dom.getSelectValueByElementId(\"domainSelect\");\n");
		header.append("    if (domainId) {\n");
		header.append("        domainId=parseInt(domainId,10);\n");
		header.append("    }\n");
		header.append("    if (domainId) {\n");
		header.append("        ZDSM.analyze.domain=ZDSM.domain.dataObjectSet.objects[domainId];\n");
		header.append("        ZDSM.process.getObjects(ZDSM.analyze.getProcessObjectsCallback);\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZDSM.analyze.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZDSM.domain.getObjects(ZDSM.analyze.getDomainObjectsCallback);\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZDSM.analyze.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZDSMMenuHtmlForPage("analyzer"));
		body.append("<div>");
		body.append("\n");

		body.append("<div>\n");
		body.append("<input type=\"button\" value=\"Analyze\" onclick=\"ZDSM.analyze.analyze();\">\n");
		body.append(" <select id=\"domainSelect\"></select>\n");
		body.append(" <select id=\"periodSelect\">\n");
		body.append("<option value=\"today\" SELECTED>Today</option>\n");
		body.append("<option value=\"7days\">Past 7 days</option>\n");
		body.append("<option value=\"14days\">Past 14 days</option>\n");
		body.append("<option value=\"30days\">Past 30 days</option>\n");
		body.append("<option value=\"90days\">Past 90 days</option>\n");
		body.append("</select>\n");
		body.append("<div id=\"showServicesToggle\" style=\"visibility: hidden; display: none\">\n");
		body.append("<input id=\"showServices\" type=\"checkbox\"\" onclick=\"ZDSM.analyze.analyze();\" CHECKED/>&nbsp;Show&nbsp;services");
		body.append("</div>\n");
		body.append("</div>\n");

		body.append("<div id=\"processes\"></div>\n");
		
		body.append("<div id=\"services\"></div>\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getServiceExportImportHtml() {
		HTMLFile file = new HTMLFile("ZDSM - Export/Import");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZDSM.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZDSM = ZDSM || {};\n");
		header.append("ZDSM.exportImport = {};\n");
		header.append("ZDSM.exportImport.doExport = true;\n");
		header.append("ZDSM.exportImport.importDataObjectSet = 0;\n");
		header.append("ZDSM.exportImport.importIndex = 0;\n");
		header.append("ZDSM.exportImport.importLog = \"\";\n");
		header.append("ZDSM.exportImport.importUpdateRequest = null;\n");

		header.append("ZDSM.exportImport.importGetObjectCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    if (dataObjectSet.objectArray.length==0) {\n");
		header.append("        var obj = ZDSM.exportImport.importUpdateRequest.dataObjectSet.objectArray[0];\n");	
		header.append("        if (ZDSM.exportImport.importDataObjectSet.className==\"" + ZDSMModel.SERVICE_CLASS_FULL_NAME + "\") {\n");
		header.append("            ZDSM.exportImport.appendImportLogLine(\"Adding service: <b>\" + obj.propertyValues[\"name\"] + \"</b>\");\n");
		header.append("        } else if (ZDSM.exportImport.importDataObjectSet.className==\"" + ZDSMModel.DOMAIN_CLASS_FULL_NAME + "\") {\n");
		header.append("            ZDSM.exportImport.appendImportLogLine(\"Adding domain: <b>\" + obj.propertyValues[\"name\"] + \"</b>\");\n");
		header.append("        }\n");
		header.append("        request = new ZODB.data.AddRequest(ZDSM.exportImport.importDataObjectSet.className);\n");
		header.append("        request.dataObjectSet.objects[obj.getId()] = obj\n");
		header.append("        request.dataObjectSet.objectArray[0] = obj\n");
		header.append("        ZODB.data.executePostRequest(request,ZDSM.exportImport.importObjectCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("    } else {\n");
		header.append("        var obj = ZDSM.exportImport.importUpdateRequest.dataObjectSet.objectArray[0];\n");	
		header.append("        if (ZDSM.exportImport.importDataObjectSet.className==\"" + ZDSMModel.SERVICE_CLASS_FULL_NAME + "\") {\n");
		header.append("            ZDSM.exportImport.appendImportLogLine(\"Updating service: <b>\" + obj.propertyValues[\"name\"] + \"</b>\");\n");
		header.append("        } else if (ZDSM.exportImport.importDataObjectSet.className==\"" + ZDSMModel.DOMAIN_CLASS_FULL_NAME + "\") {\n");
		header.append("            ZDSM.exportImport.appendImportLogLine(\"Updating domain: <b>\" + obj.propertyValues[\"name\"] + \"</b>\");\n");
		header.append("        }\n");
		header.append("        ZODB.data.executePostRequest(ZDSM.exportImport.importUpdateRequest,ZDSM.exportImport.importObjectCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.importObjectCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    ZDSM.exportImport.appendImportLogLine(ZDSM.exportImport.convertLogForDisplay(dataObjectSet.log))\n");
		header.append("    request = ZDSM.exportImport.getNextUpdateRequest();\n");
		header.append("    if (request!=null) {\n");
		header.append("        ZDSM.exportImport.importUpdateRequest = request;\n");	
		header.append("        ZODB.data.executePostRequest(request.getRequest,ZDSM.exportImport.importGetObjectCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("    } else {\n");
		header.append("        ZDSM.exportImport.appendImportLogLine(\"Done.\");\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.getDomainObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.domain.getObjectsCallback(xhttp);\n");
		header.append("    ZDSM.service.getObjectsFilterActive(ZDSM.exportImport.getServiceObjectsCallback,false);\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.getServiceObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.service.getObjectsCallback(xhttp);\n");
		header.append("    if (ZDSM.exportImport.doExport) {\n");
		header.append("        var dataExport = ZODB.dom.getSelectValueByElementId(\"dataExport\");\n");
		header.append("        if (dataExport==\"Services\") {\n");
		header.append("            for (var id in ZDSM.service.dataObjectSet.objects) {\n");
		header.append("                var obj = ZDSM.service.dataObjectSet.objects[id];\n");
		header.append("                obj.propertyValues[\"requestBody\"] = ZDSM.exportImport.convertBodyForExport(obj.propertyValues[\"requestBody\"]);\n");
		header.append("                obj.propertyValues[\"body\"] = ZDSM.exportImport.convertBodyForExport(obj.propertyValues[\"body\"]);\n");
		header.append("                obj.propertyValues[\"active\"] = false;\n");
		header.append("            }\n"); 
		header.append("            document.getElementById(\"dataTextArea\").value = \"<dataSet>\" + ZDSM.service.dataObjectSet.toXML() + \"</dataSet>\";\n");
		header.append("        } else if (dataExport==\"Domains\") {\n");
		header.append("            for (var id in ZDSM.domain.dataObjectSet.objects) {\n");
		header.append("                var obj = ZDSM.domain.dataObjectSet.objects[id];\n");
		header.append("                obj.propertyValues[\"userName\"] = \"\";\n");
		header.append("                obj.propertyValues[\"userPassword\"] = \"\";\n");
		header.append("                obj.propertyValues[\"active\"] = false;\n");
		header.append("            }\n"); 
		header.append("            document.getElementById(\"dataTextArea\").value = \"<dataSet>\" + ZDSM.domain.dataObjectSet.toXML() + \"</dataSet>\";\n");
		header.append("        }\n"); 
		header.append("    } else {\n");
		header.append("        ZDSM.exportImport.importIndex = 0;\n");
		header.append("        ZDSM.exportImport.importLog = \"\";\n");
		header.append("        try {\n");
		header.append("            ZDSM.exportImport.importDataObjectSet = ZDSM.exportImport.parseDataTextArea();\n");
		header.append("        } catch(e) {\n");
		header.append("            ZDSM.exportImport.importDataObjectSet = null;\n");
		header.append("            ZDSM.exportImport.appendImportLogLine(\"Error: \" + e);\n");
		header.append("        }\n");
		header.append("        request = ZDSM.exportImport.getNextUpdateRequest();\n");
		header.append("        if (ZDSM.exportImport.importDataObjectSet && request!=null) {\n");
		header.append("            ZDSM.exportImport.appendImportLogLine(\"Importing ...\");\n");
		header.append("            ZDSM.exportImport.importUpdateRequest = request;\n");	
		header.append("            ZODB.data.executePostRequest(request.getRequest,ZDSM.exportImport.importGetObjectCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("        } else {\n");
		header.append("            ZDSM.exportImport.appendImportLogLine(\"No objects to import.\");\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.convertBodyForExport = function(body) {\n");
		header.append("    if (body) {\n");
		header.append("        body = body.replace(/&/g,\"&amp;\");\n");
		header.append("    }\n");
		header.append("    return body;\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.convertBodyForImport = function(body) {\n");
		header.append("    if (body) {\n");
		header.append("        body = body.replace(/&amp;lt;/g,\"&lt;\");\n");
		header.append("        body = body.replace(/&amp;gt;/g,\"&gt;\");\n");
		header.append("    }\n");
		header.append("    return body;\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.convertLogForDisplay = function(log) {\n");
		header.append("    if (log) {\n");
		header.append("        log = log.replace(/\\n/g,\"<br />\");\n");
		header.append("    }\n");
		header.append("    return log;\n");
		header.append("};\n");
		
		header.append("ZDSM.exportImport.appendImportLogLine = function(line) {\n");
		header.append("    ZDSM.exportImport.importLog += line + \"<br />\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"importLog\",ZDSM.exportImport.importLog);\n");
		header.append("};\n");
		
		header.append("ZDSM.exportImport.getNextUpdateRequest = function() {\n");
		header.append("    var request = null;\n");
		header.append("    if (ZDSM.exportImport.importIndex<ZDSM.exportImport.importDataObjectSet.objectArray.length) {\n");
		header.append("        var obj = ZDSM.exportImport.importDataObjectSet.objectArray[ZDSM.exportImport.importIndex];\n");
		header.append("        obj.propertyValues[\"requestBody\"] = ZDSM.exportImport.convertBodyForImport(obj.propertyValues[\"requestBody\"]);\n");
		header.append("        obj.propertyValues[\"body\"] = ZDSM.exportImport.convertBodyForImport(obj.propertyValues[\"body\"]);\n");
		header.append("        request = new ZODB.data.UpdateRequest(ZDSM.exportImport.importDataObjectSet.className);\n");
		header.append("        request.getRequest.filters[\"id\"] = obj.getId();\n");
		header.append("        request.dataObjectSet.objects[obj.getId()] = obj\n");
		header.append("        request.dataObjectSet.objectArray[0] = obj\n");
		header.append("        ZDSM.exportImport.importIndex++;\n");
		header.append("    }\n");
		header.append("    return request;\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.parseDataTextArea = function() {\n");
		header.append("    var dataObjectSet = null;\n");
		header.append("    var xml = window.document.getElementById(\"dataTextArea\").value;\n");
		header.append("    var xmlDoc = null;\n");
		header.append("    if (window.DOMParser) {\n");
		header.append("        parser = new DOMParser();\n");
		header.append("        xmlDoc = parser.parseFromString(xml, \"text/xml\");\n");
		header.append("    } else {\n");
		header.append("        xmlDoc = new ActiveXObject(\"Microsoft.XMLDOM\");\n");
		header.append("        xmlDoc.async = false;\n");
		header.append("        xmlDoc.loadXML(xml);\n");
		header.append("    }\n");
		header.append("    if (xmlDoc!=null) {\n");
		header.append("        dataObjectSet = ZODB.data.parseDbDataObjectSetFromXml(xmlDoc);\n");
		header.append("    }\n");
		header.append("    return dataObjectSet;\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.export = function() {\n");
		header.append("    ZDSM.exportImport.doExport = true;\n");
		header.append("    ZDSM.domain.getObjectsFilterActive(ZDSM.exportImport.getDomainObjectsCallback,false);\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.import = function() {\n");
		header.append("    ZDSM.exportImport.doExport = false;\n");
		header.append("    ZDSM.service.getObjects(ZDSM.exportImport.getServiceObjectsCallback);\n");
		header.append("};\n");

		header.append("ZDSM.exportImport.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZDSM.exportImport.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZDSMMenuHtmlForPage(""));
		body.append("<div>");
		body.append("\n");

		body.append("<div>");
		body.append("\n");
		body.append("<b>Data export and import</b><br />");
		body.append("</div>");
		body.append("\n");

		body.append("<div>");
		body.append("\n");
		body.append("<input type=\"button\" value=\"Export\" onclick=\"ZDSM.exportImport.export();\"/>\n");
		body.append(" <select id=\"dataExport\">\n");
		body.append("<option value=\"Services\" SELECTED>Services</option>\n");
		body.append("<option value=\"Domains\">Domains</option>\n");
		body.append("</select>\n");
		body.append("</div>");
		body.append("\n");

		body.append("<div>");
		body.append("\n");
		body.append("<textarea id=\"dataTextArea\">\n");
		body.append("Press the 'Export' button above to export the selected data into this text area. ");
		body.append("To import, paste the exported data into this text area and press the 'Import' button below. ");
		body.append("Services and domains will be exported as inactive. ");
		body.append("Domains will be exported without account information. ");
		body.append("</textarea>\n");
		body.append("</div>");
		body.append("\n");

		body.append("<div>");
		body.append("\n");
		body.append("<input type=\"button\" value=\"Import\" onclick=\"ZDSM.exportImport.import();\"/>\n");
		body.append("</div>");
		body.append("\n");
		
		body.append("<div id=\"importLog\"></div>");
		body.append("\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getManagerHtml() {
		HTMLFile file = new HTMLFile("ZDSM - Manager");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZDSM.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZDSM = ZDSM || {};\n");
		header.append("ZDSM.manager = {};\n");

		header.append("ZDSM.manager.Saver = function(request,cb) {\n");
		header.append("    var that = this;\n");
		header.append("    this.request = request;\n");
		header.append("    this.execute = function() {\n");
		header.append("        ZODB.data.executePostRequest(this.request,this.callback,this.errorCallback);\n");	
		header.append("    }\n");
		header.append("    this.callback = function(xhttp) {\n");
		header.append("    }\n");
		header.append("    this.errorCallback = function(xhttp) {\n");
		header.append("    }\n");
		header.append("    if (cb) {\n");
		header.append("        this.callback = cb;\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.manager.getProcessObjectsCallbackTest = function(xhttp) {\n");
		header.append("    ZDSM.process.getObjectsCallback(xhttp);\n");
		header.append("    ZDSM.manager.renderDomains();\n");
		header.append("};\n");

		header.append("ZDSM.manager.getDomainObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.domain.getObjectsCallback(xhttp);\n");
		header.append("    var total = ZDSM.domain.dataObjectSet.objectArray.length;\n");
		header.append("    var add = new ZODB.data.DbDataObject();\n");
		header.append("    add.propertyValues[\"id\"] = \"0\";\n");
		header.append("    add.propertyValues[\"active\"] = \"true\";\n");
		header.append("    add.propertyValues[\"name\"] = \"\";\n");
		header.append("    add.propertyValues[\"address\"] = \"\";\n");
		header.append("    add.propertyValues[\"port\"] = 80;\n");
		header.append("    add.propertyValues[\"checkProcessSeconds\"] = 900;\n");
		header.append("    add.propertyValues[\"checkServiceSeconds\"] = 600;\n");
		header.append("    add.propertyValues[\"keepProcessLogDays\"] = 90;\n");
		header.append("    add.propertyValues[\"keepResponseDays\"] = 30;\n");
		header.append("    add.propertyValues[\"removeMe\"] = false;\n");
		header.append("    add.propertyValues[\"https\"] = false;\n");
		header.append("    add.propertyValues[\"addDomainToPath\"] = true;\n");
		header.append("    add.propertyValues[\"addHeaderHost\"] = true;\n");
		header.append("    add.propertyValues[\"addHeaderHostPort\"] = true;\n");
		header.append("    add.propertyValues[\"addHeaderAuthBasic\"] = false;\n");
		header.append("    add.propertyValues[\"userName\"] = \"\";\n");
		header.append("    add.propertyValues[\"userPassword\"] = \"\";\n");
		header.append("    ZDSM.domain.dataObjectSet.objectArray[total]=add;\n");
		header.append("    ZDSM.domain.dataObjectSet.objects[\"0\"]=add;\n");
		header.append("    ZDSM.manager.renderDomains();\n");
		header.append("    ZDSM.process.getObjectsFilterActive(ZDSM.manager.getProcessObjectsCallbackTest,false);\n");
		header.append("};\n");

		header.append("ZDSM.manager.renderDomains = function() {\n");
		header.append("    var html=\"\";\n");
		header.append("    html+=\"<table>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Active\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Name\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Address\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Port\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    if (ZDSM.process && ZDSM.process.dataObjectSet && ZDSM.process.dataObjectSet.objectArray.length>0) {\n");
		header.append("        html+=\"<th>\";\n");
		header.append("        html+=\"Check process seconds\";\n");
		header.append("        html+=\"</th>\";\n");
		header.append("    }\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Check service seconds\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    if (ZDSM.process && ZDSM.process.dataObjectSet && ZDSM.process.dataObjectSet.objectArray.length==0) {\n");
		header.append("        html+=\"<th>\";\n");
		header.append("        html+=\"Keep response days\";\n");
		header.append("        html+=\"</th>\";\n");
		header.append("    }\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    if (ZDSM.domain.dataObjectSet.objectArray.length>0) {\n");
		header.append("        for (var i=0; i < ZDSM.domain.dataObjectSet.objectArray.length; i++) {\n");
		header.append("            var domainId = ZDSM.domain.dataObjectSet.objectArray[i].getId();\n");
		header.append("            var domain = ZDSM.domain.dataObjectSet.objects[domainId];\n");
		header.append("            var idPfx = \"domain-\" + domainId + \"-\";\n");
		header.append("            var activeChecked = \"\";\n");
		header.append("            if (domain.propertyValues[\"active\"]==\"true\") {\n");
		header.append("                activeChecked = \" CHECKED\";\n");
		header.append("            }\n");
		header.append("            html+=\"<tr>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"active' type='checkbox'\" + activeChecked + \" />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"name' type='text' class='textInput' value='\" + domain.propertyValues[\"name\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"address' type='text' class='textInput' value='\" + domain.propertyValues[\"address\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"port' type='number' value='\" + domain.propertyValues[\"port\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            if (ZDSM.process && ZDSM.process.dataObjectSet && ZDSM.process.dataObjectSet.objectArray.length>0) {\n");
		header.append("                html+=\"<td>\";\n");
		header.append("                html+=\"<input id='\" + idPfx + \"checkProcessSeconds' type='number' value='\" + domain.propertyValues[\"checkProcessSeconds\"] + \"' />\";\n");
		header.append("                html+=\"</td>\";\n");
		header.append("            }\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"checkServiceSeconds' type='number' value='\" + domain.propertyValues[\"checkServiceSeconds\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            if (ZDSM.process && ZDSM.process.dataObjectSet && ZDSM.process.dataObjectSet.objectArray.length==0) {\n");
		header.append("                html+=\"<td>\";\n");
		header.append("                html+=\"<input id='\" + idPfx + \"keepResponseDays' type='number' value='\" + domain.propertyValues[\"keepResponseDays\"] + \"' />\";\n");
		header.append("                html+=\"</td>\";\n");
		header.append("            }\n");
		header.append("            html+=\"</tr>\";\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    html+=\"</table>\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"domainTable\",html);\n");
		header.append("};\n");

		header.append("ZDSM.manager.saveDomains = function() {\n");
		header.append("    if (ZDSM.domain.dataObjectSet.objectArray.length>0) {\n");
		header.append("        for (var i=0; i < ZDSM.domain.dataObjectSet.objectArray.length; i++) {\n");
		header.append("            var domainId = ZDSM.domain.dataObjectSet.objectArray[i].getId();\n");
		header.append("            var domain = ZDSM.domain.dataObjectSet.objects[domainId];\n");
		header.append("            var idPfx = \"domain-\" + domainId + \"-\";\n");
		header.append("            var active = ZODB.dom.getInputCheckedByElementId(idPfx + \"active\");\n");
		header.append("            var name = ZODB.dom.getInputValueByElementId(idPfx + \"name\");\n");
		header.append("            var address = ZODB.dom.getInputValueByElementId(idPfx + \"address\");\n");
		header.append("            var port = ZODB.dom.getInputValueByElementId(idPfx + \"port\");\n");
		header.append("            var checkProcessSeconds = ZODB.dom.getInputValueByElementId(idPfx + \"checkProcessSeconds\");\n");
		header.append("            var checkServiceSeconds = ZODB.dom.getInputValueByElementId(idPfx + \"checkServiceSeconds\");\n");
		header.append("            var keepProcessDays = ZODB.dom.getInputValueByElementId(idPfx + \"keepProcessDays\");\n");
		header.append("            domain.propertyValues[\"active\"] = active.toString();\n");
		header.append("            domain.propertyValues[\"name\"] = name;\n");
		header.append("            domain.propertyValues[\"address\"] = address;\n");
		header.append("            domain.propertyValues[\"port\"] = port;\n");
		header.append("            if (checkProcessSeconds) {\n");
		header.append("                domain.propertyValues[\"checkProcessSeconds\"] = checkProcessSeconds;\n");
		header.append("            }\n");
		header.append("            domain.propertyValues[\"checkServiceSeconds\"] = checkServiceSeconds;\n");
		header.append("            if (keepProcessDays) {\n");
		header.append("                domain.propertyValues[\"keepProcessDays\"] = keepProcessDays;\n");
		header.append("            }\n");
		header.append("            if (domain.getId()>0 || (name!=\"\" && address!=\"\")) {\n");
		header.append("                var request = null;\n");
		header.append("                var callback = null;\n");
		header.append("                if (domain.getId()>0) {\n");
		header.append("                    request = new ZODB.data.UpdateRequest(\"" + ZDSMModel.DOMAIN_CLASS_FULL_NAME + "\");\n");
		header.append("                    request.getRequest.filters[\"id\"] = domainId;\n");
		header.append("                    request.dataObjectSet.objects[domainId] = domain\n");
		header.append("                    request.dataObjectSet.objectArray[0] = domain\n");
		header.append("                } else {\n");
		header.append("                    request = new ZODB.data.AddRequest(\"" + ZDSMModel.DOMAIN_CLASS_FULL_NAME + "\");\n");
		header.append("                    request.dataObjectSet.objects[domainId] = domain\n");
		header.append("                    request.dataObjectSet.objectArray[0] = domain\n");
		header.append("                    callback = ZDSM.manager.refreshDomains;\n");
		header.append("                }\n");
		header.append("                var saver = new ZDSM.manager.Saver(request,callback);\n");
		header.append("                saver.execute();\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZDSM.manager.refreshDomains = function() {\n");
		header.append("    ZDSM.domain.getObjectsFilterActive(ZDSM.manager.getDomainObjectsCallback,false);\n");
		header.append("};\n");
		
		header.append("ZDSM.manager.getProcessObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.process.getObjectsCallback(xhttp);\n");
		header.append("    var html=\"\";\n");
		header.append("    html+=\"<table>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Active\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Name\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Domain name contains\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Class name\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    if (ZDSM.process.dataObjectSet.objectArray.length>0) {\n");
		header.append("        for (var i=0; i < ZDSM.process.dataObjectSet.objectArray.length; i++) {\n");
		header.append("            var processId = ZDSM.process.dataObjectSet.objectArray[i].getId();\n");
		header.append("            var process = ZDSM.process.dataObjectSet.objects[processId];\n");
		header.append("            var idPfx = \"process-\" + processId + \"-\";\n");
		header.append("            var activeChecked = \"\";\n");
		header.append("            if (process.propertyValues[\"active\"]==\"true\") {\n");
		header.append("                activeChecked = \" CHECKED\";\n");
		header.append("            }\n");
		header.append("            html+=\"<tr>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"active' type='checkbox'\" + activeChecked + \" />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"name' type='text' class='textInput' value='\" + process.propertyValues[\"name\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"domainNameContains' type='text' class='textInput' value='\" + process.propertyValues[\"domainNameContains\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=process.propertyValues[\"className\"];\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"</tr>\";\n");
		header.append("        }\n");
		header.append("        ZODB.dom.showElementById(\"processManager\");\n");
		header.append("    }\n");
		header.append("    html+=\"</table>\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"processTable\",html);\n");
		header.append("};\n");

		header.append("ZDSM.manager.saveProcesses = function() {\n");
		header.append("    if (ZDSM.process.dataObjectSet.objectArray.length>0) {\n");
		header.append("        for (var i=0; i < ZDSM.process.dataObjectSet.objectArray.length; i++) {\n");
		header.append("            var processId = ZDSM.process.dataObjectSet.objectArray[i].getId();\n");
		header.append("            var process = ZDSM.process.dataObjectSet.objects[processId];\n");
		header.append("            var idPfx = \"process-\" + processId + \"-\";\n");
		header.append("            var active = ZODB.dom.getInputCheckedByElementId(idPfx + \"active\");\n");
		header.append("            var name = ZODB.dom.getInputValueByElementId(idPfx + \"name\");\n");
		header.append("            var domainNameContains = ZODB.dom.getInputValueByElementId(idPfx + \"domainNameContains\");\n");
		header.append("            process.propertyValues[\"active\"] = active.toString();\n");
		header.append("            process.propertyValues[\"name\"] = name;\n");
		header.append("            process.propertyValues[\"domainNameContains\"] = domainNameContains;\n");
		header.append("            var request = new ZODB.data.UpdateRequest(\"" + ZDSMModel.PROCESS_CLASS_FULL_NAME + "\");\n");
		header.append("            request.getRequest.filters[\"id\"] = processId;\n");
		header.append("            request.dataObjectSet.objects[processId] = process\n");
		header.append("            request.dataObjectSet.objectArray[0] = process\n");
		header.append("            var saver = new ZDSM.manager.Saver(request);\n");
		header.append("            saver.execute();\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZDSM.manager.refreshProcesses = function() {\n");
		header.append("    ZDSM.process.getObjectsFilterActive(ZDSM.manager.getProcessObjectsCallback,false);\n");
		header.append("};\n");

		header.append("ZDSM.manager.getServiceObjectsCallback = function(xhttp) {\n");
		header.append("    ZDSM.service.getObjectsCallback(xhttp);\n");
		header.append("    var html=\"\";\n");
		header.append("    html+=\"<table>\";\n");
		header.append("    html+=\"<tr>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Active\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Name\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Domain name contains\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Method\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Path\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Expected code\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Time out\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"<th>\";\n");
		header.append("    html+=\"Time out seconds\";\n");
		header.append("    html+=\"</th>\";\n");
		header.append("    html+=\"</tr>\";\n");
		header.append("    var total = ZDSM.service.dataObjectSet.objectArray.length;\n");
		header.append("    var add = new ZODB.data.DbDataObject();\n");
		header.append("    add.propertyValues[\"id\"] = \"0\";\n");
		header.append("    add.propertyValues[\"active\"] = \"true\";\n");
		header.append("    add.propertyValues[\"name\"] = \"\";\n");
		header.append("    add.propertyValues[\"domainNameContains\"] = \"\";\n");
		header.append("    add.propertyValues[\"method\"] = \"GET\";\n");
		header.append("    add.propertyValues[\"path\"] = \"\";\n");
		header.append("    add.propertyValues[\"expectedCode\"] = \"200\";\n");
		header.append("    add.propertyValues[\"timeOut\"] = \"true\";\n");
		header.append("    add.propertyValues[\"timeOutSeconds\"] = 3;\n");
		header.append("    add.propertyValues[\"removeMe\"] = false;\n");
		header.append("    add.propertyValues[\"addHeaderContentLength\"] = true;\n");
		header.append("    ZDSM.service.dataObjectSet.objectArray[total]=add;\n");
		header.append("    ZDSM.service.dataObjectSet.objects[\"0\"]=add;\n");
		header.append("    if (ZDSM.service.dataObjectSet.objectArray.length>0) {\n");
		header.append("        for (var i=0; i < ZDSM.service.dataObjectSet.objectArray.length; i++) {\n");
		header.append("            var serviceId = ZDSM.service.dataObjectSet.objectArray[i].getId();\n");
		header.append("            var service = ZDSM.service.dataObjectSet.objects[serviceId];\n");
		header.append("            var idPfx = \"service-\" + serviceId + \"-\";\n");
		header.append("            var activeChecked = \"\";\n");
		header.append("            if (service.propertyValues[\"active\"]==\"true\") {\n");
		header.append("                activeChecked = \" CHECKED\";\n");
		header.append("            }\n");
		header.append("            var timeOutChecked = \"\";\n");
		header.append("            if (service.propertyValues[\"timeOut\"]==\"true\") {\n");
		header.append("                timeOutChecked = \" CHECKED\";\n");
		header.append("            }\n");
		header.append("            html+=\"<tr>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"active' type='checkbox'\" + activeChecked + \" />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"name' type='text' class='textInput' value='\" + service.propertyValues[\"name\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"domainNameContains' type='text' class='textInput' value='\" + service.propertyValues[\"domainNameContains\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=ZDSM.manager.getHttpMethodSelect(idPfx + \"method\",service.propertyValues[\"method\"]);\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"path' type='text' class='textInput' value='\" + service.propertyValues[\"path\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"expectedCode' type='text' class='textInput' value='\" + service.propertyValues[\"expectedCode\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"timeOut' type='checkbox'\" + timeOutChecked + \" />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"<td>\";\n");
		header.append("            html+=\"<input id='\" + idPfx + \"timeOutSeconds' type='text' class='textInput' value='\" + service.propertyValues[\"timeOutSeconds\"] + \"' />\";\n");
		header.append("            html+=\"</td>\";\n");
		header.append("            html+=\"</tr>\";\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    html+=\"</table>\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"serviceTable\",html);\n");
		header.append("};\n");

		header.append("ZDSM.manager.getHttpMethodSelect = function(id,value) {\n");
		header.append("    var html =\"\";\n");
		header.append("    html+=\"<select id='\" + id + \"'>\";\n");
		header.append("    html+=\"<option value='OPTIONS'\";\n");
		header.append("    if (value==\"OPTIONS\") {\n");
		header.append("        html+=\" SELECTED\";\n");
		header.append("    }\n");
		header.append("    html+=\">OPTIONS</option>\";\n");
		header.append("    html+=\"<option value='GET'\";\n");
		header.append("    if (value==\"GET\") {\n");
		header.append("        html+=\" SELECTED\";\n");
		header.append("    }\n");
		header.append("    html+=\">GET</option>\";\n");
		header.append("    html+=\"<option value='HEAD'\";\n");
		header.append("    if (value==\"HEAD\") {\n");
		header.append("        html+=\" SELECTED\";\n");
		header.append("    }\n");
		header.append("    html+=\">HEAD</option>\";\n");
		header.append("    html+=\"<option value='POST'\";\n");
		header.append("    if (value==\"POST\") {\n");
		header.append("        html+=\" SELECTED\";\n");
		header.append("    }\n");
		header.append("    html+=\">POST</option>\";\n");
		header.append("    html+=\"<option value='PUT'\";\n");
		header.append("    if (value==\"PUT\") {\n");
		header.append("        html+=\" SELECTED\";\n");
		header.append("    }\n");
		header.append("    html+=\">PUT</option>\";\n");
		header.append("    html+=\"<option value='DELETE'\";\n");
		header.append("    if (value==\"DELETE\") {\n");
		header.append("        html+=\" SELECTED\";\n");
		header.append("    }\n");
		header.append("    html+=\">DELETE</option>\";\n");
		header.append("    html+=\"<option value='TRACE'\";\n");
		header.append("    if (value==\"TRACE\") {\n");
		header.append("        html+=\" SELECTED\";\n");
		header.append("    }\n");
		header.append("    html+=\">TRACE</option>\";\n");
		header.append("    html+=\"<option value='CONNECT'\";\n");
		header.append("    if (value==\"CONNECT\") {\n");
		header.append("        html+=\" SELECTED\";\n");
		header.append("    }\n");
		header.append("    html+=\">CONNECT</option>\";\n");
		header.append("    html+=\"</select>\";\n");
		header.append("    return html;\n");
		header.append("};\n");
		
		header.append("ZDSM.manager.saveServices = function() {\n");
		header.append("    if (ZDSM.service.dataObjectSet.objectArray.length>0) {\n");
		header.append("        for (var i=0; i < ZDSM.service.dataObjectSet.objectArray.length; i++) {\n");
		header.append("            var serviceId = ZDSM.service.dataObjectSet.objectArray[i].getId();\n");
		header.append("            var service = ZDSM.service.dataObjectSet.objects[serviceId];\n");
		header.append("            var idPfx = \"service-\" + serviceId + \"-\";\n");
		header.append("            var active = ZODB.dom.getInputCheckedByElementId(idPfx + \"active\");\n");
		header.append("            var name = ZODB.dom.getInputValueByElementId(idPfx + \"name\");\n");
		header.append("            var domainNameContains = ZODB.dom.getInputValueByElementId(idPfx + \"domainNameContains\");\n");
		header.append("            var method = ZODB.dom.getInputValueByElementId(idPfx + \"method\");\n");
		header.append("            var path = ZODB.dom.getInputValueByElementId(idPfx + \"path\");\n");
		header.append("            var expectedCode = ZODB.dom.getInputValueByElementId(idPfx + \"expectedCode\");\n");
		header.append("            var timeOut = ZODB.dom.getInputCheckedByElementId(idPfx + \"timeOut\");\n");
		header.append("            var timeOutSeconds = ZODB.dom.getInputValueByElementId(idPfx + \"timeOutSeconds\");\n");
		header.append("            service.propertyValues[\"active\"] = active.toString();\n");
		header.append("            service.propertyValues[\"name\"] = name;\n");
		header.append("            service.propertyValues[\"domainNameContains\"] = domainNameContains;\n");
		header.append("            service.propertyValues[\"method\"] = method;\n");
		header.append("            service.propertyValues[\"path\"] = path;\n");
		header.append("            service.propertyValues[\"expectedCode\"] = expectedCode;\n");
		header.append("            service.propertyValues[\"timeOut\"] = timeOut.toString();\n");
		header.append("            service.propertyValues[\"timeOutSeconds\"] = timeOutSeconds;\n");
		header.append("            if (service.getId()>0 || (name!=\"\" && method!=\"\")) {\n");
		header.append("                var request = null;\n");
		header.append("                var callback = null;\n");
		header.append("                if (service.getId()>0) {\n");
		header.append("                    request = new ZODB.data.UpdateRequest(\"" + ZDSMModel.SERVICE_CLASS_FULL_NAME + "\");\n");
		header.append("                    request.getRequest.filters[\"id\"] = serviceId;\n");
		header.append("                    request.dataObjectSet.objects[serviceId] = service\n");
		header.append("                    request.dataObjectSet.objectArray[0] = service\n");
		header.append("                } else {\n");
		header.append("                    request = new ZODB.data.AddRequest(\"" + ZDSMModel.SERVICE_CLASS_FULL_NAME + "\");\n");
		header.append("                    request.dataObjectSet.objects[serviceId] = service\n");
		header.append("                    request.dataObjectSet.objectArray[0] = service\n");
		header.append("                    callback = ZDSM.manager.refreshServices;\n");
		header.append("                }\n");
		header.append("                var saver = new ZDSM.manager.Saver(request,callback);\n");
		header.append("                saver.execute();\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZDSM.manager.refreshServices = function() {\n");
		header.append("    ZDSM.service.getObjectsFilterActive(ZDSM.manager.getServiceObjectsCallback,false);\n");
		header.append("};\n");

		header.append("ZDSM.manager.refresh = function() {\n");
		header.append("    ZDSM.manager.refreshDomains();\n");
		header.append("    ZDSM.manager.refreshProcesses();\n");
		header.append("    ZDSM.manager.refreshServices();\n");
		header.append("};\n");

		header.append("ZDSM.manager.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZDSM.manager.refresh();\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZDSM.manager.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZDSMMenuHtmlForPage("manager"));
		body.append("<div>");
		body.append("\n");

		body.append("<div><input type=\"button\" value=\"Refresh\" onclick=\"ZDSM.manager.refreshDomains();\" /> <a href=\"/domainManager.html\">Domain manager</a></div>\n");
		body.append("<div id=\"domainTable\"></div>\n");
		body.append("<div><input type=\"button\" value=\"Save\" onclick=\"ZDSM.manager.saveDomains();\" /></div>\n");
		
		body.append("<div id=\"processManager\" style=\"visibility: hidden; display: none\">\n");
		body.append("<div><br /></div>\n");
		body.append("<div><input type=\"button\" value=\"Refresh\" onclick=\"ZDSM.manager.refreshProcesses();\" /> <a href=\"/processManager.html\">Process manager</a></div>\n");
		body.append("<div id=\"processTable\"></div>\n");
		body.append("<div><input type=\"button\" value=\"Save\" onclick=\"ZDSM.manager.saveProcesses();\" /></div>\n");
		body.append("</div>\n");

		body.append("<div><br /></div>\n");
		body.append("<div><input type=\"button\" value=\"Refresh\" onclick=\"ZDSM.manager.refreshServices();\" /> <a href=\"/serviceManager.html\">Service manager</a></div>\n");
		body.append("<div id=\"serviceTable\"></div>\n");
		body.append("<div><input type=\"button\" value=\"Save\" onclick=\"ZDSM.manager.saveServices();\" /></div>\n");
		
		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}		

	protected HTMLFile getProcessManagerHtml() {
		String[] properties = {"name","domainNameContains","className","active","removeMe"};
		String[] disabledProperties = {};
		
		SortedMap<String,String> initialPropertyValues = new TreeMap<String,String>();
		initialPropertyValues.put("active","true");
		initialPropertyValues.put("removeMe","false");
		
		return getManagerHtmlForPackageClass(
			ZDSMModel.class.getPackage().getName(),
			ZDSMModel.PROCESS_CLASS_NAME,
			"ZDSM - Process manager",
			"Process manager",
			getZDSMMenuHtmlForPage(""),
			properties,
			initialPropertyValues,
			disabledProperties
			);
	}
	

	protected HTMLFile getServiceManagerHtml() {
		String[] properties = {"name","domainNameContains","method","path","expectedCode","active","removeMe"};
		String[] disabledProperties = {};
		
		SortedMap<String,String> initialPropertyValues = new TreeMap<String,String>();
		initialPropertyValues.put("method","GET");
		initialPropertyValues.put("addHeaderContentLength","true");
		initialPropertyValues.put("timeOut","true");
		initialPropertyValues.put("timeOutSeconds","3");
		initialPropertyValues.put("expectedCode","200");
		initialPropertyValues.put("active","true");
		initialPropertyValues.put("removeMe","false");
		
		return getManagerHtmlForPackageClass(
			ZDSMModel.class.getPackage().getName(),
			ZDSMModel.SERVICE_CLASS_NAME,
			"ZDSM - Service manager",
			"Service manager",
			getZDSMMenuHtmlForPage(""),
			properties,
			initialPropertyValues,
			disabledProperties
			);
	}
	
	protected HTMLFile getDomainManagerHtml() {
		String[] properties = {"name","address","port","checkProcessSeconds","checkServiceSeconds","keepProcessLogDays","keepResponseDays","https","active","removeMe"};
		String[] disabledProperties = {};
		
		SortedMap<String,String> initialPropertyValues = new TreeMap<String,String>();
		initialPropertyValues.put("address","localhost");
		initialPropertyValues.put("port","80");
		initialPropertyValues.put("checkProcessSeconds","900");
		initialPropertyValues.put("checkServiceSeconds","600");
		initialPropertyValues.put("keepProcessLogDays","90");
		initialPropertyValues.put("keepResponseDays","30");
		initialPropertyValues.put("active","true");
		initialPropertyValues.put("removeMe","false");
		initialPropertyValues.put("https","false");
		initialPropertyValues.put("addDomainToPath","true");
		initialPropertyValues.put("addHeaderHost","true");
		initialPropertyValues.put("addHeaderHostPort","true");
		initialPropertyValues.put("addHeaderAuthBasic","false");
		
		return getManagerHtmlForPackageClass(
			ZDSMModel.class.getPackage().getName(),
			ZDSMModel.DOMAIN_CLASS_NAME,
			"ZDSM - Domain manager",
			"Domain manager",
			getZDSMMenuHtmlForPage(""),
			properties,
			initialPropertyValues,
			disabledProperties
			);
	}

	protected StringBuilder getDomainJavaScript() {
		return getDataJavaScript(ZDSMModel.DOMAIN_CLASS_FULL_NAME,"domain");
	}

	protected StringBuilder getServiceJavaScript() {
		return getDataJavaScript(ZDSMModel.SERVICE_CLASS_FULL_NAME,"service");
	}

	protected StringBuilder getProcessJavaScript() {
		return getDataJavaScript(ZDSMModel.PROCESS_CLASS_FULL_NAME,"process");
	}
	
	protected StringBuilder getDataJavaScript(String fullClassName,String nameSpace) {
		StringBuilder script = new StringBuilder();
		script.append("var ZDSM = ZDSM || {};\n");
		script.append("ZDSM." + nameSpace + " = {};\n");
		script.append("ZDSM." + nameSpace + ".dataObjectSet = null;\n");

		script.append("ZDSM." + nameSpace + ".getObjects = function(callback) {\n");
		script.append("    ZDSM." + nameSpace + ".getObjectsFilterActive(callback,true);\n");
		script.append("}\n");

		script.append("ZDSM." + nameSpace + ".getObjectsFilterActive = function(callback,active) {\n");
		script.append("    if (!callback) {\n");
		script.append("        callback = ZDSM." + nameSpace + ".getObjectsCallback;\n");
		script.append("    }\n");
		script.append("    var getRequest = new ZODB.data.GetRequest(\"" + fullClassName + "\");\n");
		script.append("    if (active) {\n");
		script.append("        getRequest.filters[\"active\"] = \"true\";\n");
		script.append("    }\n");
		script.append("    getRequest.filters[\"removeMe\"] = \"false\";\n");
		script.append("    getRequest.orderBy = \"name\";\n");
		script.append("    getRequest.orderAscending = true;\n");
		script.append("    getRequest.properties[0] = \"*\";\n");
		script.append("    ZODB.data.executeGetRequest(getRequest,callback,ZODB.gui.genericResponseCallback);\n");	
		script.append("};\n");
		
		script.append("ZDSM." + nameSpace + ".getObjectsCallback = function(xhttp) {\n");
		script.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		script.append("    ZDSM." + nameSpace + ".dataObjectSet = dataObjectSet;\n");
		script.append("};\n");

		return script;
	}

	protected StringBuilder getCSS() {
		StringBuilder css = super.getCSS();

		css.append(".red {\n");
		css.append("    background-color: red;\n");
		css.append("}\n");
		css.append(".green {\n");
		css.append("    background-color: #00ff00;\n");
		css.append("}\n");
				
		return css;
	}
}
