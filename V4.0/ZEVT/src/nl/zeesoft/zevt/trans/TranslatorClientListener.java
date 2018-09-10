package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringBuilder;

public interface TranslatorClientListener {
	public void handledRequest(TranslatorRequestResponse response, ZStringBuilder err, Exception ex);
}
