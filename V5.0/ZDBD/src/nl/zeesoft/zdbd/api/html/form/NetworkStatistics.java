package nl.zeesoft.zdbd.api.html.form;

import nl.zeesoft.zdk.neural.model.ModelStatistics;

public class NetworkStatistics extends FormHtml {
	public NetworkStatistics(ModelStatistics statistics) {
		if (statistics==null) {
			statistics = new ModelStatistics();
		}
		addProperty("cells", "Cells", statistics.cells, FormProperty.TEXT);
		addProperty("proximalSegments", "Proximal segments", statistics.proximalSegments, FormProperty.TEXT);
		addProperty("proximalSynapses", "Proximal synapses", statistics.proximalSynapses, FormProperty.TEXT);
		addProperty("distalSegments", "Distal segments", statistics.distalSegments, FormProperty.TEXT);
		addProperty("distalSynapses", "Distal synapses", statistics.distalSynapses, FormProperty.TEXT);
	}
}
