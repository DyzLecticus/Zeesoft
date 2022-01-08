package nl.zeesoft.zdk.dai;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class MapPredictionStringConvertor extends ObjectStringConvertor {
	public ObjMapStringConvertor	mapConvertor	= (ObjMapStringConvertor) ObjectStringConvertors.getConvertor(ObjMap.class);
	public String					dataSeparator	= "%";
	
	@Override
	public Class<?> getObjectClass() {
		return MapPrediction.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof MapPrediction) {
			appendData(r, (MapPrediction) obj);
		}
		return r;
	}

	@Override
	public MapPrediction fromStringBuilder(StringBuilder str) {
		MapPrediction r = new MapPrediction();
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()==2) {
			r.predictedMap = mapConvertor.fromStringBuilder(data.get(0));
			r.support = Util.parseFloat(data.get(1).toString());
		}
		return r;
	}

	protected void appendData(StringBuilder str, MapPrediction prediction) {
		str.append(mapConvertor.toStringBuilder(prediction.predictedMap));
		str.append(dataSeparator);
		str.append(prediction.support);
	}
}
