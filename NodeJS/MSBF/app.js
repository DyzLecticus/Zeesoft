var builder = require('botbuilder');
var zmbf = require('zmbf');

// Create the adapter
var adapter = new zmbf.MSBotFrameworkAdapter();

// Create an intent recognizer
var ir = new zmbf.IntentRecognizer();
ir.getIntent = function(session) {
	var intent = "DutchGreeting";
	if (session.getInput().indexOf("?")>=0) {
		intent = "DutchQuestion";
	}
	return intent;
}

// Attach intent recognizer to adapter
adapter.setIntentRecognizer(ir);

// Create a bot
var b1 = new zmbf.Bot("DutchGreeter");
b1.addIntent("DutchGreeting");
b1.handleSessionInput = function(session) {
	if (session.getInput().toLowerCase()==="hallo") {
		session.setOutput("Hallo!",this);
	} else if (session.getInput().toLowerCase()==="hoi") {
		session.setOutput("Hoi!",this);
	} else {
		session.setOutput("Groeters!",this);
	}
};

// Create another bot
var b2 = new zmbf.Bot("DutchQnA");
b2.addIntent("DutchQuestion");
b2.handleSessionInput = function(session) {
	session.setOutput("Het antwoord is 42.",this);
};

// Attach bots to adapter
adapter.addBot(b1);
adapter.addBot(b2);

var connector = new builder.ConsoleConnector().listen();
var bot = new builder.UniversalBot(connector, function (session) {
	// Use the adapter to handle session input
	adapter.handleSessionInput(session);
});
