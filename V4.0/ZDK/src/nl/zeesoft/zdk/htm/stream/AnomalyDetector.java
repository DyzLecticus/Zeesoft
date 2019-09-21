package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.thread.Locker;

public class AnomalyDetector extends Locker implements StreamListener {
	protected PredictionStream				stream			= null;
	
	private	List<AnomalyDetectorListener>	listeners		= new ArrayList<AnomalyDetectorListener>();

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

	public AnomalyDetector(PredictionStream stream) {
		super(stream.getMessenger());
		this.stream = stream;
	}

	protected Stream getStream() {
		return stream;
	}
	
	public void addListener(AnomalyDetectorListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}

	public void setWindow(int window) {
		lockMe(this);
		this.window = window;
		unlockMe(this);
	}

	public void setChangeWindow(int changeWindow) {
		lockMe(this);
		this.changeWindow = changeWindow;
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
			
		}
		
		SDR compareSDR = null;
		if (getCompareIndex()==-1) {
			compareSDR = result.inputSDR;
		} else {
			if (getCompareIndex()>=0 && getCompareIndex()<result.outputSDRs.size()) {
				compareSDR = result.outputSDRs.get(getCompareIndex());
			}
		}
		
		float acc = 0;
		if (compareSDR!=null && predictedSDR!=null) {
			acc = calculateAccuracy(predictedSDR,compareSDR);
		}
		
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
				if (absoluteChange < 0) {
					absoluteChange = absoluteChange * -1F;
				}
				if (seen>=start && absoluteChange>threshold && recovery==0) {
					warn = true;
					averageAccuracy = average;
					averageAccuracyChange = averageChange;
					recovery = recoveryWindow;
				}
			}
		}
		
		predictedSDR = result.outputSDRs.get(getPredictedIndex());
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
	
	protected int getPredictedIndex() {
		return 2;
	}
	
	protected int getCompareIndex() {
		return 0;
	}
	
	protected float calculateAccuracy(SDR predictedSDR,SDR compareSDR) {
		return (float) compareSDR.getOverlapScore(predictedSDR) / (float) compareSDR.onBits();
	}
}
