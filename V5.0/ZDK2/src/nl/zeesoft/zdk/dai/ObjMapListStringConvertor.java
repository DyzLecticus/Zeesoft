package nl.zeesoft.zdk.dai;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class ObjMapListStringConvertor extends ObjectStringConvertor {
	public ObjMapStringConvertor	mapConvertor		= (ObjMapStringConvertor) ObjectStringConvertors.getConvertor(ObjMap.class);
	public String					dataSeparator		= "@";
	
	@Override
	public Class<?> getObjectClass() {
		return ObjMapList.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof ObjMapList) {
			appendData(r, (ObjMapList) obj);
		}
		return r;
	}

	@Override
	public ObjMapList fromStringBuilder(StringBuilder str) {
		ObjMapList r = new ObjMapList();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()>2) {
			r.maxSize = Util.parseInt(data.remove(0).toString());
			int numKeys = Util.parseInt(data.remove(0).toString());
			for (int i = 0; i < numKeys; i++) {
				r.keys.add(data.remove(0).toString());
			}
			for (StringBuilder dat: data) {
				ObjMap map = mapConvertor.fromStringBuilder(dat);
				r.list.add(map);
			}
		}
		return r;
	}

	protected void appendData(StringBuilder str, ObjMapList maps) {
		str.append(maps.maxSize);
		str.append(dataSeparator);
		str.append(maps.keys.size());
		for (String key: maps.keys) {
			str.append(dataSeparator);
			str.append(key);
		}
		for (ObjMap map: maps.list) {
			str.append(dataSeparator);
			str.append(mapConvertor.toStringBuilder(map));
		}
	}
}
