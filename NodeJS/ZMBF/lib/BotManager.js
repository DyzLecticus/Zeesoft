function BotManager() {
	this.bots = [];
};
BotManager.prototype.addBot = function(bot) {
	this.bots[this.bots.length] = bot;
};
BotManager.prototype.getBot = function(name) {
	var r = null;
	for (var i = 0; i<this.bots.length; i++) {
		if (this.bots[i].name==name) {
			r = this.bots[i];
			break;
		}
	}
	return r;
};
BotManager.prototype.getBotByIntent = function(intent) {
	var r = null;
	for (var i = 0; i<this.bots.length; i++) {
		if (this.bots[i].hasIntent(intent)) {
			r = this.bots[i];
			break;
		}
	}
	return r;
};

exports.BotManager = BotManager;
