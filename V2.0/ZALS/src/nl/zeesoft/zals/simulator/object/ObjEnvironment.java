package nl.zeesoft.zals.simulator.object;

import nl.zeesoft.zals.database.model.Environment;
import nl.zeesoft.zals.database.model.ZALSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class ObjEnvironment extends HlpGetControllerObject {
	public ObjEnvironment() {
		super(ZALSModel.ENVIRONMENT_CLASS_FULL_NAME);
		setTimeOutSeconds(30);
	}

	@Override
	protected HlpObject getNewObject() {
		return new Environment();
	}
	
	public Environment getEnvironment() {
		Environment r = null;
		for (HlpObject object: getObjectsAsList()) {
			r = (Environment)object;
			break;
		}
		return r;
	}
}
