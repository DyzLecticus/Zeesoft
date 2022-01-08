package nl.zeesoft.zdk.dai;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class KeyPredictionsStringConvertor extends ObjectStringConvertor {
	public KeyPredictionStringConvertor	keyConvertor	= (KeyPredictionStringConvertor) ObjectStringConvertors.getConvertor(KeyPrediction.class);
	public ObjMapStringConvertor		mapConvertor	= (ObjMapStringConvertor) ObjectStringConvertors.getConvertor(ObjMap.class);
	public String						dataSeparator	= "&";
	
	@Override
	public Class<?> getObjectClass() {
		return KeyPredictions.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof KeyPredictions) {
			appendData(r, (KeyPredictions) obj);
		}
		return r;
	}

	@Override
	public KeyPredictions fromStringBuilder(StringBuilder str) {
		KeyPredictions r = new KeyPredictions();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()>=4) {
			r.predictedMap = mapConvertor.fromStringBuilder(data.remove(0));
			r.weightedMap = mapConvertor.fromStringBuilder(data.remove(0));
			r.weightsMap = mapConvertor.fromStringBuilder(data.remove(0));
			int numKeys = Util.parseInt(data.remove(0).toString());
			for (int i = 0; i < numKeys; i++) {
				r.keys.add(data.remove(0).toString());
			}
			for (StringBuilder dat: data) {
				r.list.add(keyConvertor.fromStringBuilder(dat));
			}
		}
		return r;
	}

	protected void appendData(StringBuilder str, KeyPredictions predictions) {
		str.append(mapConvertor.toStringBuilder(predictions.predictedMap));
		str.append(dataSeparator);
		str.append(mapConvertor.toStringBuilder(predictions.weightedMap));
		str.append(dataSeparator);
		str.append(mapConvertor.toStringBuilder(predictions.weightsMap));
		str.append(dataSeparator);
		str.append(predictions.keys.size());
		for (String key: predictions.keys) {
			str.append(dataSeparator);
			str.append(key);
		}
		for (KeyPrediction kp: predictions.list) {
			str.append(dataSeparator);
			str.append(keyConvertor.toStringBuilder(kp));
		}
	}
}
