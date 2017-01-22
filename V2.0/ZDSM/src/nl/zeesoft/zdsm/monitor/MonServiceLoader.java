package nl.zeesoft.zdsm.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdsm.database.model.Service;
import nl.zeesoft.zdsm.database.model.ZDSMModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class MonServiceLoader extends HlpGetControllerObject {
	private SortedMap<String,Service>	servicesByName	= new TreeMap<String,Service>();

	protected MonServiceLoader() {
		super(ZDSMModel.SERVICE_CLASS_FULL_NAME);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Service();
	}

	@Override
	public void reinitialize() {
		super.reinitialize();
		servicesByName.clear();
	}

	@Override
	protected void addedObject(HlpObject object) {
		Service obj = (Service) object;
		servicesByName.put(obj.getName(),obj);
	}
	
	protected List<Service> getServicesAsList() {
		List<Service> r = new ArrayList<Service>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Service)object);
		}
		return r;
	}
}
