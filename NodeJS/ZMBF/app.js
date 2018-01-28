/**
 * Zeesoft Multi Bot Framework.
 * 
 * Chatbot decomposition and dialog routing.
 */

// Libraries
var bot = require("./lib/Bot");
exports.Bot = bot.Bot;
var botManager = require("./lib/BotManager");
exports.BotManager = botManager.BotManager;

var intentRecognizer = require("./lib/IntentRecognizer");
exports.IntentRecognizer = intentRecognizer.IntentRecognizer;

var session = require("./lib/Session");
exports.Session = session.Session;
var sessionManager = require("./lib/SessionManager");
exports.SessionManager = sessionManager.SessionManager;
var sessionHandler = require("./lib/SessionHandler");
exports.SessionHandler = sessionHandler.SessionHandler;

// Adapters
var msBotFrameworkAdapter = require("./adapters/MSBotFrameworkAdapter");
exports.MSBotFrameworkAdapter = msBotFrameworkAdapter.MSBotFrameworkAdapter;
