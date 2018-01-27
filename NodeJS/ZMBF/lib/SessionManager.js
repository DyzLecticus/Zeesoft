var session = require("./Session");

function SessionManager(seed) {
	this.sessionId = 0;
	if (seed) {
		this.sessionId = seed;
	}
	this.sessions = [];
};	
SessionManager.prototype.getNewSession = function() {
	this.sessionId++;
	this.sessions[this.sessions.length] = new session.Session(this.sessionId);
	return this.sessions[this.sessions.length - 1];
};
SessionManager.prototype.getSession = function(id) {
	var r = null;
	for (var i = 0; i<this.sessions.length; i++) {
		if (this.sessions[i].id==id) {
			r = this.sessions[i];
			break;
		}
	}
	return r;
};
SessionManager.prototype.getSessionByExternalId = function(externalId) {
	var r = null;
	for (var i = 0; i<this.sessions.length; i++) {
		if (this.sessions[i].externalId==externalId) {
			r = this.sessions[i];
			break;
		}
	}
	return r;
};
SessionManager.prototype.removeSession = function(session) {
	var r = -1;
	for (var i = 0; i<this.sessions.length; i++) {
		if (this.sessions[i]==session) {
			r = i;
			break;
		}
	}
	if (r>=0) {
		this.sessions.splice(r,1);
	}
};

exports.SessionManager = SessionManager;
