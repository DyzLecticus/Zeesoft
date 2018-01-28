var sessionHandler = require("../lib/SessionHandler");

function MSBotFrameworkAdapter() {
	this.sessionHandler = new sessionHandler.SessionHandler();
};
MSBotFrameworkAdapter.prototype.setIntentRecognizer = function(intentRecognizer) {
	this.sessionHandler.intentRecognizer = intentRecognizer;
}
MSBotFrameworkAdapter.prototype.addBot = function(bot) {
	this.sessionHandler.botManager.addBot(bot);
}
MSBotFrameworkAdapter.prototype.handleSessionInput = function(session) {
	var conversationId = session.message.address.conversation ? session.message.address.conversation.id : null;
	var s = this.sessionHandler.sessionManager.getSessionByExternalId(conversationId);
	if (s==null) {
		s = this.sessionHandler.sessionManager.getNewSession();
		s.externalId = conversationId;
	}
	this.sessionHandler.handleExternalSessionInput(conversationId,session.message.text);
	session.send(s.getOutput());
}

exports.MSBotFrameworkAdapter = MSBotFrameworkAdapter;
