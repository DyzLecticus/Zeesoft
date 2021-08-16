package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.MathUtil;

public class Classification {
	public int						step				= 0;
	public HashMap<Object,Integer>	valueCounts			= new HashMap<Object,Integer>();
	public Object					value				= null;
	
	public List<Object> getMostCountedValues() {
		List<Object> mostCountedValues = new ArrayList<Object>();
		int maxValueCounts = 0;
		for (Entry<Object,Integer> entry: valueCounts.entrySet()) {
			if (entry.getValue() > maxValueCounts) {
				maxValueCounts = entry.getValue();
				mostCountedValues.clear();
			}
			if (entry.getValue() == maxValueCounts) {
				mostCountedValues.add(entry.getKey());
			}
		}
		return mostCountedValues;
	}
	
	public float getValueCountPercentage(Object value) {
		float r = 0;
		Integer valCount = valueCounts.get(value);
		if (valCount!=null) {
			float vc = valCount.floatValue();
			float total = 0;
			for (Integer count: valueCounts.values()) {
				total += count.floatValue();
			}
			r = vc / total;
		}
		return r;
	}
	
	public float getStandardDeviation() {
		List<Float> values = new ArrayList<Float>();
		for (Integer count: valueCounts.values()) {
			values.add((float) count);
		}
		return MathUtil.getStandardDeviation(values);
	}
}

