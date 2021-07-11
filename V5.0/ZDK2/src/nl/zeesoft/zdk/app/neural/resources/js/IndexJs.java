package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class IndexJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, (new HttpRequestJs()).render());
		
		append(r, "var loadState = (callback) => {");
		append(r, "    var request = new HttpRequest(\"GET\",\"/app/state.txt\");");
		append(r, "    request.execute((xhr) => { callback(xhr.response); });");
		append(r, "};");
	}
}
