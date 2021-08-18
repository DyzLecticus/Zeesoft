package nl.zeesoft.zdk.neural.processor.cl;

import java.util.HashMap;

public class ClBit {
	public ClConfig						config			= null;
	public int							index			= 0;
	
	public HashMap<Object,ValueCount>	valueCounts		= new HashMap<Object,ValueCount>();

	public void associate(Object value, int processed) {
		ValueCount vc = valueCounts.get(value);
		if (vc==null) {
			valueCounts.put(value,new ValueCount(config.initialCount, processed));
		} else {
			vc.count++;
			vc.lastProcessed = processed;
		}
	}
}
