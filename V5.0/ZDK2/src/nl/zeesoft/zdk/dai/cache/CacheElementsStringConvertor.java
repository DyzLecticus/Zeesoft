package nl.zeesoft.zdk.dai.cache;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class CacheElementsStringConvertor extends ObjectStringConvertor {
	public CacheElementStringConvertor	elementConvertor	= (CacheElementStringConvertor) ObjectStringConvertors.getConvertor(CacheElement.class);
	public String						dataSeparator		= "&";
	
	@Override
	public Class<?> getObjectClass() {
		return CacheElements.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof CacheElements) {
			appendData(r, (CacheElements) obj);
		}
		return r;
	}

	@Override
	public CacheElements fromStringBuilder(StringBuilder str) {
		CacheElements r = new CacheElements();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()>=1) {
			r.maxSize = Util.parseInt(data.remove(0).toString());
			for (StringBuilder dat: data) {
				r.elements.add(elementConvertor.fromStringBuilder(dat));
			}
		}
		return r;
	}

	protected void appendData(StringBuilder str, CacheElements cache) {
		str.append(cache.maxSize);
		for (CacheElement ce: cache.elements) {
			str.append(dataSeparator);
			str.append(elementConvertor.toStringBuilder(ce));
		}
	}
}
