package nl.zeesoft.zdk.neural.network.analyzer;

import nl.zeesoft.zdk.json.JsonTransient;

@JsonTransient
public class IOAccuracyCalc {
	protected NetworkIOAccuracy 	accuracy			= null;
	protected NetworkIOAnalyzer		analyzer			= null;
	protected int					start				= 0;
	protected int					end					= 0;
	protected boolean				useAvgPrediction	= true;
	
	protected IOAccuracyCalc(NetworkIOAccuracy accuracy, NetworkIOAnalyzer analyzer, int start, int end, boolean useAvgPrediction) {
		this.accuracy = accuracy;
		this.analyzer = analyzer;
		this.start = start;
		this.end = end;
		this.useAvgPrediction = useAvgPrediction;
	}
}
