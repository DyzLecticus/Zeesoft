package nl.zeesoft.zdk.dai.cache;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.ObjMapListStringConvertor;
import nl.zeesoft.zdk.dai.ObjMapStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class CacheElementStringConvertor extends ObjectStringConvertor {
	public ObjMapListStringConvertor	mapsConvertor		= (ObjMapListStringConvertor) ObjectStringConvertors.getConvertor(ObjMapList.class);
	public ObjMapStringConvertor		mapConvertor		= (ObjMapStringConvertor) ObjectStringConvertors.getConvertor(ObjMap.class);
	public String						dataSeparator		= "%";
	
	@Override
	public Class<?> getObjectClass() {
		return CacheElement.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof CacheElement) {
			appendData(r, (CacheElement) obj);
		}
		return r;
	}

	@Override
	public CacheElement fromStringBuilder(StringBuilder str) {
		CacheElement r = new CacheElement();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()==3) {
			r.baseList = mapsConvertor.fromStringBuilder(data.get(0));
			r.nextMap = mapConvertor.fromStringBuilder(data.get(1));
			r.count = Util.parseInt(data.get(2).toString());
		}
		return r;
	}

	protected void appendData(StringBuilder str, CacheElement element) {
		str.append(mapsConvertor.toStringBuilder(element.baseList));
		str.append(dataSeparator);
		str.append(mapConvertor.toStringBuilder(element.nextMap));
		str.append(dataSeparator);
		str.append(element.count);
	}
}
