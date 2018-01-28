var zmbf = require('zmbf');

var bot = new zmbf.Bot("DutchGreeter");
bot.addIntent("DutchGreeting");
bot.handleSessionInput = function(session) {
	if (session.getInput().toLowerCase()==="hallo") {
		session.setOutput("Hallo!",this);
	} else if (session.getInput().toLowerCase()==="hoi") {
		session.setOutput("Hoi!",this);
	} else {
		session.setOutput("Groeters!",this);
	}
};

exports.bot = bot;
