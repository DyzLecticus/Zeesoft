package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A Detector is used to detect anomalies in memory burst outputs.
 * It tracks and compares long term and short term accuracy.
 * If the difference between the long and short term accuracy is above a certain threshold, it will attach Anomaly objects to the output SDR.
 */
public class Detector extends ProcessorObject {
	public static final String	ANOMALY_KEY		= "ANOMALY";
	
	private List<SDR>			contextSDRs		= new ArrayList<SDR>();
	
	private int					seen			= 0;
	private HistoricalFloats	historyLong		= new HistoricalFloats();
	private HistoricalFloats	historyShort	= new HistoricalFloats();
	
	private boolean				detectAnomalies = true;
	
	public Detector(DetectorConfig config) {
		super(config);
		historyLong.window = config.windowLong;
		historyShort.window = config.windowShort;
	}
	
	/**
	 * Indicates anomalies should be detected.
	 * 
	 * @param detectAnomalies Indicates anomalies should be detected
	 */
	public void setDetectAnomalies(boolean detectAnomalies) {
		this.detectAnomalies = detectAnomalies;
	}
	
	@Override
	public DetectorConfig getConfig() {
		return (DetectorConfig) super.getConfig();
	}
	
	@Override
	public ZStringBuilder getDescription() {
		ZStringBuilder r = getConfig().getDescription();
		if (seen>=getConfig().start) {
			r.append(" (ACTIVE)");
		}
		return r;
	}
	
	@Override
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("" + seen);
		r.append(";");
		r.append(historyLong.toStringBuilder());
		r.append(";");
		r.append(historyShort.toStringBuilder());
		return r;
	}

	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		List<ZStringBuilder> elems = str.split(";");
		if (elems.size()==3) {
			seen = Integer.parseInt(elems.get(0).toString());
			historyLong.fromStringBuilder(elems.get(1));
			historyShort.fromStringBuilder(elems.get(2));
		}
	}
	
	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		contextSDRs = context;
		List<SDR> r = super.getSDRsForInput(input, context, learn);
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input, boolean learn) {
		DateTimeSDR r = null;

		long start = System.nanoTime();
		r = detectAnomalies(input);
		logStatsValue("detectAnomalies",System.nanoTime() - start);
		
		if (r==null) {
			if (input!=null) {
				r = new DateTimeSDR(input);
			} else {
				r = new DateTimeSDR(100);
			}
		}
		
		return r;
	}
	
	protected DateTimeSDR detectAnomalies(SDR input) {
		DateTimeSDR r = null;
		if (contextSDRs.size()==2) {
			SDR poolerSDR = contextSDRs.get(0);
			SDR burstSDR = contextSDRs.get(1);
			if (poolerSDR!=null && poolerSDR.onBits()>0 && burstSDR!=null) {
				
				float accuracy = 1F - (float) burstSDR.onBits() / (float) poolerSDR.onBits();
				historyShort.addFloat(accuracy);
				
				float averageLong = historyLong.average;
				float averageShort = historyShort.average;
				float difference = 1F - getFloatDifference(averageLong,averageShort);
				
				if (detectAnomalies && seen>=getConfig().start && difference>getConfig().threshold) {
					Anomaly anomaly = new Anomaly();
					anomaly.detectedAccuracy = accuracy;
					anomaly.averageLongTermAccuracy = averageLong;
					anomaly.averageShortTermAccuracy = averageShort;
					anomaly.difference = difference;
					if (input!=null) {
						r = new DateTimeSDR(input);
					} else {
						r = new DateTimeSDR(100);
					}
					r.keyValues.put(ANOMALY_KEY,anomaly);
				}
				
				historyLong.addFloat(accuracy);
				
				if (seen<getConfig().start) {
					seen++;
				}
			}
		}
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
