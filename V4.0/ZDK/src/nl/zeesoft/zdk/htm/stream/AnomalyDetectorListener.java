package nl.zeesoft.zdk.htm.stream;

public interface AnomalyDetectorListener {
	public void detectedAnomaly(float averageAccuracy,float latestAccuracy, float difference,StreamResult result);
}
