package nl.zeesoft.zdk.dai;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class KeyPredictionStringConvertor extends ObjectStringConvertor {
	public String	dataSeparator	= "#";
	
	@Override
	public Class<?> getObjectClass() {
		return KeyPrediction.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof KeyPrediction) {
			appendData(r, (KeyPrediction) obj);
		}
		return r;
	}

	@Override
	public KeyPrediction fromStringBuilder(StringBuilder str) {
		KeyPrediction r = new KeyPrediction();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()==4) {
			r.key = data.get(0).toString();
			r.predictedValue = ObjMapStringConvertor.parseValue(data.get(1));
			r.support = Util.parseFloat(data.get(2).toString());
			r.weight = Util.parseFloat(data.get(3).toString());
		}
		return r;
	}

	protected void appendData(StringBuilder str, KeyPrediction prediction) {
		str.append(prediction.key);
		str.append(dataSeparator);
		str.append(prediction.predictedValue);
		str.append(dataSeparator);
		str.append(prediction.support);
		str.append(dataSeparator);
		str.append(prediction.weight);
	}
}
