package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A Detector is used to detect anomalies in memory burst outputs.
 */
public class Detector extends ProcessorObject {
	public static final String	ANOMALY_KEY		= "ANOMALY";
	
	private List<SDR>			contextSDRs		= new ArrayList<SDR>();
	
	private int					seen			= 0;
	private HistoricalFloats	historyLong		= new HistoricalFloats();
	private HistoricalFloats	historyShort	= new HistoricalFloats();
	
	public Detector(DetectorConfig config) {
		super(config);
		historyLong.window = config.windowLong;
		historyShort.window = config.windowShort;
	}
	
	@Override
	public DetectorConfig getConfig() {
		return (DetectorConfig) super.getConfig();
	}
	
	@Override
	public ZStringBuilder getDescription() {
		return getConfig().getDescription();
	}
	
	@Override
	public ZStringBuilder toStringBuilder() {
		// TODO: Implement
		return new ZStringBuilder();
	}

	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		// TODO: Implement
	}
	
	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		contextSDRs = context;
		List<SDR> r = super.getSDRsForInput(input, context, learn);
		return r;
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input, boolean learn) {
		// TODO: Stats logging
		
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
				
				//if (seen>=getConfig().start && seen>=2645 && seen<=2664) {
				//	System.out.println("Seen: " + seen + ", average long: " + averageLong + ", average short: " + averageShort + ", difference: " + difference + ", onBits: " + burstSDR.onBits());
				//}
				
				if (seen>=getConfig().start && difference>getConfig().threshold) {
					Anomaly anomaly = new Anomaly();
					anomaly.detectedAccuracy = accuracy;
					anomaly.averageLongTermAccuracy = averageLong;
					anomaly.averageShortTermAccuracy = averageShort;
					anomaly.difference = difference;
					r = new DateTimeSDR(input);
					r.keyValues.put(ANOMALY_KEY,anomaly);
				}
				
				historyLong.addFloat(accuracy);
				
				//seen++;
				if (seen<getConfig().start) {
					seen++;
				}
			}
		}
		if (r==null) {
			if (input!=null) {
				r = new DateTimeSDR(input);
			} else {
				r = new DateTimeSDR(100);
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
