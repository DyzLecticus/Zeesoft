package nl.zeesoft.zsds.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class BaseStyleSheet {
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder script = new ZStringBuilder();
		
		script.append("textarea {\n");
		script.append("    width: 100%;\n");
		script.append("    height: 400px;\n");
		script.append("    white-space: pre;\n");
		script.append("    overflow-wrap: normal;\n");
		script.append("    overflow-x: scroll;\n");
		script.append("}\n");
		
		return script;
	}
}
