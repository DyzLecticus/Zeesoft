package nl.zeesoft.zdk.neural;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class SdrHistoryStringConvertor extends ObjectStringConvertor {
	public String				dataSeparator	= ";";
	public SdrStringConvertor	sdrConvertor	= (SdrStringConvertor) ObjectStringConvertors.getConvertor(Sdr.class);
	
	@Override
	public Class<?> getObjectClass() {
		return SdrHistory.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof SdrHistory) {
			SdrHistory sh = (SdrHistory) obj;
			r.append(sh.length);
			r.append(dataSeparator);
			r.append(sh.capacity);
			for (int i = sh.sdrs.size() - 1; i >= 0; i--) {
				r.append(dataSeparator);
				r.append(sdrConvertor.toStringBuilder(sh.sdrs.get(i)));
			}
		}
		return r;
	}

	@Override
	public SdrHistory fromStringBuilder(StringBuilder str) {
		SdrHistory r = null;
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		int i = 0;
		for (StringBuilder dat: data) {
			if (i==0) {	
				r = parseLength(dat);
			} else if (i==1) {
				if (r==null) {
					break;
				}
				r.capacity = Util.parseInt(dat.toString());
			} else {
				r.push(sdrConvertor.fromStringBuilder(dat));
			}
			i++;
		}
		return r;
	}
	
	protected SdrHistory parseLength(StringBuilder dat) {
		SdrHistory r = null;
		Integer v = Util.parseInt(dat.toString());
		if (v!=null) {
			r = new SdrHistory();
			r.initialize(v);
		}
		return r;
	}
}
