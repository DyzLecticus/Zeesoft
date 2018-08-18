package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.dialog.DialogInstance;

public class TestJavaScript {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		script.append("var ZSDS = ZSDS || {};\n");
		
		script.append("ZSDS.test = {};\n");
		script.append("ZSDS.test.testCase = {};\n");
		script.append("ZSDS.test.testCase.name = \"RecordedTestCase\";\n");
		script.append("ZSDS.test.testCase.io = [];\n");
		script.append("ZSDS.test.request = null;\n");
		script.append("ZSDS.test.sessionVariableValues = {};\n");
		script.append("ZSDS.test.getRequest = function() {\n");
		script.append("    ZSDS.xhr.getJSON(\"testDialogRequest.json\",ZSDS.test.getRequestCallback,ZSDS.test.getRequestCallback);\n");
		script.append("};\n");
		script.append("ZSDS.test.getRequestCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    var object = ZSDS.xhr.parseResponseJSON(response);\n");
		script.append("    ZSDS.test.request = object;\n");
		script.append("    ZSDS.test.refreshRequest();\n");
		script.append("};\n");
		script.append("ZSDS.test.sendRequest = function() {\n");
		script.append("    var elem = window.document.getElementById(\"request\");\n");
		script.append("    if (elem!=null && elem.value.length>0) {\n");
		script.append("        elemResponse = window.document.getElementById(\"response\");\n");
		script.append("        if (elemResponse!=null) {\n");
		script.append("            elemResponse.value = \"Sending request ...\";\n");
		script.append("        }\n");
		script.append("        ZSDS.test.request = JSON.parse(elem.value);\n");
		script.append("        ZSDS.test.refreshRequest();\n");
		script.append("        ZSDS.test.request = JSON.parse(elem.value);\n");
		script.append("        ZSDS.test.addTestCaseIO(JSON.parse(elem.value));\n");
		script.append("        ZSDS.xhr.postJSON(\"dialogRequestHandler.json\",ZSDS.test.request,ZSDS.test.sendRequestCallback,ZSDS.test.sendRequestCallback);\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.test.sendRequestCallback = function(xhr) {\n");
		script.append("    var response = xhr.responseText;\n");
		script.append("    ZSDS.test.addTestCaseIOResponse(JSON.parse(response));\n");
		script.append("    var object = ZSDS.xhr.parseResponseJSON(response);\n");
		script.append("    ZSDS.test.showResponse(object);\n");
		script.append("};\n");
		script.append("ZSDS.test.focusInput = function() {\n");
		script.append("    var elem = window.document.getElementById(\"input\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        elem.blur();\n");
		script.append("        elem.focus();\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.test.refreshRequest = function() {\n");
		script.append("    var elem = window.document.getElementById(\"request\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        var elemPrompt = window.document.getElementById(\"prompt\");\n");
		script.append("        var elemInput = window.document.getElementById(\"input\");\n");
		script.append("        if (ZSDS.test.request!=null) {\n");
		script.append("            ZSDS.test.request.prompt = elemPrompt.value;\n");
		script.append("            ZSDS.test.request.input = elemInput.value;\n");
		script.append("            elem.value = JSON.stringify(ZSDS.test.request,null,2);\n");
		script.append("         } else {\n");
		script.append("             elem.value = \"\";\n");
		script.append("         }\n");
		script.append("    }\n");
		script.append("}\n");
		script.append("ZSDS.test.showResponse = function(object) {\n");
		script.append("    var elem = window.document.getElementById(\"response\");\n");
		script.append("    if (elem!=null) {\n");
		script.append("        if (typeof(object.contextOutputs)!==\"undefined\" && typeof(object.contextOutputs[0])!==\"undefined\") {\n");
		script.append("            var elemOutput = window.document.getElementById(\"output\");\n");
		script.append("            var elemPrompt = window.document.getElementById(\"prompt\");\n");
		script.append("            var elemInput = window.document.getElementById(\"input\");\n");
		script.append("            if (elemOutput!=null && elemPrompt!=null && elemInput!=null) {\n");
		script.append("                elemOutput.value = object.contextOutputs[0].output;\n");
		script.append("                elemPrompt.value = object.contextOutputs[0].prompt;\n");
		script.append("                elemInput.value = \"\";\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        if (typeof(object.classifiedLanguages)!==\"undefined\" && typeof(object.classifiedLanguages[0])!==\"undefined\") {\n");
		script.append("            ZSDS.test.request.language = object.classifiedLanguages[0].symbol;\n");
		script.append("        }\n");
		script.append("        if (typeof(object.classifiedMasterContexts)!==\"undefined\" && typeof(object.classifiedMasterContexts[0])!==\"undefined\") {\n");
		script.append("            ZSDS.test.request.masterContext = object.classifiedMasterContexts[0].symbol;\n");
		script.append("        }\n");
		script.append("        if (typeof(object.classifiedContexts)!==\"undefined\" && typeof(object.classifiedContexts[0])!==\"undefined\") {\n");
		script.append("            ZSDS.test.request.context = object.classifiedContexts[0].symbol;\n");
		script.append("        }\n");
		script.append("        var values = {};\n");
		script.append("        if (\n");
		script.append("            typeof(object.contextOutputs)!==\"undefined\" &&\n");
		script.append("            typeof(object.contextOutputs[0])!==\"undefined\" &&\n");
		script.append("            typeof(object.contextOutputs[0].dialogVariableValues)!==\"undefined\"\n");
		script.append("            ) {\n");
		script.append("            ZSDS.test.request.dialogVariableValues = object.contextOutputs[0].dialogVariableValues;\n");
		script.append("            for (var num in object.contextOutputs[0].dialogVariableValues) {\n");
		script.append("            	   values[num] = object.contextOutputs[0].dialogVariableValues[num];\n");
		script.append("                if (values[num].session) {\n");
		script.append("                    ZSDS.test.sessionVariableValues[values[num].name] = values[num];\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        if (\n");
		script.append("            typeof(object.contextOutputs)!==\"undefined\" &&\n");
		script.append("            typeof(object.contextOutputs[0])!==\"undefined\" &&\n");
		script.append("            typeof(object.contextOutputs[0].promptVariableName)!==\"undefined\" &&\n");
		script.append("            object.contextOutputs[0].promptVariableName===\"" + DialogInstance.VARIABLE_NEXT_DIALOG + "\"\n");
		script.append("            ) {\n");
		script.append("            ZSDS.test.request.masterContext = \"\";\n");
		script.append("            ZSDS.test.request.context = \"\";\n");
		script.append("            ZSDS.test.request.dialogVariableValues = [];\n");
		script.append("            values = {};\n");
		script.append("        }\n");
		script.append("        for (var name in ZSDS.test.sessionVariableValues) {\n");
		script.append("            var found = false;\n");
		script.append("            for (var num in values) {\n");
		script.append("                if (values[num].name==name) {\n");
		script.append("                    found = true;\n");
		script.append("                    break;\n");
		script.append("                }\n");
		script.append("            }\n");
		script.append("            if (!found) {\n");
		script.append("                var add = 0;\n");
		script.append("                if (typeof(ZSDS.test.request.dialogVariableValues)===\"object\") {\n");
		script.append("                    for (var num in ZSDS.test.request.dialogVariableValues) {\n");
		script.append("                        add++;\n");
		script.append("                    }\n");
		script.append("                    add = \"\" + add;\n");
		script.append("                } else {\n");
		script.append("                    add = ZSDS.test.request.dialogVariableValues.length;\n");
		script.append("                }\n");
		script.append("                ZSDS.test.request.dialogVariableValues[add] = ZSDS.test.sessionVariableValues[name];\n");
		script.append("            }\n");
		script.append("        }\n");
		script.append("        ZSDS.test.refreshRequest();\n");
		script.append("        var response = JSON.stringify(object,null,2);\n");
		script.append("        response = response.replace(/\\\\n/g,\"\\n\");\n");
		script.append("        elem.value = response;\n");
		script.append("        elem.scrollTop = elem.scrollHeight;\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.test.addTestCaseIO = function(request) {\n");
		script.append("    if (!ZSDS.test.request.randomizeOutput) {\n");
		script.append("        var obj = {};\n");
		script.append("        obj.request = request;\n");
		script.append("        ZSDS.test.testCase.io[ZSDS.test.testCase.io.length] = obj;\n");
		script.append("    }\n");
		script.append("};\n");
		script.append("ZSDS.test.addTestCaseIOResponse = function(response) {\n");
		script.append("    if (!ZSDS.test.request.randomizeOutput) {\n");
		script.append("        response.debugLog = \"\";\n");
		script.append("        var obj = ZSDS.test.testCase.io[(ZSDS.test.testCase.io.length - 1)];\n");
		script.append("        obj.expectedResponse = response;\n");
		script.append("        elemResponse = window.document.getElementById(\"testCase\");\n");
		script.append("        if (elemResponse!=null) {\n");
		script.append("            elemResponse.value = JSON.stringify(ZSDS.test.testCase,null,2);\n");
		script.append("        }\n");
		script.append("    }\n");
		script.append("};\n");
		
		return script;
	}
}
