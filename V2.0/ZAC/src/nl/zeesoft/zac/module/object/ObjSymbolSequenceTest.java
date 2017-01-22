package nl.zeesoft.zac.module.object;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.model.SymbolSequenceTest;
import nl.zeesoft.zac.database.model.ZACModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;

public class ObjSymbolSequenceTest extends HlpGetControllerObject {
	private long moduleId	= 0;
	
	public ObjSymbolSequenceTest(long moduleId) {
		super(ZACModel.SYMBOL_SEQUENCE_TEST_CLASS_FULL_NAME);
		this.moduleId = moduleId;
	}

	@Override
	protected HlpObject getNewObject() {
		return new SymbolSequenceTest();
	}
	
	@Override
	protected ReqGet getNewGetRequest() {
		ReqGet get = super.getNewGetRequest();
		get.addFilter("module",ReqGetFilter.CONTAINS,"" + moduleId);
		return get;
	}
	
	public List<SymbolSequenceTest> getTestSequencesAsList() {
		List<SymbolSequenceTest> r = new ArrayList<SymbolSequenceTest>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((SymbolSequenceTest)object);
		}
		return r;
	}
}
