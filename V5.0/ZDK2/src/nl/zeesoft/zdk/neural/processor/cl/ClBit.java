package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClBit {
	public ClConfig						config			= null;
	public int							index			= 0;
	
	public HashMap<Object,ValueCount>	valueCounts		= new HashMap<Object,ValueCount>();

	public boolean associate(Object value, int processed) {
		boolean r = false;
		ValueCount vc = valueCounts.get(value);
		if (vc==null) {
			vc = new ValueCount();
			vc.count = config.initialCount;
			valueCounts.put(value,vc);
		}
		vc.count++;
		vc.lastProcessed = processed;
		if (vc.count>=config.maxCount) {
			r = true;
		}
		return r;
	}
	
	public void divideValueCountsBy(int div) {
		List<Object> values = new ArrayList<Object>(valueCounts.keySet());
		for (Object value: values) {
			ValueCount vc = valueCounts.get(value);
			if (vc.count<div) {
				valueCounts.remove(value);
			} else {
				vc.count = vc.count / 2;
			}
		}
	}
}
