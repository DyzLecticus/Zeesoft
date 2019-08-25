package nl.zeesoft.zenn.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;

public class CssZENN {
	public ZStringBuilder css = new ZStringBuilder();
	
	public CssZENN() {
		css.append(".envTable {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: separate;\n");
		css.append("    border-spacing: 0px;\n");
		css.append("}\n");
		css.append(".envTr {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: separate;\n");
		css.append("    border-spacing: 0px;\n");
		css.append("}\n");
		css.append(".envTd {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: separate;\n");
		css.append("    border-spacing: 0px;\n");
		css.append("}\n");
		
		/*
		css.append("table, th, td {\n");
		css.append("    border: 1px solid black;\n");
		css.append("    border-collapse: separate;\n");
		css.append("    border-spacing: 0px;\n");
		css.append("}\n");
		*/

		css.append("@media (max-width: 400px) and (max-height: 400px) {\n");
		css.append("    .envTable {\n");
		css.append("        width: 220px;\n");
		css.append("        height: 220px;\n");
		css.append("    }\n");
		css.append("    .envTd {\n");
		css.append("        padding: 0px;\n");
		css.append("        width: 5px;\n");
		css.append("        height: 5px;\n");
		css.append("    }\n");
		css.append("}\n");

		css.append("@media (min-width: 401px) and (min-height: 401px) {\n");
		css.append("    .envTable {\n");
		css.append("        width: 344px;\n");
		css.append("        height: 344px;\n");
		css.append("    }\n");
		css.append("    .envTd {\n");
		css.append("        padding: 0px;\n");
		css.append("        width: 8px;\n");
		css.append("        height: 8px;\n");
		css.append("    }\n");
		css.append("}\n");

		css.append("@media (min-width: 541px) and (min-height: 541px) {\n");
		css.append("    .envTable {\n");
		css.append("        width: 440px;\n");
		css.append("        height: 440px;\n");
		css.append("    }\n");
		css.append("    .envTd {\n");
		css.append("        padding: 0px;\n");
		css.append("        width: 10px;\n");
		css.append("        height: 10px;\n");
		css.append("    }\n");
		css.append("}\n");

		css.append("@media (min-width: 740px) and (min-height: 740px) {\n");
		css.append("    .envTable {\n");
		css.append("        width: 660px;\n");
		css.append("        height: 660px;\n");
		css.append("    }\n");
		css.append("    .envTd {\n");
		css.append("        padding: 0px;\n");
		css.append("        width: 15px;\n");
		css.append("        height: 15px;\n");
		css.append("    }\n");
		css.append("}\n");
		
		css.append(".red {\n");
		css.append("    background-color: red;\n");
		css.append("}\n");
		css.append(".green {\n");
		css.append("    background-color: green;\n");
		css.append("}\n");
		css.append(".lime {\n");
		css.append("    background-color: lime;\n");
		css.append("}\n");
		css.append(".yellow {\n");
		css.append("    background-color: yellow;\n");
		css.append("}\n");
		css.append(".blue {\n");
		css.append("    background-color: blue;\n");
		css.append("}\n");
		css.append(".purple {\n");
		css.append("    background-color: purple;\n");
		css.append("}\n");
		css.append(".black {\n");
		css.append("    background-color: black;\n");
		css.append("}\n");

		css.append(".redBox {\n");
		css.append("    border: 1px solid red;\n");
		css.append("}\n");
		css.append(".greenBox {\n");
		css.append("    border: 1px solid green;\n");
		css.append("}\n");
		css.append(".limeBox {\n");
		css.append("    border: 1px solid lime;\n");
		css.append("}\n");
		css.append(".yellowBox {\n");
		css.append("    border: 1px solid yellow;\n");
		css.append("}\n");
		css.append(".blueBox {\n");
		css.append("    border: 1px solid blue;\n");
		css.append("}\n");
		css.append(".purpleBox {\n");
		css.append("    border: 1px solid purple;\n");
		css.append("}\n");
		css.append(".greyBox {\n");
		css.append("    border: 1px solid grey;\n");
		css.append("}\n");
		css.append(".whiteBox {\n");
		css.append("    border: 1px solid white;\n");
		css.append("}\n");
	}
}
