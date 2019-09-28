package nl.zeesoft.zdk.htm.proc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PoolerConnections {
	protected PoolerConfig							config							= null;
	protected HashMap<Integer,Set<Integer>> 		connectedIndexesPerInputIndex	= new HashMap<Integer,Set<Integer>>();
	
	protected PoolerConnections(PoolerConfig config) {
		this.config = config;
	}

	protected void clear() {
		connectedIndexesPerInputIndex.clear();
	}
	
	protected void initialize(List<PoolerColumn> columns) {
		connectedIndexesPerInputIndex.clear();
		for (PoolerColumn col: columns) {
			for (ProximalLink lnk: col.proxLinks) {
				addColumnLink(col,lnk);
			}
		}
	}
	
	protected void addColumnLink(PoolerColumn col,ProximalLink lnk) {
		Set<Integer> list = connectedIndexesPerInputIndex.get(lnk.inputIndex);
		if (list==null) {
			list = new HashSet<Integer>();
			connectedIndexesPerInputIndex.put(lnk.inputIndex,list);
		}
		list.add(col.index);
	}

	protected void removeColumnLink(PoolerColumn col,ProximalLink lnk) {
		Set<Integer> list = connectedIndexesPerInputIndex.get(lnk.inputIndex);
		if (list!=null) {
			list.remove((Integer)col.index);
			if (list.size()==0) {
				connectedIndexesPerInputIndex.remove(lnk.inputIndex);
			}
		}
	}
}
