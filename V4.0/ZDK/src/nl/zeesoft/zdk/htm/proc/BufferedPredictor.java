package nl.zeesoft.zdk.htm.proc;

import java.util.List;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMapElement;
import nl.zeesoft.zdk.htm.sdr.SDRSet;

public class BufferedPredictor extends Predictor {
	private SDRSet			buffer				= null;
	private	int				maxBufferSize		= 1000;
	
	private DateTimeSDR		predictedValueSDR	= null;
	
	private String			valueKey			= null;
	private int				matchDepth			= 4;
	private DateTimeSDR		predictedLowerSDR	= null;
	private DateTimeSDR		predictedUpperSDR	= null;
	
	public BufferedPredictor(MemoryConfig config) {
		super(config);
		matchDepth = config.bits / 4;
		if (matchDepth < 1) {
			matchDepth = 1;
		}
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

	public void setMatchDepth(int matchDepth) {
		this.matchDepth = matchDepth;
	}

	public DateTimeSDR getPredictedUpperSDR() {
		return predictedUpperSDR;
	}

	public DateTimeSDR getPredictedLowerSDR() {
		return predictedLowerSDR;
	}

	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		SDR inputSDR = context.get(0);
		SDR outputSDR = context.get(1);
		if (inputSDR instanceof DateTimeSDR) {
			buffer.add(outputSDR,inputSDR);
			if (buffer.size()>maxBufferSize) {
				buffer.remove(0);
			}
		}
		List<SDR> r = super.getSDRsForInput(input, context, learn);
		r.add(predictedValueSDR);
		if (predictedLowerSDR!=null && predictedUpperSDR!=null) {
			r.add(predictedLowerSDR);
			r.add(predictedUpperSDR);
		}
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input, boolean learn) {
		SDR r = super.getSDRForInputSDR(input, learn);
		predictedValueSDR = null;
		predictedLowerSDR = null;
		predictedUpperSDR = null;
		long start = 0;

		start = System.nanoTime();
		generateValuePredictionSDRs();
		logStatsValue("generateValuePredictionSDRs",System.nanoTime() - start);
		
		return r;
	}
	
	protected void initialize(String valueKey) {
		setMaxOnBits(config.bits);
		if (valueKey!=null && valueKey.length()>0) {
			this.valueKey = valueKey;
		}
		buffer = new SDRSet(config.length,config.bits);
	}
	
	protected void generateValuePredictionSDRs() {
		SDR predictionSDR = getPredictionSDR();
		if (predictionSDR!=null && buffer.size()>1) {
			List<SDRMapElement> elements = buffer.getClosestMatches(predictionSDR,matchDepth);
			if (elements.size()>0) {
				predictedValueSDR = (DateTimeSDR) elements.remove(0).value;
				if (valueKey!=null) {
					if (elements.size()==0) {
						predictedLowerSDR = predictedValueSDR;
						predictedUpperSDR = predictedValueSDR;
					} else if (elements.size()==1) {
						predictedLowerSDR = (DateTimeSDR) elements.get(0).value;
						predictedUpperSDR = (DateTimeSDR) elements.get(0).value;
					} else {
						float min = Float.MAX_VALUE;
						float max = Float.MIN_VALUE;
						SDRMapElement minElem = null;
						SDRMapElement maxElem = null;
						for (SDRMapElement elementC: elements) {
							Float value = DateTimeSDR.getValueFromSDR((DateTimeSDR) elementC.value,valueKey);
							if (value!=null) {
								if (value < min) {
									min = value;
									minElem = elementC;
								}
								if (value > max) {
									max = value;
									maxElem = elementC;
								}
							}
						}
						if (minElem!=null && maxElem!=null) {
							predictedLowerSDR = (DateTimeSDR) minElem.value;
							predictedUpperSDR = (DateTimeSDR) maxElem.value;
						}
					}
				}
			}
		}
		if (predictedValueSDR==null) {
			predictedValueSDR = new DateTimeSDR(config.length);
			predictedLowerSDR = null;
			predictedUpperSDR = null;
		}
	}
}
