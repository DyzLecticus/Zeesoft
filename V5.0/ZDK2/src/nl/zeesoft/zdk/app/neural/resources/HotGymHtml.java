package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.neural.handlers.HotGymJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;

public class HotGymHtml extends HtmlResource {
	public HotGymHtml() {
		this.title = "Zeesoft NeuralServer - Hot Gym";
		onload = "loadApp(); hotGym.loadData();";
		scriptFiles.add(IndexJsHandler.PATH);
		scriptFiles.add(HotGymJsHandler.PATH);
		styleFiles.add(IndexCssHandler.PATH);
	}
	
	@Override
	protected StringBuilder renderBody() {
		StringBuilder body = super.renderBody();
		append(body, "<h1>Hot gym</h1>");
		append(body, "<p>This page can be used to train the neural network using the Numenta Hot Gym example <a href='" + HotGymJs.SOURCE_URL + "'>data</a>.</p>");
		append(body, "<p>Network state: <b><span id=\"networkStateText\" /></b></p>");
		append(body, "<hr />");
		append(body, "<p>");
		append(body, "<input id='trainNetworkButton' type='button' value='Train' onclick='hotGym.trainNetwork();' DISABLED/>");
		append(body, "<input id='pauzeNetworkTrainingButton' type='button' value='Pauze' onclick='hotGym.pauzeNetworkTraining();' DISABLED/>");
		append(body, "&nbsp;<span id=\"networkTrainingStateText\" />");
		append(body, "</p>");
		renderNetworkIO(body);
		return body;
	}
	
	protected void renderNetworkIO(StringBuilder body) {
		append(body, getAccordion("networkIOAccordion","Input/output","<div id=\"networkIO\" class=\"x-scrollable\"></div>"));
	}
}
