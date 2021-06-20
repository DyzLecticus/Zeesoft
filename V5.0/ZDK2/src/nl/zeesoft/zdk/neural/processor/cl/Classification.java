package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.Util;

public class Classification {
	public String					name				= "";
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
	
	public float getStandardDeviation() {
		List<Float> values = new ArrayList<Float>();
		for (Integer count: valueCounts.values()) {
			values.add((float) count);
		}
		return Util.getStandardDeviation(values);
	}
}

