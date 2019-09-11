package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.functions.ZRandomize;

public class Pooler {
	protected PoolerConfig			config		= null;
	
	protected List<PoolerColumn>	columns		= new ArrayList<PoolerColumn>();
	
	public Pooler(PoolerConfig config) {
		this.config = config;
		initialize();
	}

	public void randomizeConnections() {
		for (PoolerColumn col: columns) {
			col.randomizeConnections();
		}
	}

	protected void initialize() {
		for (int i = 0; i < config.outputSize; i++) {
			PoolerColumn col = new PoolerColumn(config,i);
			columns.add(col);
		}
	}
	
	protected List<PoolerColumn> getTopColumnsByOverlapScore() {
		List<PoolerColumn> r = new ArrayList<PoolerColumn>();
		SortedMap<Integer,List<PoolerColumn>> map = new TreeMap<Integer,List<PoolerColumn>>();
		for (PoolerColumn col: columns) {
			List<PoolerColumn> list = map.get(col.overlapScore);
			if (list==null) {
				list = new ArrayList<PoolerColumn>();
				map.put(col.overlapScore,list);
			}
			list.add(col);
		}
		Integer[] keys = (Integer[]) map.keySet().toArray();
		for (int i = map.size(); i>=0; i--) {
			List<PoolerColumn> list = map.get(keys[i]);
			if (config.outputBits - r.size() < list.size()) {
				for (int s = 0; s < config.outputBits - r.size(); s++) {
					int sel = ZRandomize.getRandomInt(0,list.size() - 1);
					r.add(list.get(sel));
					list.remove(sel);
				}
			} else {
				for (PoolerColumn col: list) {
					r.add(col);
					if (r.size()>=config.outputBits) {
						break;
					}
				}
			}
			if (r.size()>=config.outputBits) {
				break;
			}
		}
		return r;
	}
}
