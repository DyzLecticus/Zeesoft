var request = require('request');
var dr = require('./dialogRequest');
var zsdsApiClient = require('./zsdsApiClient');

function MsbfSessionHandler(config) {
	this.config = config;
};

MsbfSessionHandler.prototype.handleSessionInput = function(session) {
	this.sendSessionDialogRequest(session);
};

MsbfSessionHandler.prototype.sendSessionDialogRequest = function(session) {
	var that = this;
	var url = this.config.ZSDS_DRH_URL;
	var workingRequest = this.getWorkingRequest(session);
	workingRequest.input = session.message.text;
	var options = {
		url: url,
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		json: workingRequest
	}
	request(options, function(error, response, body) {
		that.handleSessionDialogResponse(that,session,error,response,body);
	});
};

MsbfSessionHandler.prototype.handleSessionDialogResponse = function(that, session, error, response, body) {
	if (!error && response.statusCode == 200) {
		that.updateSessionWorkingRequest(that,session,body);
	} else if (response.statusCode == 503) {
		if (body && body.error) {
			session.send(body.error);
		}
	} else {
		console.log('ERROR: HTTP response code: ' + response.statusCode);
		if (error) {
			console.log(error);
		}
	}
};

MsbfSessionHandler.prototype.updateSessionWorkingRequest = function(that, session, body) {
	var workingRequest = null;
	var workingVariableValues = null;
	if (session.conversationData.workingRequest) {
		workingRequest = session.conversationData.workingRequest;
	}
	session.conversationData.workingVariableValues = session.conversationData.workingVariableValues || {};
	workingVariableValues = session.conversationData.workingVariableValues;
	
	var client = new zsdsApiClient.Client(workingRequest,workingVariableValues);
	
	client.processRequestResponse(body);
	if (client.response.output.length>0) {
		session.send(client.response.output);
	}
	if (client.response.prompt.length>0) {
		session.send(client.response.prompt);
	}
}

MsbfSessionHandler.prototype.getWorkingRequest = function(session) {
	var workingRequest = null;
	if (session.conversationData.workingRequest) {
		workingRequest = session.conversationData.workingRequest;
	} else {
		workingRequest = new dr.getNewDialogRequest();
		session.conversationData.workingRequest = workingRequest;
	}
	return workingRequest;
};

MsbfSessionHandler.prototype.setWorkingVariableValue = function(session,value) {
	session.conversationData.workingVariableValues = session.conversationData.workingVariableValues || {};
	session.conversationData.workingVariableValues[value.name] = value;
};

MsbfSessionHandler.prototype.addWorkingVariableValuesToWorkingRequest = function(session) {
	session.conversationData.workingVariableValues = session.conversationData.workingVariableValues || {};
	var vars = session.conversationData.workingRequest.dialogVariableValues;
	for (name in session.conversationData.workingVariableValues) {
		var found = false;
		for (var i = 0; i < vars.length; i++) {
			if (vars.name==name) {
				found = true;
				break;
			}
		}
		if (!found) {
			vars[vars.length] = session.conversationData.workingVariableValues[name]
		}
	}
};

exports.MsbfSessionHandler = MsbfSessionHandler;
