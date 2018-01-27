function Session(id) {
	this.id = id;
	this.externalId = "";
	this.io = [];
};
Session.prototype.getInput = function() {
	var r = null;
	if (this.io.length>0) {
		r = this.io[this.io.length - 1].input;
	}
	return r;
};
Session.prototype.getIntent = function() {
	var r = null;
	if (this.io.length>0) {
		r = this.io[this.io.length - 1].intent;
	}
	return r;
};
Session.prototype.getOutput = function() {
	var r = null;
	if (this.io.length>0) {
		r = this.io[this.io.length - 1].output;
	}
	return r;
};
Session.prototype.addInputOutput = function() {
	this.io[this.io.length] = new InputOutput();
};
Session.prototype.setInput = function(input) {
	this.io[this.io.length - 1].input = input;
};
Session.prototype.setIntent = function(intent) {
	this.io[this.io.length - 1].intent = intent;
};
Session.prototype.setOutput = function(output,outputBy) {
	this.io[this.io.length - 1].output = output;
	this.io[this.io.length - 1].outputBy = outputBy;
};

function InputOutput() {
	this.input = "";
	this.output = "";
	this.outputBy = null;
	this.intent = "";
};

exports.Session = Session;
