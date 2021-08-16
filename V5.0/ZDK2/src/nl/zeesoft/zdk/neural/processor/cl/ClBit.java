package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClBit {
	public ClConfig		config	= null;
	public int			index	= 0;
	
	public HashMap<Object,Integer>	valueCounts		= new HashMap<Object,Integer>();

	public boolean associate(Object value) {
		boolean r = false;
		Integer count = valueCounts.get(value);
		if (count==null) {
			count = new Integer(config.initialCount);
		}
		count++;
		valueCounts.put(value,count);
		if (count>=config.maxCount) {
			r = true;
		}
		return r;
	}
	
	public void divideValueCountsBy(int div) {
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
