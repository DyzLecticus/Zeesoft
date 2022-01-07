package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.ObjMapTransformer;

public class TransformationPredictor extends Predictor {
	protected ObjMapTransformer		transformer		= new ObjMapTransformer();
	protected ObjMapList			absoluteHistory	= new ObjMapList();
	
	@Override
	public synchronized void configure(PredictorConfig config) {
		super.configure(config);
		absoluteHistory = new ObjMapList(config.maxHistorySize);
		if (config instanceof TransformationPredictorConfig) {
			TransformationPredictorConfig cfg = (TransformationPredictorConfig) config;
			transformer = cfg.transformer;
		}
	}
	
	@Override
	public synchronized void add(ObjMap map) {
		absoluteHistory.add(map);
		if (transformer==null) {
			super.add(map);
		} else if (absoluteHistory.list.size()>1) {
			super.add(transformer.getTransformation(absoluteHistory.list.get(1), map));
		}
	}
	
	@Override
	public synchronized void processRequest(PredictorRequest request) {
		request.setTransform(transformer, absoluteHistory.list.get(0));
		super.processRequest(request);
	}
}
