package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;

public class NetworkTrainer {
	public Network				network					= null;
	public List<NetworkIO>		trainingSet				= new ArrayList<NetworkIO>();
	
	public NetworkIOAnalyzer	analyzer				= new NetworkIOAnalyzer();
	
	public int					enableTemporalMemory	= 1000;
	public List<Integer>		temporalMemoryLayers	= new ArrayList<Integer>();
	public int					enableClassifiers		= 2000;
	public List<Integer>		classifierLayers		= new ArrayList<Integer>();
	public int					maximumIO				= 3000;
	
	public float				minimumAverageAccuracy	= 0.99F;
	public boolean				stopIfAccurate			= true;
	
	public int					processed				= 0;

	public NetworkTrainer(Network network) {
		this.network = network;
	}
	
	public void trainNetwork() {
		disableTemporalMemoryAndClassifiers();
		for (NetworkIO io: trainingSet) {
			network.processIO(io);
			if (processedIO(io)) {
				break;
			} else {
				checkEnableTemporalMemoryAndClassifiers();
			}
		}
	}
	
	protected boolean processedIO(NetworkIO io) {
		boolean r = false;
		analyzer.add(io);
		processed++;
		if ((processed>=maximumIO) ||
			(minimumAverageAccuracy>0F && analyzer.getAccuracy().average>=minimumAverageAccuracy)
			) {
			r = true;
		}
		return r;
	}

	protected void checkEnableTemporalMemoryAndClassifiers() {
		if (processed==enableTemporalMemory) {
			toggleLearn(temporalMemoryLayers, true);
		}
		if (processed==enableClassifiers) {
			toggleLearn(classifierLayers, true);
		}
	}
	
	protected void disableTemporalMemoryAndClassifiers() {
		toggleLearn(temporalMemoryLayers, false);
		toggleLearn(classifierLayers, false);
	}
	
	protected void toggleLearn(List<Integer> layers, boolean learn) {
		for (Integer layer: layers) {
			network.setLearn(layer, Network.ALL_PROCESSORS, learn);
		}
	}
}
