var botManager = require("./BotManager");

function IntentRecognizer() {
	this.botManager = null;
};
IntentRecognizer.prototype.getIntent = function(session) {
	// Override to implement
	return "DutchGreeting";
};

exports.IntentRecognizer = IntentRecognizer;
