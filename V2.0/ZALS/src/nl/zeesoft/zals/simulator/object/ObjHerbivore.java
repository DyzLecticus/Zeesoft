package nl.zeesoft.zals.simulator.object;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zals.database.model.Herbivore;
import nl.zeesoft.zals.database.model.ZALSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetAddControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;

public class ObjHerbivore extends HlpGetAddControllerObject {
	private long environmentId	= 0;
	
	public ObjHerbivore(long environmentId) {
		super(ZALSModel.HERBIVORE_CLASS_FULL_NAME);
		this.environmentId = environmentId;
		setTimeOutSeconds(30);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Herbivore();
	}
	
	@Override
	protected ReqGet getNewGetRequest() {
		ReqGet get = super.getNewGetRequest();
		get.addFilter("environment",ReqGetFilter.CONTAINS,"" + environmentId);
		return get;
	}
	
	public List<Herbivore> getHerbivoresAsList() {
		List<Herbivore> r = new ArrayList<Herbivore>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Herbivore)object);
		}
		return r;
	}
	
	public void addHerbivores(List<Herbivore> addHerbivores) {
		List<HlpObject> addObjects = new ArrayList<HlpObject>();
		addObjects.addAll(addHerbivores);
		addHlpObjects(addObjects);
	}
}
