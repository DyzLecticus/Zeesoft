package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.resource.Resource;

public class HotGymJs extends Resource {
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
		append(r, "var hotGym = hotGym || {};");
		append(r, "hotGym.data = { inputs: [] };");
		append(r, "hotGym.trainingIO = 0;");
		append(r, "hotGym.pauze = false;");
	}
	
	protected void renderLoadApp(StringBuilder r) {
		append(r, "hotGym.loadApp = () => {");
		append(r, "    loadApp();");
		append(r, "    hotGym.loadData();");
		append(r, "    dom.setInnerHTML(\"networkIOHist\", networkIOHist.toHtmlTable());");
		append(r, "};");
	}
	
	protected void renderLoadData(StringBuilder r) {
		append(r, "hotGym.loadData = () => {");
		append(r, "    var request = new HttpRequest(\"GET\",\"" + SOURCE_URL +"\");");
		append(r, "    request.headers = {};");
		append(r, "    request.execute((xhr) => {");
		append(r, "        var json = hotGym.parseData(xhr.response);");
		append(r, "        hotGym.data = json;");
		append(r, "        dom.setDisabled(\"trainNetworkButton\", false);");
		append(r, "        changePublisher.setValue(\"hotGymData\", json);");
		append(r, "    });");
		append(r, "};");
	}
	
	protected void renderParseData(StringBuilder r) {
		append(r, "hotGym.parseData = (csv) => {");
		append(r, "    var inputs = [];");
		append(r, "    var lines = csv.split(\"\\n\");");
		append(r, "    for (var i = 3; i < lines.length; i++) {");
		append(r, "        var dtv = lines[i].split(\",\");");
		append(r, "        if (dtv.length==2) {");
		append(r, "            inputs[inputs.length] = {");
		append(r, "                dateTime: hotGym.parseDateTime(dtv[0]),");
		append(r, "                value: parseFloat(dtv[1])");
		append(r, "            };");
		append(r, "        }");
		append(r, "    }");
		append(r, "    return { inputs };");
		append(r, "};");
	}
	
	protected void renderParseDateTime(StringBuilder r) {
		append(r, "hotGym.parseDateTime = (dateTimeStr) => {");
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
		append(r, "hotGym.trainNetwork = () => {");
		append(r, "    if (hotGym.data.inputs.length>0) {");
		append(r, "        hotGym.pauze = false;");
		append(r, "        dom.setDisabled(\"trainNetworkButton\", true);");
		append(r, "        dom.setDisabled(\"pauzeNetworkTrainingButton\", false);");
		append(r, "        hotGym.trainNetworkRequest();");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderPauzeNetworkTraining(StringBuilder r) {
		append(r, "hotGym.pauzeNetworkTraining = () => {");
		append(r, "    hotGym.pauze = true;");
		append(r, "    dom.setDisabled(\"pauzeNetworkTrainingButton\", true);");
		append(r, "};");
	}
	
	protected void renderTrainNetworkRequest(StringBuilder r) {
		append(r, "hotGym.trainNetworkRequest = () => {");
		append(r, "    var input = hotGym.data.inputs[hotGym.trainingIO];");
		append(r, "    var perc = Math.round((hotGym.trainingIO / hotGym.data.inputs.length) * 100);");
		append(r, "    var html = input.dateTime.toISOString() + \" = <b>\" + util.formatDecimal(input.value) + \"</b> (\" + perc + \"%)\";");
		append(r, "    dom.setInnerHTML(\"networkTrainingStateText\", html);");
		append(r, "    hotGym.getNewNetworkIO(input).execute(hotGym.handleTrainNetworkResponse);");
		append(r, "};");
	}

	protected void renderProcessedNetworkIO(StringBuilder r) {
		append(r, "changePublisher.addListener((key, oldValue, newValue) => {");
		append(r, "    if (key == \"networkIO\") {");
		append(r, "        hotGym.processedNetworkIO(newValue);");
		append(r, "    }");
		append(r, "});");
		append(r, "hotGym.processedNetworkIO = () => {");
		append(r, "    hotGym.trainingIO++;");
		append(r, "    if (!hotGym.pauze && hotGym.trainingIO < hotGym.data.inputs.length) {");
		append(r, "        setTimeout(() => { hotGym.trainNetworkRequest(); }, 10);");
		append(r, "    } else {");
		append(r, "        if (!hotGym.pauze) {");
		append(r, "            hotGym.trainingIO = 0;");
		append(r, "            dom.setInnerHTML(\"networkTrainingStateText\",\"\");");
		append(r, "        }");
		append(r, "        dom.setDisabled(\"trainNetworkButton\", false);");
		append(r, "    }");
		append(r, "};");
	}
	
	protected void renderGetNewNetworkIO(StringBuilder r) {
		append(r, "hotGym.getNewNetworkIO = (input) => {");
		append(r, "    var io = new NetworkIO();");
		append(r, "    io.json.inputs.keyValues[0].value.value = input.dateTime.getTime();");
		append(r, "    io.json.inputs.keyValues[1].value.value = input.value;");
		append(r, "    return io;");
		append(r, "};");
	}
}
