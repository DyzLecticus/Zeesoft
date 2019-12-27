package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.htm.proc.Anomaly;

public class ResultLog {
	public long						dateTime			= 0;
	public int						predictedValue		= Integer.MIN_VALUE;
	public int						actualValue			= Integer.MIN_VALUE;
	public float					accuracy			= 0;
	public float					averageAccuracy		= 0;
	public Anomaly					detectedAnomaly		= null;
}
