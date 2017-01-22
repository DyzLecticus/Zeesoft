package nl.zeesoft.zids.server.resource;

public abstract class RscJavaScriptObject extends RscObject {
	@Override
	public String getContentType() {
		return "text/javascript";
	}
}
