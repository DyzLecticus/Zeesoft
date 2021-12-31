package nl.zeesoft.zdk.dai;

import java.util.ArrayList;
import java.util.List;

public class ObjMapList {
	public List<ObjMap>		list	= new ArrayList<ObjMap>();
	public List<String>		keys	= new ArrayList<String>();
	public int				maxSize	= 1000;
	
	public ObjMapList() {
		
	}
	
	public ObjMapList(int maxSize) {
		this.maxSize = maxSize;
	}
	
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
	
	@Override
	public boolean equals(Object other) {
		boolean r = false;
		if (other!=null && other instanceof ObjMapList && ((ObjMapList)other).list.size() == list.size()) {
			r = true;
			ObjMapList otherMapList = ((ObjMapList)other);
			int i = 0;
			for (ObjMap map: list) {
				ObjMap otherMap = otherMapList.list.get(i);
				if (!map.equals(otherMap)) {
					r = false;
					break;
				}
				i++;
			}
		}
		return r;
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
	
	protected void addMap(ObjMap map) {
		list.add(0, map);
		for (String key: map.values.keySet()) {
			if (!keys.contains(key)) {
				keys.add(key);
			}
		}
	}
	
	public ObjMapList getSubList(int start, List<Integer> indexes) {
		ObjMapList subList = new ObjMapList();
		for (Integer index: indexes) {
			int i = start + index;
			if (i < list.size()) {
				subList.list.add(list.get(i));
			}
		}
		return subList;
	}			
}
