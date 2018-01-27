var bot = require("../../lib/Bot");

describe("Bot", function() {
	var b = new bot.Bot("DutchGreeter");

	it("should accept intents", function() {
		expect(b.name).toBe("DutchGreeter");
		b.addIntent("DutchGreeting");
		b.addIntent("DutchWelcome");
		expect(b.intents.length).toBe(2);
	});
	
	it("should return true if it has a certain intent", function() {
		expect(b.hasIntent("DutchGreeting")).toBe(true);
	});
});
