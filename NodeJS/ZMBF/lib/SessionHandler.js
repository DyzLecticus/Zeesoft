var botManager = require("./BotManager");
var sessionManager = require("./SessionManager");
var intentRecognizer = require("./IntentRecognizer");

function SessionHandler() {
	this.botManager = new botManager.BotManager();
	this.sessionManager = new sessionManager.SessionManager();
	this.intentRecognizer = new intentRecognizer.IntentRecognizer();
};
SessionHandler.prototype.handleSessionInput = function(session) {
	var intent = this.intentRecognizer.getIntent(session);
	session.setIntent(intent);
	var bot = null;
	if (intent && intent.length>0) {
		bot = this.botManager.getBotByIntent(intent);
	}
	if (bot!=null) {
		bot.handleSessionInput(session);
	}
};
SessionHandler.prototype.handleExternalSessionInput = function(externalId,input) {
	var session = this.sessionManager.getSessionByExternalId(externalId);
	session.addInputOutput();
	session.setInput(input);
	this.handleSessionInput(session);
}

exports.SessionHandler = SessionHandler;
