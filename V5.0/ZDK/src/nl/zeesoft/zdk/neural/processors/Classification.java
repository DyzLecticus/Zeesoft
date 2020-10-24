package nl.zeesoft.zdk.neural.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;

public class Classification implements StrAble {
	public int						step				= 0;
	public String					valueKey			= "";
	public HashMap<Object,Integer>	valueCounts			= null;

	@Override
	public Str toStr() {
		Str r = new Str();
		r.sb().append(step);
		r.sb().append(";");
		r.sb().append(valueKey);
		r.sb().append(";");
		if (valueCounts!=null && valueCounts.size()>0) {
			Str valCounts = new Str();
			String className = "";
			for (Entry<Object,Integer> entry: valueCounts.entrySet()) {
				if (valCounts.length()>0) {
					valCounts.sb().append("%");
				}
				valCounts.sb().append(entry.getKey());
				valCounts.sb().append(",");
				valCounts.sb().append(entry.getValue());
				className = entry.getKey().getClass().getName();
			}
			valCounts.sb().insert(0, ";");
			valCounts.sb().insert(0, className);
			r.sb().append(valCounts.sb());
		} else {
			r.sb().append(";");
		}
		return r;
	}

	@Override
	public void fromStr(Str str) {
		List<Str> elems = str.split(";");
		if (valueCounts==null) {
			valueCounts = new HashMap<Object,Integer>();
		} else {
			valueCounts.clear();
		}
		step = Integer.parseInt(elems.get(0).toString());
		valueKey = elems.get(1).toString();
		String className = elems.get(2).toString();
		List<Str> valCounts = elems.get(3).split("%");
		for (Str valCount: valCounts) {
			List<Str> vc = valCount.split(",");
			Integer count = Integer.parseInt(vc.get(1).toString());
			Object value = null;
			if (className.equals(String.class.getName())) {
				value = vc.get(0).toString();
			} else if (className.equals(Str.class.getName())) {
				value = vc.get(0);
			} else if (className.equals(Long.class.getName())) {
				value = Long.parseLong(vc.get(0).toString());
			} else if (className.equals(Integer.class.getName())) {
				value = Integer.parseInt(vc.get(0).toString());
			} else if (className.equals(Double.class.getName())) {
				value = Double.parseDouble(vc.get(0).toString());
			} else if (className.equals(Float.class.getName())) {
				value = Float.parseFloat(vc.get(0).toString());
			} else if (className.equals(Boolean.class.getName())) {
				value = Boolean.parseBoolean(vc.get(0).toString());
			}
			if (value!=null) {
				valueCounts.put(value, count);
			}
		}
	}
	
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
}
