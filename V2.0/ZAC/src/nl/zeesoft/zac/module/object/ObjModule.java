package nl.zeesoft.zac.module.object;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zac.database.model.Module;
import nl.zeesoft.zac.database.model.ZACModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class ObjModule extends HlpGetControllerObject {
	private SortedMap<String,Module>	modulesByName	= new TreeMap<String,Module>();

	public ObjModule() {
		super(ZACModel.MODULE_CLASS_FULL_NAME);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Module();
	}

	@Override
	public void reinitialize() {
		super.reinitialize();
		modulesByName.clear();
	}

	@Override
	protected void addedObject(HlpObject object) {
		Module obj = (Module) object;
		modulesByName.put(obj.getName(),obj);
	}

	@Override
	protected void removedObject(HlpObject object) {
		Module obj = (Module) object;
		modulesByName.remove(obj.getName());
	}
	
	public List<Module> getModulesAsList() {
		List<Module> r = new ArrayList<Module>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Module)object);
		}
		return r;
	}

	public Module getModuleByName(String name) {
		return modulesByName.get(name);
	}
}
