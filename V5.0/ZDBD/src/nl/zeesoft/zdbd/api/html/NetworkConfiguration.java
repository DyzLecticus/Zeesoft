package nl.zeesoft.zdbd.api.html;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.network.NetworkConfig;

public class NetworkConfiguration extends ResponseObject {
	@Override
	public Str render() {
		NetworkConfig config = NetworkConfigFactory.getNetworkConfig();

		Str desc = config.getDescription();
		desc.replace(" ", "&nbsp;");
		desc.replace("\n", "<br />");
		
		Str r = new Str();
		append(r,"<div>");
		append(r,desc);
		append(r,"</div>");
		
		append(r,FormHtml.renderOkCancel("","","Done","modal.hide();"));
		return r;
	}
}