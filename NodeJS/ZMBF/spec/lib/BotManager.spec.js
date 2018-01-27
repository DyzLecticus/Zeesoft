var botManager = require("../../lib/BotManager");
var bot = require("../../lib/Bot");

describe("Bot manager", function() {
	var bm = new botManager.BotManager(1);

	it("should accept bots", function() {
		var b1 = new bot.Bot("DutchGreeter");
		b1.addIntent("DutchGreeting");
		b1.addIntent("DutchWelcome");

		var b2 = new bot.Bot("DutchQnA");
		b1.addIntent("DutchQnA");
		
		bm.addBot(b1);
		bm.addBot(b2);
		expect(bm.bots.length).toBe(2);
	});
	
	it("should return a bot using the name", function() {
		var b = bm.getBot("DutchGreeter");
		expect(b).not.toBe(null);
	});
	it("should return a bot using the intent", function() {
		var b1 = bm.getBot("DutchGreeter");
		var b2 = bm.getBotByIntent("DutchWelcome");
		expect(b1).not.toBe(null);
		expect(b2).not.toBe(null);
		expect(b1).toEqual(b2);
	});
});
