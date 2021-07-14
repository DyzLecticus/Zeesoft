package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.resource.Resource;

public class IndexCss extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderBody(r);
		renderTablePadding(r);
		renderHidden(r);
		renderClickable(r);
		renderXScrollable(r);
	}

	protected void renderBody(StringBuilder r) {
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
	
	protected void renderTablePadding(StringBuilder r) {
		append(r, "table.padded tr td {");
		append(r, "    padding: 4px;");
		append(r, "}");
		append(r, "table.padded tr th {");
		append(r, "    padding: 4px;");
		append(r, "}");
	}
	
	protected void renderHidden(StringBuilder r) {
		append(r,".hidden {");
		append(r,"    display: none;");
		append(r,"    visibility: hidden;");
		append(r,"}");
	}
	
	protected void renderClickable(StringBuilder r) {
		append(r,".clickable {");
		append(r,"    cursor: pointer;");
		append(r,"}");
	}
	
	protected void renderXScrollable(StringBuilder r) {
		append(r,".x-scrollable {");
		append(r,"    overflow-x: auto;");
		append(r,"}");
	}
}
