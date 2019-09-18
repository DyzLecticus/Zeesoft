package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class AnomalyDetector extends Locker implements StreamListener {
	private SDR				predictedSDR	= null;
	private List<Float>		accuracy		= new ArrayList<Float>();
	private int				window			= 1000;
	private float			average			= 0;
	
	public AnomalyDetector(Messenger msgr) {
		super(msgr);
	}

	@Override
	public void processedResult(Stream stream, StreamResult r) {
		lockMe(this);
		if (predictedSDR!=null) {
			SDR outputSDR = r.outputSDRs.get(0);
			float acc = (float) outputSDR.getOverlapScore(predictedSDR) / (float) outputSDR.onBits();
			accuracy.add(acc);
			while(accuracy.size()>window) {
				accuracy.remove(0);
			}
			average = 0;
			for (Float hist: accuracy) {
				average += hist;
			}
			if (average > 0) {
				average = average / window;
			}
		}
		predictedSDR = r.outputSDRs.get(2);
		unlockMe(this);
	}

	public float getAverageAccuracy() {
		float r = 0;
		lockMe(this);
		r = average;
		unlockMe(this);
		return r;
	}
	
}
