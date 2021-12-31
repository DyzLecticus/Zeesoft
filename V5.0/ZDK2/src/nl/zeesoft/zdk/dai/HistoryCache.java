package nl.zeesoft.zdk.dai;

public class HistoryCache {
	public ObjMapList 	baseList 	= null;
	public ObjMap		nextMap		= null;
	public int			count		= 0;
	
	public HistoryCache() {
		
	}
	
	public HistoryCache(ObjMapList baseList, ObjMap nextInput) {
		this.baseList = baseList;
		this.nextMap = nextInput;
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
