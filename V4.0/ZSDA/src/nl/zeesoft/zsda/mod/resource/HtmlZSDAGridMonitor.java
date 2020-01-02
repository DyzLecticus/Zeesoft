package nl.zeesoft.zsda.mod.resource;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModZODB;
import nl.zeesoft.zodb.mod.resource.HtmlResource;
import nl.zeesoft.zsda.mod.ModZSDA;

public class HtmlZSDAGridMonitor extends HtmlResource {
	public HtmlZSDAGridMonitor(Config config) {
		super(config);
	}
	@Override
	public ZStringBuilder toStringBuilder() {
		setTitle("ZSDA - Grid monitor");	

		getScriptFiles().add("../" + ModZODB.NAME + "/ZODB.js");
		getScriptFiles().add("../" + ModZSDA.NAME + "/gridConfig.js");
		getScriptFiles().add("../" + ModZSDA.NAME + "/gridMonitor.js");
		
		setOnload("ZSDA.monitor.onload();");
		
		ZStringBuilder html = new ZStringBuilder();
		html.append("<a href=\"./index.html\">Back to functions</a>");
		html.append("<hr />");
		
		html.append("<div id=\"columnIO\">\n");
		html.append("</div>\n");
		
		getBodyElements().add(html);
		
		return super.toStringBuilder();
	}
}
