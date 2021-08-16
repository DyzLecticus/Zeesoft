package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.MathUtil;

public class Classification {
	public int						step				= 0;
	public HashMap<Object,Float>	valueCounts			= new HashMap<Object,Float>();
	public Object					value				= null;
	
	public List<Object> getMostCountedValues() {
		List<Object> mostCountedValues = new ArrayList<Object>();
		Float maxValueCounts = 0F;
		for (Entry<Object,Float> entry: valueCounts.entrySet()) {
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
		Float vc = valueCounts.get(value);
		if (vc!=null) {
			float total = 0;
			for (Float count: valueCounts.values()) {
				total += count;
			}
			r = vc / total;
		}
		return r;
	}
	
	public float getStandardDeviation() {
		List<Float> values = new ArrayList<Float>();
		for (Float count: valueCounts.values()) {
			values.add(count);
		}
		return MathUtil.getStandardDeviation(values);
	}
}

