function getDialogRequest() {
	var r = {
		'prompt': '',
		'input': '',
		'language': '',
		'masterContext': '',
		'context': '',
		'isTestRequest': false,
		'appendDebugLog': true,
		'classifyLanguage': true,
		'minLanguageChangeDifference': 0.1,
		'correctInput': true,
		'classifyMasterContext': true,
		'classifyMasterContextThreshold': 0.25,
		'classifyContext': true,
		'classifyContextThreshold': 0.25,
		'checkProfanity': true,
		'translateEntityValues': true,
		'translateEntityTypes': [],
		'matchThreshold': 0.1,
		'randomizeOutput': false,
		'filterContexts': [],
		'dialogVariableValues': []
	}
	return r;
}

function getNewDialogRequest() {
	var r = getDialogRequest();
	r.appendDebugLog = false;
	r.randomizeOutput = true;
	return r;
}

exports.getNewDialogRequest = getNewDialogRequest;
