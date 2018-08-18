function MsbfSessionHandler(config) {
	this.config = config;
	this.started = false;
};

MsbfSessionHandler.prototype.handleSessionInput = function(session) {
	if (!this.started) {
		this.started = true;
		var conversationId = session.message.address.conversation ? session.message.address.conversation.id : null;
		console.log("Conversation ID: " + conversationId);
	}
	
	//session.message.text
	//session.send(s.getOutput());
}
MsbfSessionHandler.prototype.sendSessionInput = function(session) {
	var url = this.config.ZSDS_DRH_URL;
}

MsbfSessionHandler.prototype.getNewRequest = function() {
	var request = {};
	request.language = "";
	request.masterContext = "";
	request.context = "";
}

exports.MsbfSessionHandler = MsbfSessionHandler;
