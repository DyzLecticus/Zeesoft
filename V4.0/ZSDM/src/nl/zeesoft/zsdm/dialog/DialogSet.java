package nl.zeesoft.zsdm.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.znlb.ZNLBConfig;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericCancel;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericClassification;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericGoodbye;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericHandshake;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericLanguage;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericMath;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericProfanity;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericQnA;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericSupport;
import nl.zeesoft.zsdm.dialog.dialogs.dutch.DutchGenericThanks;
import nl.zeesoft.zsdm.mod.ModZSDM;

public class DialogSet extends InitializerObject {
	public DialogSet(ZNLBConfig config) {
		super(config,ModZSDM.NAME + "/Dialogs/");
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
		addObjectNoLock(new DutchGenericCancel());
		addObjectNoLock(new DutchGenericClassification());
		addObjectNoLock(new DutchGenericGoodbye());
		addObjectNoLock(new DutchGenericHandshake());
		addObjectNoLock(new DutchGenericLanguage());
		addObjectNoLock(new DutchGenericMath());
		addObjectNoLock(new DutchGenericProfanity());
		addObjectNoLock(new DutchGenericQnA());
		addObjectNoLock(new DutchGenericSupport());
		addObjectNoLock(new DutchGenericThanks());
		for (Dialog dialog: getDialogsNoLock()) {
			Languages languages = ((ModZNLB) getConfiguration().getModule(ModZNLB.NAME)).getLanguages(); 
			dialog.setName(languages.getNameForCode(dialog.getLanguage()) + "/" + dialog.getMasterContext() + "/" + dialog.getContext());
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
