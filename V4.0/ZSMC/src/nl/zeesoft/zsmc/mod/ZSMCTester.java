package nl.zeesoft.zsmc.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.TesterObject;
import nl.zeesoft.zodb.mod.TesterRequest;
import nl.zeesoft.zsmc.confab.ContextResult;
import nl.zeesoft.zsmc.confab.Correction;
import nl.zeesoft.zsmc.db.ConfabulatorSet;
import nl.zeesoft.zsmc.request.ConfabulatorRequest;
import nl.zeesoft.zsmc.request.ConfabulatorResponse;

public class ZSMCTester extends TesterObject {
	public ZSMCTester(Config config, String url) {
		super(config, url);
	}

	@Override
	protected void initializeRequestsNoLock() {
		ConfabulatorRequest req = null;
		ConfabulatorResponse res = null;
		ContextResult context = null;
		Correction correction = null;

		req = new ConfabulatorRequest(
			ConfabulatorRequest.CONTEXT,ConfabulatorSet.TEST_CONFAB_1,
			"My name is Dyz Lecticus.");
		res = new ConfabulatorResponse();
		context = new ContextResult();
		context.contextSymbol = "Name";
		context.prob = 0.049295774647887335;
		context.probNormalized = 1D;
		res.contextResults.add(context);
		addRequestNoLock(req,res);

		req = new ConfabulatorRequest(
			ConfabulatorRequest.CORRECT,ConfabulatorSet.TEST_CONFAB_1,
			"My nam is Dyz Lecitcus.");
		res = new ConfabulatorResponse();
		res.corrected.append("My name is Dyz Lecticus.");
		correction = new Correction();
		correction.index = 1;
		correction.symbol = "nam";
		correction.correction = "name";
		res.corrections.add(correction);
		correction = new Correction();
		correction.index = 4;
		correction.symbol = "Lecitcus";
		correction.correction = "Lecticus";
		res.corrections.add(correction);
		addRequestNoLock(req,res);
		
		req = new ConfabulatorRequest(
			ConfabulatorRequest.EXTEND,ConfabulatorSet.TEST_CONFAB_1,
			"I am");
		req.extend = 6;
		res = new ConfabulatorResponse();
		res.extension.append("an artificially intelligent virtual agent.");
		addRequestNoLock(req,res);

		req = new ConfabulatorRequest(
			ConfabulatorRequest.EXTEND,ConfabulatorSet.TEST_CONFAB_1,
			"My");
		req.extend = 5;
		req.contextSymbol = "Name";
		res = new ConfabulatorResponse();
		res.extension.append("name is Dyz Lecticus.");
		addRequestNoLock(req,res);
	}
	
	@Override
	protected void addLogLineForRequest(TesterRequest request) {
		ConfabulatorRequest req = new ConfabulatorRequest();
		req.fromJson(request.request);
		ConfabulatorResponse res = new ConfabulatorResponse();
		res.fromJson(request.response);
		if (req.type.equals(ConfabulatorRequest.CONTEXT)) {
			addLogLineNoLock("Confabulating context took: " + request.time + " ms, contexts: " + res.contextResults.size());
		} else if (req.type.equals(ConfabulatorRequest.CORRECT)) {
			addLogLineNoLock("Confabulating correction took: " + request.time + " ms");
			addLogLineNoLock("  <<< '" + req.input + "'");
			addLogLineNoLock("  >>> '" + res.corrected + "'");
		} else if (req.type.equals(ConfabulatorRequest.EXTEND)) {
			addLogLineNoLock("Confabulating extension took: " + request.time + " ms");
			addLogLineNoLock("  <<< '" + req.input + "'");
			addLogLineNoLock("  >>> '" + res.extension + "'");
		}
	}
}
