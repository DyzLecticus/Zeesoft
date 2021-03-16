package nl.zeesoft.zdk.neural.processor.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Classification {
	public String					name				= "";
	public int						step				= 0;
	public HashMap<Object,Integer>	valueCounts			= null;
	
	public List<Object> getMostCountedValues() {
		List<Object> mostCountedValues = new ArrayList<Object>();
		int maxValueCounts = 0;
		if (valueCounts!=null) {
			for (Entry<Object,Integer> entry: valueCounts.entrySet()) {
				if (entry.getValue() > maxValueCounts) {
					maxValueCounts = entry.getValue();
					mostCountedValues.clear();
				}
				if (entry.getValue() == maxValueCounts) {
					mostCountedValues.add(entry.getKey());
				}
			}
		}
		return mostCountedValues;
	}
	
	public float getStandardDeviation() {
		float r = 0.0F;
		if (valueCounts.size()>1) {
			float sum = 0.0F;
			float dev = 0.0F;
			int size = valueCounts.size();
	
			for(Integer count: valueCounts.values()) {
				sum += (float)count;
			}
	
			float mean = sum / size;
	
			for(Integer count: valueCounts.values()) {
				dev += Math.pow((float)count - mean, 2);
			}
			r = (float) Math.sqrt(dev/(size - 1));
		}
		return r;
	}}
