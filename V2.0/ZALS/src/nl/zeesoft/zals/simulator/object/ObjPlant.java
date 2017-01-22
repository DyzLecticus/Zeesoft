package nl.zeesoft.zals.simulator.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zals.database.model.Environment;
import nl.zeesoft.zals.database.model.Plant;
import nl.zeesoft.zals.database.model.ZALSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;

public class ObjPlant extends HlpGetControllerObject {
	private long environmentId	= 0;
	
	public ObjPlant(long environmentId) {
		super(ZALSModel.PLANT_CLASS_FULL_NAME);
		this.environmentId = environmentId;
		setTimeOutSeconds(30);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Plant();
	}
	
	@Override
	protected ReqGet getNewGetRequest() {
		ReqGet get = super.getNewGetRequest();
		get.addFilter("environment",ReqGetFilter.CONTAINS,"" + environmentId);
		return get;
	}
	
	public List<Plant> getPlantsAsList() {
		List<Plant> r = new ArrayList<Plant>();
		for (HlpObject object: getObjectsAsList()) {
			r.add((Plant)object);
		}
		return r;
	}

	public List<Plant> getLivingPlantsAsList(Environment env) {
		Date now = new Date();
		List<Plant> r = new ArrayList<Plant>();
		for (HlpObject object: getObjectsAsList()) {
			Plant obj = (Plant) object;
			if (obj.getDateTimeDied() < (now.getTime() - (env.getDeathDurationSeconds() * 1000))) {
				r.add(obj);
			}
		}
		return r;
	}
}
