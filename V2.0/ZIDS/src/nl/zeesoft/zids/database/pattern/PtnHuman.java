package nl.zeesoft.zids.database.pattern;

import nl.zeesoft.zids.database.model.Human;
import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class PtnHuman extends PtnDatabaseObject {
	public PtnHuman() {
		super(Human.class.getName(),new PtnGetHuman());
	}
	@Override
	public int getMaximumSymbols() {
		return ZIDSModel.MAXIMUM_HUMAN_NAME_SYMBOLS;
	}
	@Override
	protected void addPatternStringAndValueForObjectNoLock(HlpObject object) {
		addPatternStringAndValueNoLock(getInstanceNameForObject(object),"" + object.getId());
	}
}

