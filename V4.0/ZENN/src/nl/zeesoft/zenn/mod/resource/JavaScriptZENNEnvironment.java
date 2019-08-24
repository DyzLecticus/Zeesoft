package nl.zeesoft.zenn.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.environment.EnvironmentConfig;
import nl.zeesoft.zenn.mod.ModZENN;
import nl.zeesoft.zenn.mod.handler.JsonZENNEnvironmentHandler;

public class JavaScriptZENNEnvironment {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		String path = "../" + ModZENN.NAME + JsonZENNEnvironmentHandler.PATH;
		
		script.append("var ZENN = ZENN || {};\n");
		script.append("ZENN.state = ZENN.state || {};\n");
		script.append("ZENN.state.working = false;\n");
		script.append("ZENN.state.statesPerSecond = 25;\n");
		script.append("ZENN.state.keepStateHistorySeconds = 10;\n");
		script.append("ZENN.state.object = null;\n");
		script.append("ZENN.state.totalHistories = 0;\n");
		script.append("ZENN.state.currentHistoryIndex = 0;\n");
		script.append("ZENN.state.currentHistoryTimeStamp = 0;\n");
		script.append("ZENN.state.displayedGridElems = {};\n");
		script.append("ZENN.state.displayTimes = 0;\n");
		script.append("ZENN.state.displayTimeTotal = 0;\n");
		script.append("ZENN.state.displayTimeAvg = 0;\n");
		script.append("ZENN.state.get = function() {\n");
		script.append("    var request = {};\n");
		script.append("    ZODB.xhr.postJSON(\"" + path + "\",request,ZENN.state.getCallback,ZENN.state.getCallback);\n");
		script.append("};\n");
		script.append("ZENN.state.getCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZODB.xhr.parseResponseJSON(response);\n");
		script.append("    if (object.code==\"503\") {\n");
		script.append("        ZENN.state.working = false;\n");
		script.append("    } else if (object.error) {\n");
		script.append("        alert(object.error);\n");
		script.append("    } else if (object.errors) {\n");
		script.append("        alert(object.errors[0]);\n");
		script.append("    } else if (object.statesPerSecond) {\n");
		script.append("        ZENN.state.working = false;\n");
		script.append("        ZENN.state.statesPerSecond = object.statesPerSecond;\n");
		script.append("        ZENN.state.keepStateHistorySeconds = object.keepStateHistorySeconds;\n");
		script.append("        var found = false;\n");
		script.append("        var total = 0;\n");
		script.append("        for (var name in object.histories) {\n");
		script.append("            total++;\n");
		script.append("            var hist = object.histories[name];\n");
		script.append("            if (hist.timeStamp==ZENN.state.currentHistoryTimeStamp) {\n");
		script.append("                ZENN.state.currentHistoryIndex = parseInt(name,10);\n");
		script.append("                found = true;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        if (total>object.statesPerSecond * 2) {\n");
		script.append("            ZENN.state.totalHistories = total;\n");
		script.append("            if (!found) {\n");
		script.append("                ZENN.state.currentHistoryIndex = Math.round(ZENN.state.totalHistories / 2);\n");
		script.append("            }\n");
		script.append("            ZENN.state.object = object;\n");
		script.append("            ZENN.state.working = true;\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    setTimeout(function() { ZENN.state.get(); },(1000 * ZENN.state.keepStateHistorySeconds / 2));\n");
		script.append("};\n");
		script.append("ZENN.state.refresh = function() {\n");
		script.append("    if (ZENN.state.object!=null) {\n");
		script.append("        if (ZENN.state.working) {\n");
		script.append("            var time = new Date();\n");
		script.append("            var hist = ZENN.state.object.histories[ZENN.state.currentHistoryIndex];\n");
		script.append("            ZENN.state.displayHistory(hist);\n");
		script.append("            ZENN.state.displayTimes++;\n");
		script.append("            ZENN.state.displayTimeTotal+= (new Date).getTime() - time.getTime();\n");
		script.append("            ZENN.state.displayTimeAvg = Math.round(ZENN.state.displayTimeTotal / ZENN.state.displayTimes);\n");
		script.append("            ZENN.state.currentHistoryIndex++;\n");
		script.append("            if (ZENN.state.object.histories[ZENN.state.currentHistoryIndex]) {\n");
		script.append("                ZENN.state.currentHistoryTimeStamp = ZENN.state.object.histories[ZENN.state.currentHistoryIndex].timeStamp;\n");
		script.append("            } else {\n");
		script.append("                ZENN.state.currentHistoryIndex--;\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    var factor = 1;\n");
		script.append("    if (ZENN.state.currentHistoryIndex < ZENN.state.totalHistories / 3) {\n");
		script.append("        factor = 0.5;\n");
		script.append("    } else if (ZENN.state.currentHistoryIndex > (ZENN.state.totalHistories / 5) * 4) {\n");
		script.append("        factor = 3.0;\n");
		script.append("    } else if (ZENN.state.currentHistoryIndex > (ZENN.state.totalHistories / 3) * 2) {\n");
		script.append("        factor = 1.5;\n");
		script.append("    }\n");
		script.append("    var to = Math.round(((1000 / ZENN.state.statesPerSecond) - ZENN.state.displayTimeAvg) * factor);\n");
		script.append("    if (to < 0) {\n");
		script.append("        to = 0;\n");
		script.append("    }\n");
		script.append("    setTimeout(function() { ZENN.state.refresh(); }, to);\n");
		script.append("};\n");
		script.append("ZENN.state.displayHistory = function(hist) {\n");
		script.append("    var displayedGridElems = ZENN.state.displayedGridElems;\n");
		script.append("    ZENN.state.displayedGridElems = {};\n");
		script.append("    var objs = hist.organismData.split(\"|\");\n");
		script.append("    var maxEnergy = parseInt(ZENN.state.object.maxEnergyPlant,10);\n");
		script.append("    for (var obj = 0; obj < objs.length; obj++) {\n");
		script.append("        var props = objs[obj].split(\";\");\n");
		script.append("        var posX = parseInt(props[2],10);\n");
		script.append("        var posY = parseInt(props[3],10);\n");
		script.append("        var tdId = posX + \"-\" + posY;\n");
		script.append("        var energy = parseInt(props[4],10);\n");
		script.append("        if (energy>0) {\n");
		script.append("            var color = props[0];\n");
		script.append("        } else {\n");
		script.append("            var color = \"" + EnvironmentConfig.COLOR_DEAD_OBJECT + "\";\n");
		script.append("        }\n");
		script.append("        if (color==\"green\") {\n");
		script.append("            if (energy==maxEnergy) {\n");
		script.append("                color=\"yellow\";\n");
		script.append("            } else if (energy>=(maxEnergy/2)) {\n");
		script.append("                color=\"lime\";\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        ZENN.state.displayedGridElems[tdId] = {};\n");
		script.append("        ZENN.state.displayedGridElems[tdId].color = color;\n");
		script.append("        ZENN.state.displayedGridElems[tdId].box = ZENN.state.displayedGridElems[tdId].color + \"Box\";\n");
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
		script.append("            if (rot==90 && posX<(" + EnvironmentConfig.SIZE_X + " - 1)) {\n");
		script.append("                tdId = (posX + 1) + \"-\" + posY;\n");
		script.append("            }\n");
		script.append("            if (rot==180 && posY<(" + EnvironmentConfig.SIZE_Y + " - 1)) {\n");
		script.append("                tdId = posX + \"-\" + (posY + 1);\n");
		script.append("            }\n");
		script.append("            if (rot==270 && posX>0) {\n");
		script.append("                tdId = (posX - 1) + \"-\" + posY;\n");
		script.append("            }\n");
		script.append("            if (tdId!=\"\") {\n");
		script.append("                if (!ZENN.state.displayedGridElems[tdId]) {\n");
		script.append("                    ZENN.state.displayedGridElems[tdId] = {};\n");
		script.append("                    ZENN.state.displayedGridElems[tdId].color = \"black\";\n");
		script.append("                }\n");
		script.append("                if (props[7]==\"" + EnvironmentConfig.ACTION_BITE + "\") {\n");
		script.append("                    ZENN.state.displayedGridElems[tdId].box = \"whiteBox\";\n");
		script.append("                } else {\n");
		script.append("                    ZENN.state.displayedGridElems[tdId].box = \"greyBox\";\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("    for (tdId in ZENN.state.displayedGridElems) {\n");
		script.append("        var classes = ZENN.state.displayedGridElems[tdId].color;\n");
		script.append("        if (ZENN.state.displayedGridElems[tdId].box!=\"\") {\n");
		script.append("            classes += \" \" + ZENN.state.displayedGridElems[tdId].box;\n");
		script.append("        }\n");
		script.append("        window.document.getElementById(tdId).className = classes;\n");
		script.append("    }\n");
		script.append("    for (tdId in displayedGridElems) {\n");
		script.append("        if (!ZENN.state.displayedGridElems[tdId]) {\n");
		script.append("           window.document.getElementById(tdId).className = \"black\";\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZENN.state.onload = function() {\n");
		script.append("    ZENN.state.get();\n");
		script.append("    ZENN.state.refresh();\n");
		script.append("};\n");
		
		return script;
	}
}
