package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.resource.Resource;

public class ApiJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		append(r, (new DomJs()).render());
		append(r, (new HttpRequestJs()).render());
		append(r, (new UtilJs()).render());
		append(r, (new ChangePublisherJs()).render());
		append(r, (new ObjectLoaderJs()).render());
		append(r, (new ApiObjectLoadersJs()).render());
		renderNetworkApiJs(r);
	}
	
	protected void renderNetworkApiJs(StringBuilder r) {
		append(r, (new SdrJs()).render());
		append(r, (new NetworkConfigJs()).render());
		append(r, (new NetworkSettingsJs()).render());
		append(r, (new NetworkStatsJs()).render());
		append(r, (new NetworkIOJs()).render());
	}
}
