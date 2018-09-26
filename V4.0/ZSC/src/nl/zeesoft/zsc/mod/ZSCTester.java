package nl.zeesoft.zsc.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.TesterObject;
import nl.zeesoft.zodb.mod.TesterRequest;
import nl.zeesoft.zsc.confab.ConfabulatorRequest;
import nl.zeesoft.zsc.confab.ConfabulatorResponse;
import nl.zeesoft.zsc.confab.ConfabulatorSet;
import nl.zeesoft.zsc.confab.confabs.ContextResult;
import nl.zeesoft.zsc.confab.confabs.Correction;

public class ZSCTester extends TesterObject {
	public ZSCTester(Config config, String url) {
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
		context.prob = 0.1408450704225352;
		context.probNormalized = 1D;
		res.contextResults.add(context);
		context = new ContextResult();
		context.contextSymbol = "Goal";
		context.prob = 0.0035211267605633804;
		context.probNormalized = 0.025000000000000005;
		res.contextResults.add(context);
		addRequestNoLock(req,res);

		req = new ConfabulatorRequest(
			ConfabulatorRequest.CORRECT,ConfabulatorSet.TEST_CONFAB_1,
			"My nam is Dyz agent.");
		res = new ConfabulatorResponse();
		res.corrected.append("My name is Dyz Lecticus.");
		correction = new Correction();
		correction.index = 1;
		correction.symbol = "nam";
		correction.correction = "name";
		res.corrections.add(correction);
		correction = new Correction();
		correction.index = 4;
		correction.symbol = "agent";
		correction.correction = "Lecticus";
		res.corrections.add(correction);
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
		}
	}
}
