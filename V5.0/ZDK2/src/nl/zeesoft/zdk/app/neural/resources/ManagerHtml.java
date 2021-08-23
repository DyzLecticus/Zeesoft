package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.ManagerJsHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;

public class ManagerHtml extends HtmlResource {
	public ManagerHtml() {
		this.title = "NeuralServer - Manager";
		onload = "manager.loadApp();";
		scriptFiles.add(IndexJsHandler.PATH);
		scriptFiles.add(ManagerJsHandler.PATH);
		styleFiles.add(IndexCssHandler.PATH);
	}
	
	@Override
	protected StringBuilder renderBody() {
		StringBuilder body = super.renderBody();
		append(body, "<h1>Manager</h1>");
		append(body, "<p>This page can be used to manage the neural network.</p>");
		append(body, "<p>Network state: <b><span id=\"networkStateText\"></span></b>");
		append(body, "<br /><input type=\"button\" value=\"Reset network\" onclick=\"manager.reset();\" />");
		append(body, "</p>");
		append(body, "<hr />");
		append(body, "<p>");
		append(body, "<h2>Configuration</h2>");
		append(body, "<textarea id=\"configEditor\" spellcheck=\"false\"></textarea><br />");
		append(body, "<input type=\"button\" value=\"Get\" onclick=\"networkConfigLoader.refresh({});\" />");
		append(body, "<input type=\"button\" value=\"Post\" onclick=\"manager.post();\" />");
		append(body, "</p>");
		return body;
	}
}
