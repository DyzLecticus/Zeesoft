package nl.zeesoft.zdk.dai.cache;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class CacheStringConvertor extends ObjectStringConvertor {
	public CacheElementsStringConvertor	elemsConvertor	= (CacheElementsStringConvertor) ObjectStringConvertors.getConvertor(CacheElements.class);
	public String						dataSeparator	= "*";
	
	@Override
	public Class<?> getObjectClass() {
		return Cache.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Cache) {
			appendData(r, (Cache) obj);
		}
		return r;
	}

	@Override
	public Cache fromStringBuilder(StringBuilder str) {
		Cache r = new Cache();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()>=2) {
			r.indexes.clear();
			int numIndexes = Util.parseInt(data.remove(0).toString());
			for (int i = 0; i < numIndexes; i++) {
				r.indexes.add(Util.parseInt(data.remove(0).toString()));
			}
			r.elements = elemsConvertor.fromStringBuilder(data.get(0));
		}
		return r;
	}

	protected void appendData(StringBuilder str, Cache cache) {
		str.append(cache.indexes.size());
		for (Integer index: cache.indexes) {
			str.append(dataSeparator);
			str.append(index);
		}
		str.append(dataSeparator);
		str.append(elemsConvertor.toStringBuilder(cache.elements));
	}
}
