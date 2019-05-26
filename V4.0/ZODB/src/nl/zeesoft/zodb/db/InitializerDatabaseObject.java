package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;

public interface InitializerDatabaseObject extends JsAble {
	public ZStringBuilder getObjectName();
}
