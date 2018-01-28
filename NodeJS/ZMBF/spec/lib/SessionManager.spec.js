var sessionManager = require("../../lib/SessionManager");

describe("Session manager", function() {
	var sm = new sessionManager.SessionManager(1);

	it("should create a session", function() {
		var session = sm.getNewSession();
		session.externalId = "externalId";
		expect(session.id).toBe(2);
	});

	it("should return null using an unknown id", function() {
		var session = sm.getSession(34);
		expect(session).toBe(null);
	});

	it("should return a session using the id", function() {
		var session = sm.getSession(2);
		expect(session).not.toBe(null);
	});

	it("should return null using an unknown external id", function() {
		var session = sm.getSessionByExternalId("unknownId");
		expect(session).toBe(null);
	});

	it("should return a session using the external id", function() {
		var session = sm.getSessionByExternalId("externalId");
		expect(session).not.toBe(null);
	});

	it("should not remove an unknown session", function() {
		var session = new Object();
		sm.removeSession(session);
		expect(sm.sessions.length).toBe(1);
	});

	it("should remove a session", function() {
		var session = sm.getSessionByExternalId("externalId");
		sm.removeSession(session);
		expect(sm.sessions.length).toBe(0);
	});
});
