/**
 * Zeesoft Multi Bot Framework.
 * 
 * Chatbot decomposition and dialog routing
 */
require("lib/Service")
require("lib/Session")
 
if (typeof(process.env.ZMBFCONFIG)==="undefined" || process.env.ZMBFCONFIG===null) {
	process.env.ZMBFCONFIG = "zmbf-config.json";
}
var config = require(process.env.ZMBFCONFIG);

