package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.resource.Resource;

public class IndexCss extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, "body {");
		append(r, "    font-family: Tahoma, sans-serif;");
		append(r, "    font-size: 16px;");
		append(r, "    background-color: #DEDEDE;");
		append(r, "}");
		append(r, "input, select {");
		append(r, "    font-family: inherit;");
		append(r, "    font-size: 16px;");
		append(r, "}");
	}
}
