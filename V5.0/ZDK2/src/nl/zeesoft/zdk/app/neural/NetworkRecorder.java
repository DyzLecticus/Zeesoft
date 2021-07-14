package nl.zeesoft.zdk.app.neural;

import java.util.List;

import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.NetworkIOAccuracy;
import nl.zeesoft.zdk.neural.network.NetworkIOAnalyzer;
import nl.zeesoft.zdk.neural.network.NetworkIOStats;

public class NetworkRecorder {
	protected NetworkIOAnalyzer		analyzer		= new NetworkIOAnalyzer();
	
	public synchronized void reset() {
		analyzer = new NetworkIOAnalyzer();
	}
	
	public synchronized List<NetworkIO> getNetworkIO() {
		return analyzer.getNetworkIO();
	}
	
	public synchronized NetworkIOAccuracy getAccuracy() {
		return analyzer.getAccuracy();
	}
	
	public synchronized NetworkIOStats getAverageStats() {
		return analyzer.getAverageStats();
	}
	
	public synchronized void add(NetworkIO io) {
		analyzer.add(io);
	}
}
