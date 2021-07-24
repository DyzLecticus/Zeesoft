package nl.zeesoft.zdk.app.neural.resources;

import nl.zeesoft.zdk.app.resource.Resource;

public class HotGymJs extends Resource {
	private static String	SOURCE_URL	= "https://raw.githubusercontent.com/numenta/nupic/master/examples/opf/clients/hotgym/prediction/one_gym/rec-center-hourly.csv";
	
	@Override
	protected void render(StringBuilder r) {
		append(r, "var hotGym = hotGym || {};");
		renderLoadData(r);
		renderParseData(r);
		renderParseDateTime(r);
	}
	
	protected void renderLoadData(StringBuilder r) {
		append(r, "hotGym.loadData = () => {");
		append(r, "    var request = new HttpRequest(\"GET\",\"" + SOURCE_URL +"\");");
		append(r, "    request.headers = {};");
		append(r, "    request.execute((xhr) => {");
		append(r, "        var json = hotGym.parseData(xhr.response);");
		append(r, "        console.log(json);");
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
		append(r, "    r.setHours(parseInt(hm[0],10));");
		append(r, "    r.setMinutes(parseInt(hm[1],10));");
		append(r, "    r.setSeconds(0);");
		append(r, "    return r;");
		append(r, "};");
	}
}
