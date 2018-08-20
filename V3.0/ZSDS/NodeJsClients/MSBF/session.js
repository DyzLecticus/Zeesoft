var request = require('request');
var zsdsApiClient = require('./zsdsApiClient');

function MsbfSessionHandler(config) {
	var that = this;
	this.config = config;
	this.handleSessionInput = function(session) {
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
			that.handleSessionDialogResponse(session,error,response,body);
		});
	};
	this.handleSessionDialogResponse = function(session, error, response, body) {
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
	this.getWorkingRequest = function(session) {
		var workingRequest = null;
		if (session.conversationData.workingRequest) {
			workingRequest = session.conversationData.workingRequest;
		} else {
			var client = new zsdsApiClient.Client();
			workingRequest = client.getNewRequest();
			session.conversationData.workingRequest = workingRequest;
		}
		return workingRequest;
	};
	this.updateSessionWorkingRequest = function(that, session, body) {
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
	};
};

exports.MsbfSessionHandler = MsbfSessionHandler;
