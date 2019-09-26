package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class ValueAnomalyDetector extends AnomalyDetector {
	private List<String>		valueKeys		= new ArrayList<String>();
	
	private HistoricalFloats	lowerRange		= new HistoricalFloats();
	private HistoricalFloats	upperRange		= new HistoricalFloats();
	
	public ValueAnomalyDetector(BufferedPredictionStream stream,String valueKey) {
		super(stream);
		setWindow(100);
		setStart(3000);
		setThreshold(0.3F);
		setRecoveryWindow(100);
		valueKeys.add(valueKey);
		setRangeWindow(1000);
	}

	public void addValueKey(String valueKey) {
		lockMe(this);
		valueKeys.add(valueKey);
		unlockMe(this);
	}

	public void setRangeWindow(int window) {
		lockMe(this);
		lowerRange.window = window;
		upperRange.window = window;
		unlockMe(this);
	}
	
	@Override
	protected float calculateAccuracy(StreamResult result,float currentAverageAccuracy) {
		SDR predictedSDR = result.outputSDRs.get(3);
		SDR compareSDR = result.inputSDR;

		float r = currentAverageAccuracy;
		if (predictedSDR instanceof DateTimeSDR && compareSDR instanceof DateTimeSDR) {
			if (valueKeys.size()==1 && result.outputSDRs.size()>=6) {
				DateTimeSDR pred = (DateTimeSDR) predictedSDR;
				DateTimeSDR lower = (DateTimeSDR) result.outputSDRs.get(4);
				DateTimeSDR upper = (DateTimeSDR) result.outputSDRs.get(5);
				DateTimeSDR comp = (DateTimeSDR) compareSDR;
				Object pVal = pred.keyValues.get(valueKeys.get(0));
				Object lVal = lower.keyValues.get(valueKeys.get(0));
				Object uVal = upper.keyValues.get(valueKeys.get(0));
				Object cVal = comp.keyValues.get(valueKeys.get(0));
				float p = DateTimeSDR.objectToFloat(pVal);
				float l = DateTimeSDR.objectToFloat(lVal);
				float u = DateTimeSDR.objectToFloat(uVal);
				float c = DateTimeSDR.objectToFloat(cVal);
				
				float min = lowerRange.minimum;
				float max = upperRange.maximum;
				
				System.out.println(min + " <= " + c + " <= " + max + " (p: " + p + ") " + ((min <= c) && (c <= max)));
				if (c < min) {
					r = getFloatDifference(l,c);
				} else if (c > max) {
					r = getFloatDifference(u,c);
				} else {
					r = 1;
					//r = getFloatDifference(p,c);
				}
				
				lowerRange.addFloat(l);
				upperRange.addFloat(u);
				
			} else if (valueKeys.size()>0) {
				/*
				r = 1;
				float total = 0;
				for (String valueKey: valueKeys) {
					DateTimeSDR pred = (DateTimeSDR) predictedSDR;
					DateTimeSDR comp = (DateTimeSDR) compareSDR;
					Object pVal = pred.keyValues.get(valueKey);
					Object cVal = comp.keyValues.get(valueKey);
					if (pVal!=null && cVal!=null && pVal.getClass().getName().equals(cVal.getClass().getName())) {
						if (pVal instanceof String && pVal.equals(cVal)) {
							total += 1;
						} else {
							total += getFloatDifference(DateTimeSDR.objectToFloat(pVal),DateTimeSDR.objectToFloat(cVal));
						}
					}
				}
				r = total / (float) valueKeys.size();
				*/
			}
		}
		return r;
	}
}
