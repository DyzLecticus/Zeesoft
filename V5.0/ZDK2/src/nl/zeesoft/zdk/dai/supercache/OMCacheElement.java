package nl.zeesoft.zdk.dai.supercache;

import nl.zeesoft.zdk.dai.ObjMapList;

public class OMCacheElement {
	public ObjMapList	key			= null;
	public ObjMapList	value		= null;
	public int			count		= 0;
	
	public OMCache		subCache	= null;
	
	public OMCacheElement() {
		
	}
	
	public OMCacheElement(ObjMapList key, ObjMapList value) {
		this.key = key;
		this.value = value;
	}
}
