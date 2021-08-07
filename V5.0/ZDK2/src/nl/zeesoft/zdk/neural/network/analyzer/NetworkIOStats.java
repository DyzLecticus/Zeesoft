package nl.zeesoft.zdk.neural.network.analyzer;

import java.util.Map.Entry;
import java.util.SortedMap;

public class NetworkIOStats {	
	public long						totalNs			= 0;
	public SortedMap<Integer,Long>	nsPerLayer		= null;
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		r.append("Total: ");
		r.append(getNsAsMs(totalNs));
		r.append(" ms");
		if (nsPerLayer!=null) {
			for (Entry<Integer,Long> entry: nsPerLayer.entrySet()) {
				r.append("\nLayer ");
				r.append((entry.getKey() + 1));
				r.append(": ");
				r.append(getNsAsMs(entry.getValue()));
				r.append(" ms");
			}
		}
		return r.toString();
	}
	
	protected float getNsAsMs(long ns) {
		return (float)ns / 1000000F;
	}
}
