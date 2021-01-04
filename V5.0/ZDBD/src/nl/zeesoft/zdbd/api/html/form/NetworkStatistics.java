package nl.zeesoft.zdbd.api.html.form;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.model.ModelStatistics;

public class NetworkStatistics extends FormHtml {
	private static DecimalFormat	df	= new DecimalFormat("0.00");
	
	public NetworkStatistics(ModelStatistics statistics, float accuracy) {
		if (statistics==null) {
			statistics = new ModelStatistics();
		}
		addProperty("cells", "Cells", statistics.cells, FormProperty.TEXT);
		addProperty("proximalSegments", "Proximal segments", statistics.proximalSegments, FormProperty.TEXT);
		addProperty("proximalSynapses", "Proximal synapses", statistics.proximalSynapses, FormProperty.TEXT);
		addProperty("distalSegments", "Distal segments", statistics.distalSegments, FormProperty.TEXT);
		addProperty("distalSynapses", "Distal synapses", statistics.distalSynapses, FormProperty.TEXT);
		if (accuracy>0) {
			Str acc = new Str();
			acc.sb().append(df.format(accuracy * 100));
			acc.sb().append("%");
			addProperty("accuracy", "Accuracy", acc, FormProperty.TEXT);
		}
	}
}
