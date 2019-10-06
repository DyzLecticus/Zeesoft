package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * An AnomalyDetector is used to monitor DefaultStream output for bursting anomalies.
 * It tracks the average column prediction accuracy and calls its listeners if the difference between the latest accuracy and the average is higher than a certain threshold.
 */
public class AnomalyDetector extends StreamListenerObject {
	private	List<AnomalyDetectorListener>	listeners		= new ArrayList<AnomalyDetectorListener>();

	private HistoricalFloats				history			= new HistoricalFloats();
	
	private int								start			= 5000;
	private float							threshold		= 0.4F;
	private int								recoveryWindow	= 500;
	
	private int								seen			= 0;
	private float							average			= 0;
	private float							latest			= 0;
	private int								recovery		= 0;	

	public AnomalyDetector(DefaultStream stream) {
		super(stream);
	}
	
	public void addListener(AnomalyDetectorListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}

	public void setWindow(int window) {
		lockMe(this);
		history.window = window;
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
		SDR poolerSDR = result.outputSDRs.get(0);
		SDR burstSDR = result.outputSDRs.get(2);
		if (poolerSDR.length() == burstSDR.length()) {
			boolean warn = false;
			float averageAccuracy = 0;
			float difference = 0;
			
			float accuracy = 1F - (float) burstSDR.onBits() / (float) poolerSDR.onBits();
			
			lockMe(this);
			List<AnomalyDetectorListener> list = new ArrayList<AnomalyDetectorListener>(listeners);
			if (seen<start) {
				seen++;
			}
			if (recovery>0) {
				recovery--;
			}
			averageAccuracy = history.average;
			difference = 1F - getFloatDifference(averageAccuracy,accuracy);
			if (seen>=start && difference>threshold && recovery==0) {
				warn = true;
				recovery = recoveryWindow;
			}
			history.addFloat(accuracy);

			average = history.average;
			latest = accuracy;
			unlockMe(this);
			
			if (warn) {
				for (AnomalyDetectorListener listener: list) {
					listener.detectedAnomaly(averageAccuracy,accuracy,difference,result);
				}
			}
		}
	}

	public float getAverageAccuracy() {
		float r = 0;
		lockMe(this);
		r = average;
		unlockMe(this);
		return r;
	}

	public float getLatestAccuracy() {
		float r = 0;
		lockMe(this);
		r = latest;
		unlockMe(this);
		return r;
	}
	
	protected static float getFloatDifference(float pV, float cV) {
		float r = ((pV - cV) / ((pV + cV) / 2F));
		if (r < 0) {
			r = r * - 1F;
		}
		if (r > 0) {
			r = 1F - (r / 2F);
		} else {
			r = 1F;
		}
		return r;
	}
}
