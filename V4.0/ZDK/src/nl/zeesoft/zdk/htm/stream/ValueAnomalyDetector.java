package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValueAnomalyDetector extends ValuePredictor {
	private	List<ValueAnomalyDetectorListener>	listeners						= new ArrayList<ValueAnomalyDetectorListener>();
	
	private boolean								usePredictedRange				= false;
	private int									start							= 1000;
	private float								threshold						= 0.1F;
	private int									recoveryWindow					= 100;
	
	private HashMap<String,HistoricalFloats>	absolutePredictionDeviations	= new HashMap<String,HistoricalFloats>();
	
	private HistoricalFloats					lowerPredictionDeviations		= new HistoricalFloats();
	private HistoricalFloats					upperPredictionDeviations		= new HistoricalFloats();
	
	private int									seen							= 0;
	private int									recovery						= 0;	
	
	public ValueAnomalyDetector(BufferedPredictionStream stream,String valueKey) {
		super(stream,valueKey);
	}
	
	public void addDetectorListener(ValueAnomalyDetectorListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}

	public void setWindow(int window) {
		lockMe(this);
		for (HistoricalFloats hist: absolutePredictionDeviations.values()) {
			hist.window = window;
		}
		lowerPredictionDeviations.window = window;
		upperPredictionDeviations.window = window;
		unlockMe(this);
	}

	public void setStart(int start) {
		lockMe(this);
		this.start = start;
		unlockMe(this);
	}

	public void setThreshold(float threshold) {
		lockMe(this);
		this.threshold = threshold;
		unlockMe(this);
	}

	public void setRecoveryWindow(int recoveryWindow) {
		lockMe(this);
		this.recoveryWindow = recoveryWindow;
		unlockMe(this);
	}

	@Override
	public void processedResult(Stream stream, StreamResult result) {
		
		HashMap<String,Object> predictedValues = getPredictedValues();

		boolean warn = false;
		String valKey = "";
		float difference = 0;
		List<ValueAnomalyDetectorListener> list = new ArrayList<ValueAnomalyDetectorListener>();
		
		if (predictedValues.size()>0) {
			List<String> valKeys = getValueKeys();
			HashMap<String,Object> currentValues = getCurrentValues(result,valKeys);

			lockMe(this);
			list = new ArrayList<ValueAnomalyDetectorListener>(listeners);
			if (seen<start) {
				seen++;
			}
			if (recovery>0) {
				recovery--;
			}
			if (usePredictedRange && valKeys.size()==1 && predictedValues.size()==3) {
				String valueKey = valKeys.get(0);
				if (predictedValues.containsKey(valueKey + "Min") &&
					predictedValues.containsKey(valueKey + "Max") &&
					currentValues.containsKey(valueKey)
					) {
					float lV = getFloatValue(predictedValues.get(valueKey + "Min"));
					float uV = getFloatValue(predictedValues.get(valueKey + "Max"));
					float cV = getFloatValue(currentValues.get(valueKey));
					float lAbs = 0;
					float uAbs = 0;
					if (cV < lV) {
						uAbs = lV - cV;
					} else if (cV > uV) {
						lAbs = cV - uV;
					}

					if (lAbs > lowerPredictionDeviations.maximum) {
						difference = ((lAbs / lowerPredictionDeviations.maximum) - 1);
					} else if (uAbs > upperPredictionDeviations.maximum) {
						difference = ((uAbs / upperPredictionDeviations.maximum) - 1);
					}
					
					if (seen>=start && difference>threshold && recovery==0) {
						warn = true;
						valKey = valueKey;
						recovery = recoveryWindow;
					}
					lowerPredictionDeviations.addFloat(lAbs);
					upperPredictionDeviations.addFloat(uAbs);
				}
			} else {
				for (String valueKey: valKeys) {
					if (predictedValues.containsKey(valueKey) && currentValues.containsKey(valueKey)) {
						float pV = getFloatValue(predictedValues.get(valueKey));
						float cV = getFloatValue(currentValues.get(valueKey));
						float abs = 0;
						if (pV > cV) {
							abs = pV - cV;
						} else if (pV < cV) {
							abs = cV - pV;
						}
						HistoricalFloats hist = absolutePredictionDeviations.get(valueKey);
						if (hist==null) {
							hist = new HistoricalFloats();
							absolutePredictionDeviations.put(valueKey,hist);
						} else {
							if (abs > hist.maximum) {
								difference = ((abs / hist.maximum) - 1);
								if (seen>=start && difference>threshold && recovery==0) {
									warn = true;
									valKey = valueKey;
									recovery = recoveryWindow;
								}
							}
						}
						hist.addFloat(abs);
					}
				}
			}
			unlockMe(this);
		}

		if (warn) {
			for (ValueAnomalyDetectorListener listener: list) {
				listener.detectedAnomaly(valKey,difference,result);
			}
		}
		
		super.processedResult(stream, result);
	}
	
	protected static float getFloatValue(Object value) {
		float r = 0;
		if (value!=null) {
			if (value instanceof Float) {
				r = (float) value;
			} else if (value instanceof Integer) {
				r = (float) (Integer) value;
			} else if (value instanceof Long) {
				r = (float) (Long) value;
			}
		}
		return r;
	}
}
