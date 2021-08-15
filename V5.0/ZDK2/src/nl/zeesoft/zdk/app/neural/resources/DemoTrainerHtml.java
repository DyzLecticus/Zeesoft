package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.neural.handlers.DemoTrainerJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;

public class DemoTrainerHtml extends HtmlResource {
	protected static String	SOURCE_URL	= "https://raw.githubusercontent.com/numenta/nupic/master/examples/opf/clients/hotgym/prediction/one_gym/rec-center-hourly.csv";
	
	public DemoTrainerHtml() {
		this.title = "NeuralServer - Demo trainer";
		onload = "demoTrainer.loadApp();";
		scriptFiles.add(IndexJsHandler.PATH);
		scriptFiles.add(DemoTrainerJsHandler.PATH);
		styleFiles.add(IndexCssHandler.PATH);
	}
	
	@Override
	protected StringBuilder renderBody() {
		StringBuilder body = super.renderBody();
		append(body, "<h1>Demo trainer</h1>");
		append(body, "<p>This page can be used to train the default neural network.<br />");
		append(body, "It expects a CSV file that is formatted and structured like the Numenta <a href='" + SOURCE_URL + "' target='_blank'>Hot Gym example</a>.<br />");
		append(body, "<p>Network state: <b><span id=\"networkStateText\"></span></b></p>");
		append(body, "<hr />");
		append(body, "<p>");
		renderInputs(body);
		append(body, "&nbsp;<span id=\"networkTrainingStateText\" />");
		append(body, "</p>");
		renderNetworkIO(body);
		renderNetworkIOHist(body);
		return body;
	}
	
	protected void renderInputs(StringBuilder body) {
		append(body, "Source data URL <input id='loadDataUrl' class='long' type='text' value='" + SOURCE_URL + "' /><br />");
		append(body, "<input id='loadDataButton' type='button' value='Load' onclick='demoTrainer.loadData(this);'/>");
		append(body, "<input id='trainNetworkButton' type='button' value='Train' onclick='demoTrainer.trainNetwork();' DISABLED/>");
		append(body, "<input id='pauzeNetworkTrainingButton' type='button' value='Pauze' onclick='demoTrainer.pauzeNetworkTraining();' DISABLED/>");
	}
	
	protected void renderNetworkIO(StringBuilder body) {
		append(body, getAccordion("networkIOAccordion","Primary output SDRs","<div id=\"networkIO\" class=\"x-scrollable\"></div>"));
	}
	
	protected void renderNetworkIOHist(StringBuilder body) {
		append(body, getAccordion("networkIOHistAccordion","Predictions","<div id=\"networkIOHist\" class=\"x-scrollable\"></div>"));
	}
}
