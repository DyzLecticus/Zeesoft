package nl.zeesoft.zac.module.object;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.model.SymbolLinkContext;
import nl.zeesoft.zac.database.model.ZACModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetAddUpdateRemoveControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;

public class ObjSymbolLinkContext extends HlpGetAddUpdateRemoveControllerObject {
	private long moduleId	= 0;
	
	public ObjSymbolLinkContext(long moduleId,List<String> updateProperties) {
		super(ZACModel.SYMBOL_LINK_CONTEXT_CLASS_FULL_NAME,updateProperties);
		this.moduleId = moduleId;
		setTimeOutSeconds(30);
	}

	@Override
	protected HlpObject getNewObject() {
		return new SymbolLinkContext();
	}
	
	@Override
	protected ReqGet getNewGetRequest() {
		ReqGet get = super.getNewGetRequest();
		get.addFilter("module",ReqGetFilter.CONTAINS,"" + moduleId);
		return get;
	}
	
	public List<SymbolLinkContext> getSymbolLinkContextsAsList() {
		List<SymbolLinkContext> r = new ArrayList<SymbolLinkContext>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((SymbolLinkContext)object);
		}
		return r;
	}
	
	public void addSymbolLinkContexts(List<SymbolLinkContext> addLinks) {
		List<HlpObject> addObjects = new ArrayList<HlpObject>();
		addObjects.addAll(addLinks);
		addHlpObjects(addObjects);
	}

	public SymbolLinkContext getSymbolLinkContextBySymbolFromTo(String symbolFrom, String symbolTo) {
		SymbolLinkContext r = null;
		for (HlpObject object: getObjectsAsList()) {
			SymbolLinkContext obj = (SymbolLinkContext) object;
			if (obj.getSymbolFrom().equals(symbolFrom) && obj.getSymbolTo().equals(symbolTo)) {
				r = obj;
				break;
			}
		}
		return r;
	}

	public List<SymbolLinkContext> getSymbolLinkContextsBySymbolFrom(String symbolFrom, int minCount) {
		List<SymbolLinkContext> r = new ArrayList<SymbolLinkContext>();
		for (HlpObject object: getObjectsAsList()) {
			SymbolLinkContext obj = (SymbolLinkContext) object;
			if (obj.getSymbolFrom().equals(symbolFrom) && obj.getCount()>=minCount) {
				r.add(obj);
			}
		}
		return r;
	}

	public List<SymbolLinkContext> getSymbolLinkContextsBySymbolTo(String symbolTo, int minCount) {
		List<SymbolLinkContext> r = new ArrayList<SymbolLinkContext>();
		for (HlpObject object: getObjectsAsList()) {
			SymbolLinkContext obj = (SymbolLinkContext) object;
			if (obj.getSymbolTo().equals(symbolTo) && obj.getCount()>=minCount) {
				r.add(obj);
			}
		}
		return r;
	}
}
