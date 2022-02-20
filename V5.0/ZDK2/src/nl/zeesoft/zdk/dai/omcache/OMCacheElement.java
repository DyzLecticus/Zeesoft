package nl.zeesoft.zdk.dai.omcache;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapList;

public class OMCacheElement {
	public ObjMapList	key			= null;
	public ObjMap		value		= null;
	public int			count		= 0;
	
	public OMCache		subCache	= null;
	
	public OMCacheElement() {
		
	}
	
	public OMCacheElement(ObjMapList key, ObjMap value) {
		this.key = key;
		this.value = value;
	}
}
