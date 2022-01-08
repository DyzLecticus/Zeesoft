package nl.zeesoft.zdk.dai.predict;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class MsLoggerStringConvertor extends ObjectStringConvertor {
	public String		dataSeparator		= "#";
	
	@Override
	public Class<?> getObjectClass() {
		return MsLogger.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof MsLogger) {
			appendData(r, (MsLogger) obj);
		}
		return r;
	}

	@Override
	public MsLogger fromStringBuilder(StringBuilder str) {
		MsLogger r = new MsLogger();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()>0) {
			r.maxSize = Util.parseInt(data.remove(0).toString());
			for (StringBuilder dat: data) {
				r.log.add(Util.parseFloat(dat.toString()));
			}
		}
		return r;
	}

	protected void appendData(StringBuilder str, MsLogger logger) {
		str.append(logger.maxSize);
		for (Float ms: logger.log) {
			str.append(dataSeparator);
			str.append(ms);
		}
	}
}
