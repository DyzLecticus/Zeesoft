package nl.zeesoft.zdk;

import java.util.List;

import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class HistoricalFloatStringConvertor extends ObjectStringConvertor {
	public String	dataSeparator	= ",";
	
	@Override
	public Class<?> getObjectClass() {
		return HistoricalFloat.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof HistoricalFloat) {
			HistoricalFloat hf = (HistoricalFloat) obj;
			r.append(hf.capacity);
			r.append(dataSeparator);
			r.append(hf.total);
			for (Float f: hf.floats) {
				r.append(dataSeparator);
				r.append(f);
			}
		}
		return r;
	}

	@Override
	public HistoricalFloat fromStringBuilder(StringBuilder str) {
		HistoricalFloat r = null;
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		int i = 0;
		for (StringBuilder dat: data) {
			if (i==0) {
				r = parseCapacity(dat);
			} else if (i==1) {
				if (r==null) {
					break;
				}
				r.total = Util.parseFloat(dat.toString());
			} else {
				r.floats.add(Util.parseFloat(dat.toString()));
			}
			i++;
		}
		return r;
	}
	
	protected HistoricalFloat parseCapacity(StringBuilder dat) {
		HistoricalFloat r = null;
		Integer v = Util.parseInt(dat.toString());
		if (v!=null) {
			r = new HistoricalFloat();
			r.capacity = v;
		}
		return r;
	}
}
