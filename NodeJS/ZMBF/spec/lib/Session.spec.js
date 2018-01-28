var session = require("../../lib/Session");

describe("Session", function() {
	var sess = new session.Session(1);

	it("should return nulls if there is no input/output", function() {
		expect(sess.getInput()).toBe(null);
		expect(sess.getIntent()).toBe(null);
		expect(sess.getOutput()).toBe(null);
	});

	it("should create an input/output", function() {
		sess.addInputOutput();
		expect(sess.io.length).toBe(1);
	});

	it("should accept and return input", function() {
		sess.setInput("Did somebody ask this question?");
		expect(sess.io[0].input).toBe("Did somebody ask this question?");
		expect(sess.getInput()).toBe("Did somebody ask this question?");
	});

	it("should accept and return intent", function() {
		sess.setIntent("QnA");
		expect(sess.io[0].intent).toBe("QnA");
		expect(sess.getIntent()).toBe("QnA");
	});

	it("should accept and return output", function() {
		sess.setOutput("This is my answer.","outputBy");
		expect(sess.io[0].output).toBe("This is my answer.");
		expect(sess.io[0].outputBy).toBe("outputBy");
		expect(sess.getOutput()).toBe("This is my answer.");
	});
});
