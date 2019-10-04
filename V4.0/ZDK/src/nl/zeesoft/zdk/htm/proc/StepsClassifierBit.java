package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;

public class StepsClassifierBit {
	protected ClassifierConfig			config		= null;
	protected int						index		= 0;
	
	protected HashMap<Object,Integer>	valueCounts	= new HashMap<Object,Integer>();
	protected HashMap<String,Integer>	labelCounts	= new HashMap<String,Integer>();
	
	protected StepsClassifierBit(ClassifierConfig config,int index) {
		this.config = config;
		this.index = index;
	}
	
	protected void associate(SDR activationSDR,DateTimeSDR inputSDR) {
		Object value = inputSDR.keyValues.get(config.valueKey);
		if (value!=null) {
			Integer count = valueCounts.get(value);
			if (count==null) {
				count = new Integer(0);
			}
			count++;
			valueCounts.put(value,count);
			if (count>=config.maxCount) {
				divideValueCountsBy(2);
			}
		}
		String label = (String) inputSDR.keyValues.get(config.labelKey);
		if (label!=null) {
			Integer count = labelCounts.get(label);
			if (count==null) {
				count = new Integer(0);
			}
			count++;
			labelCounts.put(label,count);
			if (count>=config.maxCount) {
				divideLabelCountsBy(2);
			}
		}
	}
	
	protected void divideValueCountsBy(int div) {
		if (div>1) {
			List<Object> values = new ArrayList<Object>(valueCounts.keySet());
			for (Object value: values) {
				Integer count = valueCounts.get(value);
				if (count<div) {
					valueCounts.remove(value);
				} else {
					count = count / 2;
					valueCounts.put(value,count);
				}
			}
		}
	}
	
	protected void divideLabelCountsBy(int div) {
		if (div>1) {
			List<String> labels = new ArrayList<String>(labelCounts.keySet());
			for (String label: labels) {
				Integer count = labelCounts.get(label);
				if (count<div) {
					labelCounts.remove(label);
				} else {
					count = count / 2;
					labelCounts.put(label,count);
				}
			}
		}
	}
}
