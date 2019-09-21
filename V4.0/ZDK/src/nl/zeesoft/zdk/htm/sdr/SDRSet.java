package nl.zeesoft.zdk.htm.sdr;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;

public class SDRSet extends SDRMap {
	private SortedMap<ZStringBuilder,List<SDRMapElement>>	matchMap		= new TreeMap<ZStringBuilder,List<SDRMapElement>>();
	
	public SDRSet(int length,int bits) {
		super(length,bits,true);
	}

	@Override
	public SDRMapElement add(SDR sdr,Object value) {
		SDRMapElement r = null;
		ZStringBuilder key = sdr.toStringBuilder();
		boolean updated = false;
		if (matchMap.containsKey(key)) {
			List<SDRMapElement> list = matchMap.get(key);
			for (SDRMapElement element: list) {
				if (value!=null) {
					if (value.equals(element.value)) {
						toLast(element);
						updated = true;
					}
				} else {
					toLast(element);
					updated = true;
				}
			}
		}
		if (!updated) {
			r = super.add(sdr,value);
			if (r!=null) {
				List<SDRMapElement> list = matchMap.get(key);
				if (list==null) {
					list = new ArrayList<SDRMapElement>();
					matchMap.put(key,list);
				}
				list.add(r);
			}
		}
		return r;
	}
	
	@Override
	public SDRMapElement remove(int index) {
		SDRMapElement r = super.remove(index);
		if (r!=null) {
			ZStringBuilder key = r.key.toStringBuilder();
			List<SDRMapElement> list = matchMap.get(key);
			if (list!=null) {
				list.remove(r);
				if (list.size()==0) {
					matchMap.remove(key);
				}
			}
		}
		return r;
	}
	
	@Override
	public void setBit(int index,int bitIndex,boolean on) {
		// Not allowed
	}
}
