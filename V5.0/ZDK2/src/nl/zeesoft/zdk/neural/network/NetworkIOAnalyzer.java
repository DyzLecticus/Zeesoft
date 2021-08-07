package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.Classification;

public class NetworkIOAnalyzer {
	protected List<NetworkIO>	networkIO	= new ArrayList<NetworkIO>();
	
	public void add(NetworkIO io) {
		networkIO.add(io);
	}
	
	public List<NetworkIO> getNetworkIO() {
		return new ArrayList<NetworkIO>(networkIO);
	}
	
	public NetworkIOStats getAverageStats() {
		return getAverageStats(100);
	}
	
	public NetworkIOAverageStats getAverageStats(int max) {
		if (max<=0) {
			max = networkIO.size();
		}
		NetworkIOAverageStats r = new NetworkIOAverageStats();
		r.nsPerLayer = new TreeMap<Integer,Long>();
		r.recorded = networkIO.size();
		int total = 0;
		List<NetworkIOStats> stats = getRecentStats(max);
		for (NetworkIOStats stat: stats) {
			total++;
			addStats(r, stat);
		}
		divideStats(r, total);
		return r;
	}
	
	public void clear() {
		networkIO.clear();;
	}
	
	public NetworkIOAccuracy getAccuracy() {
		return getAccuracy(100);
	}
	
	public NetworkIOAccuracy getAccuracy(int max) {
		return new NetworkIOAccuracy(this, max);
	}
	
	protected Classification getPrediction(int pIndex, String name) {
		Classification r = null;
		if (pIndex>=0) {
			NetworkIO prevIO = networkIO.get(pIndex); 
			ProcessorIO ppio = prevIO.getProcessorIO(name);
			if (ppio!=null && ppio.outputValue instanceof Classification) {
				r = (Classification) ppio.outputValue;
			}
		}
		return r;
	}
	
	protected void addStats(NetworkIOStats base, NetworkIOStats add) {
		base.totalNs += add.totalNs;
		List<Integer> keySet = new ArrayList<Integer>(add.nsPerLayer.keySet());
		for (Integer layer: keySet) {
			Long ns = new Long(0);
			if (base.nsPerLayer.containsKey(layer)) {
				ns = base.nsPerLayer.get(layer);
			}
			ns += add.nsPerLayer.get(layer);
			base.nsPerLayer.put(layer, ns);
		}
	}
	
	protected void divideStats(NetworkIOStats stats, int total) {
		if (total>0) {
			stats.totalNs = stats.totalNs / total;
			for (Integer layer: stats.nsPerLayer.keySet()) {
				Long ns = stats.nsPerLayer.get(layer);
				stats.nsPerLayer.put(layer, (ns / total));
			}
		}
	}

	protected List<NetworkIOStats> getRecentStats(int max) {
		List<NetworkIOStats> r = new ArrayList<NetworkIOStats>();
		List<NetworkIOStats> stats = new ArrayList<NetworkIOStats>();
		for (NetworkIO io: networkIO) {
			NetworkIOStats stat = io.getStats();
			if (stat!=null) {
				stats.add(stat);
			}
		}
		int i = 0;
		int start = stats.size() - max;
		for (NetworkIOStats stat: stats) {
			if (i>=start) {
				r.add(stat);
			}
			i++;
		}
		return r;
	}
}
