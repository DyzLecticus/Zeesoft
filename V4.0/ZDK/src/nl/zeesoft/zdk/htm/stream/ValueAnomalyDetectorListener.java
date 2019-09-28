package nl.zeesoft.zdk.htm.stream;

import java.util.HashMap;

public interface ValueAnomalyDetectorListener {
	public void detectedAnomaly(String valueKey,HashMap<String,Object> predictedValues,float difference,StreamResult result);
}
