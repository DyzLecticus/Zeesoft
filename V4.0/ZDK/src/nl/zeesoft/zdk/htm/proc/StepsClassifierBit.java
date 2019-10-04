package nl.zeesoft.zdk.htm.proc;

import java.util.HashMap;

import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class StepsClassifierBit {
	protected ClassifierConfig			config		= null;
	protected int						index		= 0;
	
	protected HashMap<Float,Integer>	valueCounts	= new HashMap<Float,Integer>();
	protected HashMap<String,Integer>	labelCounts	= new HashMap<String,Integer>();
	
	protected StepsClassifierBit(ClassifierConfig config,int index) {
		this.config = config;
		this.index = index;
	}
	
	protected void associate(SDR activationSDR,DateTimeSDR inputSDR) {
		Object value = inputSDR.keyValues.get(config.valueKey);
		if (value!=null) {
			Float val = DateTimeSDR.objectToFloat(value);
			Integer count = valueCounts.get(val);
			if (count==null) {
				count = new Integer(0);
			}
			count++;
			valueCounts.put(val,count);
		}
		String label = (String) inputSDR.keyValues.get(config.labelKey);
		if (label!=null) {
			Integer count = labelCounts.get(label);
			if (count==null) {
				count = new Integer(0);
			}
			count++;
			labelCounts.put(label,count);
		}
	}
}
