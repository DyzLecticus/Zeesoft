package nl.zeesoft.zdk.neural;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class SdrStringConvertor extends ObjectStringConvertor {
	public String	dataSeparator	= ",";
	
	@Override
	public Class<?> getObjectClass() {
		return Sdr.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Sdr) {
			Sdr sdr = (Sdr) obj;
			r.append(sdr.length);
			for (Integer onBit: sdr.onBits) {
				r.append(dataSeparator);
				r.append(onBit);
			}
		}
		return r;
	}

	@Override
	public Sdr fromStringBuilder(StringBuilder str) {
		Sdr r = null;
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		int i = 0;
		for (StringBuilder dat: data) {
			if (i==0) {	
				r = parseLength(dat);
			} else {
				if (r==null) {
					break;
				}
				r.onBits.add(Util.parseInt(dat.toString()));
			}
			i++;
		}
		return r;
	}
	
	protected Sdr parseLength(StringBuilder dat) {
		Sdr r = null;
		Integer v = Util.parseInt(dat.toString());
		if (v!=null) {
			r = new Sdr(v);
		}
		return r;
	}
}
