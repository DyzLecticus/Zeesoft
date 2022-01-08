package nl.zeesoft.zdk.dai;

import java.util.List;

import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class PredictionStringConvertor extends ObjectStringConvertor {
	public KeyPredictionsStringConvertor	kpsConvertor	= (KeyPredictionsStringConvertor) ObjectStringConvertors.getConvertor(KeyPredictions.class);
	public MapPredictionStringConvertor		mapConvertor	= (MapPredictionStringConvertor) ObjectStringConvertors.getConvertor(MapPrediction.class);
	public String							dataSeparator	= "$";
	
	@Override
	public Class<?> getObjectClass() {
		return Prediction.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Prediction) {
			appendData(r, (Prediction) obj);
		}
		return r;
	}

	@Override
	public Prediction fromStringBuilder(StringBuilder str) {
		Prediction r = new Prediction();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()>=1) {
			r.keyPredictions = kpsConvertor.fromStringBuilder(data.remove(0));
			for (StringBuilder dat: data) {
				r.mapPredictions.add(mapConvertor.fromStringBuilder(dat));
			}
		}
		return r;
	}

	protected void appendData(StringBuilder str, Prediction prediction) {
		str.append(kpsConvertor.toStringBuilder(prediction.keyPredictions));
		for (MapPrediction mp: prediction.mapPredictions) {
			str.append(dataSeparator);
			str.append(mapConvertor.toStringBuilder(mp));
		}
	}
}
