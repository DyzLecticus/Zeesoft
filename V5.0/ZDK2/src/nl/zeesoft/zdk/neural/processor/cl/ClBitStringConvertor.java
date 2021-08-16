package nl.zeesoft.zdk.neural.processor.cl;

import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class ClBitStringConvertor extends ObjectStringConvertor {
	public String					dataSeparator		= "#";
	public String					valueSeparator		= ";";
	
	@Override
	public Class<?> getObjectClass() {
		return ClBit.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof ClBit) {
			ClBit bit = (ClBit) obj;
			if (bit.valueCounts.size()>0) {
				appendData(r, bit);
			}
		}
		return r;
	}

	@Override
	public ClBit fromStringBuilder(StringBuilder str) {
		ClBit r = null;
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()>2) {
			int index = Util.parseInt(data.remove(0).toString());
			Class<?> dataType = Instantiator.getClassForName(data.remove(0).toString());
			if (dataType!=null) {
				r = new ClBit();
				r.index = index;
				parseValueCounts(r, dataType, data);
			}
		}
		return r;
	}

	protected void appendData(StringBuilder str, ClBit bit) {
		str.append(bit.index);
		boolean first = true;
		for (Entry<Object,ValueCount> entry: bit.valueCounts.entrySet()) {
			if (first) {
				first = false;
				str.append(dataSeparator);
				str.append(entry.getKey().getClass().getName());
			}
			appendData(str, entry.getKey(), entry.getValue());
		}
	}
	
	protected void appendData(StringBuilder str, Object value, ValueCount vc) {
		str.append(dataSeparator);
		str.append(value);
		str.append(valueSeparator);
		str.append(vc.count);
		str.append(valueSeparator);
		str.append(vc.lastProcessed);
	}
	
	protected void parseValueCounts(ClBit bit, Class<?> dataType, List<StringBuilder> data) {
		for (StringBuilder dat: data) {
			List<StringBuilder> keyVal = StrUtil.split(dat, valueSeparator); 
			Object value = getObjectForStringBuilder(dataType, keyVal.get(0));
			ValueCount vc = new ValueCount();
			vc.count = Util.parseInt(keyVal.get(1).toString());
			vc.lastProcessed = Util.parseInt(keyVal.get(2).toString());
			bit.valueCounts.put(value, vc);
		}
	}
}
