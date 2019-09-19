package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;

public class ValueAnomalyDetector extends AnomalyDetector {
	private List<String>	valueKeys	= new ArrayList<String>();
	
	public ValueAnomalyDetector(Messenger msgr,String valueKey) {
		super(msgr);
		predictedIndex = 3;
		compareIndex = -1;
		setWindow(100);
		setChangeWindow(10);
		setStart(3000);
		setThreshold(0.3F);
		setRecoveryWindow(100);
		valueKeys.add(valueKey);
	}

	public void addValueKey(String valueKey) {
		lockMe(this);
		valueKeys.add(valueKey);
		unlockMe(this);
	}
	
	@Override
	protected float calculateAccuracy(SDR predictedSDR,SDR compareSDR) {
		float r = 0;
		if (predictedSDR instanceof DateTimeSDR && compareSDR instanceof DateTimeSDR) {
			r = 0;
			if (valueKeys.size()>0) {
				float total = 0;
				for (String valueKey: valueKeys) {
					DateTimeSDR pred = (DateTimeSDR) predictedSDR;
					DateTimeSDR comp = (DateTimeSDR) compareSDR;
					Object pVal = pred.keyValues.get(valueKey);
					Object cVal = comp.keyValues.get(valueKey);
					if (pVal!=null && cVal!=null && pVal.getClass().getName().equals(cVal.getClass().getName())) {
						if (pVal instanceof Float) {
							float pV = (Float) pVal;
							float cV = (Float) cVal;
							total += getFloatDifference(pV,cV);
						} else if (pVal instanceof Integer) {
							float pV = (float) (Integer) pVal;
							float cV = (float) (Integer) cVal;
							total += getFloatDifference(pV,cV);
						} else if (pVal instanceof Long) {
							float pV = (float) (Long) pVal;
							float cV = (float) (Long) cVal;
							total += getFloatDifference(pV,cV);
						} else if (pVal instanceof String && pVal.equals(cVal)) {
							total += 1;
						}
					}
				}
				r = total / (float) valueKeys.size();
			}
		}
		return r;
	}
	
	private float getFloatDifference(float pV, float cV) {
		float r = ((pV - cV) / ((pV + cV) / 2F));
		if (r < 0) {
			r = r * - 1F;
		}
		if (r > 0) {
			r = 1F - (r / 2F);
		} else {
			r = 1;
		}
		return r;
	}
}
