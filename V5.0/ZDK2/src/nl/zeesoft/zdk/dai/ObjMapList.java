package nl.zeesoft.zdk.dai;

import java.util.ArrayList;
import java.util.List;

public class ObjMapList {
	public List<ObjMap>		list	= new ArrayList<ObjMap>();
	public List<String>		keys	= new ArrayList<String>();
	public int				maxSize	= 1000;
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (ObjMap map: list) {
			if (str.length()>0) {
				str.append("\n");
			}
			str.append(map);
		}
		return str.toString();
	}
	
	public void add(ObjMap map) {
		addMap(map);
		applyMaxSize();
	}
	
	public void addAll(List<ObjMap> list) {
		for (ObjMap map: list) {
			addMap(map);
		}
		applyMaxSize();
	}
	
	public void applyMaxSize() {
		while(list.size()>maxSize) {
			list.remove((int)(list.size() - 1));
		}
	}
	
	private void addMap(ObjMap map) {
		list.add(0, map);
		for (String key: map.values.keySet()) {
			if (!keys.contains(key)) {
				keys.add(key);
			}
		}
	}
}
