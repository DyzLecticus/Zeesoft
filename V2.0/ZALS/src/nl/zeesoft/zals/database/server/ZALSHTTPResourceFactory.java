package nl.zeesoft.zals.database.server;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.server.ZACHTTPResourceFactory;
import nl.zeesoft.zals.database.model.Animal;
import nl.zeesoft.zals.database.model.Environment;
import nl.zeesoft.zals.database.model.ZALSModel;
import nl.zeesoft.zodb.file.HTMLFile;

public class ZALSHTTPResourceFactory extends ZACHTTPResourceFactory {

	@Override
	protected void initializeHTTPResources() {
		super.initializeHTTPResources();
		addFile("ZALS.css",getZALSCSS());
		addFile("ZALS.js",getEnvironmentJavaScript());
		addFile("environment.html",getEnvironmentHtml().toStringBuilder());
		addFile("animals.html",getAnimalsHtml().toStringBuilder());
		addFile("brains.html",getBrainsHtml().toStringBuilder());
		addFile("zac.html",getZACHtml().toStringBuilder());
	}

	@Override
	protected String getIndexMenuTitle() {
		return "ZALS";
	}

	protected StringBuilder getZALSMenuHtmlForPage(String page) {
		List<String> pages = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		
		pages.add("index");
		pages.add("environment");
		pages.add("animals");
		pages.add("brains");
		pages.add("zac");
		pages.add("home");

		titles.add("Home");
		titles.add("Environment");
		titles.add("Animals");
		titles.add("Brains");
		titles.add("ZAC");
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
		return "ZALS - Authorizer";
	}
	
	@Override
	protected StringBuilder getAuthorizerMenuHtml() {
		return getZALSMenuHtmlForPage("");
	}
	
	@Override
	protected HTMLFile get404Html() {
		HTMLFile file = new HTMLFile("ZALS - 404 Not Found");
		file.setBodyBgColor(BACKGROUND_COLOR);
		
		StringBuilder body = new StringBuilder();
		body.append(getZALSMenuHtmlForPage(""));
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
		HTMLFile file = new HTMLFile("ZALS - Home");
		file.setBodyBgColor(BACKGROUND_COLOR);

		StringBuilder body = new StringBuilder();
		body.append(getZALSMenuHtmlForPage("index"));
		body.append("<div>");
		body.append("The <b>Zeesoft Artificial Life Simulator</b> is a program that implements <a href=\"/zac.html\">Zeesoft Artificial Cognition</a> in simulated life forms. ");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getZACHtml() {
		HTMLFile file = new HTMLFile("ZALS - ZAC");
		file.setBodyBgColor(BACKGROUND_COLOR);
		HTMLFile zac = super.getIndexHtml();
		file.getBodyElements().add(getZALSMenuHtmlForPage("zac").toString());
		file.getBodyElements().add(zac.getBodyElements().get(1));
		return file;
	}

	protected StringBuilder getEnvironmentJavaScript() {
		StringBuilder script = new StringBuilder();
		
		script.append("var ZALS = ZALS || {};\n");
		script.append("ZALS.environment = ZALS.environment || {};\n");
		script.append("ZALS.environment = {};\n");
		script.append("ZALS.environment.displayHistoryFunction = null;\n");
		script.append("ZALS.environment.environment = null;\n");
		script.append("ZALS.environment.stateHistory = [];\n");
		script.append("ZALS.environment.stateHistoryMax = 200;\n");
		script.append("ZALS.environment.displayIndex = 0;\n");
		script.append("ZALS.environment.displayTimeout = 100;\n");
		script.append("ZALS.environment.displayTimeoutCurrent = 100;\n");
		script.append("ZALS.environment.displayedGridElems = {};\n");

		script.append("ZALS.environment.displayTimes = 0;\n");
		script.append("ZALS.environment.displayTimeTotal = 0;\n");
		script.append("ZALS.environment.displayTimeAvg = 0;\n");

		script.append("ZALS.environment.getEnvironmentCallBack = function(xhttp) {\n");
		script.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		script.append("    if (dataObjectSet.objectArray.length>0) {\n");
		script.append("        ZALS.environment.environment = dataObjectSet.objectArray[0];\n");
		script.append("        ZALS.environment.displayTimeout = parseInt(ZALS.environment.environment.propertyValues[\"statesPerSecond\"],10);\n");
		script.append("        ZALS.environment.displayTimeout = ((1000 / ZALS.environment.displayTimeout) - 1);\n");
		script.append("        ZALS.environment.resetDisplayTimeOutCurrent();\n");
		script.append("        ZALS.environment.stateHistoryMax = parseInt(ZALS.environment.environment.propertyValues[\"statesPerSecond\"],10) * parseInt(ZALS.environment.environment.propertyValues[\"keepStateHistorySeconds\"],10) * 2;\n");
		script.append("    }\n");
		script.append("    ZALS.environment.getHistory();\n");
		script.append("};\n");

		script.append("ZALS.environment.resetDisplayTimeOutCurrent = function() {\n");
		script.append("    ZALS.environment.displayTimeoutCurrent = ZALS.environment.displayTimeout;\n");
		script.append("};\n");

		script.append("ZALS.environment.getEnvironment = function() {\n");
		script.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZALSModel.ENVIRONMENT_CLASS_FULL_NAME + "\");\n");
		script.append("    getRequest.properties[0] = \"*\";\n");
		script.append("    ZODB.data.executeGetRequest(getRequest,ZALS.environment.getEnvironmentCallBack,ZODB.gui.genericResponseCallback);\n");	
		script.append("};\n");

		script.append("ZALS.environment.getHistoryCallBack = function(xhttp) {\n");
		script.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		script.append("    if (dataObjectSet.objectArray.length>4) {\n");
		script.append("        ZALS.environment.stateHistory = dataObjectSet.objectArray;\n");
		script.append("        if (ZALS.environment.displayIndex==0) {\n");
		script.append("            ZALS.environment.displayIndex = 1;\n");
		script.append("            ZALS.environment.refresh();\n");
		script.append("            ZALS.environment.refreshHistory();\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");

		script.append("ZALS.environment.getHistory = function() {\n");
		script.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZALSModel.STATE_HISTORY_CLASS_FULL_NAME + "\");\n");
		script.append("    getRequest.filterOperators[\"environment\"] = ZALS.environment.environment.getId();\n");
		script.append("    ZODB.data.executeGetRequest(getRequest,ZALS.environment.getHistoryCallBack,ZODB.gui.genericResponseCallback);\n");	
		script.append("};\n");

		script.append("ZALS.environment.updateHistoryCallBack = function(xhttp) {\n");
		script.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		script.append("    var newArray = [];\n");
		script.append("    if (dataObjectSet.objectArray.length>0) {\n");
		script.append("        for (var obj = 0; obj < dataObjectSet.objectArray.length; obj++) {\n");
		script.append("            ZALS.environment.stateHistory[ZALS.environment.stateHistory.length] = dataObjectSet.objectArray[obj];\n");
		script.append("        }\n");
		script.append("        for (var obj = (ZALS.environment.displayIndex - 1); obj < ZALS.environment.stateHistory.length; obj++) {\n");
		script.append("            newArray[newArray.length] = ZALS.environment.stateHistory[obj];\n");
		script.append("        }\n");
		script.append("        ZALS.environment.stateHistory = newArray;\n");
		script.append("        ZALS.environment.displayIndex = 1;\n");
		script.append("        if (newArray.length > ZALS.environment.stateHistoryMax) {\n");
		script.append("            ZALS.environment.displayTimeoutCurrent = Math.round(ZALS.environment.displayTimeoutCurrent / 2);\n");
		script.append("            if (ZALS.environment.displayTimeoutCurrent<=1) {\n");
		script.append("                ZALS.environment.displayTimeoutCurrent=0;\n");
		script.append("                ZALS.environment.displayIndex = newArray.length - (ZALS.environment.stateHistoryMax / 2);\n");
		script.append("            }\n");
		script.append("        } else if (newArray.length < (ZALS.environment.stateHistoryMax / 2)) {\n");
		script.append("            ZALS.environment.resetDisplayTimeOutCurrent();\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		
		script.append("ZALS.environment.updateHistory = function() {\n");
		script.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZALSModel.STATE_HISTORY_CLASS_FULL_NAME + "\");\n");
		script.append("    getRequest.filterOperators[\"environment\"] = ZALS.environment.environment.getId();\n");
		script.append("    getRequest.filters[\"dateTime\"] = ZALS.environment.stateHistory[(ZALS.environment.stateHistory.length - 1)].propertyValues[\"dateTime\"];\n");
		script.append("    getRequest.filterOperators[\"dateTime\"] = \"greaterOrEquals\";\n");
		script.append("    getRequest.properties[0] = \"*\";\n");
		script.append("    ZODB.data.executePostRequest(getRequest,ZALS.environment.updateHistoryCallBack,ZODB.gui.genericResponseCallback);\n");	
		script.append("};\n");
		
		script.append("ZALS.environment.displayNextHistory = function() {\n");
		script.append("    if (ZALS.environment.displayIndex>0 && ZALS.environment.displayIndex<ZALS.environment.stateHistory.length) {\n");
		script.append("        var time = new Date();\n");
		script.append("        var hist = ZALS.environment.stateHistory[ZALS.environment.displayIndex];\n");
		script.append("        ZALS.environment.displayHistoryFunction(hist);\n");
		script.append("        ZALS.environment.displayTimes++;\n");
		script.append("        ZALS.environment.displayTimeTotal+= (new Date).getTime() - time.getTime();\n");
		script.append("        ZALS.environment.displayTimeAvg = Math.round(ZALS.environment.displayTimeTotal / ZALS.environment.displayTimes);\n");
		script.append("        ZALS.environment.displayIndex++;\n");
		script.append("    }\n");
		script.append("};\n");
		
		script.append("ZALS.environment.displayHistory = function(hist) {\n");
		script.append("    var displayedGridElems = ZALS.environment.displayedGridElems;\n");
		script.append("    ZALS.environment.displayedGridElems = {};\n");
		script.append("    var objs = hist.propertyValues[\"objectData\"].split(\"\\n\");\n");
		script.append("    var maxEnergy = parseInt(ZALS.environment.environment.propertyValues[\"maxEnergyPlant\"],10);\n");
		script.append("    for (var obj = 0; obj < objs.length; obj++) {\n");
		script.append("        var props = objs[obj].split(\";\");\n");
		script.append("        var posX = parseInt(props[2],10);\n");
		script.append("        var posY = parseInt(props[3],10);\n");
		script.append("        var tdId = posX + \"-\" + posY;\n");
		script.append("        var energy = parseInt(props[4],10);\n");
		script.append("        if (energy>0) {\n");
		script.append("            var color = props[0];\n");
		script.append("        } else {\n");
		script.append("            var color = \"" + Environment.COLOR_DEAD_OBJECT + "\";\n");
		script.append("        }\n");
		script.append("        if (color==\"green\") {\n");
		script.append("            if (energy==maxEnergy) {\n");
		script.append("                color=\"yellow\";\n");
		script.append("            } else if (energy>=(maxEnergy/2)) {\n");
		script.append("                color=\"lime\";\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        ZALS.environment.displayedGridElems[tdId] = {};\n");
		script.append("        ZALS.environment.displayedGridElems[tdId].color = color;\n");
		script.append("        ZALS.environment.displayedGridElems[tdId].box = ZALS.environment.displayedGridElems[tdId].color + \"Box\";\n");
		script.append("    }\n");
		script.append("    for (var obj = 0; obj < objs.length; obj++) {\n");
		script.append("        var props = objs[obj].split(\";\");\n");
		script.append("        if (props[0]!=\"green\" && parseInt(props[4],10)>0) {\n");
		script.append("            var posX = parseInt(props[2],10);\n");
		script.append("            var posY = parseInt(props[3],10);\n");
		script.append("            var rot = parseInt(props[5],10);\n");
		script.append("            tdId = \"\";\n");
		script.append("            if (rot==0 && posY>0) {\n");
		script.append("                tdId = posX + \"-\" + (posY - 1);\n");
		script.append("            }\n");
		script.append("            if (rot==90 && posX<(" + Environment.SIZE_X + " - 1)) {\n");
		script.append("                tdId = (posX + 1) + \"-\" + posY;\n");
		script.append("            }\n");
		script.append("            if (rot==180 && posY<(" + Environment.SIZE_Y + " - 1)) {\n");
		script.append("                tdId = posX + \"-\" + (posY + 1);\n");
		script.append("            }\n");
		script.append("            if (rot==270 && posX>0) {\n");
		script.append("                tdId = (posX - 1) + \"-\" + posY;\n");
		script.append("            }\n");
		script.append("            if (tdId!=\"\") {\n");
		script.append("                if (!ZALS.environment.displayedGridElems[tdId]) {\n");
		script.append("                    ZALS.environment.displayedGridElems[tdId] = {};\n");
		script.append("                    ZALS.environment.displayedGridElems[tdId].color = \"black\";\n");
		script.append("                }\n");
		script.append("                ZALS.environment.displayedGridElems[tdId].box = \"greyBox\";\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    for (var obj = 0; obj < objs.length; obj++) {\n");
		script.append("        var props = objs[obj].split(\";\");\n");
		script.append("        if (props[0]!=\"green\" && parseInt(props[4],10)>0 && !props[10]==\"\") {\n");
		script.append("            var posX = parseInt(props[2],10);\n");
		script.append("            var posY = parseInt(props[3],10);\n");
		script.append("            var rot = parseInt(props[5],10);\n");
		script.append("            var actions = props[10].split(\" \");\n");
		script.append("            for (var a = 0; a < actions.length; a++) {\n");
		script.append("                if (actions[a]==\"" + Animal.ACTION_TURN_LEFT + "\") {\n");
		script.append("                    if (rot==0) {\n");
		script.append("                        rot=270; \n");
		script.append("                    } else {\n");
		script.append("                        rot-=90; \n");
		script.append("                    }\n");
		script.append("                } else if (actions[a]==\"" + Animal.ACTION_TURN_RIGHT + "\") {\n");
		script.append("                    if (rot==270) {\n");
		script.append("                        rot=0; \n");
		script.append("                    } else {\n");
		script.append("                        rot+=90; \n");
		script.append("                    }\n");
		script.append("                } else if (actions[a]==\"" + Animal.ACTION_MOVE_FORWARD + "\") {\n");
		script.append("                    if (rot==0) {\n");
		script.append("                        posY--; \n");
		script.append("                    } else if (rot==90) {\n");
		script.append("                        posX++; \n");
		script.append("                    } else if (rot==180) {\n");
		script.append("                        posY++; \n");
		script.append("                    } else if (rot==270) {\n");
		script.append("                        posX--; \n");
		script.append("                    }\n");
		script.append("                    if (posX>=0 && posX<" + Environment.SIZE_X + " && posY>=0 && posY<" + Environment.SIZE_Y + ") {\n");
		script.append("                        var tdId = posX + \"-\" + posY;\n");
		script.append("                        if (!ZALS.environment.displayedGridElems[tdId]) {\n");
		script.append("                            ZALS.environment.displayedGridElems[tdId] = {};\n");
		script.append("                            ZALS.environment.displayedGridElems[tdId].color = \"black\";\n");
		script.append("                        }\n");
		script.append("                        ZALS.environment.displayedGridElems[tdId].box = \"whiteBox\";\n");
		script.append("                    }\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    for (tdId in ZALS.environment.displayedGridElems) {\n");
		script.append("        var classes = ZALS.environment.displayedGridElems[tdId].color;\n");
		script.append("        if (ZALS.environment.displayedGridElems[tdId].box!=\"\") {\n");
		script.append("            classes += \" \" + ZALS.environment.displayedGridElems[tdId].box;\n");
		script.append("        }\n");
		script.append("        window.document.getElementById(tdId).className = classes;\n");
		script.append("    }\n");
		script.append("    for (tdId in displayedGridElems) {\n");
		script.append("        if (!ZALS.environment.displayedGridElems[tdId]) {\n");
		script.append("           window.document.getElementById(tdId).className = \"black\";\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZALS.environment.displayHistoryFunction = ZALS.environment.displayHistory;\n");

		script.append("ZALS.environment.refresh = function() {\n");
		script.append("    ZALS.environment.displayNextHistory();\n");
		script.append("    var to = ZALS.environment.displayTimeoutCurrent - ZALS.environment.displayTimeAvg;\n");
		script.append("    if (to < 0) {\n");
		script.append("        to = 0;\n");
		script.append("    }\n");
		script.append("    setTimeout(ZALS.environment.refresh,to);\n");
		script.append("};\n");

		script.append("ZALS.environment.refreshHistory = function() {\n");
		script.append("    var to = parseInt(ZALS.environment.environment.propertyValues[\"keepStateHistorySeconds\"],10);\n");
		script.append("    to = Math.round((to * 1000) / 3);\n");
		script.append("    ZALS.environment.updateHistory();\n");
		script.append("    setTimeout(ZALS.environment.refreshHistory,to);\n");
		script.append("};\n");

		return script;
	}

	protected StringBuilder getEnvironmentHtmlGridTable() {
		StringBuilder body = new StringBuilder();
		body.append("<table id=\"grid\">");
		for (int y = 0; y < Environment.SIZE_Y; y++) {
			body.append("<tr>");
			for (int x = 0; x < Environment.SIZE_X; x++) {
				String tdId = x + "-" + y;
				body.append("<td id=\"" + tdId + "\" class=\"black\">");
				body.append("</td>");
			}
			body.append("</tr>");
		}
		body.append("</table>");
		return body;
	}
	
	protected HTMLFile getEnvironmentHtml() {
		HTMLFile file = new HTMLFile("ZALS - Environment");
		file.setBodyBgColor(BACKGROUND_COLOR);

		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZALS.js");
		file.getStyleFiles().add("ZALS.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZALS = ZALS || {};\n");
		header.append("ZALS.environment = ZALS.environment || {};\n");

		header.append("ZALS.environment.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZALS.environment.getEnvironment();\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());

		file.setOnload("ZODB.model.getModel(ZALS.environment.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZALSMenuHtmlForPage("environment"));
		body.append("<div>");
		body.append(getEnvironmentHtmlGridTable());
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getAnimalsHtml() {
		HTMLFile file = new HTMLFile("ZALS - Animals");
		file.setBodyBgColor(BACKGROUND_COLOR);

		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZALS.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZALS = ZALS || {};\n");
		header.append("ZALS.environment = ZALS.environment || {};\n");

		header.append("ZALS.environment.displayHistoryAnimals = function(hist) {\n");
		header.append("    var html = \"\";\n");
		header.append("    html += \"<table>\";\n");
		header.append("    html += \"<tr>\";\n");
		header.append("    html += \"<th width='1%'>\";\n");
		header.append("    html += \"&nbsp;\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += \"Energy\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += \"Score\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += \"Top score\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += \"Generation\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th class='notImportant'>\";\n");
		header.append("    html += \"Trained states\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th class='notImportant'>\";\n");
		header.append("    html += \"Using modules\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th class='notImportant'>\";\n");
		header.append("    html += \"Training modules\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"</tr>\";\n");
		header.append("    var objs = hist.propertyValues[\"objectData\"].split(\"\\n\");\n");
		header.append("    for (var obj = 0; obj < objs.length; obj++) {\n");
		header.append("        var props = objs[obj].split(\";\");\n");
		header.append("        var color = props[0];\n");
		header.append("        var energy = parseInt(props[4],10);\n");
		header.append("        if (color!=\"green\") {\n");
		header.append("            var score = props[6];\n");
		header.append("            var topScore = props[7];\n");
		header.append("            var generation = props[8];\n");
		header.append("            var trainedStates = props[9];\n");
		header.append("            var cMods = props[11];\n");
		header.append("            var tMods = props[12];\n");
		header.append("            html += \"<tr>\";\n");
		header.append("            html += \"<td align='right' class='\" + color + \"'>\";\n");
		header.append("            html += \"&nbsp;\";\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"<td align='right'>\";\n");
		header.append("            html += energy;\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"<td align='right'>\";\n");
		header.append("            html += score;\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"<td align='right'>\";\n");
		header.append("            html += topScore;\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"<td align='right'>\";\n");
		header.append("            html += generation;\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"<td align='right' class='notImportant'>\";\n");
		header.append("            html += trainedStates;\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"<td class='notImportant'>\";\n");
		header.append("            html += cMods;\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"<td class='notImportant'>\";\n");
		header.append("            html += tMods;\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"</tr>\";\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    html += \"</table>\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"animals\",html);\n");
		header.append("};\n");
		
		header.append("ZALS.environment.initialize = function(xhttp) {\n");
		header.append("    ZALS.environment.displayHistoryFunction = ZALS.environment.displayHistoryAnimals;\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZALS.environment.getEnvironment();\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());

		file.setOnload("ZODB.model.getModel(ZALS.environment.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZALSMenuHtmlForPage("animals"));
		body.append("<div id=\"animals\">");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		
		return file;
	}

	protected HTMLFile getBrainsHtml() {
		HTMLFile file = new HTMLFile("ZALS - Brains");
		file.setBodyBgColor(BACKGROUND_COLOR);

		file.getScriptFiles().add("ZODB.js");
		file.getScriptFiles().add("ZALS.js");
		file.getStyleFiles().add("ZODB.css");

		StringBuilder header = new StringBuilder();
		header.append("<script type=\"text/javascript\">\n");

		header.append("var ZALS = ZALS || {};\n");
		header.append("ZALS.brains = ZALS.brains || {};\n");
		header.append("ZALS.brains.modules = [];\n");
		header.append("ZALS.brains.animals = [];\n");

		header.append("ZALS.brains.idA = null;\n");
		header.append("ZALS.brains.idB = null;\n");
		header.append("ZALS.brains.linkType = null;\n");
		header.append("ZALS.brains.linkOrderBy = null;\n");
		header.append("ZALS.brains.comparing = false;\n");

		header.append("ZALS.brains.TYPE_CONTEXT = \"" + ZALSModel.SYMBOL_LINK_CONTEXT_CLASS_FULL_NAME + "\";\n");
		header.append("ZALS.brains.TYPE_SEQUENCE = \"" + ZALSModel.SYMBOL_LINK_SEQUENCE_CLASS_FULL_NAME + "\";\n");

		header.append("ZALS.brains.moduleNames = [];\n");
		header.append("ZALS.brains.animalModules = [];\n");

		header.append("ZALS.brains.getModulesCallback = function(xhttp) {\n");
		header.append("    ZALS.brains.modules = [];\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    if (dataObjectSet.objectArray.length>0) {\n");
		header.append("        ZALS.brains.modules = dataObjectSet.objectArray;\n");
		header.append("    }\n");
		header.append("    ZALS.brains.getAnimals();\n");
		header.append("};\n");

		header.append("ZALS.brains.getModules = function() {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZALSModel.MODULE_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.properties[0] = \"*\";\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZALS.brains.getModulesCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");
		
		header.append("ZALS.brains.getAnimalsCallback = function(xhttp,type) {\n");
		header.append("    var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("    if (dataObjectSet.objectArray.length>0) {\n");
		header.append("        for (var i = 0; i < dataObjectSet.objectArray.length; i++) {\n");
		header.append("            ZALS.brains.animals[ZALS.brains.animals.length] = dataObjectSet.objectArray[i];\n");
		header.append("            ZALS.brains.animals[(ZALS.brains.animals.length - 1)].type = type;\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZALS.brains.getHerbivoresCallback = function(xhttp) {\n");
		header.append("    ZALS.brains.getAnimalsCallback(xhttp,\"H\");\n");
		header.append("    ZALS.brains.getCarnivores();\n");
		header.append("};\n");
		
		header.append("ZALS.brains.getCarnivoresCallback = function(xhttp) {\n");
		header.append("    ZALS.brains.getAnimalsCallback(xhttp,\"C\");\n");
		header.append("    ZALS.brains.refreshSelects();\n");
		header.append("};\n");

		header.append("ZALS.brains.getHerbivores = function() {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZALSModel.HERBIVORE_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.properties[0] = \"*\";\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZALS.brains.getHerbivoresCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZALS.brains.getCarnivores = function() {\n");
		header.append("    var getRequest = new ZODB.data.GetRequest(\"" + ZALSModel.CARNIVORE_CLASS_FULL_NAME + "\");\n");
		header.append("    getRequest.properties[0] = \"*\";\n");
		header.append("    ZODB.data.executeGetRequest(getRequest,ZALS.brains.getCarnivoresCallback,ZODB.gui.genericResponseCallback);\n");	
		header.append("};\n");

		header.append("ZALS.brains.getAnimals = function() {\n");
		header.append("    ZALS.brains.animals = [];\n");
		header.append("    ZALS.brains.getHerbivores();\n");
		header.append("};\n");
		
		header.append("ZALS.brains.refreshSelects = function(xhttp) {\n");
		header.append("    ZODB.dom.clearSelectOptionsByElementId(\"selectAnimalA\");\n");
		header.append("    ZODB.dom.clearSelectOptionsByElementId(\"selectAnimalB\");\n");
		header.append("    for (var i = 0; i < ZALS.brains.animals.length; i++) {\n");
		header.append("        var animal = ZALS.brains.animals[i];\n");
		header.append("        var animalId = animal.type + \":\" + animal.getId();\n");
		header.append("        var animalText = animalId + \" (\" + animal.propertyValues[\"topScore\"] + \")\";\n");
		header.append("        ZODB.dom.addSelectOptionByElementId(\"selectAnimalA\",animalId,animalText);\n");
		header.append("        ZODB.dom.addSelectOptionByElementId(\"selectAnimalB\",animalId,animalText);\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZALS.brains.compare = function() {\n");
		header.append("    if (ZALS.brains.comparing) {\n");
		header.append("        return;\n");
		header.append("    }\n");
		header.append("    ZALS.brains.comparing = true;\n");
		header.append("    var idA = ZODB.dom.getSelectValueByElementId(\"selectAnimalA\");\n");
		header.append("    var idB = ZODB.dom.getSelectValueByElementId(\"selectAnimalB\");\n");
		header.append("    if (idA==idB) {\n");
		header.append("        alert(\"Select two different animals to compare\");\n");
		header.append("        ZALS.brains.comparing = false;\n");
		header.append("    } else {\n");
		header.append("        ZALS.brains.idA = idA;\n");
		header.append("        ZALS.brains.idB = idB;\n");
		header.append("        ZALS.brains.linkType = ZODB.dom.getSelectValueByElementId(\"selectLinkType\");\n");
		header.append("        ZALS.brains.linkOrderBy = ZODB.dom.getSelectValueByElementId(\"selectLinkOrderBy\");\n");
		header.append("        ZALS.brains.setAnimalModules(idA,idB);\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZALS.brains.setAnimalModules = function(idA,idB) {\n");
		header.append("    var namesA = [];\n");
		header.append("    var namesB = [];\n");
		header.append("    for (var i = 0; i < ZALS.brains.modules.length; i++) {\n");
		header.append("        var module = ZALS.brains.modules[i];\n");
		header.append("        var name = module.propertyValues[\"name\"].split(\":\")[2];\n");
		header.append("        if (module.propertyValues[\"name\"].indexOf(idA)==0) {\n");
		header.append("            namesA[namesA.length] = name;\n");
		header.append("        }\n");
		header.append("        if (module.propertyValues[\"name\"].indexOf(idB)==0) {\n");
		header.append("            namesB[namesB.length] = name;\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    ZALS.brains.moduleNames = [];\n");
		header.append("    for (var i = 0; i < namesA.length; i++) {\n");
		header.append("        var found = false;\n");
		header.append("        for (var i2 = 0; i2 < namesB.length; i2++) {\n");
		header.append("            if (namesA[i]==namesB[i2]) {\n");
		header.append("                found = true;\n");
		header.append("                break;\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("        if (found) {\n");
		header.append("            ZALS.brains.moduleNames[ZALS.brains.moduleNames.length] = namesA[i];\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    ZALS.brains.animalModules = [];\n");
		header.append("    for (var i = 0; i < ZALS.brains.modules.length; i++) {\n");
		header.append("        var module = ZALS.brains.modules[i];\n");
		header.append("        var name = module.propertyValues[\"name\"];\n");
		header.append("        if (name.indexOf(idA)==0 || name.indexOf(idB)==0) {\n");
		header.append("            var found = false;\n");
		header.append("            for (var i2 = 0; i2 < ZALS.brains.moduleNames.length; i2++) {\n");
		header.append("                if (name.indexOf(ZALS.brains.moduleNames[i2])>=0) {\n");
		header.append("                    found = true;\n");
		header.append("                    break;\n");
		header.append("                }\n");
		header.append("            }\n");
		header.append("            if (found) {\n");
		header.append("                ZALS.brains.animalModules[ZALS.brains.animalModules.length] = module;\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    var html = \"\";\n");
		header.append("    html += \"<br/>\";\n");
		header.append("    html += \"<table>\";\n");
		header.append("    html += \"<tr>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += \"Comparable modules\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += idA;\n");
		header.append("    html += \" Links\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += idB;\n");
		header.append("    html += \" Links\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"</tr>\";\n");
		header.append("    for (var i = 0; i < ZALS.brains.moduleNames.length; i++) {\n");
		header.append("        html += \"<tr>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += ZALS.brains.moduleNames[i];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"<td align='right' id='\" + idA + \":\" + ZALS.brains.moduleNames[i] + \"'>\";\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"<td  align='right' id='\" + idB + \":\" + ZALS.brains.moduleNames[i] + \"'>\";\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"</tr>\";\n");
		header.append("    }\n");
		header.append("    html += \"</table>\";\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"modules\",html);\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"links\",\"<br/><b>Loading data ...</b>\");\n");
		header.append("    var getters = [];\n");
		header.append("    for (var i = 0; i < ZALS.brains.animalModules.length; i++) {\n");
		header.append("        var getter = new ZALS.brains.GetLinks(ZALS.brains.animalModules[i]);\n");
		header.append("        getters[getters.length] = getter;\n");
		header.append("    }\n");
		header.append("    for (var i = 0; i < getters.length; i++) {\n");
		header.append("        var getter = getters[i];\n");
		header.append("        getter.get();\n");
		header.append("    }\n");
		header.append("};\n");
		
		header.append("ZALS.brains.GetLinks = function(module) {\n");
		header.append("    var that = this;\n");
		header.append("    this.module = module;\n");
		header.append("    this.module.links = null;\n");
		header.append("    this.module.gotLinks = false;\n");
		header.append("    this.moduleName = module.propertyValues[\"name\"];\n");
		header.append("    this.start = 0;\n");
		header.append("    this.get = function() {\n");
		header.append("        ZODB.data.executePostRequest(this.getRequest,this.callback,ZODB.gui.genericResponseCallback);\n");	
		header.append("    }\n");
		header.append("    this.callback = function(xhttp) {\n");
		header.append("        var dataObjectSet = ZODB.data.getDataForClassCallback(xhttp);\n");
		header.append("        if (!that.module.links) {\n");
		header.append("            that.module.links = dataObjectSet.objectArray;\n");
		header.append("        } else {\n");
		header.append("            for (var i = 0; i < dataObjectSet.objectArray.length; i++) {\n");
		header.append("                that.module.links[that.module.links.length] = dataObjectSet.objectArray[i];\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("        var elem = window.document.getElementById(that.moduleName);\n");
		header.append("        elem.innerHTML = that.module.links.length;\n");
		header.append("        if (dataObjectSet.objectArray.length>0) {\n");
		header.append("            that.start += dataObjectSet.objectArray.length;\n");
		header.append("            that.getRequest = that.getNextRequest();\n");
		header.append("            that.get();\n");
		header.append("        } else {\n");
		header.append("            that.module.gotLinks = true;\n");
		header.append("            ZALS.brains.gotLinks();\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    this.getNextRequest = function() {\n");
		header.append("        var getRequest = new ZODB.data.GetRequest(ZALS.brains.linkType);\n");
		header.append("        getRequest.filters[\"module\"] = this.module.getId();\n");
		header.append("        getRequest.filterOperators[\"module\"] = \"contains\";\n");
		header.append("        getRequest.properties[0] = \"*\";\n");
		header.append("        getRequest.orderBy = ZALS.brains.linkOrderBy;\n");
		header.append("        getRequest.start = this.start;\n");
		header.append("        getRequest.limit = 100;\n");
		header.append("        return getRequest;\n");
		header.append("    }\n");
		header.append("    this.getRequest = this.getNextRequest();\n");
		header.append("};\n");

		header.append("ZALS.brains.gotLinks = function() {\n");
		header.append("    var gotAll = true;\n");
		header.append("    for (var i = 0; i < ZALS.brains.animalModules.length; i++) {\n");
		header.append("        if (!ZALS.brains.animalModules[i].gotLinks) {\n");
		header.append("            gotAll = false;\n");
		header.append("            break;\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    if (gotAll) {\n");
		header.append("        ZALS.brains.gotAllLinks();\n");
		header.append("    }\n");
		header.append("};\n");

		header.append("ZALS.brains.gotAllLinks = function() {\n");
		header.append("    var html = \"\";\n");
		header.append("    for (var i = 0; i < ZALS.brains.moduleNames.length; i++) {\n");
		header.append("        var name = ZALS.brains.moduleNames[i];\n");
		header.append("        var moduleNameA = ZALS.brains.idA + \":\" + name;\n");
		header.append("        var moduleNameB = ZALS.brains.idB + \":\" + name;\n");
		header.append("        var moduleA = null;\n");
		header.append("        var moduleB = null;\n");
		header.append("        for (var i2 = 0; i2 < ZALS.brains.animalModules.length; i2++) {\n");
		header.append("            var moduleName = ZALS.brains.animalModules[i2].propertyValues[\"name\"];\n");
		header.append("            if (moduleName==moduleNameA) {\n");
		header.append("                moduleA = ZALS.brains.animalModules[i2];\n");
		header.append("            }\n");
		header.append("            if (moduleName==moduleNameB) {\n");
		header.append("                moduleB = ZALS.brains.animalModules[i2];\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("        html += \"<br/>\";\n");
		header.append("        html += \"<b>\" + name + \"</b><br/>\";\n");
		header.append("        html += ZALS.brains.getSummaryForModules(moduleA,moduleB);\n");
		header.append("        html += \"<br/>\";\n");
		header.append("        html += ZALS.brains.getTableForModules(moduleA,moduleB);\n");
		header.append("    }\n");
		header.append("    ZODB.dom.setDivInnerHTMLByElementId(\"links\",html);\n");
		header.append("    ZALS.brains.comparing = false;\n");
		header.append("};\n");

		header.append("ZALS.brains.getSummaryForModules = function(moduleA,moduleB) {\n");
		header.append("    var uniqueA = 0;\n");
		header.append("    var uniqueB = 0;\n");
		header.append("    var overlap = 0;\n");
		header.append("    for (var i = 0; i < moduleA.links.length; i++) {\n");
		header.append("        var linkA = moduleA.links[i];\n");
		header.append("        var linkB = ZALS.brains.getMatchingLinkFromModule(linkA,moduleB);\n");
		header.append("        if (linkB==null) {\n");
		header.append("            uniqueA++;\n");
		header.append("        } else {\n");
		header.append("            overlap++;\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    for (var i = 0; i < moduleB.links.length; i++) {\n");
		header.append("        var linkB = moduleB.links[i];\n");
		header.append("        var linkA = ZALS.brains.getMatchingLinkFromModule(linkB,moduleA);\n");
		header.append("        if (linkA==null) {\n");
		header.append("            uniqueB++;\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    var html = \"\";\n");
		header.append("    html += ZALS.brains.idA + \" unique: <b>\" + uniqueA + \", </b>\";\n");
		header.append("    html += ZALS.brains.idB + \" unique: <b>\" + uniqueB + \", </b>\";\n");
		header.append("    html += \"overlap: <b>\" + overlap + \"</b>\";\n");
		header.append("    return html;\n");
		header.append("};\n");

		header.append("ZALS.brains.getTableForModules = function(moduleA,moduleB) {\n");
		header.append("    var html = \"\";\n");
		header.append("    html += \"<table>\";\n");
		header.append("    html += \"<tr>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += \"Symbol from\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += \"Symbol to\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    if (ZALS.brains.linkType==ZALS.brains.TYPE_SEQUENCE) {\n");
		header.append("        html += \"<th>\";\n");
		header.append("        html += \"Distance\";\n");
		header.append("        html += \"</th>\";\n");
		header.append("    }\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += ZALS.brains.idA + \" Count\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"<th>\";\n");
		header.append("    html += ZALS.brains.idB + \" Count\";\n");
		header.append("    html += \"</th>\";\n");
		header.append("    html += \"</tr>\";\n");
		header.append("    for (var i = 0; i < moduleA.links.length; i++) {\n");
		header.append("        var linkA = moduleA.links[i];\n");
		header.append("        var linkB = ZALS.brains.getMatchingLinkFromModule(linkA,moduleB);\n");
		header.append("        html += \"<tr>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += linkA.propertyValues[\"symbolFrom\"];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"<td>\";\n");
		header.append("        html += linkA.propertyValues[\"symbolTo\"];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        if (ZALS.brains.linkType==ZALS.brains.TYPE_SEQUENCE) {\n");
		header.append("            html += \"<td>\";\n");
		header.append("            html += linkA.propertyValues[\"distance\"];\n");
		header.append("            html += \"</td>\";\n");
		header.append("        }\n");
		header.append("        html += \"<td align='right'>\";\n");
		header.append("        html += linkA.propertyValues[\"count\"];\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"<td align='right'>\";\n");
		header.append("        if (linkB!=null) {\n");
		header.append("            html += linkB.propertyValues[\"count\"];\n");
		header.append("        }\n");
		header.append("        html += \"</td>\";\n");
		header.append("        html += \"</tr>\";\n");
		header.append("    }\n");
		header.append("    for (var i = 0; i < moduleB.links.length; i++) {\n");
		header.append("        var linkB = moduleB.links[i];\n");
		header.append("        var linkA = ZALS.brains.getMatchingLinkFromModule(linkB,moduleA);\n");
		header.append("        if (linkA==null) {\n");
		header.append("            html += \"<tr>\";\n");
		header.append("            html += \"<td>\";\n");
		header.append("            html += linkB.propertyValues[\"symbolFrom\"];\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"<td>\";\n");
		header.append("            html += linkB.propertyValues[\"symbolTo\"];\n");
		header.append("            html += \"</td>\";\n");
		header.append("            if (ZALS.brains.linkType==ZALS.brains.TYPE_SEQUENCE) {\n");
		header.append("                html += \"<td>\";\n");
		header.append("                html += linkB.propertyValues[\"distance\"];\n");
		header.append("                html += \"</td>\";\n");
		header.append("            }\n");
		header.append("            html += \"<td align='right'>\";\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"<td align='right'>\";\n");
		header.append("            html += linkB.propertyValues[\"count\"];\n");
		header.append("            html += \"</td>\";\n");
		header.append("            html += \"</tr>\";\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    html += \"</table>\";\n");
		header.append("    return html;\n");
		header.append("};\n");

		header.append("ZALS.brains.getMatchingLinkFromModule = function(linkA,moduleB) {\n");
		header.append("    var linkB = null;\n");
		header.append("    for (var i = 0; i < moduleB.links.length; i++) {\n");
		header.append("        var link = moduleB.links[i];\n");
		header.append("        if (ZALS.brains.linkType==ZALS.brains.TYPE_CONTEXT) {\n");
		header.append("            if (link.propertyValues[\"symbolFrom\"]==linkA.propertyValues[\"symbolFrom\"] &&\n");
		header.append("                link.propertyValues[\"symbolTo\"]==linkA.propertyValues[\"symbolTo\"]) {\n");
		header.append("                linkB = link;\n");
		header.append("                break;\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("        if (ZALS.brains.linkType==ZALS.brains.TYPE_SEQUENCE) {\n");
		header.append("            if (link.propertyValues[\"symbolFrom\"]==linkA.propertyValues[\"symbolFrom\"] &&\n");
		header.append("                link.propertyValues[\"symbolTo\"]==linkA.propertyValues[\"symbolTo\"] &&\n");
		header.append("                link.propertyValues[\"distance\"]==linkA.propertyValues[\"distance\"]) {\n");
		header.append("                linkB = link;\n");
		header.append("                break;\n");
		header.append("            }\n");
		header.append("        }\n");
		header.append("    }\n");
		header.append("    return linkB;\n");
		header.append("};\n");

		header.append("ZALS.brains.initialize = function(xhttp) {\n");
		header.append("    ZODB.model.getModelCallback(xhttp);\n");
		header.append("    ZALS.brains.getModules();\n");
		header.append("};\n");
		
		header.append("</script>\n");
		file.getHeadElements().add(header.toString());

		file.setOnload("ZODB.model.getModel(ZALS.brains.initialize);");

		StringBuilder body = new StringBuilder();
		body.append(getZALSMenuHtmlForPage("brains"));
		body.append("<div>");
		body.append("<table>");
		body.append("<tr>");
		body.append("<td width=\"30%\">");
		body.append("Animal&nbsp;A");
		body.append("</td>");
		body.append("<td>");
		body.append("<select id=\"selectAnimalA\">");
		body.append("</select>");
		body.append("</td>");
		body.append("<tr>");
		body.append("</tr>");
		body.append("<td>");
		body.append("Animal&nbsp;B");
		body.append("</td>");
		body.append("<td>");
		body.append("<select id=\"selectAnimalB\">");
		body.append("</select>");
		body.append("</td>");
		body.append("<tr>");
		body.append("</tr>");
		body.append("<td>");
		body.append("Link&nbsp;type");
		body.append("</td>");
		body.append("<td>");
		body.append("<select id=\"selectLinkType\">");
		body.append("<option value=\"" + ZALSModel.SYMBOL_LINK_CONTEXT_CLASS_FULL_NAME + "\">Context</option>");
		body.append("<option value=\"" + ZALSModel.SYMBOL_LINK_SEQUENCE_CLASS_FULL_NAME + "\">Sequence</option>");
		body.append("</select>");
		body.append("</td>");
		body.append("</tr>");
		body.append("<td>");
		body.append("Order&nbsp;by");
		body.append("</td>");
		body.append("<td>");
		body.append("<select id=\"selectLinkOrderBy\">");
		body.append("<option value=\"symbolFrom\">Symbol from</option>");
		body.append("<option value=\"symbolTo\">Symbol to</option>");
		body.append("<option value=\"count\">Count</option>");
		body.append("</select>");
		body.append("</td>");
		body.append("</tr>");
		body.append("</table>");
		body.append("<input type=\"button\" value=\"Compare\" onclick=\"ZALS.brains.compare();\" />");
		body.append("</div>");
		body.append("<div id=\"modules\">");
		body.append("</div>");
		body.append("<div id=\"links\">");
		body.append("</div>");
		file.getBodyElements().add(body.toString());

		return file;
	}
	
	protected StringBuilder getColorCSS() {
		StringBuilder css = new StringBuilder();
		css.append(".red {\n");
		css.append("    background-color: red;\n");
		css.append("}\n");
		css.append(".green {\n");
		css.append("    background-color: green;\n");
		css.append("}\n");
		css.append(".lime {\n");
		css.append("    background-color: lime;\n");
		css.append("}\n");
		css.append(".yellow {\n");
		css.append("    background-color: yellow;\n");
		css.append("}\n");
		css.append(".blue {\n");
		css.append("    background-color: blue;\n");
		css.append("}\n");
		css.append(".purple {\n");
		css.append("    background-color: purple;\n");
		css.append("}\n");
		css.append(".black {\n");
		css.append("    background-color: black;\n");
		css.append("}\n");

		css.append(".redBox {\n");
		css.append("    border: 1px solid red;\n");
		css.append("}\n");
		css.append(".greenBox {\n");
		css.append("    border: 1px solid green;\n");
		css.append("}\n");
		css.append(".limeBox {\n");
		css.append("    border: 1px solid lime;\n");
		css.append("}\n");
		css.append(".yellowBox {\n");
		css.append("    border: 1px solid yellow;\n");
		css.append("}\n");
		css.append(".blueBox {\n");
		css.append("    border: 1px solid blue;\n");
		css.append("}\n");
		css.append(".purpleBox {\n");
		css.append("    border: 1px solid purple;\n");
		css.append("}\n");
		css.append(".greyBox {\n");
		css.append("    border: 1px solid grey;\n");
		css.append("}\n");
		css.append(".whiteBox {\n");
		css.append("    border: 1px solid white;\n");
		css.append("}\n");
		return css;
	}

	@Override
	protected StringBuilder getCSS() {
		StringBuilder css = super.getCSS();
		css.append(getColorCSS());
		return css;
	}

	protected StringBuilder getZALSCSS() {
		StringBuilder css = new StringBuilder();
		
		css.append("table, th, td {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: separate;\n");
		css.append("    border-spacing: 0px;\n");
		css.append("}\n");

		css.append("@media (max-width: 400px) and (max-height: 400px) {\n");
		css.append("    table {\n");
		css.append("        width: 220px;\n");
		css.append("        height: 220px;\n");
		css.append("    }\n");
		css.append("    td {\n");
		css.append("        padding: 0px;\n");
		css.append("        width: 5px;\n");
		css.append("        height: 5px;\n");
		css.append("    }\n");
		css.append("}\n");

		css.append("@media (min-width: 401px) and (min-height: 401px) {\n");
		css.append("    table {\n");
		css.append("        width: 344px;\n");
		css.append("        height: 344px;\n");
		css.append("    }\n");
		css.append("    td {\n");
		css.append("        padding: 0px;\n");
		css.append("        width: 8px;\n");
		css.append("        height: 8px;\n");
		css.append("    }\n");
		css.append("}\n");

		css.append("@media (min-width: 541px) and (min-height: 541px) {\n");
		css.append("    table {\n");
		css.append("        width: 440px;\n");
		css.append("        height: 440px;\n");
		css.append("    }\n");
		css.append("    td {\n");
		css.append("        padding: 0px;\n");
		css.append("        width: 10px;\n");
		css.append("        height: 10px;\n");
		css.append("    }\n");
		css.append("}\n");

		css.append("@media (min-width: 740px) and (min-height: 740px) {\n");
		css.append("    table {\n");
		css.append("        width: 660px;\n");
		css.append("        height: 660px;\n");
		css.append("    }\n");
		css.append("    td {\n");
		css.append("        padding: 0px;\n");
		css.append("        width: 15px;\n");
		css.append("        height: 15px;\n");
		css.append("    }\n");
		css.append("}\n");
		
		css.append(getColorCSS());
		
		return css;
	}
}
