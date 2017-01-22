package nl.zeesoft.zids.database.pattern;

import nl.zeesoft.zids.database.model.Human;
import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpGetControllerObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class PtnGetHuman extends HlpGetControllerObject {
	protected PtnGetHuman() {
		super(ZIDSModel.HUMAN_CLASS_FULL_NAME);
	}
	@Override
	protected HlpObject getNewObject() {
		return new Human();
	}
}

