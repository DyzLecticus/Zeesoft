package nl.zeesoft.zdk.app.resource;

import nl.zeesoft.zdk.str.StrUtil;

public abstract class Resource {
	public StringBuilder render() {
		StringBuilder r = new StringBuilder();
		render(r);
		return r;
	}
	
	protected abstract void render(StringBuilder r);
	
	protected static void append(StringBuilder r, String line) {
		StrUtil.appendLine(r, line);
	}
	
	protected static void append(StringBuilder r, StringBuilder content) {
		StrUtil.appendLine(r, content);
	}
}
