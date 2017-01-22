package nl.zeesoft.zals.simulator.object;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.model.Module;
import nl.zeesoft.zals.database.model.Animal;
import nl.zeesoft.zals.database.model.Carnivore;
import nl.zeesoft.zals.database.model.Environment;
import nl.zeesoft.zals.database.model.Herbivore;
import nl.zeesoft.zals.database.model.ZALSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetAddControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;

public class ObjAnimalModule extends HlpGetAddControllerObject {
	private Animal animal = null;
	
	public ObjAnimalModule(Animal animal) {
		super(ZALSModel.MODULE_CLASS_FULL_NAME);
		this.animal = animal;
		setTimeOutSeconds(30);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Module();
	}
	
	@Override
	protected ReqGet getNewGetRequest() {
		ReqGet get = super.getNewGetRequest();
		get.addFilter("name",ReqGetFilter.CONTAINS,getModuleNamePrefix());
		return get;
	}
	
	public List<Module> getModulesAsList() {
		List<Module> r = new ArrayList<Module>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Module)object);
		}
		return r;
	}
	
	public void addModules(List<Module> addModules) {
		List<HlpObject> addObjects = new ArrayList<HlpObject>();
		addObjects.addAll(addModules);
		addHlpObjects(addObjects);
	}

	public Module getOrAddModuleByName(String name,int maxSequenceDistance) {
		Module r = getModuleByName(name);
		if (r==null) {
			r = new Module();
			r.setMaxContextCount(Environment.MAX_CONTEXT_COUNT);
			r.setMaxSequenceCount(Environment.MAX_SEQUENCE_COUNT);
			r.setMaxSequenceDistance(maxSequenceDistance);
			r.setName(getModuleNamePrefix() + name);
			List<HlpObject> addObjects = new ArrayList<HlpObject>();
			addObjects.add(r);
			addHlpObjects(addObjects);
		}
		return r;
	}

	private Module getModuleByName(String name) {
		Module r = null;
		for (HlpObject object: getObjectsAsList()) {
			Module obj = (Module) object;
			if (obj.getName().equals(getModuleNamePrefix() + name)) {
				r = obj;
				break;
			}
		}
		return r;
	}

	private String getModuleNamePrefix() {
		String pfx = "";
		if (animal instanceof Herbivore) {
			pfx = "H:";
		} else if (animal instanceof Carnivore) {
			pfx = "C:";
		}
		pfx += animal.getId() + ":";
		return pfx;
	}
}
