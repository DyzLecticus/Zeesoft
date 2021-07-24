package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.resource.Resource;

public class IndexCss extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderBody(r);
		renderLists(r);
		renderHidden(r);
		renderClickable(r);
		renderXScrollable(r);
		renderMarginPaddingZero(r);
		renderAccordionTitle(r);
		renderTablePadding(r);
		renderSdrTable(r);
		renderBgColors(r);
	}

	protected void renderBody(StringBuilder r) {
		append(r, "body {");
		append(r, "    font-family: Arial, Helvetica, sans-serif;");
		append(r, "    font-size: 1em;");
		append(r, "    background-color: #DEDEDE;");
		append(r, "}");
		append(r, "input, select {");
		append(r, "    font-family: inherit;");
		append(r, "    font-size: 1em;");
		append(r, "}");
		append(r, "td {");
		append(r, "    vertical-align: top;");
		append(r, "}");
	}

	protected void renderLists(StringBuilder r) {
		append(r, "ul, ol {");
		append(r, "    margin-top: 0;");
		append(r, "    margin-block-start: 0;");
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
	
	protected void renderMarginPaddingZero(StringBuilder r) {
		renderZero("padding", r);
		renderZero("margin", r);
	}
	
	protected void renderZero(String type, StringBuilder r) {
		String t = type.substring(0, 1);
		append(r,"." + t + "-0 { " + type + ": 0; }");
		if (type.equals("margin")) {
			append(r,"." + t + "t-0 { " + type + "-top: 0; " + type + "-block-start: 0 }");
			append(r,"." + t + "b-0 { " + type + "-bottom: 0; " + type + "-block-end: 0 }");
		} else {
			append(r,"." + t + "t-0 { " + type + "-top: 0; }");
			append(r,"." + t + "b-0 { " + type + "-bottom: 0 }");
		}
		append(r,"." + t + "l-0 { " + type + "-left: 0; }");
		append(r,"." + t + "r-0 { " + type + "-right: 0 }");
	}
	
	protected void renderAccordionTitle(StringBuilder r) {
		append(r, ".accordion-title {");
		append(r, "    padding-top: 0.5em;");
		append(r, "    padding-bottom: 0.5em;");
		append(r, "}");
	}
	
	protected void renderTablePadding(StringBuilder r) {
		append(r, "table.padded tr td {");
		append(r, "    padding-left: 0.5em;");
		append(r, "    padding-right: 0.5em;");
		append(r, "}");
		append(r, "table.padded tr th {");
		append(r, "    padding-left: 0.5em;");
		append(r, "    padding-right: 0.5em;");
		append(r, "}");
	}

	protected void renderSdrTable(StringBuilder r) {
		append(r, "table.sdr {\n");
		append(r, "    border: 1px solid grey;\n");
		append(r, "    border-collapse: separate;\n");
		append(r, "    border-spacing: 0px;\n");
		append(r, "}\n");
		append(r, "table.sdr tr {\n");
		append(r, "    border: 1px solid grey;\n");
		append(r, "    border-collapse: separate;\n");
		append(r, "    border-spacing: 0px;\n");
		append(r, "}\n");
		append(r, "table.sdr tr td {\n");
		append(r, "    border: 1px solid grey;\n");
		append(r, "    border-collapse: separate;\n");
		append(r, "    border-spacing: 0px;\n");
		append(r, "    padding: 0px;\n");
		append(r, "    width: 5px;\n");
		append(r, "    height: 5px;\n");
		append(r, "    font-size: 1px;\n");
		append(r, "}\n");
	}

	protected void renderBgColors(StringBuilder r) {
		append(r, ".bg-r {\n");
		append(r, "    background-color: red;\n");
		append(r, "}\n");
		append(r, ".bg-g {\n");
		append(r, "    background-color: green;\n");
		append(r, "}\n");
		append(r, ".bg-b {\n");
		append(r, "    background-color: blue;\n");
		append(r, "}\n");
		append(r, ".bg-w {\n");
		append(r, "    background-color: white;\n");
		append(r, "}\n");
	}
}
