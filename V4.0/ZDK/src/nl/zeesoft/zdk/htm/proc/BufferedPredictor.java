package nl.zeesoft.zdk.htm.proc;

import java.util.List;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;
import nl.zeesoft.zdk.htm.sdr.SDRMapElement;

public class BufferedPredictor extends Predictor implements Processable, ProcessableContextInput {
	private SDRMap			buffer				= null;
	private	int				maxBufferSize		= 1000;
	
	private DateTimeSDR		predictedValueSDR	= null;
	
	private String			valueKey			= null;
	private DateTimeSDR		predictedLowerSDR	= null;
	private DateTimeSDR		predictedUpperSDR	= null;
	
	public BufferedPredictor(MemoryConfig config) {
		super(config);
		initialize(null);
	}

	public BufferedPredictor(MemoryConfig config,String valueKey) {
		super(config);
		initialize(valueKey);
	}

	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	public DateTimeSDR getPredictedValueSDR() {
		return predictedValueSDR;
	}

	public DateTimeSDR getPredictedUpperSDR() {
		return predictedUpperSDR;
	}

	public DateTimeSDR getPredictedLowerSDR() {
		return predictedLowerSDR;
	}

	@Override
	public void setContextSDRs(List<SDR> contextSDRs) {
		SDR inputSDR = contextSDRs.get(0);
		SDR outputSDR = contextSDRs.get(1);
		if (inputSDR instanceof DateTimeSDR) {
			buffer.add(outputSDR,inputSDR);
			if (buffer.size()>maxBufferSize) {
				buffer.remove(0);
			}
		}
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input, boolean learn) {
		SDR r = super.getSDRForInputSDR(input, learn);
		predictedValueSDR = null;
		long start = 0;

		PredictorStats pStats = (PredictorStats) stats;
		
		start = System.nanoTime();
		
		SDR predictionSDR = getPredictionSDR();
		if (predictionSDR!=null && buffer.size()>1) {
			if (valueKey!=null) {
				List<SDRMapElement> elements = buffer.getClosestMatches(predictionSDR);
				if (elements!=null) {
					if (elements.size()==1) {
						if (elements.get(0).value instanceof DateTimeSDR) {
							predictedValueSDR = (DateTimeSDR) elements.get(0).value;
							predictedLowerSDR = (DateTimeSDR) elements.get(0).value;
							predictedUpperSDR = (DateTimeSDR) elements.get(0).value;
						}
					} else {
						float min = Float.MAX_VALUE;
						float max = Float.MIN_VALUE;
						SDRMapElement minElem = null;
						SDRMapElement maxElem = null;
						for (SDRMapElement element: elements) {
							Float value = DateTimeSDR.getValueFromSDR((DateTimeSDR) element.value,valueKey);
							if (value!=null) {
								if (value < min) {
									min = value;
									minElem = element;
								}
								if (value > max) {
									max = value;
									maxElem = element;
								}
							}
						}
						predictedLowerSDR = (DateTimeSDR) minElem.value;
						predictedUpperSDR = (DateTimeSDR) maxElem.value;
						if (elements.size()>2) {
							elements.remove(minElem);
							elements.remove(maxElem);
						}
						if (elements.size()==1) {
							predictedValueSDR = (DateTimeSDR) elements.get(0).value;
						} else {
							predictedValueSDR = (DateTimeSDR) elements.get(ZRandomize.getRandomInt(0,elements.size() - 1)).value;
						}
					}
				}
			} else {
				SDRMapElement element = buffer.getRandomClosestMatch(predictionSDR);
				if (element!=null) {
					predictedValueSDR = (DateTimeSDR) element.value;
				}
			}
		}
		if (predictedValueSDR==null) {
			predictedValueSDR = new DateTimeSDR(config.length);
		}
		
		pStats.generatingPredictionsNs += System.nanoTime() - start;
		
		return r;
	}

	@Override
	public void addSecondarySDRs(List<SDR> outputSDRs) {
		super.addSecondarySDRs(outputSDRs);
		outputSDRs.add(predictedValueSDR);
		if (predictedLowerSDR!=null && predictedUpperSDR!=null) {
			outputSDRs.add(predictedLowerSDR);
			outputSDRs.add(predictedUpperSDR);
		}
	}
	
	protected void initialize(String valueKey) {
		setMaxOnBits(config.bits);
		if (valueKey!=null && valueKey.length()>0) {
			this.valueKey = valueKey;
		}
		buffer = new SDRMap(config.length,config.bits);
	}
}
