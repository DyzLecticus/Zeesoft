package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.resource.Resource;

public class DemoTrainerJs extends Resource {
	protected static String	SOURCE_URL	= "https://raw.githubusercontent.com/numenta/nupic/master/examples/opf/clients/hotgym/prediction/one_gym/rec-center-hourly.csv";
	
	@Override
	protected void render(StringBuilder r) {
		renderObject(r);
		renderLoadApp(r);
		renderLoadData(r);
		renderParseData(r);
		renderParseDateTime(r);
		renderTrainNetwork(r);
		renderPauzeNetworkTraining(r);
		renderTrainNetworkRequest(r);
		renderProcessedNetworkIO(r);
		renderGetNewNetworkIO(r);
	}
	
	protected void renderObject(StringBuilder r) {
		append(r, "var demoTrainer = demoTrainer || {};");
		append(r, "demoTrainer.data = { inputs: [] };");
		append(r, "demoTrainer.trainingIO = 0;");
		append(r, "demoTrainer.pauze = false;");
	}
	
	protected void renderLoadApp(StringBuilder r) {
		append(r, "demoTrainer.loadApp = () => {");
		append(r, "    loadApp();");
		append(r, "    demoTrainer.loadData();");
		append(r, "    dom.setInnerHTML(\"networkIOHist\", networkIOHist.toHtmlTable());");
		append(r, "};");
	}
	
	protected void renderLoadData(StringBuilder r) {
		append(r, "demoTrainer.loadData = () => {");
		append(r, "    var request = new HttpRequest(\"GET\",\"" + SOURCE_URL +"\");");
		append(r, "    request.headers = {};");
		append(r, "    request.execute((xhr) => {");
		append(r, "        var json = demoTrainer.parseData(xhr.response);");
		append(r, "        demoTrainer.data = json;");
		append(r, "        dom.setDisabled(\"trainNetworkButton\", false);");
		append(r, "        changePublisher.setValue(\"demoTrainerData\", json);");
		append(r, "    });");
		append(r, "};");
	}
	
	protected void renderParseData(StringBuilder r) {
		append(r, "demoTrainer.parseData = (csv) => {");
		append(r, "    var inputs = [];");
		append(r, "    var lines = csv.split(\"\\n\");");
		append(r, "    for (var i = 3; i < lines.length; i++) {");
		append(r, "        var dtv = lines[i].split(\",\");");
		append(r, "        if (dtv.length==2) {");
		append(r, "            inputs[inputs.length] = {");
		append(r, "                dateTime: demoTrainer.parseDateTime(dtv[0]),");
		append(r, "                value: parseFloat(dtv[1])");
		append(r, "            };");
		append(r, "        }");
		append(r, "    }");
		append(r, "    return { inputs };");
		append(r, "};");
	}
	
	protected void renderParseDateTime(StringBuilder r) {
		append(r, "demoTrainer.parseDateTime = (dateTimeStr) => {");
		append(r, "    var r = new Date();");
		append(r, "    var dt = dateTimeStr.split(\" \");");
		append(r, "    var mdy = dt[0].split(\"/\");");
		append(r, "    if (mdy[2].length==2) {;");
		append(r, "        mdy[2] = \"20\" + mdy[2];");
		append(r, "    }");
		append(r, "    var hm = dt[1].split(\":\");");
		append(r, "    r.setMonth(parseInt(mdy[0],10) - 1);");
		append(r, "    r.setDate(parseInt(mdy[1],10));");
		append(r, "    r.setFullYear(parseInt(mdy[2],10));");
		append(r, "    r.setHours(parseInt(hm[0],10),parseInt(hm[1],10),0,0);");
		append(r, "    return r;");
		append(r, "};");
	}
	
	protected void renderTrainNetwork(StringBuilder r) {
		append(r, "demoTrainer.trainNetwork = () => {");
		append(r, "    if (demoTrainer.data.inputs.length>0) {");
		append(r, "        demoTrainer.pauze = false;");
		append(r, "        dom.setDisabled(\"trainNetworkButton\", true);");
		append(r, "        dom.setDisabled(\"pauzeNetworkTrainingButton\", false);");
		append(r, "        demoTrainer.trainNetworkRequest();");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderPauzeNetworkTraining(StringBuilder r) {
		append(r, "demoTrainer.pauzeNetworkTraining = () => {");
		append(r, "    demoTrainer.pauze = true;");
		append(r, "    dom.setDisabled(\"pauzeNetworkTrainingButton\", true);");
		append(r, "};");
	}
	
	protected void renderTrainNetworkRequest(StringBuilder r) {
		append(r, "demoTrainer.trainNetworkRequest = () => {");
		append(r, "    if (demoTrainer.trainingIO % 10 == 0) {");
		append(r, "        var perc = Math.round((demoTrainer.trainingIO / demoTrainer.data.inputs.length) * 100);");
		append(r, "        var html = demoTrainer.trainingIO + \" / \" + demoTrainer.data.inputs.length + \" (\" + perc + \"%)\";");
		append(r, "        dom.setInnerHTML(\"networkTrainingStateText\", html);");
		append(r, "    }");
		append(r, "    var input = demoTrainer.data.inputs[demoTrainer.trainingIO];");
		append(r, "    demoTrainer.getNewNetworkIO(input).execute(demoTrainer.handleTrainNetworkResponse);");
		append(r, "};");
	}

	protected void renderProcessedNetworkIO(StringBuilder r) {
		append(r, "changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "    if (key == \"networkIO\") {");
		append(r, "        demoTrainer.processedNetworkIO(newValue);");
		append(r, "    }");
		append(r, "});");
		append(r, "demoTrainer.processedNetworkIO = () => {");
		append(r, "    demoTrainer.trainingIO++;");
		append(r, "    if (!demoTrainer.pauze && demoTrainer.trainingIO < demoTrainer.data.inputs.length) {");
		append(r, "        demoTrainer.trainNetworkRequest();");
		append(r, "    } else {");
		append(r, "        if (!demoTrainer.pauze) {");
		append(r, "            demoTrainer.trainingIO = 0;");
		append(r, "            dom.setInnerHTML(\"networkTrainingStateText\",\"\");");
		append(r, "            dom.setDisabled(\"pauzeNetworkTrainingButton\", true);");
		append(r, "        }");
		append(r, "        dom.setDisabled(\"trainNetworkButton\", false);");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderGetNewNetworkIO(StringBuilder r) {
		append(r, "demoTrainer.getNewNetworkIO = (input) => {");
		append(r, "    var io = new NetworkIO();");
		append(r, "    io.json.inputs.keyValues[0].value.value = input.dateTime.getTime();");
		append(r, "    io.json.inputs.keyValues[1].value.value = input.value;");
		append(r, "    return io;");
		append(r, "};");
	}
}
