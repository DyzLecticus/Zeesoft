package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class BaseStyleSheet {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		script.append("textarea {\n");
		script.append("    width: 100%;\n");
		script.append("    height: 200px;\n");
		script.append("}\n");
		
		return script;
	}
}
