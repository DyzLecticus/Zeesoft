package nl.zeesoft.zids.database.pattern;

import java.util.List;

import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zids.dialog.pattern.PtnObjectLiteralToValue;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public abstract class PtnDatabaseObject extends PtnObjectLiteralToValue {
	private String					className		= "";
	private HlpGetControllerObject 	objectLoader	= null;
	
	public PtnDatabaseObject(String className, HlpGetControllerObject objectLoader) {
		super(TYPE_OBJECT, className);
		this.className = className;
		this.objectLoader = objectLoader;
	}
	
	@Override
	public final void initializePatternStrings(PtnManager manager) {
		lockMe(this);
		getObjectLoader().reinitialize();
		List<HlpObject> objects = getObjectLoader().getObjectsAsList();
		for (HlpObject object: objects) {
			if (getInstanceNameForObject(object).length()>0) {
				addPatternStringAndValueForObjectNoLock(object);
				addPatternStringAndValueNoLock(getInstanceNameForObject(object).toLowerCase(),"" + object.getId());
			}
		}
		unlockMe(this);
	}

	protected void addPatternStringAndValueForObjectNoLock(HlpObject object) {
		// Override to extend
	}
	
	protected String getClassName() {
		return className;
	}

	protected HlpGetControllerObject getObjectLoader() {
		return objectLoader;
	}
	
	public static String getInstanceNameForObject(HlpObject object) {
		String r = "";
		if (object instanceof PtnDatabaseObjectInterface) {
			PtnDatabaseObjectInterface name = (PtnDatabaseObjectInterface) object;
			r = name.getInstanceName();
		}
		return r;
	}
}
