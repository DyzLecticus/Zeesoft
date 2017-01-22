package nl.zeesoft.zids.server.resource;

public class RscZIDSCss extends RscCssObject {
	@Override
	public StringBuilder toStringBuilder() {
		StringBuilder css = new StringBuilder();
		css.append(".textInput {\n");
		css.append("    width: 100%;\n");
		css.append("}\n");
		css.append("textarea {\n");
		css.append("    width: 100%;\n");
		css.append("    height: 200px;\n");
		css.append("}\n");
		return css;
	}
}
