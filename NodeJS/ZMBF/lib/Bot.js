function Bot(name) {
	this.name = name;
	this.intents = [];
};
Bot.prototype.handleSessionInput = function(session) {
	// Override to implement
};
Bot.prototype.addIntent = function(intent) {
	this.intents[this.intents.length] = intent;
};
Bot.prototype.hasIntent = function(intent) {
	var r = false;
	for (var i = 0; i<this.intents.length; i++) {
		if (this.intents[i]==intent) {
			r = true;
			break;
		}
	}
	return r;
};

exports.Bot = Bot;
