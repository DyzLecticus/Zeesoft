package nl.zeesoft.zids.database.pattern;

import nl.zeesoft.zids.database.model.Self;
import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class PtnGetSelf extends HlpGetControllerObject {
	protected PtnGetSelf() {
		super(ZIDSModel.SELF_CLASS_FULL_NAME);
	}
	@Override
	protected HlpObject getNewObject() {
		return new Self();
	}
}

