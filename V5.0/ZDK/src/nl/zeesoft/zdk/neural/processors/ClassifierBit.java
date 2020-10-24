package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassifierBit {
	// Configuration
	protected int						index				= 0;
	protected String					valueKey			= "";
	protected int						maxCount			= 0;
	
	// State
	protected HashMap<Object,Integer>	valueCounts			= new HashMap<Object,Integer>();
	
	protected ClassifierBit(int index, String valueKey, int maxCount) {
		this.index = index;
		this.valueKey = valueKey;
		this.maxCount = maxCount;
	}

	protected boolean associate(Object value) {
		boolean r = false;
		if (value!=null) {
			Integer count = valueCounts.get(value);
			if (count==null) {
				count = new Integer(0);
			}
			count++;
			valueCounts.put(value,count);
			if (count>=maxCount) {
				r = true;
			}
		}
		return r;
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
}
