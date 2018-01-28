var zmbf = require('zmbf');

var intentRecognizer = new zmbf.IntentRecognizer();
intentRecognizer.getIntent = function(session) {
	var intent = "DutchGreeting";
	if (session.getInput().indexOf("?")>=0) {
		intent = "DutchQuestion";
	}
	return intent;
}

exports.intentRecognizer = intentRecognizer;
