package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.thread.Locker;

public class AnomalyDetector extends Locker implements StreamListener {
	protected PredictionStream				stream			= null;
	
	private	List<AnomalyDetectorListener>	listeners		= new ArrayList<AnomalyDetectorListener>();

	private HistoricalAccuracy				history			= new HistoricalAccuracy();
	
	private int								start			= 3000;
	private float							threshold		= 0.4F;
	private int								recoveryWindow	= 500;
	
	private SDR								predictedSDR	= null;
	
	private int								seen			= 0;
	private float							average			= 0;
	private float							latest			= 0;
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
		boolean warn = false;
		float averageAccuracy = 0;
		float accuracy = 0;
		float difference = 0;
		
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
		
		if (compareSDR!=null && predictedSDR!=null) {
			accuracy = calculateAccuracy(predictedSDR,compareSDR);
		}
		
		averageAccuracy = history.average;
		difference = 1F - getFloatDifference(averageAccuracy,accuracy);
		if (seen>=start && difference>threshold && recovery==0) {
			warn = true;
			recovery = recoveryWindow;
		}
		history.addAccuracy(accuracy);

		average = history.average;
		latest = accuracy;
				
		predictedSDR = result.outputSDRs.get(getPredictedIndex());
		unlockMe(this);
		
		if (warn) {
			for (AnomalyDetectorListener listener: list) {
				listener.detectedAnomaly(averageAccuracy,accuracy,difference,result);
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
	
	public static float getFloatDifference(float pV, float cV) {
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
	
	protected int getPredictedIndex() {
		return 2;
	}
	
	protected int getCompareIndex() {
		return 0;
	}
	
	protected float calculateAccuracy(SDR predictedSDR,SDR compareSDR) {
		return (float) compareSDR.getOverlapScore(predictedSDR) / (float) compareSDR.onBits();
	}
	
	protected float getFloatDifference(float pV, float cV,int resolution) {
		return getFloatDifference(pV * resolution, cV * resolution);
	}
}
