package nl.zeesoft.zdk.dai.cache;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapList;

public class CacheElement {
	public ObjMapList 		baseList 	= null;
	public ObjMap			nextMap		= null;
	public int				count		= 0;
	
	public CacheElements	elements	= null;
	
	public CacheElement() {
		
	}
	
	public CacheElement(ObjMapList baseList, ObjMap nextMap) {
		this.baseList = baseList;
		this.nextMap = nextMap;
	}
	
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		r.append(baseList);
		r.append("\n-> ");
		r.append(nextMap);
		r.append(", count: " + count);
		return r.toString();
	}
}
