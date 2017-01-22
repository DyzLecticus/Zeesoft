package nl.zeesoft.zac.module.object;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.model.SymbolSequenceTraining;
import nl.zeesoft.zac.database.model.ZACModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;

public class ObjSymbolSequenceTraining extends HlpGetControllerObject {
	private long moduleId	= 0;
	
	public ObjSymbolSequenceTraining(long moduleId) {
		super(ZACModel.SYMBOL_SEQUENCE_TRAINING_CLASS_FULL_NAME);
		this.moduleId = moduleId;
	}

	@Override
	protected HlpObject getNewObject() {
		return new SymbolSequenceTraining();
	}
	
	@Override
	protected ReqGet getNewGetRequest() {
		ReqGet get = super.getNewGetRequest();
		get.addFilter("module",ReqGetFilter.CONTAINS,"" + moduleId);
		return get;
	}
	
	public List<SymbolSequenceTraining> getModuleTrainingSequencesAsList() {
		List<SymbolSequenceTraining> r = new ArrayList<SymbolSequenceTraining>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((SymbolSequenceTraining)object);
		}
		return r;
	}
}
