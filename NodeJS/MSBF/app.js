var builder = require('botbuilder');
var zmbf = require('zmbf');

var ir = require('./IntentRecognizer');

var dg = require('./bots/DutchGreeter');
var dq = require('./bots/DutchQnA');

// Create the adapter
var adapter = new zmbf.MSBotFrameworkAdapter();

// Attach intent recognizer to adapter
adapter.setIntentRecognizer(ir.intentRecognizer);

// Attach bots to adapter
adapter.addBot(dg.bot);
adapter.addBot(dq.bot);

var connector = new builder.ConsoleConnector().listen();
var bot = new builder.UniversalBot(connector, function (session) {
	// Use the adapter to handle session input
	adapter.handleSessionInput(session);
});
