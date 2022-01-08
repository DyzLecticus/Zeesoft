package nl.zeesoft.zdk.dai;

import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class ObjMapStringConvertor extends ObjectStringConvertor {
	public String					dataSeparator		= "#";
	public String					valueSeparator		= ";";
	
	@Override
	public Class<?> getObjectClass() {
		return ObjMap.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof ObjMap) {
			ObjMap map = (ObjMap) obj;
			if (map.values.size()>0) {
				appendData(r, map);
			}
		}
		return r;
	}

	@Override
	public ObjMap fromStringBuilder(StringBuilder str) {
		ObjMap r = new ObjMap();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()>0) {
			for (StringBuilder dat: data) {
				parseKeyValue(dat, r);
			}
		}
		return r;
	}

	protected void appendData(StringBuilder str, ObjMap map) {
		for (Entry<String,Object> entry: map.values.entrySet()) {
			if (str.length()>0) {
				str.append(dataSeparator);
			}
			str.append(entry.getKey());
			str.append(valueSeparator);
			str.append(entry.getValue());
		}
	}
	
	protected void parseKeyValue(StringBuilder dat, ObjMap map) {
		List<StringBuilder> keyVal = StrUtil.split(dat, valueSeparator);
		String key = keyVal.get(0).toString();
		map.values.put(key, parseValue(keyVal.get(1)));
	}
	
	public static Object parseValue(StringBuilder str) {
		Object r = null;
		if (!str.equals(StrUtil.NULL)) {
			String strVal = str.toString();
			r = Util.parseInt(strVal);
			if (r==null) {
				r = Util.parseFloat(strVal);
			}
			if (r==null) {
				r = strVal;
			}
		}
		return r;
	}
}
