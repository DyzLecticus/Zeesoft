package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class ValueAnomalyDetector extends AnomalyDetector {
	private List<String>	valueKeys	= new ArrayList<String>();
	
	public ValueAnomalyDetector(BufferedPredictionStream stream,String valueKey) {
		super(stream);
		setWindow(100);
		setStart(3000);
		setThreshold(0.5F);
		setRecoveryWindow(100);
		valueKeys.add(valueKey);
	}

	public void addValueKey(String valueKey) {
		lockMe(this);
		valueKeys.add(valueKey);
		unlockMe(this);
	}
	
	@Override
	protected float calculateAccuracy(StreamResult result) {
		SDR predictedSDR = result.outputSDRs.get(3);
		SDR compareSDR = result.inputSDR;

		float r = 0;
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
				//System.out.println(l + " > " + c + " < " + u + " (p: " + p + ")");
				if (c < l) {
					r = getFloatDifference(l,c);
				} else if (c > u) {
					r = getFloatDifference(u,c);
				} else {
					r = getFloatDifference(p,c);
				}
			} else if (valueKeys.size()>0) {
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
			}
		}
		return r;
	}
}
