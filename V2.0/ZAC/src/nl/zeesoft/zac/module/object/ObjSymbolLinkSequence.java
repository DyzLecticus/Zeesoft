package nl.zeesoft.zac.module.object;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.model.SymbolLinkSequence;
import nl.zeesoft.zac.database.model.ZACModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetAddUpdateRemoveControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;

public class ObjSymbolLinkSequence extends HlpGetAddUpdateRemoveControllerObject {
	private long moduleId	= 0;
	
	public ObjSymbolLinkSequence(long moduleId,List<String> updateProperties) {
		super(ZACModel.SYMBOL_LINK_SEQUENCE_CLASS_FULL_NAME,updateProperties);
		this.moduleId = moduleId;
		setTimeOutSeconds(30);
	}

	@Override
	protected HlpObject getNewObject() {
		return new SymbolLinkSequence();
	}
	
	@Override
	protected ReqGet getNewGetRequest() {
		ReqGet get = super.getNewGetRequest();
		get.addFilter("module",ReqGetFilter.CONTAINS,"" + moduleId);
		return get;
	}
	
	public List<SymbolLinkSequence> getSymbolLinkSequencesAsList() {
		List<SymbolLinkSequence> r = new ArrayList<SymbolLinkSequence>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((SymbolLinkSequence)object);
		}
		return r;
	}
	
	public void addSymbolLinkSequences(List<SymbolLinkSequence> addLinks) {
		List<HlpObject> addObjects = new ArrayList<HlpObject>();
		addObjects.addAll(addLinks);
		addHlpObjects(addObjects);
	}
	
	public SymbolLinkSequence getSymbolLinkSequenceBySymbolFromToDistance(String symbolFrom, String symbolTo,int distance) {
		SymbolLinkSequence r = null;
		for (HlpObject object: getObjectsAsList()) {
			SymbolLinkSequence obj = (SymbolLinkSequence) object;
			if (obj.getSymbolFrom().equals(symbolFrom) && obj.getSymbolTo().equals(symbolTo) && obj.getDistance()==distance) {
				r = obj;
				break;
			}
		}
		return r;
	}

	public List<SymbolLinkSequence> getSymbolLinkSequencesBySymbolFrom(String symbolFrom, int distance, int minCount) {
		List<SymbolLinkSequence> r = new ArrayList<SymbolLinkSequence>();
		for (HlpObject object: getObjectsAsList()) {
			SymbolLinkSequence obj = (SymbolLinkSequence) object;
			if (obj.getSymbolFrom().equals(symbolFrom) && obj.getDistance()==distance && obj.getCount()>=minCount) {
				r.add(obj);
			}
		}
		return r;
	}

	public List<SymbolLinkSequence> getSymbolLinkSequencesBySymbolTo(String symbolTo, int distance, int minCount) {
		List<SymbolLinkSequence> r = new ArrayList<SymbolLinkSequence>();
		for (HlpObject object: getObjectsAsList()) {
			SymbolLinkSequence obj = (SymbolLinkSequence) object;
			if (obj.getSymbolTo().equals(symbolTo) && obj.getDistance()==distance && obj.getCount()>=minCount) {
				r.add(obj);
			}
		}
		return r;
	}
}
