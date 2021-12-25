package nl.zeesoft.zdk.dai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class ObjMap {
	public SortedMap<String,Object>		values	= new TreeMap<String,Object>();
	
	public ObjMap(Object... values) {
		for (int i = 0; i < values.length; i++) {
			this.values.put("" + (i+1), values[i]);
		}
	}
	
	public ObjMap(List<Object> values) {
		for (int i = 0; i < values.size(); i++) {
			this.values.put("" + (i+1), values.get(i));
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Entry<String,Object> entry: values.entrySet()) {
			if (str.length()>0) {
				str.append(", ");
			}
			str.append(entry.getKey() + ":" + entry.getValue());
		}
		str.insert(0, "{");
		str.append("}");
		return str.toString();
	}

	@Override
	public boolean equals(Object other) {
		boolean r = false;
		if (other!=null && other instanceof ObjMap && ((ObjMap)other).values.size() == values.size()) {
			r = true;
			ObjMap otherMap = ((ObjMap)other);
			for (String key: values.keySet()) {
				if (!otherMap.values.containsKey(key)) {
					r = false;
					break;
				} else {
					Object val = values.get(key);
					Object otherVal = otherMap.values.get(key);
					if ((val!=null && otherVal==null) || (val==null && otherVal!=null) || !val.equals(otherVal)) {
						r = false;
						break;
					}
				}
			}
		}
		return r;
	}
	
	public List<String> getOverlappingKeys(ObjMap other) {
		List<String> r = new ArrayList<String>();
		for (String key: values.keySet()) {
			if (other.values.containsKey(key)) {
				r.add(key);
			}
		}
		return r;
	}
	
	public List<String> getNonOverlappingKeys(ObjMap other) {
		List<String> r = new ArrayList<String>();
		List<String> overlap = getOverlappingKeys(other);
		for (String key: values.keySet()) {
			if (!overlap.contains(key)) {
				r.add(key);
			}
		}
		for (String key: other.values.keySet()) {
			if (!overlap.contains(key)) {
				r.add(key);
			}
		}
		return r;
	}
}
