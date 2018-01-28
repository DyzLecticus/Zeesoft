var sessionHandler = require("../../lib/SessionHandler");
var bot = require("../../lib/Bot");

describe("Session handler", function() {
	var sh = new sessionHandler.SessionHandler();

	var b1 = new bot.Bot("DutchGreeter");
	b1.addIntent("DutchGreeting");
	b1.addIntent("DutchWelcome");
	b1.handleSessionInput = function(session) {
		session.setOutput("Hallo!",this);
	};
	sh.botManager.addBot(b1);
	
	var b2 = new bot.Bot("DutchQnA");
	sh.botManager.addBot(b2);

	var session = sh.sessionManager.getNewSession();
	session.externalId = "externalId";

	it("should handle session input", function() {
		session.addInputOutput();
		session.setInput("Goedemorgen.");
		sh.handleSessionInput(session);
		expect(session.getIntent()).toBe("DutchGreeting");
		expect(session.getOutput()).toBe("Hallo!");
	});

	it("should handle external session input", function() {
		sh.handleExternalSessionInput("externalId","Goedemorgen.");
		expect(session.io.length).toBe(2);
		expect(session.getOutput()).toBe("Hallo!");
		sh.handleExternalSessionInput("externalId","Goedemorgen.");
		expect(session.io.length).toBe(3);
		expect(session.getOutput()).toBe("Hallo!");
	});
});
