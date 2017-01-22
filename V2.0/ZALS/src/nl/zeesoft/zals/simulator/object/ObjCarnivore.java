package nl.zeesoft.zals.simulator.object;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zals.database.model.Carnivore;
import nl.zeesoft.zals.database.model.ZALSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetAddControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;

public class ObjCarnivore extends HlpGetAddControllerObject {
	private long environmentId	= 0;
	
	public ObjCarnivore(long environmentId) {
		super(ZALSModel.CARNIVORE_CLASS_FULL_NAME);
		this.environmentId = environmentId;
		setTimeOutSeconds(30);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Carnivore();
	}
	
	@Override
	protected ReqGet getNewGetRequest() {
		ReqGet get = super.getNewGetRequest();
		get.addFilter("environment",ReqGetFilter.CONTAINS,"" + environmentId);
		return get;
	}
	
	public List<Carnivore> getCarnivoresAsList() {
		List<Carnivore> r = new ArrayList<Carnivore>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Carnivore)object);
		}
		return r;
	}
	
	public void addCarnivores(List<Carnivore> addCarnivores) {
		List<HlpObject> addObjects = new ArrayList<HlpObject>();
		addObjects.addAll(addCarnivores);
		addHlpObjects(addObjects);
	}
}
