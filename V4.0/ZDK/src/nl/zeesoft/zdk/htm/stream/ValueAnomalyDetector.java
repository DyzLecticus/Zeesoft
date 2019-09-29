package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;

public class ValueAnomalyDetector extends ValuePredictor {
	private	List<ValueAnomalyDetectorListener>	listeners						= new ArrayList<ValueAnomalyDetectorListener>();
	
	private int									start							= 1000;
	private float								threshold						= 0.5F;
	private int									recoveryWindow					= 100;

	private HashMap<String,HistoricalFloats>	deviations	= new HashMap<String,HistoricalFloats>();
	private HashMap<String,HistoricalFloats>	accuracy						= new HashMap<String,HistoricalFloats>();
	private HistoricalFloats					rangeAccuracy					= new HistoricalFloats();
	
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
		for (HistoricalFloats dev: deviations.values()) {
			dev.window = window;
		}
		for (HistoricalFloats acc: accuracy.values()) {
			acc.window = window;
		}
		rangeAccuracy.window = window;
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

			for (String valueKey: valKeys) {
				if (predictedValues.containsKey(valueKey) && currentValues.containsKey(valueKey)) {
					float pV = DateTimeSDR.objectToFloat(predictedValues.get(valueKey));
					float cV = DateTimeSDR.objectToFloat(currentValues.get(valueKey));
					
					HistoricalFloats acc = accuracy.get(valueKey);
					if (acc==null) {
						acc = new HistoricalFloats();
						accuracy.put(valueKey,acc);
					}
					
					float abs = 0;
					if (pV > cV) {
						abs = pV - cV;
						acc.addFloat(0);
					} else if (pV < cV) {
						abs = cV - pV;
						acc.addFloat(0);
					} else {
						acc.addFloat(1);
					}
					
					HistoricalFloats dev = deviations.get(valueKey);
					if (dev==null) {
						dev = new HistoricalFloats();
						deviations.put(valueKey,dev);
					} else {
						if (abs > dev.maximum) {
							difference = ((abs / dev.maximum) - 1);
							if (seen>=start && difference>threshold && recovery==0) {
								warn = true;
								valKey = valueKey;
								recovery = recoveryWindow;
							}
						}
					}
					dev.addFloat(abs);
				}
			}

			if (valKeys.size()==1 && predictedValues.size()==3) {
				String valueKey = valKeys.get(0);
				if (predictedValues.containsKey(valueKey + "Min") &&
					predictedValues.containsKey(valueKey + "Max") &&
					currentValues.containsKey(valueKey)
					) {
					float lV = DateTimeSDR.objectToFloat(predictedValues.get(valueKey + "Min"));
					float uV = DateTimeSDR.objectToFloat(predictedValues.get(valueKey + "Max"));
					float cV = DateTimeSDR.objectToFloat(currentValues.get(valueKey));
					if (lV < cV && cV < uV) {
						rangeAccuracy.addFloat(1);
					} else {
						rangeAccuracy.addFloat(0);
					}
				}
			}
			unlockMe(this);
		}

		if (warn) {
			for (ValueAnomalyDetectorListener listener: list) {
				listener.detectedAnomaly(valKey,predictedValues,difference,result);
			}
		}
		
		super.processedResult(stream, result);
	}
	
	public float getAverageAccuracy(String valueKey) {
		float r = 0;
		lockMe(this);
		HistoricalFloats acc = accuracy.get(valueKey);
		if (acc!=null) {
			r = acc.average;
		}
		unlockMe(this);
		return r;
	}
	
	public float getAverageDeviation(String valueKey) {
		float r = 0;
		lockMe(this);
		HistoricalFloats dev = deviations.get(valueKey);
		if (dev!=null) {
			r = dev.average;
		}
		unlockMe(this);
		return r;
	}
	
	public float getAverageRangeAccuracy() {
		float r = 0;
		lockMe(this);
		r = rangeAccuracy.average;
		unlockMe(this);
		return r;
	}
}
