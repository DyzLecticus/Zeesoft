package nl.zeesoft.znlb.mod;

import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.znlb.prepro.PreprocessorRequestResponse;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.TesterObject;
import nl.zeesoft.zodb.mod.TesterRequest;

public class ZNLBTester extends TesterObject {
	public ZNLBTester(Config config, String url) {
		super(config, url);
	}

	@Override
	protected void initializeRequestsNoLock() {
		addPreprocessorRequest(
			"i wouldn't do this after ten o'clock",
			"I would not do this after ten oclock.");
		addPreprocessorRequest(Languages.NLD,
			"i wouldn't do this after ten o'clock",
			"I wouldn't do this after ten o'clock.");
		addPreprocessorRequest(
			"tien uur 's ochtends",
			"Tien uur sochtends.");
		addPreprocessorRequest(
			"tien uur 's avondslaat",
			"Tien uur savonds laat.");
	}
	
	@Override
	protected void addLogLineForRequest(TesterRequest request) {
		PreprocessorRequestResponse req = new PreprocessorRequestResponse();
		req.fromJson(request.request);
		PreprocessorRequestResponse res = new PreprocessorRequestResponse();
		res.fromJson(request.response);
		if (req.sequence.length()>0) {
			addLogLineNoLock("Processing took: " + request.time + " ms");
			addLogLineNoLock("  <<< '" + req.sequence + "'");
			addLogLineNoLock("  >>> '" + res.processed + "'");
		}
	}
	
	private void addPreprocessorRequest(String seq,String proc) {
		addPreprocessorRequest("",seq,proc);
	}
	
	private void addPreprocessorRequest(String language,String seq,String proc) {
		PreprocessorRequestResponse req = null;
		PreprocessorRequestResponse res = null;
		
		req = new PreprocessorRequestResponse();
		if (language.length()>0) {
			req.languages.add(language);
			req.languages.add(Languages.UNI);
		}
		req.sequence.append(seq);
		res = new PreprocessorRequestResponse();
		if (language.length()>0) {
			res.languages.add(language);
			res.languages.add(Languages.UNI);
		}
		res.sequence.append(seq);
		res.processed.append(proc);
		addRequestNoLock(req,res);
	}
}
