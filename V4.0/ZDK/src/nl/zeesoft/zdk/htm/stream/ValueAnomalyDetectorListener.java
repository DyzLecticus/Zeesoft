package nl.zeesoft.zdk.htm.stream;

public interface ValueAnomalyDetectorListener {
	public void detectedAnomaly(String valueKey, float difference,StreamResult result);
}
