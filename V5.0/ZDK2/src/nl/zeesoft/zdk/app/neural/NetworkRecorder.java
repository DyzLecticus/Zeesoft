package nl.zeesoft.zdk.app.neural;

import java.util.List;

import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.analyzer.NetworkIOAccuracy;
import nl.zeesoft.zdk.neural.network.analyzer.NetworkIOAnalyzer;
import nl.zeesoft.zdk.neural.network.analyzer.NetworkIOStats;

public class NetworkRecorder {
	protected NetworkIOAnalyzer		analyzer	= new NetworkIOAnalyzer();
	
	public synchronized void reset() {
		analyzer.clear();
	}
	
	public synchronized List<NetworkIO> getNetworkIO() {
		return analyzer.getNetworkIO();
	}
	
	public synchronized NetworkIOAccuracy getAccuracy(int max) {
		return analyzer.getAccuracy(max, true);
	}
	
	public synchronized NetworkIOStats getAverageStats() {
		return analyzer.getAverageStats();
	}
	
	public synchronized void add(NetworkIO io) {
		analyzer.add(io);
	}
}
