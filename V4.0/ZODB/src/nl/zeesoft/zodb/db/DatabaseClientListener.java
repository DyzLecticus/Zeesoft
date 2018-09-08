package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.ZStringBuilder;

public interface DatabaseClientListener {
	public void handledRequest(DatabaseResponse res, ZStringBuilder err, Exception ex);
}
