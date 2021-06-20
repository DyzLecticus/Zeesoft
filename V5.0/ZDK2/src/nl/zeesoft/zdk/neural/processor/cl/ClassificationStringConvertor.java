package nl.zeesoft.zdk.neural.processor.cl;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class ClassificationStringConvertor extends ObjectStringConvertor {
	public String				dataSeparator	= "&";
	public String				keySeparator	= ":";
	public String				valueSeparator	= "=";
	
	@Override
	public Class<?> getObjectClass() {
		return Classification.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Classification) {
			Classification cl = (Classification) obj;
			r.append(cl.name);
			r.append(dataSeparator);
			r.append(cl.step);
			r.append(dataSeparator);
			r.append(getDataTypeStringBuilderForObject(cl.value, valueSeparator));
			r.append(getValueCountsStringBuilder(cl.valueCounts));
		}
		return r;
	}

	@Override
	public Classification fromStringBuilder(StringBuilder str) {
		Classification r = null;
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()>=3) {
			r = new Classification();
			r.name = data.get(0).toString();
			r.step = Util.parseInt(data.get(1).toString());
			r.value = getObjectForDataTypeStringBuilder(data.get(2), valueSeparator);
			r.valueCounts = parseValueCounts(data);
		}
		return r;
	}
	
	protected StringBuilder getValueCountsStringBuilder(HashMap<Object,Integer> valueCounts) {
		StringBuilder r = new StringBuilder();
		for (Entry<Object,Integer> entry: valueCounts.entrySet()) {
			r.append(dataSeparator);
			r.append(getDataTypeStringBuilderForObject(entry.getKey(), valueSeparator));
			r.append(keySeparator);
			r.append(entry.getValue());
		}		
		return r;
	}
	
	protected HashMap<Object,Integer> parseValueCounts(List<StringBuilder> data) {
		HashMap<Object,Integer> r = new HashMap<Object,Integer>();
		for (int i = 0; i < data.size(); i++) {
			if (i >= 3) {
				StringBuilder dat = data.get(i);
				List<StringBuilder> kv = StrUtil.split(dat, keySeparator);
				Object key = getObjectForDataTypeStringBuilder(kv.get(0), valueSeparator);
				r.put(key, Util.parseInt(kv.get(1).toString()));
			}
		}
		return r;
	}
}
