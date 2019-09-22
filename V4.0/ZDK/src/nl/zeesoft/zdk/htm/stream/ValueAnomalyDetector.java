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
	protected int getPredictedIndex() {
		return 3;
	}
	
	@Override
	protected int getCompareIndex() {
		return -1;
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
