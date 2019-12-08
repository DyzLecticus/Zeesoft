package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A Merger is used to merge multiple SDRs into a single DateTimeSDR.
 * It concatenates SDRs by default but can also be configured to create a union.
 * Key values of input and context DateTimeSDR objects are included in the merged DateTimeSDR.
 */
public class Merger extends ProcessorObject {
	protected List<SDR>		contextSDRs		= new ArrayList<SDR>();
	
	public Merger(MergerConfig config) {
		super(config);
	}
	
	@Override
	public MergerConfig getConfig() {
		return (MergerConfig) super.getConfig();
	}
	
	@Override
	public ZStringBuilder getDescription() {
		return getConfig().getDescription();
	}
	
	@Override
	public ZStringBuilder toStringBuilder() {
		return new ZStringBuilder();
	}

	@Override
	public void fromStringBuilder(ZStringBuilder str) {
		
	}
	
	@Override
	public List<SDR> getSDRsForInput(SDR input,List<SDR> context,boolean learn) {
		contextSDRs = context;
		return super.getSDRsForInput(input, context, learn);
	}

	@Override
	protected SDR getSDRForInputSDR(SDR input, boolean learn) {
		DateTimeSDR r = null;
		SDR t = null;
		long dateTime = 0;
		SortedMap<String,Object> keyValues = new TreeMap<String,Object>();
		boolean skipFirst = false;
		if (input!=null) {
			t = input;
			if (input instanceof DateTimeSDR) {
				DateTimeSDR sdr = (DateTimeSDR) input;
				dateTime = sdr.dateTime;
				for (Entry<String,Object> entry: sdr.keyValues.entrySet()) {
					keyValues.put(entry.getKey(),entry.getValue());
				}
			}
		} else if (contextSDRs.size()>0){
			t = contextSDRs.get(0);
			skipFirst = true;
		} else {
			t = new SDR(100);
		}
		boolean first = true;
		for (SDR contextSDR: contextSDRs) {
			if (!skipFirst || !first) {
				if (getConfig().union) {
					t = SDR.and(t,contextSDR);
				} else {
					t = SDR.concat(t,contextSDR);
				}
			}
			first = false;
			if (contextSDR instanceof DateTimeSDR) {
				DateTimeSDR sdr = (DateTimeSDR) contextSDR;
				if (dateTime==0) {
					dateTime = sdr.dateTime;
				}
				for (Entry<String,Object> entry: sdr.keyValues.entrySet()) {
					keyValues.put(entry.getKey(),entry.getValue());
				}
			}
		}
		
		if (getConfig().maxOnBits>0) {
			t.subsample(getConfig().maxOnBits);
		}
		
		r = new DateTimeSDR(t);
		r.dateTime = dateTime;
		for (Entry<String,Object> entry: keyValues.entrySet()) {
			r.keyValues.put(entry.getKey(),entry.getValue());
		}
		return r;
	}
}
