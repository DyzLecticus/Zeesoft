package nl.zeesoft.zdk.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class NetworkIOAnalyzer {
	public List<NetworkIO>	networkIO = new ArrayList<NetworkIO>();
	
	public NetworkIOStats getAverage() {
		NetworkIOStats r = new NetworkIOStats();
		r.nsPerLayer = new TreeMap<Integer,Long>();
		int total = 0;
		for (NetworkIO io: networkIO) {
			NetworkIOStats stats = io.getStats();
			if (stats!=null) {
				total++;
				addStats(r, stats);
			}
		}
		if (total>0) {
			r.totalNs = r.totalNs / total;
			for (Integer layer: r.nsPerLayer.keySet()) {
				Long ns = r.nsPerLayer.get(layer);
				r.nsPerLayer.put(layer, (ns / total));
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
}
