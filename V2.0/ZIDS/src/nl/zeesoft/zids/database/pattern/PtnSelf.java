package nl.zeesoft.zids.database.pattern;

import nl.zeesoft.zids.database.model.Self;
import nl.zeesoft.zids.database.model.ZIDSModel;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class PtnSelf extends PtnDatabaseObject {
	public PtnSelf() {
		super(Self.class.getName(),new PtnGetSelf());
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

