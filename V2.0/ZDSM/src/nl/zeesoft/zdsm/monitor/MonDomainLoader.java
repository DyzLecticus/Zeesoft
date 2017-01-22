package nl.zeesoft.zdsm.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdsm.database.model.Domain;
import nl.zeesoft.zdsm.database.model.ZDSMModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class MonDomainLoader extends HlpGetControllerObject {
	private SortedMap<String,Domain>	domainsByName	= new TreeMap<String,Domain>();

	protected MonDomainLoader() {
		super(ZDSMModel.DOMAIN_CLASS_FULL_NAME);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Domain();
	}

	@Override
	public void reinitialize() {
		super.reinitialize();
		domainsByName.clear();
	}

	@Override
	protected void addedObject(HlpObject object) {
		Domain obj = (Domain) object;
		domainsByName.put(obj.getName(),obj);
	}
	
	protected List<Domain> getDomainsAsList() {
		List<Domain> r = new ArrayList<Domain>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Domain)object);
		}
		return r;
	}
}
