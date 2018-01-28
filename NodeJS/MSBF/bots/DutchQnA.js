var zmbf = require('zmbf');

var bot = new zmbf.Bot("DutchQnA");
bot.addIntent("DutchQuestion");
bot.handleSessionInput = function(session) {
	session.setOutput("Het antwoord is 42.",this);
};

exports.bot = bot;
