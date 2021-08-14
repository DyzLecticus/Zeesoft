package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.neural.handlers.DemoTrainerJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;

public class DemoTrainerHtml extends HtmlResource {
	public DemoTrainerHtml() {
		this.title = "Zeesoft NeuralServer - Example trainer";
		onload = "demoTrainer.loadApp();";
		scriptFiles.add(IndexJsHandler.PATH);
		scriptFiles.add(DemoTrainerJsHandler.PATH);
		styleFiles.add(IndexCssHandler.PATH);
	}
	
	@Override
	protected StringBuilder renderBody() {
		StringBuilder body = super.renderBody();
		append(body, "<h1>Demo trainer</h1>");
		append(body, "<p>This page can be used to train the neural network using the Numenta Hot Gym example <a href='" + DemoTrainerJs.SOURCE_URL + "' target='_blank'>data</a>.</p>");
		append(body, "<p>Network state: <b><span id=\"networkStateText\"></span></b></p>");
		append(body, "<hr />");
		append(body, "<p>");
		append(body, "<input id='trainNetworkButton' type='button' value='Train' onclick='demoTrainer.trainNetwork();' DISABLED/>");
		append(body, "<input id='pauzeNetworkTrainingButton' type='button' value='Pauze' onclick='demoTrainer.pauzeNetworkTraining();' DISABLED/>");
		append(body, "&nbsp;<span id=\"networkTrainingStateText\" />");
		append(body, "</p>");
		renderNetworkIO(body);
		renderNetworkIOHist(body);
		return body;
	}
	
	protected void renderNetworkIO(StringBuilder body) {
		append(body, getAccordion("networkIOAccordion","Primary output SDRs","<div id=\"networkIO\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderNetworkIOHist(StringBuilder body) {
		append(body, getAccordion("networkIOHistAccordion","Predictions","<div id=\"networkIOHist\" class=\"x-scrollable\"></div>"));
	}
}
