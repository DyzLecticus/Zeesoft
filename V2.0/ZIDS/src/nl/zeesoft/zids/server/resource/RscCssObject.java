package nl.zeesoft.zids.server.resource;

public abstract class RscCssObject extends RscObject {
	@Override
	public String getContentType() {
		return "text/css";
	}
}
