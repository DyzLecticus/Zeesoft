package nl.zeesoft.zsdm.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.znlb.ZNLBConfig;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;
import nl.zeesoft.zsdm.dialog.entities.dutch.DutchProfanity;
import nl.zeesoft.zsdm.mod.ModZSDM;

public class DialogSet extends InitializerObject {
	private ZNLBConfig	configuration	= null;
	
	public DialogSet(ZNLBConfig config) {
		super(config,ModZSDM.NAME + "/Dialogs/");
		configuration = config;
	}
	
	public List<Dialog> getDialogs() {
		lockMe(this);
		List<Dialog> r = getDialogsNoLock();
		unlockMe(this);
		return r;
	}

	protected List<Dialog> getDialogsNoLock() {
		List<Dialog> r = new ArrayList<Dialog>();
		for (InitializerDatabaseObject object: getObjectsNoLock()) {
			r.add((Dialog)object);
		}
		return r;
	}

	@Override
	protected void initializeDatabaseObjectsNoLock() {
		addObjectNoLock(new DutchProfanity());
		for (Dialog dialog: getDialogsNoLock()) {
			dialog.setName(configuration.getLanguages().getNameForCode(dialog.getLanguage()) + "/" + dialog.getMasterContext() + "/" + dialog.getContext());
			dialog.initialize();
		}
	}

	@Override
	protected InitializerDatabaseObject getNewObjectNoLock(String name) {
		Dialog dialog = new Dialog();
		dialog.setName(name);
		return dialog;
	}
}
