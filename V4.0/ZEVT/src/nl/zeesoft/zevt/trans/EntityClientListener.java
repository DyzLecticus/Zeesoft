package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringBuilder;

public interface EntityClientListener {
	public void handledRequest(EntityRequestResponse response, ZStringBuilder err, Exception ex);
}
