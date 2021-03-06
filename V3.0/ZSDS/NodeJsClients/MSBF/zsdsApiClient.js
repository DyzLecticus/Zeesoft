var ZSDS = ZSDS || {};
ZSDS.api = {};

ZSDS.api.Client = function(workingRequest,workingVariableValues) {
    var that = this;

    this.processRequestResponse = function(json) {
        that.response.output = "";
        that.response.prompt = "";
        var nextDialog = false;
        if (json.contextOutputs && json.contextOutputs.length>0) {
            var copyValues = true;
            out = json.contextOutputs[0];
            if (out.output.length>0) {
                that.response.output = out.output;
            }
            if (out.prompt.length>0) {
                that.response.prompt = out.prompt;
                that.workingRequest.prompt = out.prompt;
            }
            if (out.promptVariableType && out.promptVariableType=="NXD") {
                nextDialog = true;
            }
            if (out.dialogVariableValues) {
                if (!nextDialog) {
                    that.workingRequest.dialogVariableValues = out.dialogVariableValues;
                }
                if (out.dialogVariableValues.length>0) {
                    for (var i = 0; i < out.dialogVariableValues.length; i++) {
                        var value = out.dialogVariableValues[i];
                        if (value.session) {
                            that.workingVariableValues = that.workingVariableValues || {};
                            that.workingVariableValues[value.name] = value;
                        }
                    }
                }
            }
        }
        if (json.classifiedLanguages && json.classifiedLanguages.length>0) {
            that.workingRequest.language = json.classifiedLanguages[0].symbol;
        }
        if (!nextDialog) {
            if (json.classifiedMasterContexts && json.classifiedMasterContexts.length>0) {
                that.workingRequest.masterContext = json.classifiedMasterContexts[0].symbol;
            }
            if (json.classifiedContexts && json.classifiedContexts.length>0) {
                that.workingRequest.context = json.classifiedContexts[0].symbol;
            }
        } else {
            that.workingRequest.dialogVariableValues = [];
        }
        that.workingVariableValues = that.workingVariableValues || {};
        var vars = that.workingRequest.dialogVariableValues;
        for (name in that.workingVariableValues) {
            var found = false;
            for (var i = 0; i < vars.length; i++) {
                if (vars.name==name) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                vars[vars.length] = that.workingVariableValues[name];
            }
        }
    }

    this.getNewRequest = function() {
        var r = {"prompt": "","input": "","language": "","masterContext": "","context": "","isTestRequest": false,"appendDebugLog": false,"classifyLanguage": true,"minLanguageChangeDifference": 0.1,"correctInput": true,"classifyMasterContext": true,"classifyMasterContextThreshold": 0.25,"classifyContext": true,"classifyContextThreshold": 0.25,"checkProfanity": true,"translateEntityValues": true,"translateEntityTypes": [],"matchThreshold": 0.1,"randomizeOutput": true,"filterContexts": [],"dialogVariableValues": []};
        return r;
    }

    this.workingRequest = that.getNewRequest();
    this.workingVariableValues = {};
    this.response = {};
    this.response.output = "";
    this.response.prompt = "";

    if (workingRequest) {
        this.workingRequest = workingRequest;
    }
    if (workingVariableValues) {
        this.workingVariableValues = workingVariableValues;
    }
};

var exports = exports || {};
exports.Client = ZSDS.api.Client;
