package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class AnomalyDetector extends Locker implements StreamListener {
	private List<AnomalyDetectorListener>	listeners		= new ArrayList<AnomalyDetectorListener>();

	private int								window			= 1000;
	private int								changeWindow	= 24;
	private int								start			= 3000;
	private float							threshold		= 0.5F;
	private int								recoveryWindow	= 500;
	
	private SDR								predictedSDR	= null;
	
	private int								seen			= 0;
	private List<Float>						accuracy		= new ArrayList<Float>();
	private float							average			= 0;
	private float							averageChange	= 0;
	private int								recovery		= 0;	
	
	public AnomalyDetector(Messenger msgr) {
		super(msgr);
	}

	public void addListener(AnomalyDetectorListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}
	
	@Override
	public void processedResult(Stream stream, StreamResult result) {
		boolean warn = false;
		float averageAccuracy = 0;
		float averageAccuracyChange = 0;
		lockMe(this);
		List<AnomalyDetectorListener> list = new ArrayList<AnomalyDetectorListener>(listeners);
		if (predictedSDR!=null) {
			if (seen<start) {
				seen++;
			}
			if (recovery>0) {
				recovery--;
			}
			
			SDR outputSDR = result.outputSDRs.get(0);
			float acc = calculateAccuracy(predictedSDR,outputSDR);
			
			accuracy.add(acc);
			while(accuracy.size()>window) {
				accuracy.remove(0);
			}
			
			average = 0;
			averageChange = 0;
			int i = 0;
			for (Float hist: accuracy) {
				average += hist;
				if (i >= window - changeWindow) {
					averageChange += hist;
				}
				i++;
			}
			
			if (average > 0) {
				average = average / window;
			}
			if (averageChange>0) {
				averageChange = averageChange / changeWindow;
				if (average > 0) {
					averageChange = 1F - (averageChange / average);
					float absoluteChange = averageChange;
					if (absoluteChange < 1) {
						absoluteChange = absoluteChange * -1F;
					}
					if (seen>=start && absoluteChange>threshold) {
						if (recovery==0) {
							warn = true;
							averageAccuracy = average;
							averageAccuracyChange = averageChange;
							recovery = recoveryWindow;
						}
					}
				}
			}
		}
		predictedSDR = result.outputSDRs.get(2);
		unlockMe(this);
		if (warn) {
			for (AnomalyDetectorListener listener: list) {
				listener.detectedAnomaly(averageAccuracy, averageAccuracyChange, result);
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

	public float getAverageAccuracyChange() {
		float r = 0;
		lockMe(this);
		r = averageChange;
		unlockMe(this);
		return r;
	}
	
	protected float calculateAccuracy(SDR predictedSDR,SDR outputSDR) {
		return (float) outputSDR.getOverlapScore(predictedSDR) / (float) outputSDR.onBits();
	}
}
