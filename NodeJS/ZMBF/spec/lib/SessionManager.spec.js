var sessionManager = require("../../lib/SessionManager");

describe("Session manager", function() {
	var sm = new sessionManager.SessionManager(1);

	it("should create a session", function() {
		var session = sm.getNewSession();
		session.externalId = "externalId";
		expect(session.id).toBe(2);
	});

	it("should return a session using the id", function() {
		var session = sm.getSession(2);
		expect(session).not.toBe(null);
	});

	it("should return a session using the external id", function() {
		var session = sm.getSessionByExternalId("externalId");
		expect(session).not.toBe(null);
	});

	it("should remove a session", function() {
		var session = sm.getSessionByExternalId("externalId");
		sm.removeSession(session);
		expect(sm.sessions.length).toBe(0);
	});
});
