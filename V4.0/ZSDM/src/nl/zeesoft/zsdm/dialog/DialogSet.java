package nl.zeesoft.zsdm.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.Config;
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
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericCancel;
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericClassification;
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericGoodbye;
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericHandshake;
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericLanguage;
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericMath;
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericProfanity;
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericQnA;
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericSupport;
import nl.zeesoft.zsdm.dialog.dialogs.english.EnglishGenericThanks;
import nl.zeesoft.zsdm.mod.ModZSDM;

public class DialogSet extends InitializerObject {
	public DialogSet(Config config) {
		super(config,ModZSDM.NAME + "/Dialogs/");
	}
	
	public List<Dialog> getDialogs() {
		lockMe(this);
		List<Dialog> r = getDialogsNoLock();
		unlockMe(this);
		return r;
	}

	@Override
	protected void initializeDatabaseObjectsNoLock() {
		addObjectNoLock(new EnglishGenericCancel());
		addObjectNoLock(new EnglishGenericClassification());
		addObjectNoLock(new EnglishGenericGoodbye());
		addObjectNoLock(new EnglishGenericHandshake());
		addObjectNoLock(new EnglishGenericLanguage());
		addObjectNoLock(new EnglishGenericMath());
		addObjectNoLock(new EnglishGenericProfanity());
		addObjectNoLock(new EnglishGenericQnA());
		addObjectNoLock(new EnglishGenericSupport());
		addObjectNoLock(new EnglishGenericThanks());
		
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

	protected List<Dialog> getDialogsNoLock() {
		List<Dialog> r = new ArrayList<Dialog>();
		for (InitializerDatabaseObject object: getObjectsNoLock()) {
			r.add((Dialog)object);
		}
		return r;
	}
}
