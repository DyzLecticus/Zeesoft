package nl.zeesoft.zodb.db.init;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;

public interface Persistable extends JsAble {
	public ZStringBuilder getObjectName();
}
