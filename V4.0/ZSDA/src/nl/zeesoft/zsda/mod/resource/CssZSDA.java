package nl.zeesoft.zsda.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class CssZSDA {
	public ZStringBuilder css = new ZStringBuilder();
	
	public CssZSDA() {
		css.append(".grid {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: separate;\n");
		css.append("    border-spacing: 0px;\n");
		css.append("}\n");

		css.append(".sdrTable {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: separate;\n");
		css.append("    border-spacing: 0px;\n");
		css.append("}\n");
		css.append(".sdrTr {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: separate;\n");
		css.append("    border-spacing: 0px;\n");
		css.append("}\n");
		css.append(".sdrTd {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: separate;\n");
		css.append("    border-spacing: 0px;\n");
		css.append("    padding: 0px;\n");
		css.append("    width: 10px;\n");
		css.append("    height: 10px;\n");
		css.append("    font-size: 1px;\n");
		css.append("}\n");
		
		css.append(".blue {\n");
		css.append("    background-color: blue;\n");
		css.append("    color: blue;\n");
		css.append("}\n");
		css.append(".white {\n");
		css.append("    background-color: white;\n");
		css.append("    color: white;\n");
		css.append("}\n");
	}
}
