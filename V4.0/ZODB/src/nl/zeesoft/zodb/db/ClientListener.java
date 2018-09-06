package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.ZStringBuilder;

public interface ClientListener {
	public void handledRequest(DatabaseResponse res, ZStringBuilder err, Exception e);
}
