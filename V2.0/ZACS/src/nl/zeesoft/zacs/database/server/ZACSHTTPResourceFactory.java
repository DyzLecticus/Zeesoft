package nl.zeesoft.zacs.database.server;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.file.HTMLFile;

public class ZACSHTTPResourceFactory extends SvrHTTPResourceFactory {

	@Override
	protected void initializeHTTPResources() {
		super.initializeHTTPResources();

		StringBuilder js = new StringBuilder();
		js.append(getCommandJavaScript());
		js.append(getControlJavaScript());
		addFile("ZACS.js",js);

		addFile("monitor.html",getMonitorHtml().toStringBuilder());
		addFile("control.html",getControlHtml().toStringBuilder());
		addFile("exampleManager.html",getExampleManagerHtml().toStringBuilder());
		addFile("assignmentManager.html",getAssignmentManagerHtml().toStringBuilder());
		addFile("crawlerManager.html",getCrawlerManagerHtml().toStringBuilder());
	}

	@Override
	protected String getIndexMenuTitle() {
		return "ZACS";
	}

	protected StringBuilder getZACSMenuHtmlForPage(String page) {
		List<String> pages = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		
		pages.add("index");
		pages.add("monitor");
		pages.add("control");
		pages.add("exampleManager");
		pages.add("assignmentManager");
		pages.add("home");

		titles.add("Home");
		titles.add("Monitor");
		titles.add("Control");
		titles.add("Examples");
		titles.add("Assignments");
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
		return "ZACS - Authorizer";
	}
	
	@Override
	protected StringBuilder getAuthorizerMenuHtml() {
		return getZACSMenuHtmlForPage("");
	}
	
	@Override
	protected HTMLFile get404Html() {
		HTMLFile file = new HTMLFile("ZACS - 404 Not Found");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append(getZACSMenuHtmlForPage(""));
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
		HTMLFile file = new HTMLFile("ZACS - Home");
		file.setBodyBgColor(BACKGROUND_COLOR);

		StringBuilder body = new StringBuilder();
		body.append(getZACSMenuHtmlForPage("index"));
		body.append("<div>");
		body.append("\n");
		body.append("Welcome to the Zeesoft Artifical Cognition Simulator. ");
		body.append("You can use this interface to <a href=\"/monitor.html\">monitor</a> and <a href=\"/control.html\">control</a> the simulation. ");
		body.append("The simulation learns text input/output <a href=\"/exampleManager.html\">examples</a> and then uses that knowledge to complete <a href=\"/assignmentManager.html\">assignments</a>. ");
		body.append("<b>Please note</b> that changes to the parameters and/or examples will take effect after reactivating the simulation. ");
		body.append("<br/>");
		body.append("<br/>");
		body.append("<b>Artificial cognition</b><br/>");
		body.append("It is thought that cognition is the result of neural networks evolving to have parts of their behavioral control structures turned inwardly, creating virtual thinking muscles. ");
		body.append("It is also thought that every mental object is represented in the brain by a certain group of neurons expressing excitation. ");
		body.append("In this simulation the virtual muscles are called modules and the neurons representing certain mental objects are called symbols. ");
		body.append("Human language capabilities are thought to be an innate result of cognition so the process that assembles symbols into thoughts is reflected in linguistic expression. ");
		body.append("This makes words and punctuation the ideal candidates to function as symbols. ");
		body.append("Knowledge is gained by learning the links between symbols in a stochastic manner. ");
		body.append("Knowledge is applied by using modules to evaluate all relevant links between symbols simultaniously. ");
		body.append("<br/>");
		body.append("<br/>");
		body.append("<b>Inspiration</b><br/>");
		body.append("This software was inspired by ideas put forth in the following books; ");
		body.append("<ul> ");
		body.append("<li><b>Langauge and Mind</b> by Noam Chomsky</li>");
		body.append("<li><b>Godel Esher Bach</b> by Douglas R. Hofstadter</li>");
		body.append("<li><b>I Am a Strange Loop</b> by Douglas R. Hofstadter</li>");
		body.append("<li><b>The Mind's I</b> by Douglas R. Hofstadter and Daniel C. Dennet</li>");
		body.append("<li><b>Consciousness Explained</b> by Daniel C. Dennet</li>");
		body.append("<li><b>Confabulation Theory</b> by Robert Hecht-Nielsen</li>");
		body.append("</ul> ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getExampleManagerHtml() {
		return getManagerHtmlForPackageClass(
			ZACSModel.class.getPackage().getName(),
			ZACSModel.EXAMPLE_CLASS_NAME,
			"ZACS - Example manager",
			"Example manager",
			getZACSMenuHtmlForPage("exampleManager")
			);
	}

	protected HTMLFile getAssignmentManagerHtml() {
		String[] properties = {"id","name","dateTimeFinished","firedSymbolLinks","durationMilliseconds"};
		String[] disabledProperties = {"dateTimeFinished","workingModule","workingContext","workingOutput","log","output","correctedInput","correctedInputSymbols","inputContext","outputContext","numberOfSymbols","firedSymbolLinks","firedContextLinks","durationMilliseconds","dateTimePrevOutput1","dateTimePrevOutput2","dateTimePrevOutput3","prevOutput1","prevOutput2","prevOutput3"};
		
		SortedMap<String,String> initialPropertyValues = new TreeMap<String,String>();
		initialPropertyValues.put("maxSymbols","64");
		initialPropertyValues.put("stopOnLineEndSymbol","false");
		initialPropertyValues.put("thinkWidth","50");
		initialPropertyValues.put("thinkFast","false");
		initialPropertyValues.put("logExtended","false");
		initialPropertyValues.put("contextDynamic","false");
		initialPropertyValues.put("correctInput","false");
		initialPropertyValues.put("correctLineEnd","false");
		initialPropertyValues.put("correctInputOnly","false");
		
		return getManagerHtmlForPackageClass(
			ZACSModel.class.getPackage().getName(),
			ZACSModel.ASSIGNMENT_CLASS_NAME,
			"ZACS - Assignment manager",
			"Assignment manager",
			getZACSMenuHtmlForPage("assignmentManager"),
			properties,
			initialPropertyValues,
			disabledProperties
			);
	}

	protected HTMLFile getCrawlerManagerHtml() {
		String[] properties = {"id","crawlUrl","scrapedTextLength","crawl","convertTextToExamples"};
		String[] disabledProperties = {"crawledUrls","scrapedText","scrapedTextLength"};

		SortedMap<String,String> initialPropertyValues = new TreeMap<String,String>();
		initialPropertyValues.put("crawl","true");
		initialPropertyValues.put("crawlUrl","https://www");
		initialPropertyValues.put("respectRobotSpec","true");
		initialPropertyValues.put("maxCrawlUrls","10");
		initialPropertyValues.put("minTextLength","500");
		initialPropertyValues.put("parseLists","false");
		initialPropertyValues.put("convertTextToExamples","false");
		initialPropertyValues.put("convertMaxExamples","100");
		initialPropertyValues.put("convertSentenceMinLength","20");
		
		return getManagerHtmlForPackageClass(
			ZACSModel.class.getPackage().getName(),
			ZACSModel.CRAWLER_CLASS_NAME,
			"ZACS - Crawler manager",
			"Crawler manager",
			getZACSMenuHtmlForPage(""),
			properties,
			initialPropertyValues,
			disabledProperties
			);
	}
	
	protected StringBuilder getCommandJavaScript() {
		StringBuilder script = new StringBuilder();
		script.append("var ZACS = ZACS || {};\n");
		script.append("ZACS.command = {};\n");
		script.append("ZACS.command.commands = null;\n");

		script.append("ZACS.command.getCommands = function(callback) {\n");
		script.append("    if (!callback) {\n");
		script.append("        callback = ZACS.command.getCommandsCallback;\n");
		script.append("    }\n");
		script.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZACSModel.COMMAND_CLASS_FULL_NAME + "\");\n");
		script.append("    ZODB.data.executeGetRequest(getRequest,callback,ZODB.gui.genericResponseCallback);\n");	
		script.append("};\n");
		
		script.append("ZACS.command.getCommandsCallback = function(xhttp) {\n");
		script.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		script.append("    ZACS.command.commands = dataObjectSet.objects;\n");
		script.append("};\n");

		script.append("ZACS.command.getCommandIdByCode = function(code) {\n");
		script.append("    var commandId = null;\n");
		script.append("    if (ZACS.command.commands) {\n");
		script.append("        for (var id in ZACS.command.commands) {\n");
		script.append("            if (ZACS.command.commands[id].propertyValues[\"code\"]==code) {\n");
		script.append("                commandId = id;\n");
		script.append("                break;\n");
		script.append("            };\n");
		script.append("        };\n");
		script.append("    };\n");
		script.append("    return commandId;\n");
		script.append("};\n");
		
		script.append("ZACS.command.getCommandByCode = function(code) {\n");
		script.append("    var command = null;\n");
		script.append("    var commandId = ZACS.command.getCommandIdByCode(code);\n");
		script.append("    if (commandId) {\n");
		script.append("        command = ZACS.command.commands[commandId];\n");
		script.append("    };\n");
		script.append("    return command;\n");
		script.append("};\n");

		return script;
	}

	protected StringBuilder getControlJavaScript() {
		StringBuilder script = new StringBuilder();
		script.append("var ZACS = ZACS || {};\n");
		script.append("ZACS.control = ZACS.control || {};\n");
		script.append("ZACS.control.controlId = null;\n");
		script.append("ZACS.control.updateControlSeconds = 3;\n");
		script.append("ZACS.control.addHistorySeconds = 10;\n");
		script.append("ZACS.control.statusDivId = null;\n");
		script.append("ZACS.control.getControlCallbackFunction = null;\n");

		script.append("ZACS.control.getControl = function(callback) {\n");
		script.append("    if (callback) {\n");
		script.append("        ZACS.control.getControlCallbackFunction = callback;\n");
		script.append("    } else if (ZACS.control.getControlCallbackFunction==null) {\n");
		script.append("        ZACS.control.getControlCallbackFunction = ZACS.control.getControlCallback;\n");
		script.append("    }\n");
		script.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZACSModel.CONTROL_CLASS_FULL_NAME + "\");\n");
		script.append("    getRequest.properties = [\"id\",\"status\",\"updateControlSeconds\",\"addHistorySeconds\"];\n");
		script.append("    ZODB.data.executeGetRequest(getRequest,ZACS.control.getControlCallbackFunction,ZODB.gui.genericResponseCallback);\n");	
		script.append("};\n");

		script.append("ZACS.control.getControlCallback = function(xhttp) {\n");
		script.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		script.append("    var status = \"\";\n");
		script.append("    for (var id in dataObjectSet.objects) {\n");
		script.append("        status = dataObjectSet.objects[id].propertyValues[\"status\"];\n");
		script.append("        ZACS.control.controlId = id;\n");
		script.append("        ZACS.control.updateControlSeconds = parseInt(dataObjectSet.objects[id].propertyValues[\"updateControlSeconds\"],10);\n");
		script.append("        ZACS.control.addHistorySeconds = parseInt(dataObjectSet.objects[id].propertyValues[\"addHistorySeconds\"],10);\n");
		script.append("        break;\n");
		script.append("    };\n");
		script.append("    if (ZACS.control.statusDivId) {\n");
		script.append("        ZODB.dom.setDivInnerHTMLByElementId(ZACS.control.statusDivId,\"Status: <b>\" + status + \"</b>\");\n");
		script.append("    }\n");
		script.append("    setTimeout(ZACS.control.getControl,(ZACS.control.updateControlSeconds * 1000));\n");
		script.append("    return dataObjectSet;\n");
		script.append("};\n");		

		return script;
	}
	
	protected HTMLFile getMonitorHtml() {
		HTMLFile file = new HTMLFile("ZACS - Monitor");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZACS.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZACS = ZACS || {};\n");
		header.append("ZACS.monitor = {};\n");
		
		header.append("ZACS.monitor.getStateHistory = function() {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZACSModel.STATE_HISTORY_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.properties = [\"statAvgSymLinkCount\",\"statAvgConLinkCount\",\"statTotalSymbolLevel\",\"statTotalSymbols\",\"statTotalSymLinks\"];\n");
		header.append("    getRequest.orderBy = \"dateTime\";\n");
		header.append("    getRequest.orderAscending = false;\n");
		header.append("    getRequest.limit = 10;\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZACS.monitor.getStateHistoryCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZACS.monitor.getStateHistoryCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    if (dataObjectSet.objectArray.length>0) {\n");
		header.append("        dataObjectSet.propertyNameArray = [\"statAvgSymLinkCount\",\"statAvgConLinkCount\",\"statTotalSymbolLevel\",\"statTotalSymbols\",\"statTotalSymLinks\"];\n");
		header.append("        var html = ZODB.dom.getTableHTMLForDbDataObjectSet(dataObjectSet);\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"stateHistoryTable\",html);\n");
		header.append("        ZODB.dom.showElementById(\"stateHistoryImage\");\n");
		header.append("        ZODB.dom.showElementById(\"stateHistory\");\n");
		header.append("    }\n");
		header.append("    setTimeout(ZACS.monitor.getStateHistory,(ZACS.control.addHistorySeconds * 1000));\n");
		header.append("};\n");		

		header.append("ZACS.monitor.getAssignment = function() {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZACSModel.ASSIGNMENT_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.properties = [\"name\",\"thinkFast\",\"thinkWidth\",\"contextDynamic\",\"correctInput\",\"correctLineEnd\",\"correctInputOnly\",\"maxSymbols\",\"context\",\"input\",\"inputContext\",\"output\",\"outputContext\",\"prevOutput2\",\"prevOutput3\",\"dateTimePrevOutput2\",\"dateTimePrevOutput3\",\"firedSymbolLinks\",\"firedContextLinks\",\"dateTimeFinished\",\"durationMilliseconds\",\"numberOfSymbols\"];\n");
		header.append("    getRequest.orderBy = \"dateTimeFinished\";\n");
		header.append("    getRequest.orderAscending = false;\n");
		header.append("    getRequest.limit = 1;\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZACS.monitor.getAssignmentCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZACS.monitor.getAssignmentCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    if (dataObjectSet.objectArray.length>0) {\n");
		header.append("        var assignmentObj = dataObjectSet.objectArray[0];\n");
		header.append("        var html = \"\";\n");
		header.append("        html += \"<table>\";\n");
		header.append("        html += \"<tr>\";\n");
		header.append("        html += \"<td width='1%'>Name&nbsp;</td>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += assignmentObj.propertyValues[\"name\"];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"</tr>\";\n");
		header.append("        html += \"<tr>\";\n");
		header.append("        html += \"<td width='1%'>Parameters&nbsp;</td>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += \"Maximum symbols: \" + assignmentObj.propertyValues[\"maxSymbols\"];\n");
		header.append("        html += \", width: \" + assignmentObj.propertyValues[\"thinkWidth\"];\n");
		header.append("        if (assignmentObj.propertyValues[\"thinkFast\"]==\"true\") {\n");
		header.append("            html += \", think fast\";\n");
		header.append("        } else {\n");
		header.append("            html += \", think deep\";\n");
		header.append("        }\n");
		header.append("        if (assignmentObj.propertyValues[\"contextDynamic\"]==\"true\") {\n");
		header.append("            html += \", context dynamic\";\n");
		header.append("        }\n");
		header.append("        if (assignmentObj.propertyValues[\"correctInput\"]==\"true\") {\n");
		header.append("            html += \", correct input\";\n");
		header.append("        }\n");
		header.append("        if (assignmentObj.propertyValues[\"correctLineEnd\"]==\"true\") {\n");
		header.append("            html += \", correct line end\";\n");
		header.append("        }\n");
		header.append("        if (assignmentObj.propertyValues[\"correctInputOnly\"]==\"true\") {\n");
		header.append("            html += \", correct input only\";\n");
		header.append("        }\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"</tr>\";\n");
		header.append("        html += \"<tr>\";\n");
		header.append("        html += \"<td width='1%'>Context&nbsp;</td>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += assignmentObj.propertyValues[\"context\"];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"</tr>\";\n");
		header.append("        html += \"<tr>\";\n");
		header.append("        html += \"<td width='1%'>Input&nbsp;</td>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += assignmentObj.propertyValues[\"input\"];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"</tr>\";\n");
		header.append("        html += \"<tr>\";\n");
		header.append("        html += \"<td width='1%'>Results&nbsp;</td>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += \"Fired symbol links: \" + assignmentObj.propertyValues[\"firedSymbolLinks\"];\n");
		header.append("        html += \", fired context links: \" + assignmentObj.propertyValues[\"firedContextLinks\"];\n");
		header.append("        html += \", time: \" + assignmentObj.propertyValues[\"durationMilliseconds\"];\n");
		header.append("        html += \" ms, symbols: \" + assignmentObj.propertyValues[\"numberOfSymbols\"];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"</tr>\";\n");
		header.append("        html += \"<tr>\";\n");
		header.append("        html += \"<td width='1%'>Input&nbsp;context&nbsp;</td>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += assignmentObj.propertyValues[\"inputContext\"];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"</tr>\";\n");
		header.append("        html += \"<tr>\";\n");
		header.append("        html += \"<td width='1%'>Output&nbsp;context&nbsp;</td>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += assignmentObj.propertyValues[\"outputContext\"];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"</tr>\";\n");
		header.append("        html += \"<tr><td width='1%'>Output&nbsp;</td><td>\";\n");
		header.append("        var value = new Date(parseInt(assignmentObj.propertyValues[\"dateTimeFinished\"],10));\n");
		header.append("        html += \"<b>\" + ZODB.date.toString(value) + \"</b>\";\n");
		header.append("        html += \"<br/>\";\n");
		header.append("        html += assignmentObj.propertyValues[\"output\"];\n");
		header.append("        html += \"</td></tr>\";\n");
		header.append("        if (assignmentObj.propertyValues[\"prevOutput2\"]!=\"\") {\n");
		header.append("            html += \"<tr><td width='1%'>&nbsp;</td><td>\";\n");
		header.append("            value = new Date(parseInt(assignmentObj.propertyValues[\"dateTimePrevOutput2\"],10));\n");
		header.append("            html += \"<b>\" + ZODB.date.toString(value) + \"</b>\";\n");
		header.append("            html += \"<br/>\";\n");
		header.append("            html += assignmentObj.propertyValues[\"prevOutput2\"];\n");
		header.append("            html += \"</td></tr>\";\n");
		header.append("        }\n");
		header.append("        if (assignmentObj.propertyValues[\"prevOutput3\"]!=\"\") {\n");
		header.append("            html += \"<tr><td width='1%'>&nbsp;</td><td>\";\n");
		header.append("            value = new Date(parseInt(assignmentObj.propertyValues[\"dateTimePrevOutput3\"],10));\n");
		header.append("            html += \"<b>\" + ZODB.date.toString(value) + \"</b>\";\n");
		header.append("            html += \"<br/>\";\n");
		header.append("            html += assignmentObj.propertyValues[\"prevOutput3\"];\n");
		header.append("            html += \"</td></tr>\";\n");
		header.append("        }\n");
		header.append("        html += \"</table>\";\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"assignmentDetails\",html);\n");
		header.append("        ZODB.dom.showElementById(\"assignmentDetails\");\n");
		header.append("    } else {\n");
		header.append("        ZODB.dom.hideElementById(\"assignmentDetails\");\n");
		header.append("    }\n");
		header.append("    setTimeout(ZACS.monitor.getStateHistory,(ZACS.control.addHistorySeconds * 1000));\n");
		header.append("};\n");		
		
		header.append("ZACS.monitor.refreshImage = function() {\n");
		header.append("    var prop = ZODB.dom.getSelectValueByElementId(\"selectProperty\");\n");
		header.append("    var per = ZODB.dom.getSelectValueByElementId(\"selectPeriod\");\n");
		header.append("    var imageSrc = \"/images/\" + prop + \"-\" + per + \".png\";\n");
		header.append("    imageSrc += \"?\" + (new Date()).getTime();\n");
		header.append("    var html = \"\";\n");
		header.append("    html += \"<img src='\" + imageSrc + \"' height='" + ZACSModel.STATE_HISTORY_IMAGE_HEIGHT + "' width='" + ZACSModel.STATE_HISTORY_IMAGE_WIDTH + "'/>\";\n");
		header.append("    html = html.replace(/'/g,'\"');\n");
		header.append("    html = html.replace(/@/g,\"'\");\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"stateHistoryImage\",html);\n");
		header.append("};\n");		

		header.append("ZACS.monitor.autoRefreshImage = function() {\n");
		header.append("    ZACS.monitor.refreshImage();\n");
		header.append("    setTimeout(ZACS.monitor.autoRefreshImage,(ZACS.control.addHistorySeconds * 1000));\n");
		header.append("};\n");		

		header.append("ZACS.monitor.toggleStateHistoryVisibility = function(source,elemId) {\n");
		header.append("    if (source && source.value) {\n");
		header.append("        if (source.value==\"-\") {\n");
		header.append("            ZODB.dom.hideElementById(elemId);\n");
		header.append("            source.value=\"+\"\n");
		header.append("        } else {\n");
		header.append("            ZODB.dom.showElementById(elemId);\n");
		header.append("            source.value=\"-\"\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZACS.monitor.getControlOverrideCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZACS.control.getControlCallback(xhttp);\n");
		header.append("    var refresh = ZODB.dom.getInputCheckedByElementId(\"assignmentAutoRefresh\");\n");
		header.append("    if (refresh) {\n");
		header.append("        ZACS.monitor.getAssignment();\n");
		header.append("    }\n");
		header.append("    return dataObjectSet;\n");
		header.append("};\n");
		
		header.append("ZACS.monitor.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZACS.monitor.autoRefreshImage();\n");
		header.append("    ZACS.control.statusDivId=\"status\";\n");
		header.append("    ZACS.control.getControl(ZACS.monitor.getControlOverrideCallback);\n");
		header.append("    ZACS.monitor.getStateHistory();\n");
		header.append("    ZACS.monitor.getAssignment();\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZACS.monitor.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZACSMenuHtmlForPage("monitor"));
		body.append("<div>");
		body.append("\n");

		body.append("<div id=\"status\">Status: </div>\n");

		body.append("<div>\n");
		body.append("<select id=\"selectProperty\" onchange=\"ZACS.monitor.refreshImage();\">\n");
		MdlClass controlCls = DbConfig.getInstance().getModel().getClassByFullName(ZACSModel.STATE_HISTORY_CLASS_FULL_NAME);
		for (MdlProperty prop: controlCls.getPropertiesExtended()) {
			if (prop.getName().startsWith("stat") && !prop.getName().equals("status")) {
				String label = prop.getName();
				label = label.replace("stat","");
				label = label.replace("Avg","Average ");
				label = label.replace("Total","Total ");
				label = label.replace("Link","Link ");
				label = label.replace("Link s","Links");
				label = label.replace("Symbol","Symbol ");
				label = label.replace("Symbol s","Symbols");
				label = label.replace("SymLink","Symbol Link");
				label = label.replace("ConLink","Context Link");
				if (prop.getName().equals("statAvgSymLinkCount")) {
					body.append("<option value=\"" + prop.getName() + "\" SELECTED>");
				} else {
					body.append("<option value=\"" + prop.getName() + "\">");
				}
				body.append(label);
				body.append("</option>\n");
			}
		}
		body.append("</select>\n");
		body.append(" <select id=\"selectPeriod\" onchange=\"ZACS.monitor.refreshImage();\">\n");
		body.append("<option value=\"tenMinutes\" SELECTED>Past ten minutes</option>\n");
		body.append("<option value=\"oneHour\">Past hour</option>\n");
		body.append("<option value=\"sixHours\">Past six hours</option>\n");
		body.append("<option value=\"twelveHours\">Past twelve hours</option>\n");
		body.append("<option value=\"oneDay\">Today</option>\n");
		body.append("</select>\n");

		body.append("<div id=\"stateHistoryImage\" style=\"visibility: hidden;\">\n");
		
		body.append("</div>");
		body.append("\n");
		
		body.append("<div id=\"stateHistory\" style=\"visibility: hidden;\">\n");
		body.append("<div><br/><input type=\"button\" value=\"+\" onclick=\"ZACS.monitor.toggleStateHistoryVisibility(this,'stateHistoryTable');\"/>&nbsp;<b>Recent state history</b></div>\n");
		body.append("<div id=\"stateHistoryTable\"style=\"visibility: hidden; display: none;\">\n");
		body.append("</div>");
		body.append("\n");

		body.append("<div id=\"assignment\">\n");
		body.append("<div><br/><b>Recently finished assignment</b><br/>\n");
		body.append("<input value=\"Refresh\" onclick=\"ZACS.monitor.getAssignment();\" type=\"button\"> \n");
		body.append("<input id=\"assignmentAutoRefresh\" type=\"checkbox\" CHECKED>&nbsp;(auto&nbsp;refresh)\n");
		body.append("</div>");
		body.append("\n");
		body.append("<div id=\"assignmentDetails\" style=\"visibility: hidden;\">\n");
		body.append("</div>");
		body.append("\n");
		
		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getControlHtml() {
		HTMLFile file = new HTMLFile("ZACS - Control");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZACS.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZACS = ZACS || {};\n");
		header.append("ZACS.control = ZACS.control || {};\n");
		header.append("ZACS.control.parameters = null;\n");
		header.append("ZACS.control.crawlers = null;\n");
		header.append("ZACS.control.crawler = null;\n");

		header.append("ZACS.control.getCrawlers = function() {\n");
		header.append("    ZACS.control.crawler = null;\n");
		header.append("    ZACS.control.refreshCrawlerTable();\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZACSModel.CRAWLER_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.properties = [\"id\",\"crawlUrl\",\"scrapedTextLength\",\"crawl\",\"convertTextToExamples\"];\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZACS.control.getCrawlersCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZACS.control.getCrawlersCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    ZACS.control.crawlers = dataObjectSet;\n");
		header.append("    ZACS.control.refreshCrawlerTable();\n");
		header.append("    ZODB.dom.showElementById(\"crawlers\");\n");
		header.append("    return dataObjectSet;\n");
		header.append("};\n");

		header.append("ZACS.control.refreshCrawlerTable = function() {\n");
		header.append("    if (ZACS.control.crawlers!=null && ZACS.control.crawlers.objectArray.length>0) {\n");
		header.append("        ZACS.control.crawlers.propertyNameArray = [\"id\",\"crawlUrl\",\"scrapedTextLength\",\"crawl\",\"convertTextToExamples\"];\n");
		header.append("        var html = ZODB.dom.getTableHTMLForDbDataObjectSet(ZACS.control.crawlers,\"crawlerIds\",\"ZACS.control.selectedCrawler(this.value);\");\n");
		header.append("        ZODB.dom.setDivInnerHTMLByElementId(\"crawlerTable\",html);\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZACS.control.selectedCrawler = function(id) {\n");
		header.append("    if (id!=null && id>0) {\n");
		header.append("        ZACS.control.crawler = ZACS.control.crawlers.objects[id];\n");
		header.append("    } else {\n");
		header.append("        ZACS.control.crawler = null;\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZACS.control.startSelectedCrawler = function() {\n");
		header.append("    var crawlAndConvert = ZODB.dom.getInputCheckedByElementId(\"crawlAndConvert\");\n");
		header.append("    ZACS.control.updateSelectedCrawler(\"crawl\",crawlAndConvert)\n");
		header.append("};\n");

		header.append("ZACS.control.convertSelectedCrawler = function() {\n");
		header.append("    ZACS.control.updateSelectedCrawler(\"convertTextToExamples\",false)\n");
		header.append("};\n");

		header.append("ZACS.control.updateSelectedCrawler = function(property,crawlAndConvert) {\n");
		header.append("    if (ZACS.control.crawler!=null) {\n");
		header.append("        if (ZACS.control.crawler.propertyValues[property]==\"true\" ||\n");
		header.append("            (crawlAndConvert && ZACS.control.crawler.propertyValues[\"convertTextToExamples\"]==\"true\")) {\n");
		header.append("            alert(\"Crawler is already working\");\n");
		header.append("            ZACS.control.crawler = null;\n");
		header.append("            ZACS.control.refreshCrawlerTable();\n");
		header.append("            ZACS.control.getCrawlers();\n");
		header.append("        } else {\n");
		header.append("            var updateRequest = new ZODB.data.UpdateRequest(\"" + ZACSModel.CRAWLER_CLASS_FULL_NAME + "\");\n");
		header.append("            updateRequest.getRequest.filters[\"id\"] = ZACS.control.crawler.getId();\n");
		header.append("            ZACS.control.crawler.propertyValues[property]=\"true\";\n");
		header.append("            if (crawlAndConvert) {\n");
		header.append("                ZACS.control.crawler.propertyValues[\"convertTextToExamples\"]=\"true\";\n");
		header.append("            }\n");
		header.append("            updateRequest.dataObjectSet.objects[ZACS.control.crawler.getId()] = ZACS.control.crawler;\n");
		header.append("            ZODB.data.executePostRequest(updateRequest,ZODB.gui.genericResponseCallback);\n");
		header.append("            ZACS.control.crawler = null;\n");
		header.append("            ZACS.control.refreshCrawlerTable();\n");
		header.append("            setTimeout(ZACS.control.getCrawlers,100);\n");
		header.append("        }\n");
		header.append("    } else {\n");
		header.append("        alert(\"No crawler selected\");\n");
		header.append("        ZACS.control.getCrawlers();\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZACS.control.getParameters = function() {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZACSModel.CONTROL_CLASS_FULL_NAME + "\");\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZACS.control.getParametersCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZACS.control.getParametersCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    var status = \"\";\n");
		header.append("    for (var id in dataObjectSet.objects) {\n");
		header.append("        ZACS.control.parameters = dataObjectSet.objects[id];\n");
		header.append("        status = ZACS.control.parameters.propertyValues[\"status\"];\n");
		header.append("        updateControlSeconds = parseInt(ZACS.control.parameters.propertyValues[\"updateControlSeconds\"],10);\n");
		header.append("        for (var name in ZACS.control.parameters.propertyValues) {\n");
		header.append("            if (name==\"autoActivate\" || name==\"autoReactivate\" || name==\"learnExamples\" || name==\"doAssignments\" || name==\"skipCountMaxStructSyms\" || name==\"saveModuleSymbolState\" || name==\"contextAssociate\") {\n");
		header.append("                ZODB.dom.selectRadioByElementName(name,ZACS.control.parameters.propertyValues[name]);\n");
		header.append("            } else {\n");
		header.append("                ZODB.dom.setInputValueByElementId(name,ZACS.control.parameters.propertyValues[name]);\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("        break;\n");
		header.append("    };\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"status\",\"Status: <b>\" + status + \"</b>\");\n");
		header.append("    ZODB.dom.showElementById(\"parameters\");\n");
		header.append("    ZODB.dom.setInputDisabledByElementId(\"saveParameters\",false);\n");
		header.append("};\n");
		
		header.append("ZACS.control.getCommandsCallback = function(xhttp) {\n");
		header.append("    ZACS.command.getCommandsCallback(xhttp);\n");
		header.append("    ZODB.dom.addSelectOptionByElementId(\"selectCommand\",0,\"\");\n");
		header.append("    for (var id in ZACS.command.commands) {\n");
		header.append("        var code = ZACS.command.commands[id].propertyValues[\"code\"];\n");
		header.append("        ZODB.dom.addSelectOptionByElementId(\"selectCommand\",id,code);\n");
		header.append("    };\n");
		header.append("};\n");

		header.append("ZACS.control.setCommand = function(commandId) {\n");
		header.append("    var updateRequest = new ZODB.data.UpdateRequest(\"" + ZACSModel.CONTROL_CLASS_FULL_NAME + "\");\n");
		header.append("    var requestObject = new ZODB.data.DbDataObject();\n");
		header.append("    requestObject.setId(ZACS.control.controlId);\n");
		header.append("    requestObject.linkValues[\"command\"] = [commandId];\n");
		header.append("    updateRequest.dataObjectSet.objects[ZACS.control.controlId] = requestObject;\n");
		header.append("    updateRequest.getRequest.filters[\"id\"] = ZACS.control.controlId;\n");
		header.append("    ZODB.data.executePostRequest(updateRequest,ZACS.control.setCommandCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZACS.control.setCommandCallback = function(xhttp) {\n");
		header.append("    ZODB.gui.genericResponseCallback(xhttp);\n");
		header.append("    var errors = ZODB.data.getErrorsCallback(xhttp);\n");
		header.append("    if (errors.length>0) {\n");
		header.append("        alert(errors[0].message);\n");
		header.append("    }\n");
		header.append("    ZODB.dom.setInputDisabledByElementId(\"executeCommand\",false);\n");
		header.append("};\n");

		header.append("ZACS.control.executeCommand = function() {\n");
		header.append("    ZODB.dom.setInputDisabledByElementId(\"executeCommand\",true);\n");
		header.append("    var value = ZODB.dom.getSelectValueByElementId(\"selectCommand\");\n");
		header.append("    if (value && value>0) {\n");
		header.append("        ZACS.control.setCommand(value);\n");
		header.append("        ZODB.dom.selectOptionByElementIdByOptionValues(\"selectCommand\",[0]);\n");
		header.append("    } else {\n");
		header.append("        alert(\"No command selected\");\n");
		header.append("        ZODB.dom.setInputDisabledByElementId(\"executeCommand\",false);\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZACS.control.saveParameters = function() {\n");
		header.append("    ZODB.dom.setInputDisabledByElementId(\"saveParameters\",true);\n");
		header.append("    var updateRequest = new ZODB.data.UpdateRequest(\"" + ZACSModel.CONTROL_CLASS_FULL_NAME + "\");\n");
		header.append("    var requestObject = new ZODB.data.DbDataObject();\n");
		header.append("    requestObject.setId(ZACS.control.controlId);\n");
		header.append("    for (var name in ZACS.control.parameters.propertyValues) {\n");
		header.append("        if (name!=\"status\" && name!=\"command\") {\n");
		header.append("            var value = null;\n");
		header.append("            if (name==\"autoActivate\" || name==\"autoReactivate\" || name==\"learnExamples\" || name==\"doAssignments\" || name==\"skipCountMaxStructSyms\" || name==\"saveModuleSymbolState\" || name==\"contextAssociate\") {\n");
		header.append("                value = ZODB.dom.getSelectedRadioByElementName(name);\n");
		header.append("            } else {\n");
		header.append("                value = ZODB.dom.getNumberInputValueByElementId(name);\n");
		header.append("            }\n");
		header.append("            if (value!=null) {\n");
		header.append("                requestObject.propertyValues[name] = value;\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    updateRequest.dataObjectSet.objects[ZACS.control.controlId] = requestObject;\n");
		header.append("    updateRequest.getRequest.filters[\"id\"] = ZACS.control.controlId;\n");
		header.append("    ZODB.data.executePostRequest(updateRequest,ZACS.control.saveParametersCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZACS.control.saveParametersCallback = function(xhttp) {\n");
		header.append("    ZODB.gui.genericResponseCallback(xhttp);\n");
		header.append("    var errors = ZODB.data.getErrorsCallback(xhttp);\n");
		header.append("    if (errors.length>0) {\n");
		header.append("        alert(errors[0].message);\n");
		header.append("    }\n");
		header.append("    ZACS.control.getParameters();\n");
		header.append("};\n");

		header.append("ZACS.control.getControlOverrideCallback = function(xhttp) {\n");
		header.append("    var dataObjectSet = ZACS.control.getControlCallback(xhttp);\n");
		header.append("    if (ZACS.control.crawler==null) {\n");
		header.append("        ZACS.control.getCrawlers();\n");
		header.append("    }\n");
		header.append("    return dataObjectSet;\n");
		header.append("};\n");
		
		header.append("ZACS.control.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZACS.control.statusDivId=\"status\";\n");
		header.append("    ZACS.control.getControl(ZACS.control.getControlOverrideCallback);\n");
		header.append("    ZACS.control.getCrawlers();\n");
		header.append("    ZACS.control.getParameters();\n");
		header.append("    ZACS.command.getCommands(ZACS.control.getCommandsCallback);\n");	
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());
		
		file.setOnload("ZODB.model.getModel(ZACS.control.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZACSMenuHtmlForPage("control"));
		body.append("<div>");
		body.append("\n");
		
		body.append("<div id=\"status\">Status: </div>\n");
		
		body.append("<div id=\"command\">Command: ");
		body.append("<select id=\"selectCommand\"></select>");
		body.append("&nbsp;<input id=\"executeCommand\" type=\"button\" value=\"Execute\"/ onclick=\"ZACS.control.executeCommand();\">");
		body.append("</div>");
		body.append("\n");

		body.append("<div id=\"parameters\" style=\"visibility: hidden;\">\n");
		body.append("<div><br/><b>Parameters</b></div>\n");
		body.append("<table>");
		body.append("\n");
		MdlClass controlCls = DbConfig.getInstance().getModel().getClassByFullName(ZACSModel.CONTROL_CLASS_FULL_NAME);
		for (MdlProperty prop: controlCls.getPropertiesExtended()) {
			if (
				!prop.getName().equals(MdlProperty.ID) &&
				!prop.getName().equals("command") &&
				!prop.getName().equals("status")
				) {
				body.append("<tr>");
				body.append("<td width=\"1%\">");
				body.append(prop.getName());
				body.append("</td>");
				body.append("<td>");
				if (prop.getName().equals("autoActivate") ||
					prop.getName().equals("autoReactivate") ||
					prop.getName().equals("learnExamples") ||
					prop.getName().equals("doAssignments") ||
					prop.getName().equals("saveModuleSymbolState") ||
					prop.getName().equals("contextAssociate") ||
					prop.getName().equals("skipCountMaxStructSyms")
					) {
					body.append("<input name=\"" + prop.getName() + "\" type=\"radio\" value=\"true\">&nbsp;True ");
					body.append("<input name=\"" + prop.getName() + "\" type=\"radio\" value=\"false\">&nbsp;False ");
				} else {
					body.append("<input id=\"" + prop.getName() + "\" type=\"number\">");
				}
				body.append("</td>");
				body.append("</tr>");
				body.append("\n");
			}
		}
		body.append("</table>");
		body.append("\n");
		body.append("<input id=\"saveParameters\" type=\"button\" value=\"Save\" onclick=\"ZACS.control.saveParameters();\">");
		body.append("</div>");
		body.append("\n");

		body.append("<div id=\"crawlers\" style=\"visibility: hidden;\">\n");
		body.append("<div>\n");
		body.append("<br/>");
		body.append("<input value=\"Refresh\" onclick=\"ZACS.control.getCrawlers();\" type=\"button\"> ");
		body.append("<a href=\"/crawlerManager.html\"><b>Crawlers</b></a>\n");
		body.append("</div>");
		body.append("\n");
		body.append("<div id=\"crawlerTable\">\n");
		body.append("</div>");
		body.append("\n");
		body.append("<input value=\"Crawl\" onclick=\"ZACS.control.startSelectedCrawler();\" type=\"button\"> \n");
		body.append("<input id=\"crawlAndConvert\" type=\"checkbox\" CHECKED>&nbsp;(and) \n");
		body.append("<input value=\"Convert\" onclick=\"ZACS.control.convertSelectedCrawler();\" type=\"button\"> \n");
		body.append("</div>");
		body.append("\n");

		body.append("</div>");
		body.append("\n");
		file.getBodyElements().add(body.toString());
		
		return file;
	}
}
