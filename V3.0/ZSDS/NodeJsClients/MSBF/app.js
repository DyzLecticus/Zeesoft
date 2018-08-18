var builder = require('botbuilder');
var config = require('./config');
var session = require('./session');

var sessionHandler = new session.MsbfSessionHandler(config.config);

var connector = new builder.ConsoleConnector().listen();
var bot = new builder.UniversalBot(connector, function (session) {
	sessionHandler.handleSessionInput(session)
});
