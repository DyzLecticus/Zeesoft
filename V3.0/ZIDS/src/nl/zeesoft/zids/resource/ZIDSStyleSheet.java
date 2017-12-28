package nl.zeesoft.zids.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class ZIDSStyleSheet {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		script.append("textarea {\n");
		script.append("    width: 100%;\n");
		script.append("    height: 200px;\n");
		script.append("}\n");
		
		return script;
	}
}
